/*
 * @(#) ExitSysCommand.java 2013-9-16
 * 
 */
package gambler.tools.cli.cmd;

import gambler.tools.cli.CLISystem;
import gambler.tools.service.ServiceException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

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
	public ExitSysCommand() {
		super("exit");
	}

	@Override
	public String[] getAlias() {
		return new String[] { "bye", "quit" };
	}

	@Override
	public void service(String[] params) throws CommandUsageException,
			ServiceException {
		saveHistory();
		System.out.println("Bye ... ...");
		System.out.println();
		System.exit(0);
	}

	public void saveHistory() {
		try {
			String historyFile = CLISystem.SYSCONFIG.getString(
					CLISystem.HISTORY_COMMAND_FILE,
					CLISystem.DEFAULT_HISTORY_COMMAND_FILE);
			FileWriter fileWriter = new FileWriter(new File(historyFile));
			BufferedWriter writer = new BufferedWriter(fileWriter);
			for (int index = 0; index < getCLISystem().sizeOfHistoryCommands(); index++) {
				ICommand history = getCLISystem().getHistoryCommand(index);
				writer.write(history + "\n");
			}
			writer.flush();
			fileWriter.close();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	@Override
	public String[] getDescription() {
		return new String[] { "exit the system" };
	}

	@Override
	public String[] getSyntax() {
		return new String[] { "exit | bye | quit" };
	}

	@Override
	public boolean isIgnorableCommand() {
		return true;
	}

}
