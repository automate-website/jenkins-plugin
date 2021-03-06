package website.automate.jenkins.mapper;

import website.automate.jenkins.model.ProjectSerializable;
import website.automate.manager.api.client.model.Project;

public class ProjectMapper extends Mapper<Project, ProjectSerializable> {

    private static final ProjectMapper INSTANCE = new ProjectMapper();
    
    private static final ScenarioMapper SCENARIO_MAPPER = ScenarioMapper.getInstance();
    
    public static ProjectMapper getInstance() {
        return INSTANCE;
    }
    
    @Override
    public ProjectSerializable map(Project source) {
        ProjectSerializable target = new ProjectSerializable();
        target.setId(source.getId());
        target.setTitle(source.getTitle());
        target.setScenarios(SCENARIO_MAPPER.safeMapList(source.getScenarios()));
        return target;
    }

}
