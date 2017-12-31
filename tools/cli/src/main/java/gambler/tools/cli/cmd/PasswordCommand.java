package gambler.tools.cli.cmd;

import java.io.IOException;
import java.util.List;

import gambler.tools.cli.bean.EncryptedPassword;
import gambler.tools.service.PasswordService;
import gambler.tools.service.ServiceException;

public class PasswordCommand extends AbstractCommand implements ICommand {

	private PasswordService passwordService = new PasswordService();

	public PasswordCommand() {
		super("password");
	}

	@Override
	public void service(String[] params) throws CommandUsageException, ServiceException {
		if (isSubCommand("encrypt")) {
			String password = params[1];
			String encrypt = passwordService.encrypt(password);
			System.out.println(String.format("encrypted password for %s: %s", password, encrypt));
		} else if (isSubCommand("decrypt")) {
			String password = params[1];
			String decrypt = passwordService.decrypt(password);
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
				int count = passwordService.saveOrUpdate(uid, site, type, password);
				System.out.println(String.format("password saved[%s, %s, %s]: %s", uid, site, type, count));
			} catch (IOException ex) {
				throw new ServiceException(ex);
			}
		} else if (isSubCommand("get")) {
			try {
				String uid = params[1];
				String site = params[2];
				String type = EncryptedPassword.TYPE_DEFAULT;
				if (params.length > 3) {
					type = params[3];
				}
				EncryptedPassword password = passwordService.get(uid, site, type);
				if (password == null) {
					System.out.println(String.format("password [%s, %s, %s] missing", uid, site, type));
				} else {
					System.out.println(String.format("password [%s, %s, %s]: %s", uid, site, type,
							passwordService.decrypt(password.getPasswd())));
				}
			} catch (IOException ex) {
				throw new ServiceException(ex);
			}
		} else if (isSubCommand("getall")) {
			try {
				List<EncryptedPassword> all = passwordService.getAll();
				for (EncryptedPassword e : all) {
					System.out.println(String.format("password [%s, %s, %s]: %s", e.getUid(), e.getSite(), e.getType(),
							passwordService.decrypt(e.getPasswd())));
				}
			} catch (IOException ex) {
				throw new ServiceException(ex);
			}
		} else if (isSubCommand("delete")) {
			try {
				String uid = params[1];
				String site = params[2];
				String type = EncryptedPassword.TYPE_DEFAULT;
				if (params.length > 3) {
					type = params[3];
				}
				int count = passwordService.delete(uid, site, type);
				System.out.println(String.format("password deleted[%s, %s, %s]: %s", uid, site, type, count));

			} catch (IOException ex) {
				throw new ServiceException(ex);
			}
		} else {
			throw new CommandUsageException("command usage error!");
		}

	}

	@Override
	public String[] getDescription() {
		return new String[] { "encrypt password", "decrypt password",
				"save password to database, optional type: login/query/pay", "get raw password",
				"get all stored password", "delete one password" };
	}

	@Override
	public String[] getSyntax() {
		return new String[] { "password encrypt /password/", "password decrypt /password/",
				"password save /uid/ /site/ /raw.password/ [type]", "password get /uid/ /site/ [type]",
				"password getall", "password delete /uid/ /site/ [type]" };
	}

	@Override
	public String[] getAlias() {
		return new String[] { "pass", "pa" };
	}

}
