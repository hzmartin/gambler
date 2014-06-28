package gambler.commons.auth;

/**
 * AuthenticationException
 *
 * @auther Martin
 */

public class AuthenticationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuthenticationException() {
		super();
	}

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthenticationException(String message) {
		super(message);
	}

	public AuthenticationException(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		return "[Authentication Exception] " + this.getMessage();
	}


}
