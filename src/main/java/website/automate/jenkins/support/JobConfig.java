package website.automate.jenkins.support;

import java.util.Map;

public class JobConfig {

  private Map<String, String> context;

  private Double timeout;

  private String resolution;

  private String boxId;

  public Map<String, String> getContext() {
    return context;
  }

  public void setContext(Map<String, String> context) {
    this.context = context;
  }

  public Double getTimeout() {
    return timeout;
  }

  public void setTimeout(Double timeout) {
    this.timeout = timeout;
  }

  public String getResolution() {
    return resolution;
  }

  public void setResolution(String resolution) {
    this.resolution = resolution;
  }

  public String getBoxId() {
    return boxId;
  }

  public void setBoxId(String boxId) {
    this.boxId = boxId;
  }
}
