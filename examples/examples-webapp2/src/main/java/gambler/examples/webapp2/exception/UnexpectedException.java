package gambler.examples.webapp2.exception;

public class UnexpectedException extends Exception {

	private static final long serialVersionUID = 8381862835758308652L;

	public UnexpectedException() {
		super();
	}

	public UnexpectedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnexpectedException(String message) {
		super(message);
	}

	public UnexpectedException(Throwable cause) {
		super(cause);
	}

}
