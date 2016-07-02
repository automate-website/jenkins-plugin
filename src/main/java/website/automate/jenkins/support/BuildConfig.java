package website.automate.jenkins.support;

import java.util.Map;

import website.automate.jenkins.service.ContextParameterResolver;

public class BuildConfig {

    public static final String PROPERTY_NAME_EXECUTION_TIMEOUT_SEC = "website.automate.executionTimeoutSec";
    
    public static final String PROPERTY_NAME_JOB_STATUS_CHECK_INTERVAL_SEC = "website.automate.jobStatusCheckIntervalSec";
    
    private static final long DEFAULT_EXECUTION_TIMEOUT_IN_SEC = 300;

    private static final long DEFAULT_JOB_STATUS_CHECK_INTERVAL_IN_SEC = 30;
    
    private long executionTimeoutSec;
    
    private long jobStatusCheckIntervalSec;
    
    private Map<String, String> context;
    
    public BuildConfig(Map<String, String> configParameters){
        this.executionTimeoutSec = getParamValueAsLong(configParameters, PROPERTY_NAME_EXECUTION_TIMEOUT_SEC, DEFAULT_EXECUTION_TIMEOUT_IN_SEC);
        this.jobStatusCheckIntervalSec = getParamValueAsLong(configParameters, PROPERTY_NAME_JOB_STATUS_CHECK_INTERVAL_SEC, DEFAULT_JOB_STATUS_CHECK_INTERVAL_IN_SEC);
        this.context = ContextParameterResolver.getInstance().resolve(configParameters);
    }

    public long getExecutionTimeoutSec() {
        return executionTimeoutSec;
    }

    public long getJobStatusCheckIntervalSec() {
        return jobStatusCheckIntervalSec;
    }

    public Map<String, String> getContext() {
        return context;
    }
    
    private static long getParamValueAsLong(Map<String, String> configParameters, String paramName, long defaultValue){
        return Long.parseLong(getOrDefault(configParameters, paramName, Long.toString(defaultValue)));
    }
    
    private static String getOrDefault(Map<String, String> map, String key, String defaultValue){
        String value = map.get(key);
        if(value == null){
            value = defaultValue;
        }
        return value;
    }
}
