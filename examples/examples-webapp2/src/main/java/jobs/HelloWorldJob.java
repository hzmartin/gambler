package jobs;

import gambler.examples.webapp2.domain.auth.User;
import gambler.examples.webapp2.service.AuthUserService;
import gambler.examples.webapp2.util.SpringContextHolder;
import java.sql.Timestamp;
import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class HelloWorldJob extends QuartzJobBean {

    private final Logger log = Logger.getLogger(getClass());

    @Override
    protected void executeInternal(JobExecutionContext jobexecutioncontext)
            throws JobExecutionException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        log.info("job start... @" + timestamp);
        AuthUserService service = SpringContextHolder
                .getBean("authUserService");
        User user = service.findUserById("wangqihui");
        JobDetail jobDetail = jobexecutioncontext.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        System.out.println("hello " + user + ", execute Job(" + jobDetail.getFullName() + ", " + jobDetail.getDescription() + ")" + jobDataMap.getString("msg") + " @"
                + timestamp);
        log.info("job end! @" + timestamp);
    }

}
