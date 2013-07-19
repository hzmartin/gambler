package gambler.commons.persistence;

public class PersistenceException extends Exception {

	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = -899912657527946858L;
	
	public PersistenceException() {
		super();
	}

	public PersistenceException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistenceException(String message) {
		super(message);
	}

	public PersistenceException(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		return "[Persistence Exception] " + this.getMessage();
	}

}
