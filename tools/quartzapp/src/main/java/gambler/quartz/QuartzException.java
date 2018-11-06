/*
 * @(#) ServiceException.java 2013-9-16
 * 
 * Copyright 2010 NetEase.com, Inc. All rights reserved.
 */
package gambler.quartz;

/**
 * Class ServiceException
 *
 * @author hzwangqh
 * @version 2013-9-16
 */
public class QuartzException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 7775124344178213893L;

    /**
     * 
     */
    public QuartzException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public QuartzException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public QuartzException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public QuartzException(Throwable cause) {
        super(cause);
    }

}
