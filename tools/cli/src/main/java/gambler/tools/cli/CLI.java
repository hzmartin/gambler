package gambler.tools.cli;

import gambler.tools.cli.cmd.CommandExecException;
import gambler.tools.cli.cmd.CommandUsageException;
import gambler.tools.cli.cmd.ICommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Command Line Tools Main Class
 * 
 * @author Martin
 * 
 */
public class CLI {

	/**
	 * @param args
	 * @throws CommandNameConflictException
	 */
	public static void main(String[] args) throws CommandNameConflictException {
		// welcome and display system menu
		System.out.println();
		System.out
				.println("=========================================================");
		System.out.println("||");
		System.out.println("|| Welcome to use Gambler CLI :)");
		System.out.println("||");
		System.out.println("|| Author: Martin hzwangqihui@gmail.com");
		System.out
				.println("|| Any problem please don't hesitate to contact me!");
		System.out.println("|| Note: try command 'help' first");
		System.out
				.println("=========================================================");

		// init system
		CLISystem cli = new CLISystem();
		cli.init();
		Scanner console = new Scanner(System.in);
		while (true) {

			System.out.print("CLI>> ");
			String cmdString = console.nextLine();
			if (cmdString == null || cmdString.trim().isEmpty()) {
				continue;
			}
			ICommand cmd = null;
			try {
				String[] params = parseCmdString(cmdString.trim());
				cmd = cli.createCommand(params);
				if (cmd == null) {
					System.out.println("Unknown command, try with 'help'");
					continue;
				}

				if (!cmd.isIgnorableCommand()) {
					cli.setPrevCommand(cmd);
					cli.addHistoryCommand(cmd);
				}
				cmd.setCLISystem(cli);

				cmd.execute();
			} catch (CommandExecException ex) {
				System.out.println("execute comand [" + cmd + "] error!");
				if (isDebugOn()) {
					ex.printStackTrace(System.err);
				}
			} catch (CommandUsageException ex) {
				cmd.printCommandUsage();
			}

		}

	}

	public static boolean isDebugOn() {
		return CLISystem.SYSCONFIG.getBoolean("cli.debug", false);
	}

	/**
	 * @param cmdString
	 * @return
	 */
	private static String[] parseCmdString(String line)
			throws CommandUsageException {
		String[] columnArray = null;

		// 参数带引号的情况（暂只支持单引号）
		if (line.contains("'")) {
			String[] tmpArray = line.split("'");

			// 引号数量不匹配
			if (tmpArray.length % 2 == 0 && !line.endsWith("'")) {
				throw new CommandUsageException(
						"single quotation marks is not matched!");
			}

			List<String> columnList = new ArrayList<String>();
			for (int i = 0; i < tmpArray.length; i++) {
				String tmp = tmpArray[i].trim();
				// 一对引号之间的部分即单个参数
				if (i % 2 != 0) {
					columnList.add(tmp);
				} // 引号以外的部分可能含多个参数，需要再分割
				else {
					String[] tmpArray2 = tmp.split("\\s+");
					columnList.addAll(Arrays.asList(tmpArray2));
				}
			}

			columnArray = columnList.toArray(new String[columnList.size()]);
		} else {
			columnArray = line.split("\\s+");
		}

		return columnArray;
	}

}
