/*
 * @(#) HistoryCommand.java 2013-9-27
 * 
 * Copyright 2010 NetEase.com, Inc. All rights reserved.
 */
package gambler.tools.cli.cmd;

import gambler.tools.cli.CLISystem;
import gambler.tools.service.ServiceException;

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
        int startIndex = 0;
        if (params.length == 1) {
            int lastNum = Integer.parseInt(params[0]);
            startIndex = getCLISystem().sizeOfHistoryCommands() - lastNum;
            startIndex = (startIndex < 0) ? 0 : startIndex;
        }
        for (int index = startIndex; index < getCLISystem().sizeOfHistoryCommands(); index++) {
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
        return new String[]{"show history command log，configurable key for max history size:" + CLISystem.MAX_HISTORY_SIZE,
            "show the last # history commands"};
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.chinatelecom.yixin.yipay.cli.ICommand#getSyntax()
     */
    @Override
    public String[] getSyntax() {
        return new String[]{"history", "history /num/"};
    }

    @Override
    public boolean isIgnorableCommand() {
        return true;
    }

    @Override
    public String[] getAlias() {
        return new String[]{"hist"};
    }
}
