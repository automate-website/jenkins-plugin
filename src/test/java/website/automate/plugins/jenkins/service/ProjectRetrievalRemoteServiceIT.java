package website.automate.plugins.jenkins.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import website.automate.plugins.jenkins.model.Project;
import website.automate.plugins.jenkins.service.support.AuthenticationUtils;

public class ProjectRetrievalRemoteServiceIT extends MockServerIT {

	private ProjectRetrievalRemoteService retrievalRemoteService = ProjectRetrievalRemoteService.getInstance();
	
	@Test
	public void projectsShouldBeRetrievedByPrincipal() throws JsonProcessingException{
		List<Project> actualProjects = retrievalRemoteService.getProjectsByPrincipal(AuthenticationUtils.getIntegrationTestAccountPrincipal());
		
		assertNotNull(actualProjects);
		assertFalse(actualProjects.isEmpty());
		Project actualProject = actualProjects.get(0);
		assertEquals(getProject(), actualProject);
	}
}
