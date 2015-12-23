package website.automate.plugins.jenkins.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import website.automate.plugins.jenkins.model.Authentication;
import website.automate.plugins.jenkins.model.Scenario;
import website.automate.plugins.jenkins.service.support.AuthenticationUtils;

public class ScenarioRetrievalRemoteServiceIT extends MockServerIT {

	private Authentication principal = AuthenticationUtils.getIntegrationTestAccountPrincipal();
	private ScenarioRetrievalRemoteService retrievalRemoteService = ScenarioRetrievalRemoteService.getInstance();
	
	@Test
	public void scenariosShouldBeRetrievedByPrincipal(){
		List<Scenario> acutalScenarios = retrievalRemoteService.getScenariosByProjectIdAndPrincipal(getProject().getId(), principal);
		
		assertNotNull(acutalScenarios);
		assertFalse(acutalScenarios.isEmpty());
		Scenario actualScenario = acutalScenarios.get(0);
		assertEquals(getScenario(), actualScenario);
	}
}
