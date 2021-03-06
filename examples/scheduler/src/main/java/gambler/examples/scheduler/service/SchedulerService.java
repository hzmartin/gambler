package gambler.examples.scheduler.service;

import gambler.commons.util.time.TimeUtils;
import gambler.examples.scheduler.dto.JobDto;
import gambler.examples.scheduler.dto.JobExecutionContextDto;
import gambler.examples.scheduler.dto.SchedulerDto;
import gambler.examples.scheduler.dto.TriggerDto;
import gambler.examples.scheduler.exception.ActionException;
import gambler.examples.scheduler.resp.ResponseStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.UnableToInterruptJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("schedulerService")
public class SchedulerService {

	@Autowired
	@Qualifier("quartzScheduler")
	private Scheduler scheduler;

	private String avoidNullWithUuid(String str) {
		if (StringUtils.isBlank(str)) {
			return UUID.randomUUID().toString();
		} else {
			return str;
		}

	}

	public List<TriggerDto> getTriggerList() throws SchedulerException {
		List<TriggerDto> triggerDtos = new ArrayList<TriggerDto>();
		String[] triggerGroupNames = scheduler.getTriggerGroupNames();
		for (String triggerGroupName : triggerGroupNames) {
			String[] triggerNames = scheduler.getTriggerNames(triggerGroupName);
			for (String triggerName : triggerNames) {
				TriggerDto triggerDto = getTrigger(triggerName,
						triggerGroupName);
				if (triggerDto != null) {
					triggerDtos.add(triggerDto);
				}
			}
		}
		return triggerDtos;
	}

	public TriggerDto getTrigger(String triggerName, String triggerGroup)
			throws SchedulerException {
		TriggerDto tDto = new TriggerDto();
		int state = scheduler.getTriggerState(triggerName, triggerGroup);
		Trigger trigger = scheduler.getTrigger(triggerName, triggerGroup);
		if (trigger == null) {
			return null;
		}
		tDto.setName(trigger.getName());
		tDto.setGroup(trigger.getGroup());
		tDto.setDescription(trigger.getDescription());
		tDto.setJobName(trigger.getJobName());
		tDto.setJobGroup(trigger.getJobGroup());
		tDto.setPreviousFireTime(TimeUtils.defaultFormat(trigger
				.getPreviousFireTime()));
		tDto.setNextFireTime(TimeUtils.defaultFormat(trigger
				.getNextFireTime()));
		tDto.setStartTime(TimeUtils.defaultFormat(trigger
				.getStartTime()));
		tDto.setEndTime(TimeUtils.defaultFormat(trigger
				.getEndTime()));
		tDto.setType(trigger.getClass().getSimpleName());
		tDto.setState(state);
		return tDto;
	}

	public int getTriggerState(String triggerName, String triggerGroup)
			throws SchedulerException {
		return scheduler.getTriggerState(triggerName, triggerGroup);
	}

	/**
	 * 
	 * @see #getJob(java.lang.String, java.lang.String, boolean)
	 */
	public List<JobDto> getJobList(boolean withTrigger)
			throws SchedulerException {
		List<JobDto> jobDtos = new ArrayList<JobDto>();
		String[] jobGroupNames = scheduler.getJobGroupNames();
		for (String jobGroup : jobGroupNames) {
			String[] jobNames = scheduler.getJobNames(jobGroup);
			for (String jobName : jobNames) {
				JobDto job = getJob(jobName, jobGroup, withTrigger);
				if (job != null) {
					jobDtos.add(job);
				}
			}
		}
		return jobDtos;
	}

	/**
	 * get job detail and related triggers if <code>withTrigger</code> is true
	 */
	public JobDto getJob(String jobName, String jobGroup, boolean withTrigger)
			throws SchedulerException {
		JobDetail jobDetail = scheduler.getJobDetail(jobName, jobGroup);
		if (jobDetail == null) {
			return null;
		}
		JobDto jobDto = new JobDto();
		jobDto.setName(jobName);
		jobDto.setGroup(jobGroup);
		jobDto.setDescription(jobDetail.getDescription());
		jobDto.setJobClass(jobDetail.getJobClass().getName());
		jobDto.setJobDataMap(jobDetail.getJobDataMap());
		if (withTrigger) {
			Trigger[] triggers = scheduler.getTriggersOfJob(jobName, jobGroup);
			if (triggers != null && triggers.length > 0) {
				List<TriggerDto> triggerDtos = new ArrayList<TriggerDto>();
				for (Trigger trigger : triggers) {
					TriggerDto tDto = new TriggerDto();
					tDto.setJobName(jobName);
					tDto.setJobGroup(jobGroup);
					tDto.setName(trigger.getName());
					tDto.setGroup(trigger.getGroup());
					tDto.setDescription(trigger.getDescription());
					tDto.setPreviousFireTime(TimeUtils
							.defaultFormat(trigger
									.getPreviousFireTime()));
					tDto.setNextFireTime(TimeUtils
							.defaultFormat(trigger.getNextFireTime()));
					tDto.setMisfireInstruction(trigger.getMisfireInstruction());
					int state = scheduler.getTriggerState(trigger.getName(),
							trigger.getGroup());
					tDto.setState(state);
					tDto.setType(trigger.getClass().getSimpleName());
					triggerDtos.add(tDto);
				}
				jobDto.setTriggers(triggerDtos);
			}
		}
		return jobDto;
	}

