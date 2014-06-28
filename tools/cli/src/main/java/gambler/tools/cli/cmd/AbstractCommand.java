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
            throw new CommandExecException("execute command error!", e);
        } catch (Exception e) {
            throw new CommandExecException("unexpected error!", e);
        }

    }

    @Override
    public void printCommandUsage() {
        System.out.println("command usage error!");
        System.out.println();
        System.out.println("usage of command " + getName() + ":");
        System.out.println("=======");
        new HelpService().showCommand(getName());
    }

    public abstract void service(String[] params) throws CommandUsageException, ServiceException;

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

    /**
     * check the params[0] with the given subCmd
     * 
     * @param subCmd
     * @return true: is the given subCmd
     */
    public boolean isSubCommand(String subCmd) {
        return subCmd.equalsIgnoreCase(params[0]);
    }

    /**
     * compare the params[paramPos] with "go"
     * "go" only can put on the final position in the params
     * 
     * @param paramPos
     * @return true: dry run
     */
    public boolean isDryRun(int paramPos) {
        if (params.length == paramPos + 1) {
            return params[paramPos].equalsIgnoreCase("go") ? false : true;
        }
        return true;
    }
}
