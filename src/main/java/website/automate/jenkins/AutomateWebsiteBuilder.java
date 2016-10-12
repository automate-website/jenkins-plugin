package website.automate.jenkins;

import static hudson.init.InitMilestone.PLUGINS_STARTED;
import static java.util.Arrays.asList;
import hudson.Launcher;
import hudson.init.Initializer;
import hudson.EnvVars;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Items;
import hudson.model.Result;
import hudson.tasks.Builder;

import org.kohsuke.stapler.DataBoundConstructor;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import website.automate.jenkins.logging.BuilderLogHandler;
import website.automate.jenkins.service.PluginExecutionService;
import website.automate.jenkins.support.BuildConfig;

@XStreamAlias("automate-website-builder")
public class AutomateWebsiteBuilder extends Builder {

    public static final String BUILDER_TITLE = "Automate Website Execution";
    
    private final String project;
    
    private String scenario;
    
    @DataBoundConstructor
    public AutomateWebsiteBuilder(String project, String scenario) {
        this.project = project;
        this.scenario = scenario;
    }

    @Initializer(before=PLUGINS_STARTED)
    public static void addAliases() {
        Items.XSTREAM2.processAnnotations(new Class<?>[] { AutomateWebsiteBuilder.class });
    }
    
    public String getProject() {
        return project;
    }
    
    public String getScenario(){
        return scenario;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
    	PluginExecutionService pluginExecutionService = PluginExecutionService.getInstance();
    	EnvVars envVars = getEnvironmentVariables(build, listener);
    	Result result = pluginExecutionService.execute(new BuildConfig(envVars),
    	        asList(getScenarioId()), 
    			getDescriptor().getAuthentication(),
    			BuilderLogHandler.getInstance(listener.getLogger()));
    	build.setResult(result);
        return true;
    }
    
    @SuppressWarnings("rawtypes")
    private EnvVars getEnvironmentVariables(AbstractBuild build, BuildListener listener){
        try {
            return build.getEnvironment(listener);
        } catch (Exception e) {
            return new EnvVars();
        }
    }
    
    private String getScenarioId(){
    	String scenario = getScenario();
    	
    	if(scenario == null){
    		return null;
    	}
    	
    	return scenario.split(":")[1];
    }

    @Override
    public AutomateWebsiteDescriptor getDescriptor() {
        return (AutomateWebsiteDescriptor)super.getDescriptor();
    }

}

