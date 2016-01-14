package gambler.examples.scheduler.dto;


public class SchedulerDto {

	private String schedulerName;

	private String schedulerInstanceId;

	private String state;

	private int threadPoolSize;

	private String runningSince;

	private String summary;

	private String version;

	private int numberOfJobsExecuted;

	public String getSchedulerName() {
		return schedulerName;
	}

	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}

	public String getSchedulerInstanceId() {
		return schedulerInstanceId;
	}

	public void setSchedulerInstanceId(String schedulerInstanceId) {
		this.schedulerInstanceId = schedulerInstanceId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	public String getRunningSince() {
		return runningSince;
	}

	public void setRunningSince(String runningSince) {
		this.runningSince = runningSince;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getNumberOfJobsExecuted() {
		return numberOfJobsExecuted;
	}

	public void setNumberOfJobsExecuted(int numberOfJobsExecuted) {
		this.numberOfJobsExecuted = numberOfJobsExecuted;
	}

}
