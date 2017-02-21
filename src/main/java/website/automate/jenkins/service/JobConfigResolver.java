package website.automate.jenkins.service;

import java.util.Map;

import website.automate.jenkins.support.JobConfig;

public class JobConfigResolver {

  private static final String TIMEOUT_PARAMETER_NAME = "website.automate.timeout";

  private static final String RESOLUTION_PARAMETER_NAME = "website.automate.resolution";
  
  private static final String BOX_PARAMETER_NAME = "website.automate.box.id";
  
  private static final JobConfigResolver INSTANCE = new JobConfigResolver();
  
  public static JobConfigResolver getInstance(){
      return INSTANCE;
  }
  
  private ContextParameterResolver contextParameterResolver = ContextParameterResolver.getInstance();
  
  public JobConfig resolve(Map<String, String> configParameters){
    Map<String, String> context = contextParameterResolver.resolve(configParameters);
    
    JobConfig jobConfig = new JobConfig();
    jobConfig.setContext(context);
    jobConfig.setTimeout(getTimeout(configParameters));
    jobConfig.setResolution(configParameters.get(RESOLUTION_PARAMETER_NAME));
    jobConfig.setBoxId(configParameters.get(BOX_PARAMETER_NAME));
    return jobConfig;
  }
  
  private Double getTimeout(Map<String, String> configParameters){
    String timeoutStr = configParameters.get(TIMEOUT_PARAMETER_NAME);

    if(timeoutStr == null){
      return null;
    }

    return Double.parseDouble(timeoutStr);
  }
}
