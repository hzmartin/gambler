package gambler.tools.cli.cmd;

import gambler.tools.cli.LoadExtCommandException;
import gambler.tools.service.ServiceException;

/**
 *
 * @author Martin
 */
public class SystemCommand extends AbstractCommand {

    public SystemCommand() {
        super("system");
    }

    @Override
    public void service(String[] params) throws CommandUsageException, ServiceException {
        if (isSubCommand("load")) {
            String cmdClass = params[1];
            try {
                getCLISystem().loadExtCommand(cmdClass);
            } catch (LoadExtCommandException ex) {
                throw new ServiceException(ex);
            }
        } else {
            throw new CommandUsageException("command usage error!");
        }
    }

    @Override
    public String[] getDescription() {
        return new String[]{"load external command by the given class"};
    }

    @Override
    public String[] getSyntax() {
        return new String[]{"system load /ext command class/"};
    }

    @Override
    public String[] getAlias() {
        return new String[]{"sys"};
    }

}
