/*
 * @(#) TimeTagCommand.java 2013-9-16
 * 
 */
package gabmler.tools.cli.cmd;

import gabmler.tools.service.ServiceException;
import gabmler.tools.service.TimeTagService;

/**
 * Class TimeTagCommand
 * 
 * @author hzwangqh
 * @version 2013-9-16
 */
public class TimeTagCommand extends AbstractCommand {

	private TimeTagService timeTagService = new TimeTagService();

	/**
	 * @param name
	 * @param handler
	 */
	public TimeTagCommand() {
		super("timetag");
	}

	@Override
	public void service(String[] params) throws CommandUsageException,
			ServiceException {
		if (params.length == 0) {
			System.out.println(timeTagService.getNowDateString());
		} else if (params.length == 1 && isSubCommand("now")) {
			System.out.println(timeTagService.getNowDateString());
		} else if (params.length == 2) {
			if (isSubCommand("now")) {
				if ("date".equalsIgnoreCase(params[1])) {
					System.out.println(timeTagService.getNowDateString());
				} else if ("time".equalsIgnoreCase(params[1])) {
					System.out.println(timeTagService.getNowTimeTag());
				} else {
					throw new CommandUsageException("Command Usage Error!");
				}
			} else if (isSubCommand("date")) {
				long seconds = Long.parseLong(params[1]);
				System.out.println(timeTagService.getDate(seconds * 1000));
			} else if (isSubCommand("time")) {
				System.out.println(timeTagService.getTimeTag(params[1]));
			} else {
				throw new CommandUsageException("Command Usage Error!");
			}
		} else {
			throw new CommandUsageException("Command Usage Error!");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.support.cli.ICommand#getDescription()
	 */
	@Override
	public String[] getDescription() {
		return new String[] { "get current time", "get current time",
				"get time, default date format: yyyy-MM-dd", "get date" };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.support.cli.ICommand#getSyntax()
	 */
	@Override
	public String[] getSyntax() {
		return new String[] { "time", "timetag now (time|date)",
				"timetag date (time in second)", "timetag time (date string)" };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.support.cli.cmd.AbstractCommand#getAlias()
	 */
	@Override
	public String[] getAlias() {
		return new String[] { "t" };
	}
}
