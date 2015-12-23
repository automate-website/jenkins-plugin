package website.automate.plugins.jenkins;

import static java.util.Arrays.asList;
import hudson.Launcher;
import hudson.Extension;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.model.AbstractProject;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.QueryParameter;

import website.automate.plugins.jenkins.logging.BuilderLogHandler;
import website.automate.plugins.jenkins.model.Authentication;
import website.automate.plugins.jenkins.model.Project;
import website.automate.plugins.jenkins.model.Scenario;
import website.automate.plugins.jenkins.service.PluginExecutionService;
import website.automate.plugins.jenkins.service.ProjectRetrievalRemoteService;
import website.automate.plugins.jenkins.support.CommunicationException;
import website.automate.plugins.jenkins.support.Constants;

import javax.servlet.ServletException;

import java.io.IOException;
import java.util.List;

public class AutomateWebsiteBuilder extends Builder {

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
    	Result result = pluginExecutionService.execute((asList(getScenarioId())), 
    			getDescriptor().getAuthentication(),
    			BuilderLogHandler.getInstance(listener.getLogger()));
    	build.setResult(result);
        return true;
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
     * See <tt>src/main/resources/website/automate/plugins/jenkins/AutomateWebsiteBuilder/*.jelly</tt>
     * for the actual HTML fragment for the configuration screen.
     */
    @Extension // This indicates to Jenkins that this is an implementation of an extension point.
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        private String username;
        
        private String password;
        
        private List<Project> projects;

        public DescriptorImpl() {
            load();
        }

        @SuppressWarnings("rawtypes")
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        public String getDisplayName() {
            return Constants.BUILDER_TITLE;
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
                List<Project> projects = ProjectRetrievalRemoteService
                        .getInstance()
                        .getProjectsWithScenariosByPrincipal(Authentication.of(newUsername, newPassword));
                setProjects(projects);
                
                save();
            }
        }
        
        public ListBoxModel doFillProjectItems() {
            List<Project> projects = getProjects();
            ListBoxModel items = new ListBoxModel();
            
            for (Project project : projects) {
                items.add(project.getTitle(), project.getId());
            }
            
            return items;
        }
        
        public ListBoxModel doFillScenarioItems(@QueryParameter("project") String projectId) {
            ListBoxModel items = new ListBoxModel();
            
            if(projectId == null || projectId.isEmpty()){
                return items;
            }

            Project project = getProjectById(projectId);
            List<Scenario> scenarios = project.getScenarios();
            
            if(scenarios == null){
                return items;
            }

            for(Scenario scenario : scenarios){
                items.add(scenario.getTitle(),  asComboId(projectId, scenario.getId()));
            }
            
            return items;
        }
        
        private String asComboId(String projectId, String scenarioId){
        	return projectId + ":" + scenarioId;
        }
        
        private Project getProjectById(String projectId){
            List<Project> projects = getProjects();
            for(Project project : projects){
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
        
        private List<Project> getProjects(){
            return projects;
        }
        
        private void setProjects(List<Project> projects){
            this.projects = projects;
        }
    }
}

