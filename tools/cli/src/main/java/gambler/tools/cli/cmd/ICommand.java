package gambler.tools.cli.cmd;

import gambler.tools.cli.CLISystem;

public interface ICommand {

	void execute() throws CommandExecException, CommandUsageException;

	String getName();

	String[] getAlias();

	String[] getDescription();

	String[] getSyntax();

	String[] getParams();

	void setParams(String[] parameter);

	boolean isIgnorableCommand();

	void setCLISystem(CLISystem cli);
	
	CLISystem getCLISystem();
        
        void printCommandUsage();
}