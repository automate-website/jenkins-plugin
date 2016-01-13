package website.automate.plugins.jenkins.mapper;

import website.automate.manager.api.client.model.Scenario;
import website.automate.plugins.jenkins.model.ScenarioSerializable;

public class ScenarioMapper extends Mapper<Scenario, ScenarioSerializable> {

    private static final ScenarioMapper INSTANCE = new ScenarioMapper();
    
    public static ScenarioMapper getInstance() {
        return INSTANCE;
    }
    
    @Override
    public ScenarioSerializable map(Scenario source) {
        ScenarioSerializable target = new ScenarioSerializable();
        target.setId(source.getId());
        target.setTitle(source.getTitle());
        return target;
    }

}
