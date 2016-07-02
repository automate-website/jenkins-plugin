package website.automate.jenkins.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ContextParameterResolver {

    private static final String CONTEXT_PARAMETER_PREFIX = "website.automate.context.";
    
    private static final ContextParameterResolver INSTANCE = new ContextParameterResolver();
    
    public static ContextParameterResolver getInstance(){
        return INSTANCE;
    }
    
    public Map<String, String> resolve(Map<String, String> configParameters){
        Map<String, String> contextParameters = new HashMap<String, String>();
        Set<String> parameterNames = configParameters.keySet();
        for(String parameterName : parameterNames){
            if(parameterName.startsWith(CONTEXT_PARAMETER_PREFIX)){
                contextParameters.put(parameterName.substring(CONTEXT_PARAMETER_PREFIX.length()), configParameters.get(parameterName));
            }
        }
        return contextParameters;
    }
}
