package gambler.tools.cli.cmd;

import gambler.tools.service.ElliottService;
import gambler.tools.service.ServiceException;

/**
 * @author wangqihui
 */
public class ElliottCommand extends AbstractCommand implements ICommand {

	private ElliottService elliott = new ElliottService();

	public ElliottCommand() {
		super("elliott");
	}

	@Override
	public void service(String[] params) throws CommandUsageException, ServiceException {
		if (isSubCommand("ruler")) {
			float start = Float.valueOf(params[1]);// 浪1起点
			float end = Float.valueOf(params[2]);// 浪1高点
			float start2 = Float.valueOf(params[3]);// 浪2起点
			elliott.printWaveRuler(start, end, start2);
		} else {
			throw new CommandUsageException("command usage error!");
		}

	}

	@Override
	public String[] getDescription() {
		return new String[] { "波浪尺，价格单位：元" };
	}

	@Override
	public String[] getSyntax() {
		return new String[] { "elliott ruler /start/ /end/ /start2/" };
	}

	@Override
	public String[] getAlias() {
		return new String[] { "elli", "wave" };
	}

}
