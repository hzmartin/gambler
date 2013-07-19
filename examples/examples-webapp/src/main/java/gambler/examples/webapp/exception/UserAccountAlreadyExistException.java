package gambler.examples.webapp.exception;

public class UserAccountAlreadyExistException extends Exception {

	public UserAccountAlreadyExistException() {
		super();
	}

	public UserAccountAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserAccountAlreadyExistException(String message) {
		super(message);
	}

	public UserAccountAlreadyExistException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = -830808557033413539L;

}
