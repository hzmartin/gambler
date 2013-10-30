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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.support.cli.ICommand#getDescription()
	 */
	@Override
	public String[] getDescription() {
		return new String[] { "previous command" };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.support.cli.ICommand#getSyntax()
	 */
	@Override
	public String[] getSyntax() {
		return new String[] { "prev" };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.support.cli.cmd.AbstractCommand#getAlias()
	 */
	@Override
	public String[] getAlias() {
		return new String[] { "p" };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.chinatelecom.yixin.support.cli.cmd.AbstractCommand#isIgnorablePrevCommand
	 * ()
	 */
	@Override
	public boolean isIgnorableCommand() {
		return true;
	}

}