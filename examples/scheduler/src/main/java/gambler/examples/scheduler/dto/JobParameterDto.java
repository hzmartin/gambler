package gambler.examples.scheduler.dto;

public class JobParameterDto {

	private String name;
	private String description;
	private boolean required;

	public JobParameterDto() {

	}

	public JobParameterDto(String name, String desciption, boolean required) {
		this.name = name;
		this.description = desciption;
                this.required = required;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string) {
		description = string;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param b
	 */
	public void setRequired(boolean b) {
		required = b;
	}

}
