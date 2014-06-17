package gambler.examples.webapp2.resp;

public class ServerResponse {

	private String code;

	private String msg;

	private Object data;

	private String debug;

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
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getDebug() {
		return debug;
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

	public void setResponseStatus(ResponseStatus status) {
		this.code = status.getCode();
		this.msg = status.getMessage();
	}

}