package gambler.tools.service;

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import gambler.commons.advmap.XMLMap;
import gambler.commons.util.jasypt.GBEnvironmentStringPBEConfig;
import gambler.tools.cli.CLISystem;
import gambler.tools.cli.bean.EncryptedPassword;
import gambler.tools.cli.util.DBUtil;

public class PasswordService {

	private static final XMLMap sysConfig = CLISystem.SYSCONFIG;

	private StandardPBEStringEncryptor getEncryptor() {
		GBEnvironmentStringPBEConfig config = new GBEnvironmentStringPBEConfig();
		String envName = sysConfig.getString("password.envName");
		if (StringUtils.isEmpty(envName)) {
			System.out.println("WARN: envName missing, default encrypt password will be used!");
		} else {
			System.out.println("envName " + envName + " will be used!");
			config.setPasswordEnvName(envName);
		}
		String algorithm = sysConfig.getString("password.algorithm");
		if (StringUtils.isEmpty(algorithm)) {
			System.out.println("WARN: algorightm missing, PBEWithMD5AndDES will be used");
			algorithm = "PBEWithMD5AndDES";
		} else {
			config.setAlgorithm(algorithm);
		}
		StandardPBEStringEncryptor encryptor = new org.jasypt.encryption.pbe.StandardPBEStringEncryptor();
		encryptor.setConfig(config);
		return encryptor;
	}

	public String decrypt(String password) {
		return getEncryptor().decrypt(password);

	}

	public String encrypt(String password) {
		return getEncryptor().encrypt(password);
	}

	public int saveOrUpdate(String uid, String site, String type, String password) throws IOException {
		EncryptedPassword p = new EncryptedPassword();
		p.setUid(uid);
		p.setType(type);
		p.setSite(site);
		p.setPasswd(encrypt(password));
		SqlSession session = DBUtil.getSqlSessionFactory().openSession(true);
		List<EncryptedPassword> passwds = session.selectList("EncryptedPassword.get", p);
		if (CollectionUtils.isEmpty(passwds)) {
			return session.insert("EncryptedPassword.insert", p);
		} else {
			return session.update("EncryptedPassword.update", p);
		}
	}

	public void get(String uid, String site, String type) throws IOException {
		EncryptedPassword p = new EncryptedPassword();
		p.setUid(uid);
		p.setType(type);
		p.setSite(site);
		SqlSession session = DBUtil.getSqlSessionFactory().openSession(true);
		session.selectList("EncryptedPassword.get", p);
		session.commit();
	}

}
