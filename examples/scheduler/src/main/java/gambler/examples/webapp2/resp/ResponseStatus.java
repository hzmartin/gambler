package gambler.examples.webapp2.resp;

public enum ResponseStatus {

	OK("OK", "success"),

	PARAM_ILLEGAL("PARAM_ILLEGAL", "illegal request parameter"),

	USER_NOT_EXSIST("USER_NOT_EXSIST", "user doesn't exist"),

	USER_NOT_LOGGED("USER_NOT_LOGGED", "user doesn't login"),

	USER_ALREADY_EXSIST("USER_ALREADY_EXSIST", "User(%s) already exist"),

	LOGIN_FAILED("LOGIN_FAILED", "username or password error"),

	NO_PERMISSION("NO_PERMISSION", "forbidden"),

	EXCEED_MAX_TRYTIMES("EXCEED_MAX_TRYTIMES", "try too many times"),

	PASSWORD_ERROR("PASSWORD_ERROR", "password error"),

	JOB_NOT_EXIST("JOB_NOT_EXIST", "job(name=%s, group=%s) doesn't exist"),

	UNEXPECTED_ERROR("UNEXPECTED_ERROR", "unexpected error"),
	
	SERVER_BUSY("SERVER_BUSY", "server busy, please try again later!"),

	SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE", "service unavailable");

	private String code;

	private String message;

	private ResponseStatus(String code) {
		this.code = code;
	}

	private ResponseStatus(String code, String message) {
		this.code = code;
		this.setMessage(message);
	}

	public String getCode() {
		return code;
	}

	@Override
	public String toString() {
		return String.format("(%s,%s)", code, message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
