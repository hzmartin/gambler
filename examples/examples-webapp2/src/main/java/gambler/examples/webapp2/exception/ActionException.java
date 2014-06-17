package gambler.examples.webapp2.exception;

import gambler.examples.webapp2.resp.ResponseStatus;

public class ActionException extends Exception {

	private ResponseStatus status = ResponseStatus.UNKNOWN_ERROR;

	public ActionException() {
		super();
	}

	public ActionException(ResponseStatus status) {
		super(status.getMessage());
		this.status = status;
	}

	public ActionException(ResponseStatus status, String debug) {
		super(debug);
		this.status = status;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	private static final long serialVersionUID = -5760584499206020701L;

}