	public boolean interruptJob(String jobName, String groupName)
			throws UnableToInterruptJobException {
		return scheduler.interrupt(jobName, groupName);
	}

	@SuppressWarnings("unchecked")
	public List<JobExecutionContextDto> getCurrentlyExecutingJobs()
			throws SchedulerException {
		List<JobExecutionContext> contexts = scheduler
				.getCurrentlyExecutingJobs();
		List<JobExecutionContextDto> contextDtoList = new ArrayList<JobExecutionContextDto>();
		for (JobExecutionContext context : contexts) {
			JobExecutionContextDto contextDto = new JobExecutionContextDto();
			JobDetail jobDetail = context.getJobDetail();
			contextDto.setJobName(jobDetail.getName());
			contextDto.setJobGroup(jobDetail.getGroup());
			contextDto.setJobDescription(jobDetail.getDescription());
			contextDto.setJobClass(jobDetail.getJobClass().getName());
			contextDto.setJobDataMap(jobDetail.getJobDataMap());
			Trigger trigger = context.getTrigger();
			contextDto.setTriggerName(trigger.getName());
			contextDto.setTriggerGroup(trigger.getGroup());
			contextDto.setTriggerDescription(trigger.getDescription());
			contextDto.setFireTime(TimeUtils.defaultFormat(context
					.getFireTime()));
			contextDto.setNextFireTime(TimeUtils
					.defaultFormat(context.getNextFireTime()));
			contextDto.setPreviousFireTime(TimeUtils
					.defaultFormat(context.getPreviousFireTime()));
			contextDto.setScheduledFireTime(TimeUtils
					.defaultFormat(context.getScheduledFireTime()));
			contextDtoList.add(contextDto);

		}
		return contextDtoList;
	}

	@SuppressWarnings("rawtypes")
	public void runOnceNow(String jobName, String jobGroup, String jobClass,
			String jobDataMapJson) throws SchedulerException {

		if (StringUtils.isNotBlank(jobClass)) {
			addJob(jobClass, jobName, jobGroup, jobDataMapJson, null, false,
					false, false, true);
			scheduler.triggerJobWithVolatileTrigger(jobName, jobGroup);
		} else {
			JSONObject fromObject = JSONObject.fromObject(jobDataMapJson);
			Set keySet = fromObject.keySet();
			JobDataMap jobDataMap = new JobDataMap();
			for (Object paramname : keySet) {
				jobDataMap.put(paramname, fromObject.get(paramname));

			}
			scheduler.triggerJobWithVolatileTrigger(jobName, jobGroup,
					jobDataMap);
		}
	}

	public Date rescheduleJob(String triggerName, String triggerGroup,
			Date startTime, Date endTime, Integer repeatCount,
			Long repeatInterval, String decription, int misfireInstruction)
			throws SchedulerException {
		if (startTime == null) {
			startTime = new Date(System.currentTimeMillis());
		}
		if (repeatCount == null) {
			repeatCount = SimpleTrigger.REPEAT_INDEFINITELY;
		}
		Trigger oldTrigger = scheduler.getTrigger(triggerName, triggerGroup);
		SimpleTrigger newTrigger = new SimpleTrigger(triggerName, triggerGroup,
				oldTrigger.getJobName(), oldTrigger.getJobGroup(), startTime,
				endTime, repeatCount, repeatInterval);
		newTrigger.setDescription(decription);
		newTrigger.setMisfireInstruction(misfireInstruction);
		return scheduler.rescheduleJob(triggerName, triggerGroup, newTrigger);
	}

	public Date rescheduleCronJob(String triggerName, String triggerGroup,
			CronExpression cronExpression, String decription,
			int misfireInstruction) throws SchedulerException {
		Trigger oldTrigger = scheduler.getTrigger(triggerName, triggerGroup);
		CronTrigger newTrigger = new CronTrigger(triggerName, triggerGroup,
				oldTrigger.getJobName(), oldTrigger.getJobGroup());
		newTrigger.setCronExpression(cronExpression);
		newTrigger.setDescription(decription);
		newTrigger.setMisfireInstruction(misfireInstruction);
		return scheduler.rescheduleJob(triggerName, triggerGroup, newTrigger);
	}

	public void pauseTrigger(String triggerName, String triggerGroup)
			throws SchedulerException {
		scheduler.pauseTrigger(triggerName, triggerGroup);

	}

	public void resumeTrigger(String triggerName, String triggerGroup)
			throws SchedulerException {
		scheduler.resumeTrigger(triggerName, triggerGroup);
	}

