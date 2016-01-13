package website.automate.plugins.jenkins.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Mapper<S, T> {

    public abstract T map(S source);
    
    public T safeMap(S source){
        if(source == null){
            return null;
        }
        return map(source);
    }
    
    public List<T> safeMapList(List<S> sources){
        List<T> targets = new ArrayList<T>();
        if(sources == null){
            return Collections.emptyList();
        }
        for(S source : sources){
            targets.add(map(source));
        }
        return targets;
    }
}
