package gabmler.tools.cli;

import gabmler.tools.cli.cmd.ExitSysCommand;
import gabmler.tools.cli.cmd.HelpCommand;
import gabmler.tools.cli.cmd.ICommand;
import gabmler.tools.cli.cmd.PasswordCommand;
import gabmler.tools.cli.cmd.PrevCmdCommand;
import gabmler.tools.cli.cmd.SysConfigCommand;
import gabmler.tools.cli.cmd.TimeTagCommand;
import gabmler.tools.service.ExitSysService;
import gabmler.tools.service.HelpService;
import gabmler.tools.service.PasswordService;
import gabmler.tools.service.ServiceException;
import gabmler.tools.service.TimeTagService;
import gambler.commons.advmap.XMLMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class AppSupportSystem
 * 
 * @author hzwangqh
 * @version 2013-9-16
 */
public final class CLISystem {

	public static final String WORKINGDIR_KEY = "workingdir";

	public static final String WHITELIST_KEY = "whitelist";

	public static final String SCAN_INTERVAL = "scanInterval";

	private static final Map<String, ICommand> commandMap = new HashMap<String, ICommand>();

	private static final List<ICommand> commandList = new ArrayList<ICommand>();

	private ICommand prevCommand = null;

	public static final XMLMap SYSCONFIG = new XMLMap(ClassLoader
			.getSystemResource("cli.config.xml").getFile());

	public CLISystem() {
		super();
	}

	public final void init() throws Exception {
		// system command list
		commandList.add(new ExitSysCommand(new ExitSysService()));
		commandList.add(new HelpCommand(new HelpService()));
		commandList.add(new TimeTagCommand(new TimeTagService()));
		commandList.add(new PrevCmdCommand(this));
		commandList.add(new PasswordCommand(new PasswordService()));
		commandList.add(new SysConfigCommand());
		for (ICommand cmd : commandList) {
			if (commandMap.containsKey(cmd.getName())) {
				throw new ServiceException("Command name " + cmd.getName()
						+ " conflicts!");
			}
			commandMap.put(cmd.getName(), cmd);
			for (String alias : cmd.getAlias()) {
				if (commandMap.containsKey(alias)) {
					throw new ServiceException("Command(" + cmd.getName()
							+ ") alias  " + alias + " conflicts!");
				}
				commandMap.put(alias, cmd);
			}
		}
	}

	public ICommand createCommand(String[] command) {
		String name = command[0];
		ICommand cmd = commandMap.get(name);
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
		return commandMap.get(name);
	}

	public ICommand getPrevCommand() {
		return prevCommand;
	}

	public void setPrevCommand(ICommand prevCommand) {
		this.prevCommand = prevCommand;
	}
}
