package gambler.examples.webapp.auth;

public class UserAlreadyExistException extends Exception {

	public UserAlreadyExistException() {
		super();
	}

	public UserAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserAlreadyExistException(String message) {
		super(message);
	}

	public UserAlreadyExistException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = -830808557033413539L;

}
