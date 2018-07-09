package org.scoreboard.response;

public enum ResponseStatus {

	EXECUTION_ERROR("EXECUTION_ERROR", "执行错误"),

	OK("OK", "操作成功"), 
	
	SIGN_ILLEGAL("SIGN_ILLEGAL", "签名校验失败"),

	PARAM_ILLEGAL("PARAM_ILLEGAL", "服务器繁忙，请稍候再试！"),

	USER_NOT_EXSIST("USER_NOT_EXSIST", "用户不存在"),

	USER_NOT_LOGGED("USER_NOT_LOGGED", "用户没有登录"),

	USER_ALREADY_EXSIST("USER_ALREADY_EXSIST", "用户已存在"),

	PASSWORD_ERROR("PASSWORD_ERROR", "密码校验失败"),

	LOGIN_FAILED("LOGIN_FAILED", "用户名或密码错误"),

	ACTIVITY_ENDED("ACTIVITY_ENDED", "活动已经结束"),

	NO_PERMISSION("NO_PERMISSION", "权限不足，无法进行该操作! 需要请求管理员开通权限(%s)"),

	SERVER_BUSY("SERVER_BUSY", "服务器繁忙，请稍候再试！");

	private String code;

	private String message;

	private ResponseStatus(String code) {
		this.code = code;
	}

	private ResponseStatus(String code, String message) {
		this.code = code;
		this.setMessage(message);
	}

	public ResponseStatus setMsg(String msg) {
		this.message = msg;
		return this;
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
