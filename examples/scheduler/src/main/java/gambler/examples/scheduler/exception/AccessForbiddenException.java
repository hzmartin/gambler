package gambler.examples.scheduler.exception;

public class AccessForbiddenException extends Exception {

	private static final long serialVersionUID = -1539089835727909312L;

	public AccessForbiddenException() {
	}

	public AccessForbiddenException(String msg) {
		super(msg);
	}

	public AccessForbiddenException(Throwable cause) {
		super(cause);
	}

	public AccessForbiddenException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
