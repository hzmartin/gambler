package gambler.examples.webapp2.dto;

import org.quartz.JobDataMap;


public class ExecutingJobDto {
    
    private String jobName;
    
    private String jobGroup;
    
    private String description;
    
    private String jobClass;
    
    private JobDataMap jobDataMap;
    
    private String triggerName;
    
    private String triggerGroup;
    
    private String previousFireTime;
    
    private String scheduledFireTime;
    
    private String nextFireTime;
    
    private String fireTime;
}
