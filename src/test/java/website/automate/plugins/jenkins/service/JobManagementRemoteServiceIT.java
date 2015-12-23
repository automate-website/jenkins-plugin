package website.automate.plugins.jenkins.service;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import website.automate.plugins.jenkins.model.Authentication;
import website.automate.plugins.jenkins.model.Job;
import website.automate.plugins.jenkins.model.Job.JobProfile;
import website.automate.plugins.jenkins.service.support.AuthenticationUtils;

public class JobManagementRemoteServiceIT extends MockServerIT {

	private Authentication principal = AuthenticationUtils.getIntegrationTestAccountPrincipal();
	private JobManagementRemoteService jobManagementRemoteService = JobManagementRemoteService.getInstance();
	
	@Test
	public void jobsShouldBeCreatedAndRetrieved(){
		List<Job> actualCreatedJobs = jobManagementRemoteService.createJobs(asList(getRequestJob()), principal);
		assertContains(actualCreatedJobs, getJob());
		
		List<Job> retrievedJobs = jobManagementRemoteService.getJobsByIdsAndPrincipal(asList(getJob().getId()), principal, JobProfile.BRIEF);
		assertContains(retrievedJobs, getSuccessfulJob());
	}
	
	private void assertContains(List<Job> actualCreatedJobs, Job expectedJob){
	    assertNotNull(actualCreatedJobs);
        Job actualCreatedJob = actualCreatedJobs.get(0);
        assertEquals(getJob(), actualCreatedJob);
	}
}
