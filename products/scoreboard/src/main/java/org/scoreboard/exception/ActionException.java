package org.scoreboard.exception;

import org.scoreboard.response.ResponseStatus;

/**
 * 业务执行异常<br/>
 * message属性是一个format，具体参数配置存放于args,返回前端采用
 * <code>(String.format(ae.getMessage(), ae.getArgs()));</code>
 */
public class ActionException extends Exception {

	/**
	 * 错误码
	 */
	private String code = ResponseStatus.EXECUTION_ERROR.getCode();

	private Object data;

	/**
	 * 调试信息
	 */
	private String debug;

	public ActionException(ResponseStatus status) {
		super(status.getMessage());
		this.code = status.getCode();
	}

	public ActionException(ResponseStatus status, Object[] args) {
		super(String.format(status.getMessage(), args));
		this.code = status.getCode();
	}

	public ActionException(ResponseStatus status, String debug) {
		this(status);
		this.debug = debug;
	}

	public ActionException(String message) {
		super(message);
	}

	public ActionException(String message, Throwable cause) {
		super(message);
		this.debug = cause.getMessage();
	}

	public ActionException(Throwable cause) {
		this(ResponseStatus.EXECUTION_ERROR, cause.getMessage());
	}

	public ActionException(String message, Object data) {
		super(message);
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDebug() {
		return debug;
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}

	private static final long serialVersionUID = -5760584499206020701L;

}
