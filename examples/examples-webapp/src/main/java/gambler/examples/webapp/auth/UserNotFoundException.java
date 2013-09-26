package gambler.examples.webapp.auth;

public class UserNotFoundException extends Exception {


	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = 5244589835544105443L;

}
