/**
 * @(#)SecurityUtilTest.java, 2014-2-27.
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package gambler.commons.util.security;

import java.io.File;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Security;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

/**
 * @author Administrator
 */
public class SecurityUtilTest {

    private static Logger logger = Logger.getLogger(SecurityUtilTest.class);

    /**
     * Test method for
     * {@link gambler.commons.util.security.SecurityUtil#sign(java.lang.String, java.lang.String)}
     * .
     * 
     * @throws Exception
     */
    @Test
    public void testSignStringString() throws Exception {

        Security.addProvider(new BouncyCastleProvider());
        // 生成公私玥
        KeyPair keyPair = SecurityUtil.generateRSAKeyPair();
        //PEM格式保存秘钥
        String pubKeyFile = "test.pem";
        File pubPem = new File(pubKeyFile);
        SecurityUtil.saveKeyAsPemFormat(keyPair.getPublic(), pubPem);
        String privKeyFile = "test.priv.key";
        File pemPriv = new File(privKeyFile);
        SecurityUtil.saveKeyAsPemFormat(keyPair.getPrivate(), pemPriv);

        // 读取PEM格式公钥
        Object pemObject1 = SecurityUtil
            .loadPemFormatKey(pubPem);
        PublicKey pubPemObject = (PublicKey) pemObject1;
        Object pemObject2 = SecurityUtil
            .loadPemFormatKey(pemPriv);
        KeyPair privPemObject = (KeyPair) pemObject2;

        String sign = SecurityUtil.sign("你好", privPemObject.getPrivate());
        boolean b = SecurityUtil.verifySign("你好", sign, pubPemObject);
        logger.info("签名校验结果:" + (b ? "Pass" : "Fail"));
    }

}
