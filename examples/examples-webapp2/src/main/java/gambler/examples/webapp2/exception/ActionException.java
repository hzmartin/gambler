package gambler.examples.webapp2.exception;

import gambler.examples.webapp2.resp.ResponseStatus;
import gambler.examples.webapp2.resp.ServerResponse;

public class ActionException extends Exception {

	private ResponseStatus status = ResponseStatus.UNKNOWN_ERROR;
	
	/**
	 * status message args
	 * @see ServerResponse#setResponseStatus(ResponseStatus, Object...)
	 */
	private Object[] args;

	public ActionException(ResponseStatus status) {
		this.status = status;
	}

	public ActionException(ResponseStatus status, String debugMessage) {
		super(debugMessage);
		this.status = status;
	}
	
	public ActionException(ResponseStatus status, Object[] args) {
		this.status = status;
		this.args = args;
	}

	public ActionException(ResponseStatus status, Object[] args, String debugMessage) {
		super(debugMessage);
		this.status = status;
		this.args = args;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	private static final long serialVersionUID = -5760584499206020701L;

}
