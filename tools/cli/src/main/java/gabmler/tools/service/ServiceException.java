package gabmler.tools.service;

/**
 * Class ServiceException
 * 
 * @author hzwangqh
 * @version 2013-9-16
 */
public class ServiceException extends Exception {

	/**
     * 
     */
	private static final long serialVersionUID = 7775124344178213893L;

	/**
     * 
     */
	public ServiceException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public ServiceException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ServiceException(Throwable cause) {
		super(cause);
	}

}
