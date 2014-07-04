package gambler.examples.webapp2.job;

import gambler.examples.webapp2.domain.auth.User;
import gambler.examples.webapp2.service.AuthUserService;
import gambler.examples.webapp2.util.SpringContextHolder;

import java.sql.Timestamp;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class HelloWorldJob extends QuartzJobBean {

	private String name;

	@Override
	protected void executeInternal(JobExecutionContext jobexecutioncontext)
			throws JobExecutionException {
		AuthUserService service = SpringContextHolder
				.getBean("authUserService");
		User user = service.findUserById("wangqihui");
		System.out.println("hello " + user + " @"
				+ new Timestamp(System.currentTimeMillis()));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
