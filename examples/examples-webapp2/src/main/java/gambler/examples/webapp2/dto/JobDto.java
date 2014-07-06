package gambler.examples.webapp2.dto;

import org.quartz.JobDataMap;


public class JobDto {
    
    private String jobName;
    
    private String jobGroup;
    
    private String description;
    
    private String jobClass;
    
    private JobDataMap jobDataMap;
    
    private List<TriggerDto> triggerList;
}
