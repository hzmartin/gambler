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
		int size = 13;
		if (isSubCommand("p4")) {
			int startprice = Integer.valueOf(params[1]);// 起始价格或者指数
			int pricestep = Integer.valueOf(params[2]);// 价格或指数步长，比如每格升100点
			if (params.length > 3) {
				size = Integer.valueOf(params[3]);
			}
			gann.printGann4Price(startprice, pricestep, size);
		} else if (isSubCommand("t4")) {
			String starttime = params[1];// 起始时间
			String timeunit = params[2];// 时间单位，可选值：d/w/m，分别代表日d/周w/月m
			if (params.length > 3) {
				size = Integer.valueOf(params[3]);
			}
			gann.printGann4Time(starttime, timeunit, size);
		} else

		{
			throw new CommandUsageException("command usage error!");
		}

	}

	@Override
	public String[] getDescription() {
		return new String[] { "价格四方图", "时间四方图, 格式：yyyy-MM-dd，unit:h/d/w/m" };
	}

	@Override
	public String[] getSyntax() {
		return new String[] { "gann p4 /startprice/ /pricestep/ [size|13]", "gann t4 /starttime/ timeunit/ [size|13]" };
	}

	@Override
	public String[] getAlias() {
		return new String[] { "ga" };
	}

}
