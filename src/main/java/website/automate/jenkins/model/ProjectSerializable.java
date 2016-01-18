package website.automate.jenkins.model;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("project")
public class ProjectSerializable extends AbstractSerializable {

    private static final long serialVersionUID = -8623239093817690252L;

    private String title;
    
    @XStreamImplicit(itemFieldName="scenario")
    private List<ScenarioSerializable> scenarios = new ArrayList<ScenarioSerializable>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ScenarioSerializable> getScenarios() {
		return scenarios;
	}

	public void setScenarios(List<ScenarioSerializable> scenarios) {
		this.scenarios = scenarios;
	}
}
