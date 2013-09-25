/*
 * @(#) PrevCmdCommand.java 2013-9-17
 * 
 * Copyright 2010 NetEase.com, Inc. All rights reserved.
 */
package gabmler.tools.cli.cmd;

import gabmler.tools.cli.CLISystem;
import gabmler.tools.cli.CommandExecException;

/**
 * Class PrevCmdCommand
 *
 * @author hzwangqh
 * @version 2013-9-17
 */
public class PrevCmdCommand extends AbstractCommand {

    private CLISystem appSupportSystem;

    /**
     * @param name
     * @param null
     */
    public PrevCmdCommand() {
        super("prev", null);
    }

    /**
     * @param appSupportSystem
     */
    public PrevCmdCommand(CLISystem appSupportSystem) {
        super("prev", null);
        this.appSupportSystem = appSupportSystem;
    }

    /*
     * (non-Javadoc)
     * @see com.chinatelecom.yixin.support.cli.ICommand#execute()
     */
    @Override
    public void execute() throws CommandExecException {
        ICommand prevCommand = appSupportSystem.getPrevCommand();
        if (prevCommand == null) {
            return;
        }
        String cmdString = prevCommand.getName() + " ";
        for (String param : prevCommand.getParameter()) {
            cmdString += param + " ";
        }
        System.out.println(cmdString);

    }

    /*
     * (non-Javadoc)
     * @see com.chinatelecom.yixin.support.cli.ICommand#getDescription()
     */
    @Override
    public String[] getDescription() {
        return new String[]{"Previous Command"};
    }

    /*
     * (non-Javadoc)
     * @see com.chinatelecom.yixin.support.cli.ICommand#getSyntax()
     */
    @Override
    public String[] getSyntax() {
        return new String[]{"prev"};
    }

    /*
     * (non-Javadoc)
     * @see com.chinatelecom.yixin.support.cli.cmd.AbstractCommand#getAlias()
     */
    @Override
    public String[] getAlias() {
        return new String[] {"p"};
    }

    /*
     * (non-Javadoc)
     * @see com.chinatelecom.yixin.support.cli.cmd.AbstractCommand#isIgnorablePrevCommand()
     */
    @Override
    public boolean isIgnorablePrevCommand() {
        return true;
    }

}
