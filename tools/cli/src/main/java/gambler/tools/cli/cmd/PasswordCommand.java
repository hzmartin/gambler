package gambler.tools.cli.cmd;

import gambler.tools.service.PasswordService;
import gambler.tools.service.ServiceException;

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
		} else {
			throw new CommandUsageException("Command usage error!");
		}

	}

	@Override
	public String[] getDescription() {
		return new String[] { "Encrypt password", "Decrypt password" };
	}

	@Override
	public String[] getSyntax() {
		return new String[] { "password encrypt /password/",
				"password decrypt /password/" };
	}

	@Override
	public String[] getAlias() {
		return new String[] { "pass", "pa" };
	}

}
