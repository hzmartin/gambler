package gabmler.tools.cli.cmd;

import gabmler.tools.cli.CommandExecException;

public interface ICommand {

	void execute() throws CommandExecException;

	String getName();

	String[] getAlias();

	String[] getDescription();

	String[] getSyntax();

	String[] getParameter();

	void setParameter(String[] parameter);

	boolean isIgnorablePrevCommand();

}