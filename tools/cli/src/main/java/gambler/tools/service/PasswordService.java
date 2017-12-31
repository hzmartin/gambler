package gambler.tools.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

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
			System.out.println("envName config missing, default encrypt password will be used!");
		} else {
			if (CLISystem.isDebugOn()) {
				System.out.println("env " + envName + " will be used!");
				String envPasswd = System.getenv(envName);
				if (StringUtils.isEmpty(envPasswd)) {
					System.out.println("ENV PASSWORD missing, default encrypt password will be used!");
				} else {
					System.out.println("ENV PASSWORD: " + envPasswd);
				}
			}

			config.setPasswordEnvName(envName);
		}
		String algorithm = sysConfig.getString("password.algorithm");
		if (StringUtils.isEmpty(algorithm)) {
			System.out.println("algorightm missing, PBEWithMD5AndDES will be used");
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
		SqlSession session = DBUtil.getSqlSessionFactory().openSession(false);
		EncryptedPassword storedPasswd = session.selectOne("EncryptedPassword.get", p);
		int count = 0;
		if (storedPasswd == null) {
			count = session.insert("EncryptedPassword.insert", p);
		} else {
			count = session.update("EncryptedPassword.update", p);
		}
		session.commit();
		return count;
	}

	public EncryptedPassword get(String uid, String site, String type) throws IOException {
		EncryptedPassword p = new EncryptedPassword();
		p.setUid(uid);
		p.setType(type);
		p.setSite(site);
		SqlSession session = DBUtil.getSqlSessionFactory().openSession(true);
		return session.selectOne("EncryptedPassword.get", p);
	}

	public List<EncryptedPassword> getAll() throws IOException {

		SqlSession session = DBUtil.getSqlSessionFactory().openSession(true);
		return session.selectList("EncryptedPassword.get");
	}

	public int delete(String uid, String site, String type) throws IOException {
		EncryptedPassword p = new EncryptedPassword();
		p.setUid(uid);
		p.setType(type);
		p.setSite(site);
		SqlSession session = DBUtil.getSqlSessionFactory().openSession(true);
		return session.delete("EncryptedPassword.delete", p);
	}

}
