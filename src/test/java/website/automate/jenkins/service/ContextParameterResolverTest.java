package website.automate.jenkins.service;

import static java.util.Collections.singleton;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ContextParameterResolverTest {

    private static final String 
        ENV_PARAM_NAME = "website.automate.context.x",
        CONTEXT_PARAM_NAME = "x",
        CONTEXT_PARAM_VALUE = "y",
        NON_ENV_PARAM_NAME = "x",
        NON_CONTEXT_PARAM_VALUE = "z";
    
    private ContextParameterResolver resolver = ContextParameterResolver.getInstance();
    
    @Mock
    private Map<String, String> contextParameters;
    
    @Test
    public void contextParameterIsExtracted(){
        when(contextParameters.keySet()).thenReturn(singleton(ENV_PARAM_NAME));
        when(contextParameters.get(ENV_PARAM_NAME)).thenReturn(CONTEXT_PARAM_VALUE);
        
        Map<String, String> context = resolver.resolve(contextParameters);
        
        assertThat(context.get(CONTEXT_PARAM_NAME), is(CONTEXT_PARAM_VALUE));
    }
    
    @Test
    public void nonContextParamIsNotExtracted(){
        when(contextParameters.keySet()).thenReturn(singleton(NON_ENV_PARAM_NAME));
        when(contextParameters.get(NON_ENV_PARAM_NAME)).thenReturn(NON_CONTEXT_PARAM_VALUE);
        
        Map<String, String> context = resolver.resolve(contextParameters);
        
        assertTrue(context.isEmpty());
    }
}
