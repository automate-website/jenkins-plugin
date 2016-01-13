package website.automate.plugins.jenkins.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("scenario")
public class ScenarioSerializable extends AbstractSerializable {

    private static final long serialVersionUID = -3320382468756010832L;

    private String title;
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
