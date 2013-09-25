/*
 * @(#) ExitSysCommand.java 2013-9-16
 * 
 * Copyright 2010 NetEase.com, Inc. All rights reserved.
 */
package gabmler.tools.cli.cmd;

import gabmler.tools.service.ExitSysService;
import gabmler.tools.service.IService;

/**
 * Class ExitSysCommand
 * 
 * @author hzwangqh
 * @version 2013-9-16
 */
public class ExitSysCommand extends AbstractCommand {

	/**
	 * @param name
	 */
	public ExitSysCommand(IService handler) {
		super("exit", handler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.support.cli.cmd.AbstractCommand#getAlias()
	 */
	@Override
	public String[] getAlias() {
		return new String[] { "bye", "quit" };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.support.cli.ICommand#execute()
	 */
	@Override
	public void execute() {
		((ExitSysService) service).exit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.support.cli.ICommand#getDescription()
	 */
	@Override
	public String[] getDescription() {
		return new String[] { "exit the system" };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.support.cli.ICommand#getSyntax()
	 */
	@Override
	public String[] getSyntax() {
		return new String[] { "exit | bye | quit" };
	}

}
