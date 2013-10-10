package gabmler.tools.cli.cmd;

public class CommandExecException extends Exception {

	private static final long serialVersionUID = 887233589674209688L;

	public CommandExecException() {
		super();
	}

	public CommandExecException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommandExecException(String message) {
		super(message);
	}

	public CommandExecException(Throwable cause) {
		super(cause);
	}

}
