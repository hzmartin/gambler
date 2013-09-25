/*
 * @(#) HelpCommand.java 2013-9-16
 * 
 * Copyright 2010 NetEase.com, Inc. All rights reserved.
 */
package gabmler.tools.cli.cmd;

import gabmler.tools.service.HelpService;
import gabmler.tools.service.IService;

/**
 * Class HelpCommand
 * 
 * @author hzwangqh
 * @version 2013-9-16
 */
public class HelpCommand extends AbstractCommand implements ICommand {

	/**
	 * @param name
	 * @param handler
	 */
	public HelpCommand(IService handler) {
		super("help", handler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.support.cli.ICommand#execute()
	 */
	@Override
	public void execute() {
		String[] param = getParameter();
		if (param.length == 1) {
			((HelpService) service).showCommand(param[0].trim());
		} else {
			((HelpService) service).showAllCommands();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.support.cli.ICommand#getDescription()
	 */
	@Override
	public String[] getDescription() {
		return new String[] { "show all commands", "show help of command" };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.support.cli.ICommand#getSyntax()
	 */
	@Override
	public String[] getSyntax() {
		return new String[] { "help", "help /command/" };
	}

}
