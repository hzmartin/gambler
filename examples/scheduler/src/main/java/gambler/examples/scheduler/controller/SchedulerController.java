package gambler.examples.scheduler.controller;

import gambler.commons.util.time.TimeUtils;
import gambler.examples.scheduler.annotation.AuthRequired;
import gambler.examples.scheduler.annotation.LogRequestParam;
import gambler.examples.scheduler.constant.AuthConstants;
import gambler.examples.scheduler.domain.auth.User;
import gambler.examples.scheduler.dto.AccountDto;
import gambler.examples.scheduler.dto.JobDefinitionDto;
import gambler.examples.scheduler.dto.JobDto;
import gambler.examples.scheduler.dto.JobExecutionContextDto;
import gambler.examples.scheduler.dto.TriggerDto;
import gambler.examples.scheduler.exception.ActionException;
import gambler.examples.scheduler.resp.ResponseStatus;
import gambler.examples.scheduler.service.DefinitionService;
import gambler.examples.scheduler.service.SchedulerService;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/scheduler")
public class SchedulerController extends AbstractController {

	@Resource
	private SchedulerService schedulerService;

	@Resource
	private DefinitionService definitionService;

	@RequestMapping(value = "/getSchedulerInfo")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_VIEW_SCHEDULE_INFO })
	@ResponseBody
	public Object getSchedulerInfo(final HttpServletRequest request)
			throws SchedulerException, ActionException {
		return schedulerService.getSchedulerInfo();
	}

	@RequestMapping(value = "/getJobDefinitionMap")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_VIEW_JOB_INFO })
	@ResponseBody
	public Object getJobDefinitionMap(final HttpServletRequest request)
			throws SchedulerException, ActionException {
		Map<String, JobDefinitionDto> definitionMap = definitionService
				.getDefinitionMap();
		Map<String, JobDefinitionDto> authorizedMap = new TreeMap<String, JobDefinitionDto>();
		AccountDto loginUser = authUserService.getLoginUser(request);
		User user = authUserService.findUserById(loginUser.getUserId());
		for (Map.Entry<String, JobDefinitionDto> entry : definitionMap
				.entrySet()) {
			String jobName = entry.getKey();
			JobDefinitionDto jobDef = entry.getValue();
			if (jobDef.getPid() != null) {
				boolean authorized = authUserService.checkUserPermission(user,
						jobDef.getPid());
				if (!authorized) {
					continue;
				}
			}
			authorizedMap.put(jobName, jobDef);
		}
		return authorizedMap;
	}

	@RequestMapping(value = "/getJobDefinition")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_VIEW_JOB_INFO })
	@ResponseBody
	public Object getJobDefinition(final HttpServletRequest request,
			@LogRequestParam(name = "jobName") @RequestParam String jobName)
			throws SchedulerException, ActionException {
		AccountDto loginUser = authUserService.getLoginUser(request);
		JobDefinitionDto jobDef = definitionService.getDefinition(jobName);
		if (jobDef.getPid() != null) {
			User user = authUserService.findUserById(loginUser.getUserId());
			boolean authorized = authUserService.checkUserPermission(user,
					jobDef.getPid());
			if (!authorized) {
				return null;
			}
		}
		return jobDef;
	}

	@RequestMapping(value = "/shutdown")
	@AuthRequired(permission = { AuthConstants.PERM_SUPER })
	@ResponseBody
	public Object shutdown(
			final HttpServletRequest request,
			@LogRequestParam(name = "waitForJobsToComplete") @RequestParam(required = false, defaultValue = "false") boolean waitForJobsToComplete)
			throws SchedulerException, ActionException {
		schedulerService.shutdown(waitForJobsToComplete);
		return schedulerService.getSchedulerInfo();
	}

	@RequestMapping(value = "/standby")
	@AuthRequired(permission = { AuthConstants.PERM_SUPER })
	@ResponseBody
	public Object standby(final HttpServletRequest request)
			throws SchedulerException, ActionException {
		schedulerService.standby();
		return schedulerService.getSchedulerInfo();
	}

	@RequestMapping(value = "/start")
	@AuthRequired(permission = { AuthConstants.PERM_SUPER })
	@ResponseBody
	public Object start(final HttpServletRequest request)
			throws SchedulerException, ActionException {
		schedulerService.start();
		return schedulerService.getSchedulerInfo();
	}

	@RequestMapping(value = "/getTriggerList")
	@AuthRequired(permission = { AuthConstants.PERM_SUPER })
	@ResponseBody
	public Object getTriggerList(final HttpServletRequest request)
			throws SchedulerException {
		return schedulerService.getTriggerList();
	}

	@RequestMapping(value = "/getTrigger")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_VIEW_TRIGGER_INFO })
	@ResponseBody
	public Object getTrigger(
			final HttpServletRequest request,
			@LogRequestParam(name = "triggerName") @RequestParam String triggerName,
			@LogRequestParam(name = "triggerGroup") @RequestParam(required = false) String triggerGroup)
			throws SchedulerException {
		return schedulerService.getTrigger(triggerName, triggerGroup);
	}

	@RequestMapping(value = "/getTriggerState")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_VIEW_TRIGGER_INFO })
	@ResponseBody
	public Object getTriggerState(
			final HttpServletRequest request,
			@LogRequestParam(name = "triggerName") @RequestParam String triggerName,
			@LogRequestParam(name = "triggerGroup") @RequestParam(required = false) String triggerGroup)
			throws SchedulerException {
		int state = schedulerService.getTriggerState(triggerName, triggerGroup);
		String stateLabel = TriggerDto.getTriggerStateLabel(state);
		JSONObject obj = new JSONObject();
		obj.put("state", state);
		obj.put("stateLabel", stateLabel);
		return obj;
	}

	@RequestMapping(value = "/getJobList")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_VIEW_JOB_INFO })
	@ResponseBody
	public Object getJobList(
			final HttpServletRequest request,
			@LogRequestParam(name = "withTrigger") @RequestParam(required = false, defaultValue = "false") boolean withTrigger)
			throws SchedulerException {
		return schedulerService.getJobList(withTrigger);
	}

	@RequestMapping(value = "/getJob")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_VIEW_JOB_INFO })
	@ResponseBody
	public Object getJob(
			final HttpServletRequest request,
			@LogRequestParam(name = "jobName") @RequestParam String jobName,
			@LogRequestParam(name = "jobGroup") @RequestParam(required = false) String jobGroup,
			@LogRequestParam(name = "withTrigger") @RequestParam(required = false, defaultValue = "false") boolean withTrigger)
			throws SchedulerException, ActionException {
		if (StringUtils.isBlank(jobName)) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"job name required!");
		}

		if (StringUtils.isBlank(jobGroup)) {
			jobGroup = null;
		}
		return schedulerService.getJob(jobName, jobGroup, withTrigger);
	}

	@RequestMapping(value = "/runOnceNow")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_EXEC_JOB })
	@ResponseBody
	public Object runOnceNow(
			final HttpServletRequest request,
			@LogRequestParam(name = "jobName") @RequestParam String jobName,
			@LogRequestParam(name = "jobGroup") @RequestParam(required = false) String jobGroup,
			@LogRequestParam(name = "jobClass") @RequestParam(required = false) String jobClass,
			@LogRequestParam(name = "jobDataMapJson") @RequestParam(required = false) String jobDataMapJson)
			throws SchedulerException {
		schedulerService
				.runOnceNow(jobName, jobGroup, jobClass, jobDataMapJson);
		return null;
	}

	@RequestMapping(value = "/interruptJob")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_EXEC_JOB })
	@ResponseBody
	public Object interruptJob(
			final HttpServletRequest request,
			@LogRequestParam(name = "jobName") @RequestParam String jobName,
			@LogRequestParam(name = "jobGroup") @RequestParam(required = false) String jobGroup)
			throws SchedulerException {
		return schedulerService.interruptJob(jobName, jobGroup);
	}

	@RequestMapping(value = "/getCurrentlyExecutingJobs")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_VIEW_JOB_INFO })
	@ResponseBody
	public Object getCurrentlyExecutingJobs(final HttpServletRequest request)
			throws SchedulerException {
		List<JobExecutionContextDto> contexts = schedulerService
				.getCurrentlyExecutingJobs();
		return contexts;
	}

	@RequestMapping(value = "/addJob")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_ADD_JOB })
	@ResponseBody
	public Object addJob(
			final HttpServletRequest request,
			@LogRequestParam(name = "jobClass") @RequestParam String jobClass,
			@LogRequestParam(name = "jobName") @RequestParam(required = false) String jobName,
			@LogRequestParam(name = "jobGroup") @RequestParam(required = false) String jobGroup,
			@LogRequestParam(name = "jobDataMapJson") @RequestParam(required = false) String jobDataMapJson,
			@LogRequestParam(name = "description") @RequestParam(required = false) String description,
			@LogRequestParam(name = "volatility") @RequestParam(required = false, defaultValue = "false") boolean volatility,
			@LogRequestParam(name = "durability") @RequestParam(required = false, defaultValue = "false") boolean durability,
			@LogRequestParam(name = "shouldRecover") @RequestParam(required = false, defaultValue = "false") boolean shouldRecover,
			@LogRequestParam(name = "replace") @RequestParam(required = false, defaultValue = "true") boolean replace)
			throws SchedulerException, ActionException {
		if (StringUtils.isBlank(jobClass)) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"job class required!");
		}
		if (StringUtils.isBlank(jobGroup)) {
			jobGroup = null;
		}
		if (StringUtils.isBlank(jobDataMapJson)) {
			jobDataMapJson = "{}";
		}
		JobDto jobDto = schedulerService.addJob(jobClass, jobName, jobGroup,
				jobDataMapJson, description, volatility, durability,
				shouldRecover, replace);
		return jobDto;
	}

	@RequestMapping(value = "/deleteJob")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_DEL_JOB })
	@ResponseBody
	public Object deleteJob(
			final HttpServletRequest request,
			@LogRequestParam(name = "jobName") @RequestParam String jobName,
			@LogRequestParam(name = "jobGroup") @RequestParam(required = false) String jobGroup)
			throws SchedulerException, ActionException {
		if (StringUtils.isBlank(jobName)) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"name illegal");
		}
		return schedulerService.deleteJob(jobName, jobGroup);
	}

	@RequestMapping(value = "/scheduleJob")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_EXEC_JOB })
	@ResponseBody
	public Object scheduleJob(
			final HttpServletRequest request,
			@LogRequestParam(name = "jobName") @RequestParam String jobName,
			@LogRequestParam(name = "jobGroup") @RequestParam(required = false) String jobGroup,
			@LogRequestParam(name = "triggerName") @RequestParam(required = false) String triggerName,
			@LogRequestParam(name = "triggerGroup") @RequestParam(required = false) String triggerGroup,
			@LogRequestParam(name = "startTime") @RequestParam(required = false) String startTime,
			@LogRequestParam(name = "endTime") @RequestParam(required = false) String endTime,
			@LogRequestParam(name = "repeatCount") @RequestParam(required = false) Integer repeatCount,
			@LogRequestParam(name = "repeatInterval") @RequestParam Long repeatInterval,
			@LogRequestParam(name = "description") @RequestParam(required = false) String description,
			@LogRequestParam(name = "misfireInstruction") @RequestParam(required = false, defaultValue = Trigger.MISFIRE_INSTRUCTION_SMART_POLICY
					+ "") int misfireInstruction) throws SchedulerException,
			ActionException {
		Date start = null;
		Date end = null;
		if (StringUtils.isNotBlank(startTime)) {
			start = TimeUtils.parseDate(startTime);
			if (start == null) {
				throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
						"start time format error");
			}
		}

		if (StringUtils.isNotBlank(endTime)) {
			end = TimeUtils.parseDate(endTime);
			if (end == null) {
				throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
						"end time format error");
			}
		}

		if (repeatCount != null && repeatCount < 0) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"repeat count must be >= 0, use null for infinite.");
		}

		if (repeatInterval == null || repeatInterval < 0) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"repeat interval must be >= 0");
		}
		if (StringUtils.isBlank(jobGroup)) {
			jobGroup = null;
		}
		if (StringUtils.isBlank(triggerGroup)) {
			triggerGroup = null;
		}
		return TimeUtils.defaultFormat(schedulerService.scheduleJob(
				jobName, jobGroup, triggerName, triggerGroup, start, end,
				repeatCount, repeatInterval, description, misfireInstruction));
	}

	@RequestMapping(value = "/scheduleCronJob")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_EXEC_JOB })
	@ResponseBody
	public Object scheduleCronJob(
			final HttpServletRequest request,
			@LogRequestParam(name = "jobName") @RequestParam String jobName,
			@LogRequestParam(name = "jobGroup") @RequestParam(required = false) String jobGroup,
			@LogRequestParam(name = "triggerName") @RequestParam(required = false) String triggerName,
			@LogRequestParam(name = "triggerGroup") @RequestParam(required = false) String triggerGroup,
			@LogRequestParam(name = "cronEx") @RequestParam String cronEx,
			@LogRequestParam(name = "description") @RequestParam(required = false) String description,
			@LogRequestParam(name = "misfireInstruction") @RequestParam(required = false, defaultValue = Trigger.MISFIRE_INSTRUCTION_SMART_POLICY
					+ "") int misfireInstruction) throws SchedulerException,
			ActionException {
		CronExpression cronExpression = null;
		try {
			cronExpression = new CronExpression(cronEx);
		} catch (ParseException e) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"cron expression illegal");
		}
		if (StringUtils.isBlank(jobGroup)) {
			jobGroup = null;
		}
		if (StringUtils.isBlank(triggerGroup)) {
			triggerGroup = null;
		}
		return TimeUtils.defaultFormat(schedulerService
				.scheduleCronJob(jobName, jobGroup, triggerName, triggerGroup,
						cronExpression, description, misfireInstruction));
	}

	@RequestMapping(value = "/rescheduleJob")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_EXEC_JOB })
	@ResponseBody
	public Object rescheduleJob(
			final HttpServletRequest request,
			@LogRequestParam(name = "triggerName") @RequestParam String triggerName,
			@LogRequestParam(name = "triggerGroup") @RequestParam(required = false) String triggerGroup,
			@LogRequestParam(name = "startTime") @RequestParam(required = false) String startTime,
			@LogRequestParam(name = "endTime") @RequestParam(required = false) String endTime,
			@LogRequestParam(name = "repeatCount") @RequestParam(required = false) Integer repeatCount,
			@LogRequestParam(name = "repeatInterval") @RequestParam Long repeatInterval,
			@LogRequestParam(name = "description") @RequestParam(required = false) String description,
			@LogRequestParam(name = "misfireInstruction") @RequestParam(required = false, defaultValue = Trigger.MISFIRE_INSTRUCTION_SMART_POLICY
					+ "") int misfireInstruction) throws SchedulerException,
			ActionException {

		if (StringUtils.isBlank(triggerName)) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"triggerName illegal");
		}
		Date start = null;
		Date end = null;
		if (startTime != null) {
			start = TimeUtils.parseDate(startTime);
			if (start == null) {
				throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
						"start time format error");
			}
		}

		if (endTime != null) {
			end = TimeUtils.parseDate(endTime);
			if (end == null) {
				throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
						"end time format error");
			}
		}

		if (repeatCount != null && repeatCount < 0) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"repeat count must be >= 0, use null for infinite.");
		}

		if (repeatInterval == null || repeatInterval < 0) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"repeat interval must be >= 0");
		}
		return TimeUtils.defaultFormat(schedulerService.rescheduleJob(
				triggerName, triggerGroup, start, end, repeatCount,
				repeatInterval, description, misfireInstruction));
	}

	@RequestMapping(value = "/rescheduleCronJob")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_EXEC_JOB })
	@ResponseBody
	public Object rescheduleCronJob(
			final HttpServletRequest request,
			@LogRequestParam(name = "triggerName") @RequestParam String triggerName,
			@LogRequestParam(name = "triggerGroup") @RequestParam(required = false) String triggerGroup,
			@LogRequestParam(name = "cronEx") @RequestParam String cronEx,
			@LogRequestParam(name = "description") @RequestParam(required = false) String description,
			@LogRequestParam(name = "misfireInstruction") @RequestParam(required = false, defaultValue = Trigger.MISFIRE_INSTRUCTION_SMART_POLICY
					+ "") int misfireInstruction) throws SchedulerException,
			ActionException {
		if (StringUtils.isBlank(triggerName)) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"triggerName illegal");
		}
		CronExpression cronExpression = null;
		try {
			cronExpression = new CronExpression(cronEx);
		} catch (ParseException e) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"cron expression illegal");
		}
		return TimeUtils.defaultFormat(schedulerService
				.rescheduleCronJob(triggerName, triggerGroup, cronExpression,
						description, misfireInstruction));
	}

	@RequestMapping(value = "/unscheduleJob")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_EXEC_JOB })
	@ResponseBody
	public Object unscheduleJob(
			final HttpServletRequest request,
			@LogRequestParam(name = "triggerName") @RequestParam String triggerName,
			@LogRequestParam(name = "triggerGroup") @RequestParam(required = false) String triggerGroup)
			throws SchedulerException, ActionException {

		if (StringUtils.isBlank(triggerName)) {
			throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
					"triggerName illegal");
		}

		return schedulerService.unscheduleJob(triggerName, triggerGroup);
	}

	@RequestMapping(value = "/pauseTrigger")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_EXEC_JOB })
	@ResponseBody
	public Object pauseTrigger(
			final HttpServletRequest request,
			@LogRequestParam(name = "triggerName") @RequestParam String triggerName,
			@LogRequestParam(name = "triggerGroup") @RequestParam(required = false) String triggerGroup)
			throws SchedulerException, ActionException {
		schedulerService.pauseTrigger(triggerName, triggerGroup);
		return null;
	}

	@RequestMapping(value = "/resumeTrigger")
	@AuthRequired(permission = { AuthConstants.PERM_SCHEDULER.PERM_EXEC_JOB })
	@ResponseBody
	public Object resumeTrigger(
			final HttpServletRequest request,
			@LogRequestParam(name = "triggerName") @RequestParam String triggerName,
			@LogRequestParam(name = "triggerGroup") @RequestParam(required = false) String triggerGroup)
			throws SchedulerException, ActionException {
		schedulerService.resumeTrigger(triggerName, triggerGroup);
		return null;
	}

}
