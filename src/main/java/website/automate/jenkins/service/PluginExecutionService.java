package website.automate.jenkins.service;

import static java.lang.String.format;
import hudson.model.Result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Logger;

import website.automate.jenkins.support.BuildConfig;
import website.automate.jenkins.support.ExecutionInterruptionException;
import website.automate.jenkins.support.JobConfig;
import website.automate.manager.api.client.JobManagementRemoteService;
import website.automate.manager.api.client.model.Authentication;
import website.automate.manager.api.client.model.Job;
import website.automate.manager.api.client.model.Job.JobProfile;
import website.automate.manager.api.client.model.Job.JobStatus;
import website.automate.manager.api.client.model.Job.TakeScreenshots;
import website.automate.manager.api.client.support.Constants;

public class PluginExecutionService {

    private static final PluginExecutionService INSTANCE = new PluginExecutionService();

    private static final Logger LOGGER = Logger.getLogger(PluginExecutionService.class.getSimpleName());

    private JobManagementRemoteService jobManagementRemoteService = JobManagementRemoteService.getInstance();

    public static PluginExecutionService getInstance() {
        return INSTANCE;
    }

    public Result execute(BuildConfig buildConfig, Collection<String> scenarioIds, Authentication principal, Handler handler) {
        LOGGER.addHandler(handler);

        if (scenarioIds == null || scenarioIds.isEmpty()) {
            LOGGER.info("Skipping execution - no scenarios were selected.");
            return Result.SUCCESS;
        }
        LOGGER.info(format("Creating jobs for selected scenarios %s ...", scenarioIds));
        List<Job> createdJobs = jobManagementRemoteService.createJobs(createJobs(buildConfig.getJobConfig(), scenarioIds), principal);
        long jobsCreatedMillis = System.currentTimeMillis();

        Collection<String> createdJobIds = asJobIds(createdJobs);
        List<Job> updatedJobs;

        do {
            if (System.currentTimeMillis() - jobsCreatedMillis >= buildConfig.getExecutionTimeoutSec() * 1000) {
                break;
            }
            try {
                Thread.sleep(buildConfig.getJobStatusCheckIntervalSec() * 1000);
            } catch (InterruptedException e) {
                throw new ExecutionInterruptionException("Unexpected plugin execution thread interrupt occured.", e);
            }

            LOGGER.info("Checking job statuses ...");
            updatedJobs = jobManagementRemoteService.getJobsByIdsAndPrincipal(createdJobIds, principal,
                    JobProfile.MINIMAL);

        } while (!areCompleted(updatedJobs));

        LOGGER.info("Jobs execution completed.");
        updatedJobs = jobManagementRemoteService.getJobsByIdsAndPrincipal(createdJobIds, principal, JobProfile.MINIMAL);

        Result result = logJobStatuses(updatedJobs);

        LOGGER.removeHandler(handler);

        return result;
    }

    private Result logJobStatuses(Collection<Job> jobs) {
        Result result = Result.SUCCESS;

        for (Job job : jobs) {
            String jobTitle = job.getTitle();
            String jobUrl = getJobUrl(job.getId());
            if (job.getStatus() == JobStatus.SUCCESS) {
                LOGGER.info(format("%s job execution succeeded (%s).", jobTitle, jobUrl));
            } else if (job.getStatus() == JobStatus.FAILURE) {
                if (result != Result.FAILURE) {
                    result = Result.UNSTABLE;
                }
                LOGGER.severe(format("%s job execution failed (%s).", jobTitle, jobUrl));
            } else {
                result = Result.FAILURE;
                LOGGER.severe(
                        format("Unexpected error occured during execution of '%s' or execution took to long (%s).",
                                jobTitle, jobUrl));
            }
        }
        return result;
    }

    private String getJobUrl(String jobId) {
        return Constants.getAppBaseUrl() + "/job/" + jobId;
    }

    private boolean areCompleted(Collection<Job> jobs) {
        for (Job job : jobs) {
            JobStatus jobStatus = job.getStatus();
            if (jobStatus == JobStatus.SCHEDULED || jobStatus == JobStatus.RUNNING) {
                return false;
            }
        }
        return true;
    }

    private Collection<String> asJobIds(Collection<Job> jobs) {
        List<String> jobIds = new ArrayList<String>();
        for (Job job : jobs) {
            jobIds.add(job.getId());
        }
        return jobIds;
    }

    private Collection<Job> createJobs(JobConfig jobConfig, Collection<String> scenarioIds) {
        List<Job> jobs = new ArrayList<Job>();
        jobs.add(createJob(jobConfig, scenarioIds));
        return jobs;
    }

    private Job createJob(JobConfig jobConfig, Collection<String> scenarioIds) {
        Job job = new Job();
        job.setScenarioIds(new HashSet<String>(scenarioIds));
        job.setTakeScreenshots(TakeScreenshots.ON_FAILURE);
        job.setContext(jobConfig.getContext());
        job.setBoxId(jobConfig.getBoxId());
        job.setTimeout(jobConfig.getTimeout());
        job.setResolution(jobConfig.getResolution());
        return job;
    }
}
