/*
 * @(#) HelpService.java 2013-9-16
 * 
 */
package gambler.tools.service;

import gambler.tools.cli.CLISystem;
import gambler.tools.cli.cmd.ICommand;

/**
 * Class HelpService
 * 
 * @author hzwangqh
 * @version 2013-9-16
 */
public class HelpService {

	/**
     * 
     */
	public void showAllCommands() {
		System.out
				.println("+----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("| Commands");
		System.out
				.println("+----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println(String.format("| %-15s| %-15s| %-100s| %s", "NAME",
				"ALIAS", "SYNTAX", "DESCRIPTION"));
		System.out
				.println("+----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		for (int cindex = 0; cindex < CLISystem.sizeOfCommands(); cindex++) {
			ICommand cmd = CLISystem.getCommand(cindex);
			StringBuilder aliases = new StringBuilder();
			for (String alias : cmd.getAlias()) {
				aliases.append(alias);
				aliases.append("  ");
			}

			int size = cmd.getSyntax().length;
			for (int index = 0; index < size; index++) {
				String syntax = cmd.getSyntax()[index];
				String description = "";
				if (cmd.getDescription().length > index) {
					description = cmd.getDescription()[index];
				}
				System.out
						.println(String.format("| %-15s| %-15s| %-100s| %s",
								cmd.getName(), aliases.toString(), syntax,
								description));
			}
			System.out
					.println("+----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		}
	}

	/**
	 * @param trim
	 */
	public void showCommand(String command) {
		ICommand cmd = CLISystem.getCommandByName(command);
		if (cmd == null) {
			return;
		}
		int size = cmd.getSyntax().length;
		for (int index = 0; index < size; index++) {
			String syntax = cmd.getSyntax()[index];
			String description = "";
			if (cmd.getDescription().length > index) {
				description = cmd.getDescription()[index];
			}
			System.out.println(String.format("%-100s#%s", syntax, description));
		}
	}
}
