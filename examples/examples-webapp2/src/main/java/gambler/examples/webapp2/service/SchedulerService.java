package gambler.examples.webapp2.service;

import gambler.examples.webapp2.exception.ActionException;
import gambler.examples.webapp2.resp.ResponseStatus;
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

    private String avoidNullName(String name) {
        if (StringUtils.isBlank(name)) {
            return UUID.randomUUID().toString();
        } else {
            return name;
        }

    }
    
    public List getCurrentlyExecutingJobs() throws SchedulerException{
        return scheduler.getCurrentlyExecutingJobs();
    }

    public void triggerJobWithVolatileTrigger(String jobName, String jobGroup, JobDataMap jobDataMap) throws SchedulerException {
        scheduler.triggerJobWithVolatileTrigger(jobName, jobGroup, jobDataMap);
    }

    public Date rescheduleJob(String triggerName, String triggerGroup, Date startTime,
            Date endTime, Integer repeatCount, Long repeatInterval) throws SchedulerException {
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
        return scheduler.rescheduleJob(triggerName, triggerGroup, newTrigger);
    }

    public Date rescheduleCronJob(String triggerName, String triggerGroup,
            CronExpression cronExpression) throws SchedulerException {
        Trigger oldTrigger = scheduler.getTrigger(triggerName, triggerGroup);
        CronTrigger newTrigger = new CronTrigger(triggerName,
                triggerGroup, oldTrigger.getJobName(), oldTrigger.getJobGroup());
        newTrigger.setCronExpression(cronExpression);
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
    public JobDetail addJob(String jobClassName, String jobName,
            String jobGroup, String jobDataMapJson, String description,
            boolean volatility, boolean durability, boolean shouldRecover,
            boolean replace) throws SchedulerException {
        Class jobClass = null;
        try {
            jobClass = Class.forName(jobClassName);
        } catch (ClassNotFoundException e) {
            throw new SchedulerException("add job " + jobClassName + " error!",
                    e);
        }
        JobDetail jobDetail = new JobDetail(avoidNullName(jobName),
                jobGroup, jobClass, volatility, durability, shouldRecover);
        jobDetail.setDescription(description);
        JSONObject fromObject = JSONObject.fromObject(jobDataMapJson);
        Set keySet = fromObject.keySet();
        for (Object paramname : keySet) {
            jobDetail.getJobDataMap().put(paramname,
                    fromObject.get(paramname));

        }
        scheduler.addJob(jobDetail, replace);
        return jobDetail;
    }

    public Date scheduleJob(String jobName, String jobGroup,
            String triggerName, String triggerGroup, Date startTime,
            Date endTime, Integer repeatCount, Long repeatInterval)
            throws SchedulerException {
        if (startTime == null) {
            startTime = new Date(System.currentTimeMillis());
        }
        if (repeatCount == null) {
            repeatCount = SimpleTrigger.REPEAT_INDEFINITELY;
        }
        SimpleTrigger trigger = new SimpleTrigger(avoidNullName(triggerName),
                triggerGroup, jobName, jobGroup, startTime, endTime,
                repeatCount, repeatInterval);
        return scheduler.scheduleJob(trigger);
    }

    public boolean unscheduleJob(String triggerName, String triggerGroup)
            throws SchedulerException {
        return scheduler.unscheduleJob(triggerName, triggerGroup);
    }

    public Date scheduleCronJob(String jobName, String jobGroup,
            String triggerName, String triggerGroup,
            CronExpression cronExpression) throws SchedulerException {
        CronTrigger cronTrigger = new CronTrigger(avoidNullName(triggerName),
                triggerGroup, jobName, jobGroup);
        cronTrigger.setCronExpression(cronExpression);
        return scheduler.scheduleJob(cronTrigger);
    }

    public void start() throws SchedulerException, ActionException {
        if (scheduler.isShutdown()) {
            throw new ActionException(ResponseStatus.SERVICE_UNAVAILABLE, "scheduler has been shutdown, please recycle the server!");
        }
        scheduler.start();
    }

    public void standby() throws SchedulerException, ActionException {
        if (scheduler.isShutdown()) {
            throw new ActionException(ResponseStatus.SERVICE_UNAVAILABLE, "scheduler has been shutdown, please recycle the server!");
        }
        if (scheduler.isStarted()) {
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
