package website.automate.jenkins.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("automate-website-scenario")
public class ScenarioSerializable extends AbstractSerializable {

    private static final long serialVersionUID = -3320382468756010832L;

    private String name;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
