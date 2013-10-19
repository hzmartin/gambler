package gambler.tools.cli;

public class CommandNameConflictException extends Exception {

	private static final long serialVersionUID = 887233589674209688L;

	public CommandNameConflictException() {
		super();
	}

	public CommandNameConflictException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommandNameConflictException(String message) {
		super(message);
	}

	public CommandNameConflictException(Throwable cause) {
		super(cause);
	}

}
