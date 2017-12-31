package gambler.tools.cli.cmd;

import java.io.IOException;

import gambler.tools.cli.bean.EncryptedPassword;
import gambler.tools.service.PasswordService;
import gambler.tools.service.ServiceException;

public class PasswordCommand extends AbstractCommand implements ICommand {

	private PasswordService pService = new PasswordService();

	public PasswordCommand() {
		super("password");
	}

	@Override
	public void service(String[] params) throws CommandUsageException, ServiceException {
		if (isSubCommand("encrypt")) {
			String password = params[1];
			String encrypt = pService.encrypt(password);
			System.out.println(String.format("encrypted password for %s: %s", password, encrypt));
		} else if (isSubCommand("decrypt")) {
			String password = params[1];
			String decrypt = pService.decrypt(password);
			System.out.println(String.format("decrypted password: %s", decrypt));
		} else if (isSubCommand("save")) {
			try {
				String uid = params[1];
				String site = params[2];
				String password = params[3];
				String type = EncryptedPassword.TYPE_DEFAULT;
				if (params.length > 4) {
					type = params[4];
				}
				int count = pService.saveOrUpdate(uid, site, type, password);
				System.out
						.println(String.format("password saved successfully[%s, %s, %s]: %s", uid, site, type, count));
			} catch (IOException ex) {
				throw new ServiceException(ex);
			}
		} else {
			throw new CommandUsageException("command usage error!");
		}

	}

	@Override
	public String[] getDescription() {
		return new String[] { "encrypt password", "decrypt password", "save password to database" };
	}

	@Override
	public String[] getSyntax() {
		return new String[] { "password encrypt /password/", "password decrypt /password/",
				"password save /uid/ /site/ /type/ /raw.password/" };
	}

	@Override
	public String[] getAlias() {
		return new String[] { "pass", "pa" };
	}

}
