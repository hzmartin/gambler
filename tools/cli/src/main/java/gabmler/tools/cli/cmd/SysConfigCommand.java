package gabmler.tools.cli.cmd;

import gabmler.tools.cli.CLISystem;
import gabmler.tools.service.ServiceException;
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
			sysConfig.setProperty(key, value);
		} else if (isSubCommand("get")) {
			String key = params[1];
			System.out.println(sysConfig.getProperty(key));
		} else if (isSubCommand("list")) {
			Set<Entry<AdvancedKey, String>> entrySet = sysConfig.entrySet();
			for (Entry<AdvancedKey, String> entry : entrySet) {
				System.out.println(entry.getKey().getNsKey() + "="
						+ entry.getValue());
			}
		} else {
			throw new CommandUsageException("Command Usage Error!");
		}

	}

	@Override
	public String[] getDescription() {
		return new String[] { "设置配置项", "查询配置项", "查看所有配置项" };
	}

	@Override
	public String[] getSyntax() {
		return new String[] { "config set key value", "config get key",
				"config list" };
	}

	@Override
	public String[] getAlias() {
		return new String[] { "c" };
	}
}
