package gambler.tools.cli.cmd;

import gambler.tools.cli.CLISystem;
import gambler.tools.service.HelpService;
import gambler.tools.service.ServiceException;

/**
 * Class AbstractCommand
 * 
 * @author Martin
 */
public abstract class AbstractCommand implements ICommand {

	private CLISystem cli;

	private final String name;

	private String[] params;

	/**
	 * @param name
	 * @param handler
	 */
	public AbstractCommand(String name) {
		super();
		this.name = name.toLowerCase();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public final void execute() throws CommandExecException {
		String[] params = getParams();
		try {
			service(params);
		} catch (CommandUsageException e) {
			printCommandUsage();
		} catch (ServiceException e) {
			throw new CommandExecException("Execute command error!", e);
		} catch (Exception e) {
			throw new CommandExecException("Unexpected error!", e);
		}

	}

        @Override
	public void printCommandUsage() {
		System.out.println("Command usage error!");
		System.out.println();
		System.out.println("Usage of command " + getName() + ":");
		System.out.println("=======");
		new HelpService().showCommand(getName());
	}

	public abstract void service(String[] params) throws CommandUsageException,
			ServiceException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.support.cli.ICommand#getAlias()
	 */
	@Override
	public String[] getAlias() {
		return new String[0];
	}

	@Override
	public String[] getParams() {
		String[] paramCopy = new String[params.length];
		for (int i = 0; i < params.length; i++) {
			paramCopy[i] = params[i];
			if (params[i].equalsIgnoreCase("null")) {
				paramCopy[i] = null;
			}
		}
		return paramCopy;
	}

	@Override
	public void setParams(String[] parameter) {
		this.params = parameter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chinatelecom.yixin.support.cli.ICommand#skipPrevCmdRecord()
	 */
	@Override
	public boolean isIgnorableCommand() {
		return false;
	}

	@Override
	public void setCLISystem(CLISystem cli) {
		this.cli = cli;
	}

	@Override
	public CLISystem getCLISystem() {
		return cli;
	}

	@Override
	public String toString() {
		String cmdString = getName() + " ";
		for (String param : getParams()) {
			cmdString += param + " ";
		}
		return cmdString;
	}

	public boolean isSubCommand(String subCmd) {
		return subCmd.equalsIgnoreCase(params[0]);
	}
}
