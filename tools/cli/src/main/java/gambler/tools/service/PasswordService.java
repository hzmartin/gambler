package gambler.tools.service;

import gambler.tools.cli.CLISystem;
import gambler.commons.advmap.XMLMap;
import gambler.commons.util.jasypt.GBEnvironmentStringPBEConfig;
import gambler.tools.cli.bean.EncryptedPassword;
import gambler.tools.cli.util.DBUtil;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class PasswordService {

    private static final XMLMap sysConfig = CLISystem.SYSCONFIG;

    public String encrypt(String password) {
        return getEncryptor().encrypt(password);
    }

    private StandardPBEStringEncryptor getEncryptor() {
        String ePassword = sysConfig.getString("password.password");
        if (StringUtils.isEmpty(ePassword)) {
            System.out
                    .println("WARN: password missing, default encrypt password will be used!");
        }
        String algorithm = sysConfig.getString("password.algorithm");
        if (StringUtils.isEmpty(algorithm)) {
            System.out
                    .println("WARN: algorightm missing, PBEWithMD5AndDES will be used");
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

    public void save(String uid, String site, String type, String password) throws IOException {
        EncryptedPassword p = new EncryptedPassword();
        p.setUid(uid);
        p.setType(type);
        p.setSite(site);
        p.setPasswd(password);
        SqlSession session = DBUtil.getSqlSessionFactory().openSession(false);
        session.insert("EncryptedPassword.insert", p);
    }

}
