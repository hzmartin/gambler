package gambler.commons.util.password;

import org.jasypt.util.password.StrongPasswordEncryptor;

/**
 * PasswordUtil provides password util methods based on StrongPasswordEncryptor
 * 
 * @auther Martin
 */

public final class PasswordUtil {

	private static final StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

	public static final String encrypt(String password) {
		return passwordEncryptor.encryptPassword(password);
	}

	public static final boolean checkPassword(String encryptedPassword,
			String clearTextPwd) {
		return passwordEncryptor.checkPassword(clearTextPwd, encryptedPassword);
	}
	
	public static void main(String[] args) {
		System.out.println(checkPassword("35kPoqVULITuJS8wdXBXa2HovWxSGPtPGcdAkNUM4/rMSc5fbfHCsuF82zEcsotQ", "hello"));
	}
}
