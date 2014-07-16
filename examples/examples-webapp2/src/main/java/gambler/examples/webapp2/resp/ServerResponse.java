package gambler.examples.webapp2.resp;

import org.apache.commons.lang.StringUtils;

public class ServerResponse {

	private String code;

	private String msg;

	private Object data;

	public ServerResponse() {
		setResponseStatus(ResponseStatus.OK);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		if (!StringUtils.isBlank(msg)) {
			this.msg = msg;
		}
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public final void setResponseStatus(ResponseStatus status, Object... args) {
		this.code = status.getCode();
		if (args == null || args.length == 0) {
			this.msg = status.getMessage();
			return;
		}
		try {
			this.msg = String.format(status.getMessage(), args);
		} catch (Exception e) {
			this.msg = status.getMessage();
		}
	}

}