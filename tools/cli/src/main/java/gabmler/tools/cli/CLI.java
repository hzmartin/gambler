package gabmler.tools.cli;

import gabmler.tools.cli.cmd.ICommand;

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
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// welcome and display system menu
		System.out.println();
		System.out
				.println("=========================================================");
		System.out.println("||");
		System.out.println("|| Welcome to use Gambler CLI :)");
		System.out.println("||");
		System.out.println("|| Author: Martin wqihui@yeah.net");
		System.out
				.println("|| Any problem please don't hesitate to contact me!");
		System.out.println("|| Note: try command 'help' first");
		System.out
				.println("=========================================================");

		// init system
		CLISystem appSupportSystem = new CLISystem();
		appSupportSystem.init();
		Scanner console = new Scanner(System.in);
		while (true) {
			System.out.print("CLI>> ");
			String cmdString = console.nextLine();
			if (cmdString.trim().isEmpty())
				continue;
			try {
				String[] params = parseCmdString(cmdString.trim());
				ICommand cmd = appSupportSystem.createCommand(params);
				if (cmd == null) {
					continue;
				}
				if (!cmd.isIgnorablePrevCommand()) {
					appSupportSystem.setPrevCommand(cmd);
				}
				cmd.execute();
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}

		}

	}

	/**
	 * @param cmdString
	 * @return
	 */
	private static String[] parseCmdString(String line) {
		String[] columnArray = null;

		// 参数带引号的情况（暂只支持单引号）
		if (line.contains("'")) {
			String[] tmpArray = line.split("'");

			// 引号数量不匹配
			if (tmpArray.length % 2 == 0 && !line.endsWith("'")) {
				throw new IllegalArgumentException("引号个数不匹配");
			}

			List<String> columnList = new ArrayList<String>();
			for (int i = 0; i < tmpArray.length; i++) {
				String tmp = tmpArray[i].trim();
				// 一对引号之间的部分即单个参数
				if (i % 2 != 0) {
					columnList.add(tmp);
				}
				// 引号以外的部分可能含多个参数，需要再分割
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
