package website.automate.jenkins.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import hudson.EnvVars;

public class ContextParameterResolver {

    private static final String CONTEXT_PARAMETER_PREFIX = "automate.website.context.";
    
    private static final ContextParameterResolver INSTANCE = new ContextParameterResolver();
    
    public static ContextParameterResolver getInstance(){
        return INSTANCE;
    }
    
    public Map<String, String> resolve(EnvVars envVars){
        Map<String, String> contextParameters = new HashMap<String, String>();
        Set<String> parameterNames = envVars.keySet();
        for(String parameterName : parameterNames){
            if(parameterName.startsWith(CONTEXT_PARAMETER_PREFIX)){
                contextParameters.put(parameterName.substring(CONTEXT_PARAMETER_PREFIX.length()), envVars.get(parameterName));
            }
        }
        return contextParameters;
    }
}
