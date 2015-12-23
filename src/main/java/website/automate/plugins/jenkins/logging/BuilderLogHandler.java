package website.automate.plugins.jenkins.logging;

import static java.lang.String.format;

import java.io.PrintStream;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class BuilderLogHandler extends Handler{

	private PrintStream logger;
	
	private BuilderLogHandler(PrintStream logger){
		this.logger = logger;
	}
	
	public static BuilderLogHandler getInstance(PrintStream logger){
		return new BuilderLogHandler(logger);
	}

	@Override
    public void publish(LogRecord record) {
	    logger.println(format("[%s]: %s", record.getLevel().getName(), record.getMessage()));
    }

	@Override
    public void flush() {
	    logger.flush();
    }

	@Override
    public void close() throws SecurityException {
	    logger.close();
    }
	
}
