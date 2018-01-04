package gambler.tools.cli.cmd;

import java.util.Date;

import gambler.commons.util.time.TimeUtils;
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
			Date starttime = TimeUtils.parseDate(params[2]);// 起始时间
			double pricestep = Double.valueOf(params[3]);// 价格或指数步长，比如每格升100点
			String timeunit = params[4];// 时间单位，可选值：d/w/m/y，分别代表日d/周w/月m/年y
			int	size = Integer.parseInt(params[5]);//default: 13
			String output = params[6];
			gann.printGann4(startprice, starttime, pricestep, timeunit, size, output);
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
