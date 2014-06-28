/*
 * @(#) PrevCmdCommand.java 2013-9-17
 * 
 */
package gambler.tools.cli.cmd;

import gambler.tools.service.ServiceException;

/**
 * Class PrevCmdCommand
 * 
 * @author hzwangqh
 * @version 2013-9-17
 */
public class PrevCmdCommand extends AbstractCommand {

	/**
	 * @param name
	 * @param null
	 */
	public PrevCmdCommand() {
		super("prev");
	}

	@Override
	public void service(String[] params) throws CommandUsageException,
			ServiceException {
		ICommand prevCommand = getCLISystem().getPrevCommand();
		if (prevCommand == null) {
			return;
		}
		String cmdString = prevCommand.getName() + " ";
		for (String param : prevCommand.getParams()) {
			cmdString += param + " ";
		}
		System.out.println(cmdString);

	}

	@Override
	public String[] getDescription() {
		return new String[] { "previous command" };
	}

	@Override
	public String[] getSyntax() {
		return new String[] { "prev" };
	}

	@Override
	public String[] getAlias() {
		return new String[] { "p" };
	}

	@Override
	public boolean isIgnorableCommand() {
		return true;
	}

}
