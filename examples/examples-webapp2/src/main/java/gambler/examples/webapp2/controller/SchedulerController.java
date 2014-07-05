package gambler.examples.webapp2.controller;

import gambler.examples.webapp2.annotation.AuthRequired;
import gambler.examples.webapp2.annotation.LogRequestParam;
import gambler.examples.webapp2.exception.ActionException;
import gambler.examples.webapp2.resp.ResponseStatus;
import gambler.examples.webapp2.service.SchedulerService;
import gambler.examples.webapp2.util.TimeTagUtil;
import java.text.ParseException;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronExpression;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/scheduler")
public class SchedulerController extends AbstractController {

    @Resource
    private SchedulerService schedulerService;

    @RequestMapping(value = "/addJob")
    @AuthRequired
    @ResponseBody
    public Object addJob(
            final HttpServletRequest request,
            @LogRequestParam(name = "jobClass") @RequestParam String jobClassName,
            @LogRequestParam(name = "jobName") @RequestParam(required = false) String jobName,
            @LogRequestParam(name = "jobGroup") @RequestParam(required = false) String jobGroup,
            @LogRequestParam(name = "jobDataMapJson") @RequestParam(required = false) String jobDataMapJson,
            @LogRequestParam(name = "description") @RequestParam(required = false) String description,
            @LogRequestParam(name = "volatility") @RequestParam(required = false, defaultValue = "false") boolean volatility,
            @LogRequestParam(name = "durability") @RequestParam(required = false, defaultValue = "false") boolean durability,
            @LogRequestParam(name = "shouldRecover") @RequestParam(required = false, defaultValue = "false") boolean shouldRecover,
            @LogRequestParam(name = "replace") @RequestParam(required = false, defaultValue = "true") boolean replace)
            throws SchedulerException, ActionException {
        JobDetail jobDetail = schedulerService.addJob(jobClassName, jobName,
                jobGroup, jobDataMapJson, description, volatility, durability,
                shouldRecover, replace);
        JSONObject object = new JSONObject();
        object.put("name", jobDetail.getName());
        object.put("group", jobDetail.getGroup());
        return object;
    }

    @RequestMapping(value = "/deleteJob")
    @AuthRequired
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
    @AuthRequired
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
            @LogRequestParam(name = "repeatInterval") @RequestParam Long repeatInterval)
            throws SchedulerException, ActionException {
        Date start = null;
        Date end = null;
        if (startTime != null) {
            try {
                start = TimeTagUtil.parseDate(startTime);
            } catch (ParseException e) {
                throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
                        "start time format error");
            }
        }

        if (endTime != null) {
            try {
                end = TimeTagUtil.parseDate(endTime);
            } catch (ParseException e) {
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
        return schedulerService.scheduleJob(jobName, jobGroup, triggerName,
                triggerGroup, start, end, repeatCount, repeatInterval);
    }

    @RequestMapping(value = "/scheduleCronJob")
    @AuthRequired
    @ResponseBody
    public Object scheduleCronJob(
            final HttpServletRequest request,
            @LogRequestParam(name = "jobName") @RequestParam String jobName,
            @LogRequestParam(name = "jobGroup") @RequestParam(required = false) String jobGroup,
            @LogRequestParam(name = "triggerName") @RequestParam(required = false) String triggerName,
            @LogRequestParam(name = "triggerGroup") @RequestParam(required = false) String triggerGroup,
            @LogRequestParam(name = "cronEx") @RequestParam String cronEx)
            throws SchedulerException, ActionException {
        CronExpression cronExpression = null;
        try {
            cronExpression = new CronExpression(cronEx);
        } catch (ParseException e) {
            throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
                    "cron expression illegal");
        }
        return schedulerService.scheduleCronJob(jobName, jobGroup, triggerName,
                triggerGroup, cronExpression);
    }

    @RequestMapping(value = "/rescheduleJob")
    @AuthRequired
    @ResponseBody
    public Object rescheduleJob(
            final HttpServletRequest request,
            @LogRequestParam(name = "triggerName") @RequestParam String triggerName,
            @LogRequestParam(name = "triggerGroup") @RequestParam(required = false) String triggerGroup,
            @LogRequestParam(name = "startTime") @RequestParam(required = false) String startTime,
            @LogRequestParam(name = "endTime") @RequestParam(required = false) String endTime,
            @LogRequestParam(name = "repeatCount") @RequestParam(required = false) Integer repeatCount,
            @LogRequestParam(name = "repeatInterval") @RequestParam Long repeatInterval)
            throws SchedulerException, ActionException {

        if (StringUtils.isBlank(triggerName)) {
            throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
                    "triggerName illegal");
        }
        Date start = null;
        Date end = null;
        if (startTime != null) {
            try {
                start = TimeTagUtil.parseDate(startTime);
            } catch (ParseException e) {
                throw new ActionException(ResponseStatus.PARAM_ILLEGAL,
                        "start time format error");
            }
        }

        if (endTime != null) {
            try {
                end = TimeTagUtil.parseDate(endTime);
            } catch (ParseException e) {
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
        return schedulerService.rescheduleJob(triggerName, triggerGroup, start, end, repeatCount, repeatInterval);
    }

    @RequestMapping(value = "/rescheduleCronJob")
    @AuthRequired
    @ResponseBody
    public Object rescheduleCronJob(
            final HttpServletRequest request,
            @LogRequestParam(name = "triggerName") @RequestParam String triggerName,
            @LogRequestParam(name = "triggerGroup") @RequestParam(required = false) String triggerGroup,
            @LogRequestParam(name = "cronEx") @RequestParam String cronEx)
            throws SchedulerException, ActionException {
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
        return schedulerService.rescheduleCronJob(triggerName,
                triggerGroup, cronExpression);
    }

    @RequestMapping(value = "/unscheduleJob")
    @AuthRequired
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
    @AuthRequired
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
    @AuthRequired
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
