/*
 * @(#) HistoryCommand.java 2013-9-27
 * 
 * Copyright 2010 NetEase.com, Inc. All rights reserved.
 */
package gabmler.tools.cli.cmd;

import gabmler.tools.cli.CLISystem;
import gabmler.tools.service.ServiceException;

/**
 * Class HistoryCommand
 * 
 * @author hzwangqh
 * @version 2013-9-27
 */
public class HistoryCommand extends AbstractCommand implements ICommand {

	/**
	 * @param name
	 * @param service
	 */
	public HistoryCommand() {
		super("history");
	}

	@Override
	public void service(String[] params) throws CommandUsageException,
			ServiceException {
		for (int index = 0; index < getCLISystem().sizeOfHistoryCommands(); index++) {
			ICommand history = getCLISystem().getHistoryCommand(index);
			System.out.println(history);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.yipay.cli.ICommand#getDescription()
	 */
	@Override
	public String[] getDescription() {
		return new String[] { "查询历史，最大记录个数配置项:" + CLISystem.MAX_HISTORY_SIZE };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.yipay.cli.ICommand#getSyntax()
	 */
	@Override
	public String[] getSyntax() {
		return new String[] { "history" };
	}

	@Override
	public boolean isIgnorableCommand() {
		return true;
	}

	@Override
	public String[] getAlias() {
		return new String[] { "hist" };
	}
}
