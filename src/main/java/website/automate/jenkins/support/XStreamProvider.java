package website.automate.jenkins.support;

import com.thoughtworks.xstream.XStream;

import website.automate.jenkins.AutomateWebsiteDescriptor;
import website.automate.jenkins.model.ProjectSerializable;
import website.automate.jenkins.model.ScenarioSerializable;

public class XStreamProvider {

  private static XStreamProvider INSTANCE = new XStreamProvider();
  
  public static XStreamProvider getInstance(){
    return INSTANCE;
  }
  
  public XStream get() {
    XStream xStream = new XStream();
    xStream.setClassLoader(this.getClass().getClassLoader());
    xStream.processAnnotations(new Class<?>[] {AutomateWebsiteDescriptor.class,
        ProjectSerializable.class, ScenarioSerializable.class});
    return xStream;
  }
}
