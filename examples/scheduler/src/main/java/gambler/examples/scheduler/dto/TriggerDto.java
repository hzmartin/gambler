package gambler.examples.scheduler.dto;

import org.quartz.Trigger;

public class TriggerDto {

	private String name;

	private String group;

	private String description;

	private String previousFireTime;

	private String nextFireTime;

	private String jobName;

	private String jobGroup;
	
	private String startTime;
	
	private String endTime;

	private String type;

	private int misfireInstruction;

	private Integer state;

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

	public String getPreviousFireTime() {
		return previousFireTime;
	}

	public void setPreviousFireTime(String previousFireTime) {
		this.previousFireTime = previousFireTime;
	}

	public String getNextFireTime() {
		return nextFireTime;
	}

	public void setNextFireTime(String nextFireTime) {
		this.nextFireTime = nextFireTime;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public int getMisfireInstruction() {
		return misfireInstruction;
	}

	public void setMisfireInstruction(int misfireInstruction) {
		this.misfireInstruction = misfireInstruction;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getStateLabel() {
		return getTriggerStateLabel(this.state);
	}

	public static String getTriggerStateLabel(Integer state) {
		if (state == null) {
			return "-";
		}
		switch (state) {
		case Trigger.STATE_NORMAL:
			return "normal";
		case Trigger.STATE_COMPLETE:
			return "complete";
		case Trigger.STATE_BLOCKED:
			return "blocked";
		case Trigger.STATE_PAUSED:
			return "paused";
		case Trigger.STATE_ERROR:
			return "error";
		case Trigger.STATE_NONE:
			return "none";
		default:
			return state + "";
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
