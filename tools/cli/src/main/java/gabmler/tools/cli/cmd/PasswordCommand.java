package gabmler.tools.cli.cmd;

import gabmler.tools.cli.CommandExecException;
import gabmler.tools.service.IService;
import gabmler.tools.service.PasswordService;

public class PasswordCommand extends AbstractCommand implements ICommand {

	public PasswordCommand(IService service) {
		super("password", service);
	}

	@Override
	public void execute() throws CommandExecException {
		String[] params = getParameter();
		PasswordService pService = (PasswordService) service;
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
			throw new CommandExecException("Unknown command!");
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
