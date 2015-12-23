package website.automate.plugins.jenkins.support;

public class CommunicationException extends RuntimeException {

    private static final long serialVersionUID = 5338303168238688676L;
    
    public CommunicationException(String msg, Throwable e){
    	super(msg, e);
    }

}
