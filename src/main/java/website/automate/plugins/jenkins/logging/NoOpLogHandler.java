package website.automate.plugins.jenkins.logging;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class NoOpLogHandler extends Handler {

	private static final NoOpLogHandler INSTANCE = new NoOpLogHandler();
	
	private NoOpLogHandler(){
	}
	
	public static NoOpLogHandler getInstance(){
		return INSTANCE;
	}
	
	@Override
    public void publish(LogRecord record) {
    }

	@Override
    public void flush() {
	    
    }

	@Override
    public void close() throws SecurityException {
    }

}
