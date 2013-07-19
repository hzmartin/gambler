package gambler.examples.webapp.exception;

public class UserAccountNotFoundException extends Exception {


	public UserAccountNotFoundException() {
		super();
	}

	public UserAccountNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserAccountNotFoundException(String message) {
		super(message);
	}

	public UserAccountNotFoundException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = 5244589835544105443L;

}
