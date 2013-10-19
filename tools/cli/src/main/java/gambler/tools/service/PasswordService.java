package gambler.tools.service;

import gambler.tools.cli.CLISystem;
import gambler.commons.advmap.XMLMap;
import gambler.commons.util.jasypt.GBEnvironmentStringPBEConfig;

import org.apache.commons.lang.StringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class PasswordService implements IService {

	private static final XMLMap sysConfig = CLISystem.SYSCONFIG;

	public String encrypt(String password) {
		return getEncryptor().encrypt(password);
	}

	private StandardPBEStringEncryptor getEncryptor() {
		String ePassword = sysConfig.getProperty("password.password");
		if (StringUtils.isEmpty(ePassword)) {
			System.out
					.println("password missing, default encrypt password will be used!");
		}
		String algorithm = sysConfig.getProperty("password.algorithm");
		if (StringUtils.isEmpty(algorithm)) {
			System.out
					.println("algorightm missing, PBEWithMD5AndDES will be used");
			algorithm = "PBEWithMD5AndDES";
		}
		StandardPBEStringEncryptor encryptor = new org.jasypt.encryption.pbe.StandardPBEStringEncryptor();
		GBEnvironmentStringPBEConfig config = new GBEnvironmentStringPBEConfig();
		config.setAlgorithm(algorithm);
		config.setPassword(ePassword);
		encryptor.setConfig(config);
		return encryptor;
	}

	public String decrypt(String password) {
		return getEncryptor().decrypt(password);

	}

}
