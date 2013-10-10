package gabmler.tools.cli;

import gabmler.tools.cli.cmd.ExitSysCommand;
import gabmler.tools.cli.cmd.HelpCommand;
import gabmler.tools.cli.cmd.HistoryCommand;
import gabmler.tools.cli.cmd.ICommand;
import gabmler.tools.cli.cmd.PasswordCommand;
import gabmler.tools.cli.cmd.PrevCmdCommand;
import gabmler.tools.cli.cmd.SysConfigCommand;
import gabmler.tools.cli.cmd.TimeTagCommand;
import gambler.commons.advmap.XMLMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Class AppSupportSystem
 * 
 * @author hzwangqh
 * @version 2013-9-16
 */
public final class CLISystem {

	public static final String MAX_HISTORY_SIZE = "cli.maxHistorySize";

	public static final String HISTORY_COMMAND_FILE = "cli.historyCommandFile";

	private static final Logger logger = Logger.getLogger(CLISystem.class);

	@SuppressWarnings("rawtypes")
	private static final Map<String, Class> commandMap = new HashMap<String, Class>();

	private static final List<ICommand> commandList = new ArrayList<ICommand>();

	private static final List<ICommand> historyCommands = new LinkedList<ICommand>();

	private ICommand prevCommand = null;

	public static final XMLMap SYSCONFIG = new XMLMap(ClassLoader
			.getSystemResource("cli.config.xml").getFile());

	public static final String DEFAULT_HISTORY_COMMAND_FILE = "/tmp/gambler_clicommand.history";

	public CLISystem() {
		super();
	}

	public final void init() throws SystemInitException {
		// system command list
		commandList.add(new ExitSysCommand());
		commandList.add(new HelpCommand());
		commandList.add(new TimeTagCommand());
		commandList.add(new PrevCmdCommand());
		commandList.add(new PasswordCommand());
		commandList.add(new SysConfigCommand());
		commandList.add(new HistoryCommand());
		for (ICommand cmd : commandList) {
			if (commandMap.containsKey(cmd.getName())) {
				throw new SystemInitException("Command name " + cmd.getName()
						+ " conflicts!");
			}
			commandMap.put(cmd.getName(), cmd.getClass());
			for (String alias : cmd.getAlias()) {
				if (commandMap.containsKey(alias)) {
					throw new SystemInitException("Command(" + cmd.getName()
							+ ") alias  " + alias + " conflicts!");
				}
				commandMap.put(alias, cmd.getClass());
			}
		}

		loadHistoryCommands();
	}

	private void loadHistoryCommands() {
		try {
			File historyFile = new File(SYSCONFIG.getProperty(
					CLISystem.HISTORY_COMMAND_FILE,
					DEFAULT_HISTORY_COMMAND_FILE));
			if (!historyFile.exists()) {
				historyFile.getParentFile().mkdirs();
				historyFile.createNewFile();
			}
			FileReader fileReader = new FileReader(historyFile);
			BufferedReader reader = new BufferedReader(fileReader);
			String line;
			while ((line = reader.readLine()) != null) {
				String[] tmpStr = line.trim().split("\\s+");
				ICommand cmd = createCommand(tmpStr);
				if (cmd == null) {
					continue;
				}
				if (!cmd.isIgnorableCommand()) {
					addHistoryCommand(cmd);
				}
			}

			fileReader.close();
			reader.close();
		} catch (Exception e) {
			logger.warn("加载历史命令文件失败！");
		}

	}

	public ICommand createCommand(String[] command) {
		String name = command[0];
		ICommand cmd = getCommandByName(name);
		if (cmd == null) {
			System.out.println("Unknown command, try with 'help'");
			return null;
		}
		String[] parameter = new String[command.length - 1];
		System.arraycopy(command, 1, parameter, 0, command.length - 1);
		cmd.setParameter(parameter);
		return cmd;
	}

	public static final int sizeOfCommands() {
		return commandList.size();
	}

	public static final ICommand getCommand(int index) {
		return commandList.get(index);
	}

	public static final ICommand getCommandByName(String name) {
		try {
			return (ICommand) commandMap.get(name.toLowerCase()).newInstance();
		} catch (Exception e) {
			return null;
		}
	}

	public ICommand getPrevCommand() {
		return prevCommand;
	}

	public void setPrevCommand(ICommand prevCommand) {
		this.prevCommand = prevCommand;
	}

	public void addHistoryCommand(ICommand command) {
		historyCommands.add(command);
		Integer max = Integer.parseInt(CLISystem.SYSCONFIG.getProperty(
				MAX_HISTORY_SIZE, "100"));
		if (historyCommands.size() > max) {
			historyCommands.remove(0);
		}
	}

	public ICommand removeHistoryCommand(int index) {
		return historyCommands.remove(index);
	}

	public int sizeOfHistoryCommands() {
		return historyCommands.size();
	}

	public ICommand getHistoryCommand(int index) {
		return historyCommands.get(index);
	}
}
