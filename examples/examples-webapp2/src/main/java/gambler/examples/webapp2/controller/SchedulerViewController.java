package gambler.examples.webapp2.controller;

import gambler.examples.webapp2.annotation.AuthRequired;
import gambler.examples.webapp2.constant.AuthConstants;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Class MiscController
 * 
 * @author hzwangqh
 * @version 2013-10-18
 */
@Controller
public class SchedulerViewController extends AbstractController{


	@RequestMapping(value = "/schedulecontrol.do")
	@AuthRequired(permission={AuthConstants.PERM_SCHEDULER.PERM_VIEW_SCHEDULE_INFO})
	public Object schedulecontrol(final HttpServletRequest request) {
		return new ModelAndView("schedulecontrol");
	}

}
