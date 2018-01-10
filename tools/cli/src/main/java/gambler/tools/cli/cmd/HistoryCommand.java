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
	public void service(String[] params) throws CommandUsageException, ServiceException {
		int startIndex = 0;
		if (isSubCommand("clear")) {
			getCLISystem().removeAllHistoryCommand();
		} else if (isSubCommand("last")) {
			int lastNum = Integer.parseInt(params[1]);
			startIndex = getCLISystem().sizeOfHistoryCommands() - lastNum;
			startIndex = (startIndex < 0) ? 0 : startIndex;
		} else {
			for (int index = startIndex; index < getCLISystem().sizeOfHistoryCommands(); index++) {
				ICommand history = getCLISystem().getHistoryCommand(index);
				System.out.println(history);
			}
		}
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				"show history command log，configurable key for max history size:" + CLISystem.MAX_HISTORY_SIZE,
				"show the last # history commands", "clear history commands" };
	}

	@Override
	public String[] getSyntax() {
		return new String[] { "history", "history last /num/", "history clear" };
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
