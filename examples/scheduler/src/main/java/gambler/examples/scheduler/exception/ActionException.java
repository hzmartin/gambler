package gambler.examples.scheduler.exception;

import gambler.examples.scheduler.resp.ResponseStatus;
import gambler.examples.scheduler.resp.ServerResponse;

public class ActionException extends Exception {

	private ResponseStatus status = ResponseStatus.UNEXPECTED_ERROR;

	/**
	 * status message args
	 * 
	 * @see ServerResponse#setResponseStatus(ResponseStatus, Object...)
	 */
	private Object[] args;

	public ActionException(String detailMessage) {
		super(detailMessage);
	}

	public ActionException(ResponseStatus status) {
		this.status = status;
	}

	/**
	 * @param detailMessage - return as <code>msg</code> if it's not blank
	 */
	public ActionException(ResponseStatus status, String detailMessage) {
		super(detailMessage);
		this.status = status;
	}

	public ActionException(ResponseStatus status, Object[] args) {
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
