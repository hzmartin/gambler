/*
 * @(#) HelpCommand.java 2013-9-16
 * 
 */
package gabmler.tools.cli.cmd;

import gabmler.tools.service.HelpService;
import gabmler.tools.service.ServiceException;

/**
 * Class HelpCommand
 * 
 * @author hzwangqh
 * @version 2013-9-16
 */
public class HelpCommand extends AbstractCommand implements ICommand {

	private HelpService helpService = new HelpService();

	/**
	 * @param name
	 * @param handler
	 */
	public HelpCommand() {
		super("help");
	}

	@Override
	public void service(String[] params) throws CommandUsageException,
			ServiceException {
		String[] param = getParameter();
		if (param.length == 1) {
			helpService.showCommand(param[0].trim());
		} else {
			helpService.showAllCommands();
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

	@Override
	public String[] getAlias() {
		return new String[] { "h" };
	}

}
