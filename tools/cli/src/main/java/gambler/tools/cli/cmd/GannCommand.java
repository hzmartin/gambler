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
		if (isSubCommand("p4")) {
			double startprice = Double.valueOf(params[1]);// 起始价格或者指数
			double pricestep = Double.valueOf(params[2]);// 价格或指数步长，比如每格升100点
			gann.printGann4Price13(startprice, pricestep);
		} else if (isSubCommand("t4")) {
			String starttime = params[1];// 起始时间
			String timeunit = params[2];// 时间单位，可选值：d/w/m，分别代表日d/周w/月m
			gann.printGann4Time13(starttime, timeunit);
		} else {
			throw new CommandUsageException("command usage error!");
		}

	}

	@Override
	public String[] getDescription() {
		return new String[] { "价格四方图", "时间四方图, 格式：yyyy-MM-dd，unit:d/w/m" };
	}

	@Override
	public String[] getSyntax() {
		return new String[] { "gann p4 /startprice/ /pricestep/", "gann t4 /starttime/ timeunit/" };
	}

	@Override
	public String[] getAlias() {
		return new String[] { "ga" };
	}

}