	public boolean deleteJob(String jobName, String jobGroup)
			throws SchedulerException {
		return scheduler.deleteJob(jobName, jobGroup);
	}

	@SuppressWarnings("rawtypes")
	public JobDto addJob(String jobClass, String jobName, String jobGroup,
			String jobDataMapJson, String description, boolean volatility,
			boolean durability, boolean shouldRecover, boolean replace)
			throws SchedulerException {
		Class jobClazz = null;
		try {
			jobClazz = Class.forName(jobClass);
		} catch (ClassNotFoundException e) {
			throw new SchedulerException("job class " + jobClass
					+ " not found!", e);
		}
		JobDetail jobDetail = new JobDetail(avoidNullWithUuid(jobName),
				jobGroup, jobClazz, volatility, durability, shouldRecover);
		jobDetail.setDescription(description);
		JSONObject fromObject = JSONObject.fromObject(jobDataMapJson);
		Set keySet = fromObject.keySet();
		for (Object paramname : keySet) {
			jobDetail.getJobDataMap().put(paramname, fromObject.get(paramname));

		}
		scheduler.addJob(jobDetail, replace);
		JobDto jobDto = new JobDto();
		jobDto.setName(jobDetail.getName());
		jobDto.setGroup(jobDetail.getGroup());
		return jobDto;
	}

	public Date scheduleJob(String jobName, String jobGroup,
			String triggerName, String triggerGroup, Date startTime,
			Date endTime, Integer repeatCount, Long repeatInterval,
			String decription, int misfireInstruction)
			throws SchedulerException {
		if (startTime == null) {
			startTime = new Date(System.currentTimeMillis());
		}
		if (repeatCount == null) {
			repeatCount = SimpleTrigger.REPEAT_INDEFINITELY;
		}
		SimpleTrigger trigger = new SimpleTrigger(
				avoidNullWithUuid(triggerName), triggerGroup, jobName,
				jobGroup, startTime, endTime, repeatCount, repeatInterval);
		trigger.setDescription(decription);
		trigger.setMisfireInstruction(misfireInstruction);
		return scheduler.scheduleJob(trigger);
	}

	public boolean unscheduleJob(String triggerName, String triggerGroup)
			throws SchedulerException {
		return scheduler.unscheduleJob(triggerName, triggerGroup);
	}

	public Date scheduleCronJob(String jobName, String jobGroup,
			String triggerName, String triggerGroup,
			CronExpression cronExpression, String decription,
			int misfireInstruction) throws SchedulerException {
		CronTrigger cronTrigger = new CronTrigger(
				avoidNullWithUuid(triggerName), triggerGroup, jobName, jobGroup);
		cronTrigger.setCronExpression(cronExpression);
		cronTrigger.setDescription(decription);
		cronTrigger.setMisfireInstruction(misfireInstruction);
		return scheduler.scheduleJob(cronTrigger);
	}

	public void start() throws SchedulerException, ActionException {
		if (scheduler.isShutdown()) {
			throw new ActionException(ResponseStatus.SERVICE_UNAVAILABLE,
					"scheduler has been shutdown, please recycle the server!");
		}
		if (scheduler.isInStandbyMode()) {
			scheduler.start();
			return;
		}
		if (scheduler.isStarted()) {
			throw new ActionException("scheduler had been started!");
		}
	}

	public void standby() throws SchedulerException, ActionException {
		if (scheduler.isShutdown()) {
			throw new ActionException(ResponseStatus.SERVICE_UNAVAILABLE,
					"scheduler has been shutdown, please recycle the server!");
		}
		if (scheduler.isInStandbyMode()) {
			throw new ActionException("scheduler had been paused!");
		} else {
			scheduler.standby();
		}
	}

	public void shutdown(boolean waitForJobsToComplete)
			throws SchedulerException, ActionException {
		if (scheduler.isShutdown()) {
			throw new ActionException("scheduler had been shutdown!");
		}
		scheduler.shutdown(waitForJobsToComplete);
	}

	public SchedulerDto getSchedulerInfo() throws SchedulerException {
		SchedulerDto scheduleDto = new SchedulerDto();
		SchedulerMetaData metaData = scheduler.getMetaData();
		scheduleDto.setSchedulerName(metaData.getSchedulerName());
		scheduleDto.setSchedulerInstanceId(metaData.getSchedulerInstanceId());
		String state = "-";
		if (metaData.isStarted()) {
			state = "started";
		}
		if (metaData.isInStandbyMode()) {
			state = "standby";
		}
		if (metaData.isShutdown()) {
			state = "shutdown";
		}
		scheduleDto.setState(state);
		scheduleDto.setRunningSince(metaData.getRunningSince().toString());
		scheduleDto.setThreadPoolSize(metaData.getThreadPoolSize());
		scheduleDto.setSummary(metaData.getSummary());
		scheduleDto.setVersion(metaData.getVersion());
		scheduleDto.setNumberOfJobsExecuted(metaData.getNumberOfJobsExecuted());
		return scheduleDto;
	}
}
