package gambler.tools.cli;

import gambler.tools.cli.cmd.ExitSysCommand;
import gambler.tools.cli.cmd.HelpCommand;
import gambler.tools.cli.cmd.HistoryCommand;
import gambler.tools.cli.cmd.ICommand;
import gambler.tools.cli.cmd.PrevCmdCommand;
import gambler.tools.cli.cmd.SysConfigCommand;
import gambler.tools.cli.cmd.TimeTagCommand;
import gambler.commons.advmap.AdvancedKey;
import gambler.commons.advmap.XMLMap;
import gambler.commons.util.io.FileProcessor;
import gambler.commons.util.io.ILineProcessor;
import gambler.tools.cli.cmd.SystemCommand;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.apache.log4j.Logger;

/**
 * Class CLISystem
 *
 * example command: ex-cmd1 param1 null param3
 *
 * for the sencond parameter, null will be set
 *
 * @author hzwangqh
 * @version 2013-9-16
 */
public final class CLISystem implements IConfigrableConstants {

    private static final Logger logger = Logger.getLogger(CLISystem.class);

    private static final Map<String, Class> commandMap = new HashMap<String, Class>();

    private static final List<ICommand> commandList = new ArrayList<ICommand>();

    private static final List<ICommand> historyCommands = new LinkedList<ICommand>();

    private ICommand prevCommand = null;

    public static final XMLMap SYSCONFIG = new XMLMap("cli.conf.xml", "cli.conf.ext.xml");

    public CLISystem() {
        super();
    }

    final void init() throws CommandNameConflictException {

        loadBuiltinCommands();

        loadExtCommands();

        loadHistoryCommands();
    }

    private void initCommandMaps() throws CommandNameConflictException {
        for (ICommand cmd : commandList) {
            initCommandMap(cmd);
        }
    }

    private void initCommandMap(ICommand cmd) throws CommandNameConflictException {
        if (commandMap.containsKey(cmd.getName())) {
            throw new CommandNameConflictException("command name " + cmd.getName()
                    + " conflicts!");
        }
        commandMap.put(cmd.getName(), cmd.getClass());
        for (String alias : cmd.getAlias()) {
            if (commandMap.containsKey(alias)) {
                throw new CommandNameConflictException("command(" + cmd.getName()
                        + ") alias  " + alias + " conflicts!");
            }
            commandMap.put(alias, cmd.getClass());
        }
    }

    private void loadBuiltinCommands() throws CommandNameConflictException {
        // system command list
        commandList.add(new ExitSysCommand());
        commandList.add(new HelpCommand());
        commandList.add(new TimeTagCommand());
        commandList.add(new PrevCmdCommand());
        commandList.add(new SysConfigCommand());
        commandList.add(new HistoryCommand());
        commandList.add(new SystemCommand());
        initCommandMaps();
    }

    private void loadExtCommands() {
        Set<AdvancedKey> keySet = SYSCONFIG.keySet();
        for (AdvancedKey key : keySet) {
            if (EXT_CMD_NAMESPACE.equals(key.getNamespace())) {
                try {
                    String cmdClass = SYSCONFIG.get(key);
                    loadExtCommand(cmdClass);
                } catch (LoadExtCommandException ex) {
                    logger.warn(ex, ex);
                    System.out.println("WARN: load external command " + key.getNsKey() + " error!");
                } catch (CommandNameConflictException ex) {
                    logger.warn(ex, ex);
                    System.out.println("load external command " + key.getNsKey() + " error: command name conflicts!");
                }
            }
        }
    }

    public final void loadExtCommand(String cmdClass) throws LoadExtCommandException, CommandNameConflictException {
        try {
            ICommand cmdInst = (ICommand) Class.forName(cmdClass).newInstance();
            commandList.add(cmdInst);
            initCommandMap(cmdInst);
        }  catch (ClassNotFoundException ex) {
            throw new LoadExtCommandException("load external command error!", ex);
        } catch (IllegalAccessException ex) {
            throw new LoadExtCommandException("load external command error!", ex);
        } catch (InstantiationException ex) {
            throw new LoadExtCommandException("load external command error!", ex);
        }
    }

    private void loadHistoryCommands() {
        try {
            File historyFile = new File(SYSCONFIG.getProperty(
                    CLISystem.HISTORY_COMMAND_FILE,
                    DEFAULT_HISTORY_COMMAND_FILE));
            if (!historyFile.exists()) {
                historyFile.getParentFile().mkdirs();
                historyFile.createNewFile();
            }
            FileProcessor fileProcessor = new FileProcessor(historyFile);
            fileProcessor.setProcessor(new ILineProcessor() {

                @Override
                public void process(int lineNumber, String line) {
                    String[] tmpStr = line.trim().split("\\s+");
                    ICommand cmd = createCommand(tmpStr);
                    if (cmd == null) {
                        return;
                    }
                    if (!cmd.isIgnorableCommand()) {
                        addHistoryCommand(cmd);
                    }
                }

                @Override
                public void cleanUp() {
                }
            });
            fileProcessor.processLines();
        } catch (Exception e) {
            logger.warn("load history commands failed！");
        }

    }

    public ICommand createCommand(String[] command) {
        String name = command[0];
        ICommand cmd = getCommandByName(name);
        if (cmd == null) {
            return null;
        }
        String[] parameter = new String[command.length - 1];
        System.arraycopy(command, 1, parameter, 0, command.length - 1);
        cmd.setParams(parameter);
        return cmd;
    }

    public static final int sizeOfCommands() {
        return commandList.size();
    }

    public static final ICommand getCommand(int index) {
        return commandList.get(index);
    }

    public static final ICommand getCommandByName(String name) {
        try {
            return (ICommand) commandMap.get(name.toLowerCase()).newInstance();
        } catch (Exception e) {
            logger.warn("create command[ " + name + "] instance error", e);
            return null;
        }
    }

    public ICommand getPrevCommand() {
        return prevCommand;
    }

    public void setPrevCommand(ICommand prevCommand) {
        this.prevCommand = prevCommand;
    }

    public void addHistoryCommand(ICommand command) {
        historyCommands.add(command);
        Integer max = Integer.parseInt(CLISystem.SYSCONFIG.getProperty(
                MAX_HISTORY_SIZE, "100"));
        if (historyCommands.size() > max) {
            historyCommands.remove(0);
        }
    }

    public ICommand removeHistoryCommand(int index) {
        return historyCommands.remove(index);
    }

    public int sizeOfHistoryCommands() {
        return historyCommands.size();
    }

    public ICommand getHistoryCommand(int index) {
        return historyCommands.get(index);
    }
}
