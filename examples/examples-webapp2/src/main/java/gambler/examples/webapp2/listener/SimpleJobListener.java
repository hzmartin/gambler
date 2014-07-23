package gambler.examples.webapp2.listener;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

public class SimpleJobListener implements JobListener {   
	private final Logger logger = Logger.getLogger(getClass());
	
    public String getName() {   
         return getClass().getSimpleName();   
    }   
 
    public void jobToBeExecuted(JobExecutionContext context) {   
         String jobName = context.getJobDetail().getName();   
         logger.info(jobName + " is about to be executed");   
    }   
    public void jobExecutionVetoed(JobExecutionContext context) {   
         String jobName = context.getJobDetail().getName();   
         logger.info(jobName + " was vetoed and not executed()");   
    }   
    public void jobWasExecuted(JobExecutionContext context,   
              JobExecutionException jobException) {   
 
         String jobName = context.getJobDetail().getName();   
         logger.info(jobName + " was executed");   
    }   
} 