package gabmler.tools.cli.cmd;

import gabmler.tools.service.PasswordService;
import gabmler.tools.service.ServiceException;

public class PasswordCommand extends AbstractCommand implements ICommand {

	private PasswordService pService = new PasswordService();

	public PasswordCommand() {
		super("password");
	}

	@Override
	public void service(String[] params) throws CommandUsageException,
			ServiceException {
		if (params[0].equalsIgnoreCase("encrypt")) {
			String password = params[1];
			String encrypt = pService.encrypt(password);
			System.out.println(String.format("encrypted password for %s: %s",
					password, encrypt));
		} else if (params[0].equalsIgnoreCase("decrypt")) {
			String password = params[1];
			String decrypt = pService.decrypt(password);
			System.out
					.println(String.format("decrypted password: %s", decrypt));
		} else {
			throw new CommandUsageException("Command Usage Error!");
		}

	}

	@Override
	public String[] getDescription() {
		return new String[] { "加密密码", "解密密码" };
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
