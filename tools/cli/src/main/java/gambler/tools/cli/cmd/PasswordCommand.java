package gambler.tools.cli.cmd;

import gambler.tools.service.PasswordService;
import gambler.tools.service.ServiceException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PasswordCommand extends AbstractCommand implements ICommand {

    private PasswordService pService = new PasswordService();

    public PasswordCommand() {
        super("password");
    }

    @Override
    public void service(String[] params) throws CommandUsageException,
            ServiceException {
        if (isSubCommand("encrypt")) {
            String password = params[1];
            String encrypt = pService.encrypt(password);
            System.out.println(String.format("encrypted password for %s: %s",
                    password, encrypt));
        } else if (isSubCommand("decrypt")) {
            String password = params[1];
            String decrypt = pService.decrypt(password);
            System.out
                    .println(String.format("decrypted password: %s", decrypt));
        } else if (isSubCommand("save")) {
            try {
                String uid = params[1];
                String site = params[2];
                String type = params[3];
                String password = params[4];
                pService.save(uid, site, type, password);
                System.out
                        .println(String.format("saved successfully: %s, %s", uid, site));
            } catch (IOException ex) {
                throw new ServiceException(ex);
            }
        } else {
            throw new CommandUsageException("command usage error!");
        }

    }

    @Override
    public String[] getDescription() {
        return new String[]{"encrypt password", "decrypt password"};
    }

    @Override
    public String[] getSyntax() {
        return new String[]{"password encrypt /password/",
            "password decrypt /password/"};
    }

    @Override
    public String[] getAlias() {
        return new String[]{"pass", "pa"};
    }

}
