package gambler.examples.webapp2.job;

import gambler.examples.webapp2.domain.auth.User;
import gambler.examples.webapp2.service.AuthUserService;
import gambler.examples.webapp2.util.SpringContextHolder;

import java.sql.Timestamp;
import org.apache.log4j.Logger;
import org.quartz.JobDataMap;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class HelloWorldJob extends QuartzJobBean {

    private final Logger log = Logger.getLogger(getClass());
    
    @Override
    protected void executeInternal(JobExecutionContext jobexecutioncontext)
            throws JobExecutionException {
        AuthUserService service = SpringContextHolder
                .getBean("authUserService");
        User user = service.findUserById("wangqihui");
        JobDataMap jobDataMap = jobexecutioncontext.getJobDetail().getJobDataMap();
        log.info("hello " + user + ", " + jobDataMap.getString("msg") + " @"
                + new Timestamp(System.currentTimeMillis()));
    }

}
