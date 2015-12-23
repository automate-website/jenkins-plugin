package website.automate.plugins.jenkins.service;

import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.List;

import website.automate.plugins.jenkins.model.Authentication;
import website.automate.plugins.jenkins.model.Job;
import website.automate.plugins.jenkins.model.Job.JobProfile;
import website.automate.plugins.jenkins.support.RestTemplate;

public class JobManagementRemoteService {

	private static final JobManagementRemoteService INSTANCE = new JobManagementRemoteService();
	
	public static JobManagementRemoteService getInstance(){
		return INSTANCE;
	}
	
	private RestTemplate restTemplate = RestTemplate.getInstance();
	
	public List<Job> getJobsByIdsAndPrincipal(Collection<String> jobIds, Authentication principal, JobProfile jobProfile) {
		return asList(restTemplate.performPost(Job [].class, 
				"/public/job/subset?profile=" + jobProfile,
				principal, jobIds));
	}
	
	public List<Job> createJobs(Collection<Job> jobs, Authentication principal){
	    return asList(restTemplate.performPost(Job [].class, "/public/job/batch", principal, jobs));
	}
	
}
