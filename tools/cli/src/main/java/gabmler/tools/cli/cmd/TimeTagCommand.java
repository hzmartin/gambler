/*
 * @(#) TimeTagCommand.java 2013-9-16
 * 
 */
package gabmler.tools.cli.cmd;

import gabmler.tools.cli.CommandExecException;
import gabmler.tools.service.IService;
import gabmler.tools.service.TimeTagService;

/**
 * Class TimeTagCommand
 * 
 * @author hzwangqh
 * @version 2013-9-16
 */
public class TimeTagCommand extends AbstractCommand {

	/**
	 * @param name
	 * @param handler
	 */
	public TimeTagCommand(IService service) {
		super("timetag", service);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.support.cli.ICommand#execute()
	 */
	@Override
	public void execute() throws CommandExecException {
		TimeTagService timeTagService = (TimeTagService) service;
		String[] param = getParameter();
		try {
			if (param.length == 1) {
				System.out.println(timeTagService.getNowDateString());
			} else if (param.length == 2) {
				if (param[0].equalsIgnoreCase("now")) {
					if (param[1].equalsIgnoreCase("date")) {
						System.out.println(timeTagService.getNowDateString());
					} else if (param[1].equalsIgnoreCase("time")) {
						System.out.println(timeTagService.getNowTimeTag());
					} else {
						throw new CommandExecException("Invalid command usage!");
					}
				} else if (param[0].equalsIgnoreCase("date")) {
					long seconds = Long.parseLong(param[1]);
					System.out.println(timeTagService.getDate(seconds * 1000));
				} else if (param[0].equalsIgnoreCase("time")) {
					System.out.println(timeTagService.getTimeTag(param[1]));
				} else {
					throw new CommandExecException("Invalid command usage!");
				}
			} else {
				throw new CommandExecException("Unknown command!");
			}
		} catch (Exception e) {
			throw new CommandExecException(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.support.cli.ICommand#getDescription()
	 */
	@Override
	public String[] getDescription() {
		return new String[] { "get current time",
				"get time, default date format: yyyy-MM-dd", "get date" };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.support.cli.ICommand#getSyntax()
	 */
	@Override
	public String[] getSyntax() {
		return new String[] { "timetag now (time|date)",
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
