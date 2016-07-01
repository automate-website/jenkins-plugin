package website.automate.jenkins.mapper;

import website.automate.jenkins.model.ScenarioSerializable;
import website.automate.manager.api.client.model.Scenario;

public class ScenarioMapper extends Mapper<Scenario, ScenarioSerializable> {

    private static final ScenarioMapper INSTANCE = new ScenarioMapper();
    
    public static ScenarioMapper getInstance() {
        return INSTANCE;
    }
    
    @Override
    public ScenarioSerializable map(Scenario source) {
        ScenarioSerializable target = new ScenarioSerializable();
        target.setId(source.getId());
        target.setName(source.getName());
        return target;
    }

}
