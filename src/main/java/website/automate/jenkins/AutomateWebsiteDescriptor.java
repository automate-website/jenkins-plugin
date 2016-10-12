package website.automate.jenkins;

import static hudson.init.InitMilestone.PLUGINS_LISTED;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import hudson.Extension;
import hudson.XmlFile;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import website.automate.jenkins.mapper.ProjectMapper;
import website.automate.jenkins.model.ProjectSerializable;
import website.automate.jenkins.model.ScenarioSerializable;
import website.automate.jenkins.support.XStreamProvider;
import website.automate.manager.api.client.ProjectRetrievalRemoteService;
import website.automate.manager.api.client.model.Authentication;
import website.automate.manager.api.client.model.Project;
import website.automate.manager.api.client.support.CommunicationException;

/**
 * Descriptor for {@link AutomateWebsiteBuilder}. Used as a singleton.
 * The class is marked as public so that it can be accessed from views.
 *
 * <p>
 * See <tt>src/main/resources/website/automate/jenkins/AutomateWebsiteBuilder/*.jelly</tt>
 * for the actual HTML fragment for the configuration screen.
 */
@Extension // This indicates to Jenkins that this is an implementation of an extension point.
@XStreamAlias("automate-website-descriptor")
public class AutomateWebsiteDescriptor extends BuildStepDescriptor<Builder> {

    private static final ProjectMapper PROJECT_MAPPER = ProjectMapper.getInstance();
    
    private static final XStreamProvider XSTREAM_PROVIDER = XStreamProvider.getInstance(); 
    
    private String username;
    
    private String password;
    
    @XStreamImplicit(itemFieldName="automate-website-project")
    private List<ProjectSerializable> projects;

    public AutomateWebsiteDescriptor() {
        super(AutomateWebsiteBuilder.class);
        load();
    }

    @SuppressWarnings("rawtypes")
    public boolean isApplicable(Class<? extends AbstractProject> aClass) {
        return true;
    }

    public String getDisplayName() {
        return AutomateWebsiteBuilder.BUILDER_TITLE;
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
                .getProjectsWithExecutableScenariosByPrincipal(Authentication.of(username, password));
        
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
    
    @Override
    protected XmlFile getConfigFile() {
      XmlFile xmlFile = new XmlFile(XSTREAM_PROVIDER.get(), new File(Jenkins.getInstance().getRootDir(),getId()+".xml"));
      return xmlFile;
    }

}
