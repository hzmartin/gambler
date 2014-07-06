package gambler.examples.webapp2.dto;

import java.util.List;
import org.quartz.JobDataMap;


public class JobDto {

    
    private String name;
    
    private String group;
    
    private String description;
    
    private String jobClass;
    
    private JobDataMap jobDataMap;
    
    private List<TriggerDto> triggers;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public JobDataMap getJobDataMap() {
        return jobDataMap;
    }

    public void setJobDataMap(JobDataMap jobDataMap) {
        this.jobDataMap = jobDataMap;
    }

    public List<TriggerDto> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<TriggerDto> triggerDtos) {
        this.triggers = triggerDtos;
    }
    
    
}
