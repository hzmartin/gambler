package gambler.tools.cli.cmd;

import gambler.tools.service.GannService;
import gambler.tools.service.ServiceException;

/**
 * @author wangqihui
 */
public class GannCommand extends AbstractCommand implements ICommand {

	private GannService gann = new GannService();
	
	public GannCommand() {
		super("gann");
	}

	@Override
	public void service(String[] params) throws CommandUsageException, ServiceException {
		if (isSubCommand("4")) {
			double startprice = Double.valueOf(params[1]);// 起始价格或者指数
			String starttime = params[2];// 起始时间
			double pricestep = Double.valueOf(params[3]);// 价格或指数步长，比如每格升100点
			String timeunit = params[4];// 时间单位，可选值：d/w/m/y，分别代表日d/周w/月m/年y
			String output = params[5];
			gann.printGann4_SIZE13(startprice, starttime, pricestep, timeunit, output);
		} else {
			throw new CommandUsageException("command usage error!");
		}

	}

	@Override
	public String[] getDescription() {
		return new String[] { "四方图" };
	}

	@Override
	public String[] getSyntax() {
		return new String[] { "gann 4" };
	}

	@Override
	public String[] getAlias() {
		return new String[] {};
	}

}
