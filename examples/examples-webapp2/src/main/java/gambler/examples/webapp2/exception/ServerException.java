package gambler.examples.webapp2.exception;

public class ServerException extends Exception {

	private static final long serialVersionUID = -1539089835727909312L;

	public ServerException() {
	}

	public ServerException(String msg) {
		super(msg);
	}

	public ServerException(Throwable cause) {
		super(cause);
	}

	public ServerException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
