package website.automate.jenkins.support;

public class ExecutionInterruptionException extends RuntimeException {

    private static final long serialVersionUID = -6963320300323826533L;

    public ExecutionInterruptionException(String msg, Throwable e){
        super(msg, e);
    }
}
