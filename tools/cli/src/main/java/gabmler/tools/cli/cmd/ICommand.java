package gabmler.tools.cli.cmd;

import gabmler.tools.cli.CLISystem;

public interface ICommand {

	void execute() throws CommandExecException;

	String getName();

	String[] getAlias();

	String[] getDescription();

	String[] getSyntax();

	String[] getParameter();

	void setParameter(String[] parameter);

	boolean isIgnorableCommand();

	void setCLISystem(CLISystem cli);
	
	CLISystem getCLISystem();
}