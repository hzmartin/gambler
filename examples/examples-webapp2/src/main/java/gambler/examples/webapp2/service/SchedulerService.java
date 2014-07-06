package gambler.examples.webapp2.service;

import gambler.examples.webapp2.dto.JobDto;
import gambler.examples.webapp2.dto.JobExecutionContextDto;
import gambler.examples.webapp2.dto.TriggerDto;
import gambler.examples.webapp2.exception.ActionException;
import gambler.examples.webapp2.resp.ResponseStatus;
import gambler.examples.webapp2.util.TimeTagUtil;
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
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
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
                TriggerDto triggerDto = getTrigger(triggerName, triggerGroupName);
                if (triggerDto != null) {
                    triggerDtos.add(triggerDto);
                }
            }
        }
        return triggerDtos;
    }

    public TriggerDto getTrigger(String triggerName, String triggerGroup) throws SchedulerException {
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
        tDto.setPreviousFireTime(TimeTagUtil.format_yyyyMMddHHmmss(trigger.getPreviousFireTime()));
        tDto.setNextFireTime(TimeTagUtil.format_yyyyMMddHHmmss(trigger.getNextFireTime()));
        tDto.setState(state);
        return tDto;
    }

    public int getTriggerState(String triggerName, String triggerGroup) throws SchedulerException {
        return scheduler.getTriggerState(triggerName, triggerGroup);
    }

    /**
     *
     * @see #getJob(java.lang.String, java.lang.String, boolean)
     */
    public List<JobDto> getJobList(boolean withTrigger) throws SchedulerException {
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
    public JobDto getJob(String jobName, String jobGroup, boolean withTrigger) throws SchedulerException {
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
                    tDto.setPreviousFireTime(TimeTagUtil.format_yyyyMMddHHmmss(trigger.getPreviousFireTime()));
                    tDto.setNextFireTime(TimeTagUtil.format_yyyyMMddHHmmss(trigger.getNextFireTime()));
                    tDto.setMisfireInstruction(trigger.getMisfireInstruction());
                    int state = scheduler.getTriggerState(trigger.getName(), trigger.getGroup());
                    tDto.setState(state);
                    triggerDtos.add(tDto);
                }
                jobDto.setTriggers(triggerDtos);
            }
        }
        return jobDto;
    }

    public List<JobExecutionContextDto> getCurrentlyExecutingJobs() throws SchedulerException {
        List<JobExecutionContext> contexts = scheduler.getCurrentlyExecutingJobs();
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
            contextDto.setFireTime(TimeTagUtil.format_yyyyMMddHHmmss(context.getFireTime()));
            contextDto.setNextFireTime(TimeTagUtil.format_yyyyMMddHHmmss(context.getNextFireTime()));
            contextDto.setPreviousFireTime(TimeTagUtil.format_yyyyMMddHHmmss(context.getPreviousFireTime()));
            contextDto.setScheduledFireTime(TimeTagUtil.format_yyyyMMddHHmmss(context.getScheduledFireTime()));
            contextDtoList.add(contextDto);

        }
        return contextDtoList;
    }

    public void runOnceNow(String jobName, String jobGroup, String jobClass, String jobDataMapJson) throws SchedulerException {

        if (StringUtils.isNotBlank(jobClass)) {
            addJob(jobClass, jobName, jobGroup, jobDataMapJson, null, false, false, false, true);
            scheduler.triggerJobWithVolatileTrigger(jobName, jobGroup);
        } else {
            JSONObject fromObject = JSONObject.fromObject(jobDataMapJson);
            Set keySet = fromObject.keySet();
            JobDataMap jobDataMap = new JobDataMap();
            for (Object paramname : keySet) {
                jobDataMap.put(paramname,
                        fromObject.get(paramname));

            }
            scheduler.triggerJobWithVolatileTrigger(jobName, jobGroup, jobDataMap);
        }
    }

    public Date rescheduleJob(String triggerName, String triggerGroup, Date startTime,
            Date endTime, Integer repeatCount, Long repeatInterval, String decription) throws SchedulerException {
        if (startTime == null) {
            startTime = new Date(System.currentTimeMillis());
        }
        if (repeatCount == null) {
            repeatCount = SimpleTrigger.REPEAT_INDEFINITELY;
        }
        Trigger oldTrigger = scheduler.getTrigger(triggerName, triggerGroup);
        SimpleTrigger newTrigger = new SimpleTrigger(triggerName,
                triggerGroup, oldTrigger.getJobName(), oldTrigger.getJobGroup(), startTime, endTime,
                repeatCount, repeatInterval);
        newTrigger.setDescription(decription);
        return scheduler.rescheduleJob(triggerName, triggerGroup, newTrigger);
    }

    public Date rescheduleCronJob(String triggerName, String triggerGroup,
            CronExpression cronExpression, String decription) throws SchedulerException {
        Trigger oldTrigger = scheduler.getTrigger(triggerName, triggerGroup);
        CronTrigger newTrigger = new CronTrigger(triggerName,
                triggerGroup, oldTrigger.getJobName(), oldTrigger.getJobGroup());
        newTrigger.setCronExpression(cronExpression);
        newTrigger.setDescription(decription);
        return scheduler.rescheduleJob(triggerName, triggerGroup, newTrigger);
    }

    public void pauseTrigger(String triggerName, String triggerGroup) throws SchedulerException {
        scheduler.pauseTrigger(triggerName, triggerGroup);

    }

    public void resumeTrigger(String triggerName, String triggerGroup) throws SchedulerException {
        scheduler.resumeTrigger(triggerName, triggerGroup);
    }

    public boolean deleteJob(String jobName, String jobGroup) throws SchedulerException {
        return scheduler.deleteJob(jobName, jobGroup);
    }

    @SuppressWarnings("rawtypes")
    public JobDto addJob(String jobClass, String jobName,
            String jobGroup, String jobDataMapJson, String description,
            boolean volatility, boolean durability, boolean shouldRecover,
            boolean replace) throws SchedulerException {
        Class jobClazz = null;
        try {
            jobClazz = Class.forName(jobClass);
        } catch (ClassNotFoundException e) {
            throw new SchedulerException("add job " + jobClass + " error!",
                    e);
        }
        JobDetail jobDetail = new JobDetail(avoidNullWithUuid(jobName),
                jobGroup, jobClazz, volatility, durability, shouldRecover);
        jobDetail.setDescription(description);
        JSONObject fromObject = JSONObject.fromObject(jobDataMapJson);
        Set keySet = fromObject.keySet();
        for (Object paramname : keySet) {
            jobDetail.getJobDataMap().put(paramname,
                    fromObject.get(paramname));

        }
        scheduler.addJob(jobDetail, replace);
        JobDto jobDto = new JobDto();
        jobDto.setName(jobDetail.getName());
        jobDto.setGroup(jobDetail.getGroup());
        return jobDto;
    }

    public Date scheduleJob(String jobName, String jobGroup,
            String triggerName, String triggerGroup, Date startTime,
            Date endTime, Integer repeatCount, Long repeatInterval, String decription)
            throws SchedulerException {
        if (startTime == null) {
            startTime = new Date(System.currentTimeMillis());
        }
        if (repeatCount == null) {
            repeatCount = SimpleTrigger.REPEAT_INDEFINITELY;
        }
        SimpleTrigger trigger = new SimpleTrigger(avoidNullWithUuid(triggerName),
                triggerGroup, jobName, jobGroup, startTime, endTime,
                repeatCount, repeatInterval);
        trigger.setDescription(decription);
        return scheduler.scheduleJob(trigger);
    }

    public boolean unscheduleJob(String triggerName, String triggerGroup)
            throws SchedulerException {
        return scheduler.unscheduleJob(triggerName, triggerGroup);
    }

    public Date scheduleCronJob(String jobName, String jobGroup,
            String triggerName, String triggerGroup,
            CronExpression cronExpression, String decription) throws SchedulerException {
        CronTrigger cronTrigger = new CronTrigger(avoidNullWithUuid(triggerName),
                triggerGroup, jobName, jobGroup);
        cronTrigger.setCronExpression(cronExpression);
        cronTrigger.setDescription(decription);
        return scheduler.scheduleJob(cronTrigger);
    }

    public void start() throws SchedulerException, ActionException {
        if (scheduler.isShutdown()) {
            throw new ActionException(ResponseStatus.SERVICE_UNAVAILABLE, "scheduler has been shutdown, please recycle the server!");
        }
        if (scheduler.isInStandbyMode()) {
            scheduler.start();
        }
    }

    public void standby() throws SchedulerException, ActionException {
        if (scheduler.isShutdown()) {
            throw new ActionException(ResponseStatus.SERVICE_UNAVAILABLE, "scheduler has been shutdown, please recycle the server!");
        }
        if (!scheduler.isInStandbyMode()) {
            scheduler.standby();
        }
    }

    public void shutdown(boolean waitForJobsToComplete) throws SchedulerException {
        if (scheduler.isShutdown()) {
            return;
        }
        scheduler.shutdown(waitForJobsToComplete);
    }
}
