package website.automate.plugins.jenkins.service;

import static java.util.Arrays.asList;

import org.junit.Test;

import website.automate.plugins.jenkins.logging.NoOpLogHandler;
import website.automate.plugins.jenkins.service.support.AuthenticationUtils;

public class PluginExecutionServiceIT extends MockServerIT {

    private PluginExecutionService executionService = PluginExecutionService.getInstance();
    
    @Test
    public void executionShouldSucceed(){
        executionService.execute(
        		asList(getScenario().getId()), 
        		AuthenticationUtils.getIntegrationTestAccountPrincipal(),
        		NoOpLogHandler.getInstance());
    }
}
