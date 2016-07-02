package website.automate.jenkins;

import static hudson.init.InitMilestone.PLUGINS_STARTED;
import static java.util.Arrays.asList;
import hudson.Launcher;
import hudson.EnvVars;
import hudson.Extension;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.init.Initializer;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Items;
import hudson.model.Result;
import hudson.model.AbstractProject;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import website.automate.jenkins.logging.BuilderLogHandler;
import website.automate.jenkins.mapper.ProjectMapper;
import website.automate.jenkins.model.ProjectSerializable;
import website.automate.jenkins.model.ScenarioSerializable;
import website.automate.jenkins.service.PluginExecutionService;
import website.automate.jenkins.support.BuildConfig;
import website.automate.manager.api.client.ProjectRetrievalRemoteService;
import website.automate.manager.api.client.model.Authentication;
import website.automate.manager.api.client.model.Project;
import website.automate.manager.api.client.support.CommunicationException;

import javax.servlet.ServletException;

import java.io.IOException;
import java.util.List;

public class AutomateWebsiteBuilder extends Builder {

    public static final String BUILDER_TITLE = "Automate Website Execution";
    
    private final String project;
    
    private String scenario;
    
    @DataBoundConstructor
    public AutomateWebsiteBuilder(String project, String scenario) {
        this.project = project;
        this.scenario = scenario;
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
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    /**
     * Descriptor for {@link AutomateWebsiteBuilder}. Used as a singleton.
     * The class is marked as public so that it can be accessed from views.
     *
     * <p>
     * See <tt>src/main/resources/website/automate/jenkins/AutomateWebsiteBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    @XStreamAlias("descriptor")
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        private static final ProjectMapper PROJECT_MAPPER = ProjectMapper.getInstance();
        
        private String username;
        
        private String password;
        
        private List<ProjectSerializable> projects;

        @Initializer(before=PLUGINS_STARTED)
        public static void addAliases() {
            Items.XSTREAM2.processAnnotations(new Class<?>[] { ProjectSerializable.class, ScenarioSerializable.class });
        }
        
        public DescriptorImpl() {
            load();
        }

        @SuppressWarnings("rawtypes")
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        public String getDisplayName() {
            return BUILDER_TITLE;
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
            configureIfRequired(username, password);
            
            return super.configure(req,formData);
        }

        public FormValidation doSyncProjects(@QueryParameter("username") final String username,
                @QueryParameter("password") final String password) throws IOException, ServletException {
            try {
                configure(username, password);
                
                return FormValidation.ok("Success");
            } catch (CommunicationException e) {
                return FormValidation.error("Client error : " + e.getMessage());
            }
        }
        
        private void configureIfRequired(String newUsername, String newPassword){
            configure(newUsername, newPassword, false);
        }
        
        private void configure(String newUsername, String newPassword){
            configure(newUsername, newPassword, true);
        }

        public Authentication getAuthentication(){
        	return Authentication.of(username, password);
        }
        
        private void configure(String newUsername, String newPassword, boolean forceProjectSync){
            String username = getUsername();
            String password = getPassword();
            
            if(!(newUsername.equals(username) && newPassword.equals(password)) || forceProjectSync){
                setUsername(newUsername);
                setPassword(newPassword);
                List<ProjectSerializable> projects = retrieveAndMapProjects(newUsername, newPassword);
                setProjects(projects);
                
                save();
            }
        }
        
        private List<ProjectSerializable> retrieveAndMapProjects(String username, String password){
            List<Project> projects = ProjectRetrievalRemoteService
                    .getInstance()
                    .getProjectsWithScenariosByPrincipal(Authentication.of(username, password));
            
            return PROJECT_MAPPER.safeMapList(projects);
        }
        
        public ListBoxModel doFillProjectItems() {
            List<ProjectSerializable> projects = getProjects();
            ListBoxModel items = new ListBoxModel();
            
            for (ProjectSerializable project : projects) {
                items.add(project.getTitle(), project.getId());
            }
            
            return items;
        }
        
        public ListBoxModel doFillScenarioItems(@QueryParameter("project") String projectId) {
            ListBoxModel items = new ListBoxModel();
            
            if(projectId == null || projectId.isEmpty()){
                return items;
            }

            ProjectSerializable project = getProjectById(projectId);
            List<ScenarioSerializable> scenarios = project.getScenarios();
            
            if(scenarios == null){
                return items;
            }

            for(ScenarioSerializable scenario : scenarios){
                items.add(scenario.getName(),  asComboId(projectId, scenario.getId()));
            }
            
            return items;
        }
        
        private String asComboId(String projectId, String scenarioId){
        	return projectId + ":" + scenarioId;
        }
        
        private ProjectSerializable getProjectById(String projectId){
            List<ProjectSerializable> projects = getProjects();
            for(ProjectSerializable project : projects){
                if(project.getId().equals(projectId)){
                    return project;
                }
            }
            return null;
        }
        
        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }
        
        private List<ProjectSerializable> getProjects(){
            return projects;
        }
        
        private void setProjects(List<ProjectSerializable> projects){
            this.projects = projects;
        }
    }
}

