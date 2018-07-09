package org.scoreboard.response;

import com.alibaba.fastjson.JSON;

public class ServerResponse {

	/**
	 * 错误码
	 */
	private String code;

	/**
	 * 错误消息
	 */
	private String message;

	/**
	 * 响应数据
	 */
	private Object data;

	/**
	 * 调试信息
	 */
	private String debug;

	public ServerResponse() {
		this.code = ResponseStatus.OK.getCode();
		this.message = ResponseStatus.OK.getMessage();
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
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

	/**
	 * 功能：设置状态码;设置状态信息
	 * 
	 * @param status
	 * @return
	 */
	public final ServerResponse setStatus(ResponseStatus status, Object... args) {
		this.code = status.getCode();
		this.message = String.format(status.getMessage(), args);
		return this;
	}

	@Override
	public String toString() {
		try {
			return JSON.toJSONString(this);
		} catch (Exception e) {
			return "[code=" + code + ", message=" + message + ", data=" + data
					+ ", debug=" + debug + "]";
		}
	}

	public String getDebug() {
		return debug;
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

}
