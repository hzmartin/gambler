package gambler.examples.scheduler.controller.view;

import gambler.examples.scheduler.annotation.AuthRequired;
import gambler.examples.scheduler.constant.AuthConstants;
import gambler.examples.scheduler.controller.AbstractController;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Class MiscController
 * 
 * @author hzwangqh
 * @version 2013-10-18
 */
@Controller
@RequestMapping("/scheduleview")
public class SchedulerViewController extends AbstractController {

	@RequestMapping(value = "/schedulecontrol")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_VIEW_SCHEDULE_INFO })
	public Object schedulecontrol(final HttpServletRequest request) {
		return new ModelAndView();
	}

	@RequestMapping(value = "/jobmgmt")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_VIEW_JOB_INFO })
	public Object joblist(final HttpServletRequest request) {
		return new ModelAndView();
	}

	@RequestMapping(value = "/listjobs")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_VIEW_JOB_INFO })
	public Object listjob(final HttpServletRequest request) {
		return new ModelAndView();
	}
	
	@RequestMapping(value = "/listtriggers")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_VIEW_TRIGGER_INFO })
	public Object listtrigger(final HttpServletRequest request) {
		return new ModelAndView();
	}

	@RequestMapping(value = "/viewjob")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_VIEW_JOB_INFO })
	public Object viewjob(final HttpServletRequest request,
			@RequestParam String jobName,
			@RequestParam(required = false) String jobGroup) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.getModel().put("jobName", jobName);
		modelAndView.getModel().put("jobGroup", jobGroup);
		return modelAndView;
	}

	@RequestMapping(value = "/addjob")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_ADD_JOB })
	public Object addjob(final HttpServletRequest request) {
		return new ModelAndView();
	}

}
