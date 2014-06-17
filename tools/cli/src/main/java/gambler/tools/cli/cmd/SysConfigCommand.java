package gambler.tools.cli.cmd;

import gambler.tools.cli.CLISystem;
import gambler.tools.service.ServiceException;
import gambler.commons.advmap.AdvancedKey;
import gambler.commons.advmap.XMLMap;

import java.util.Map.Entry;
import java.util.Set;

public class SysConfigCommand extends AbstractCommand implements ICommand {

	public SysConfigCommand() {
		super("config");
	}

	@Override
	public void service(String[] params) throws CommandUsageException,
			ServiceException {
		XMLMap sysConfig = CLISystem.SYSCONFIG;
		if (isSubCommand("set")) {
			String key = params[1];
			String value = params[2];
			sysConfig.setString(key, value);
		} else if (isSubCommand("get")) {
			String key = params[1];
			System.out.println(sysConfig.getString(key));
		} else if (isSubCommand("list")) {
			Set<Entry<AdvancedKey, String>> entrySet = sysConfig.entrySet();
			for (Entry<AdvancedKey, String> entry : entrySet) {
				System.out.println(entry.getKey().getNsKey() + "="
						+ entry.getValue());
			}
		} else {
			throw new CommandUsageException("command usage error!");
		}

	}

	@Override
	public String[] getDescription() {
		return new String[] { "set environment", "get environment setting for the key", "list all environment settings" };
	}

	@Override
	public String[] getSyntax() {
		return new String[] { "config set /key/ /value/", "config get /key/",
				"config list" };
	}

	@Override
	public String[] getAlias() {
		return new String[] { "c" };
	}
}
