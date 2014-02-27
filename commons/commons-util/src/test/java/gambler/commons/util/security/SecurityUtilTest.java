/**
 * @(#)SecurityUtilTest.java, 2014-2-27.
 *
 * Copyright 2014 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package gambler.commons.util.security;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 *
 * @author Administrator
 *
 */
public class SecurityUtilTest {

    private static Logger logger = Logger.getLogger(SecurityUtilTest.class);
    
    /**
     * Test method for {@link gambler.commons.util.security.SecurityUtil#sign(java.lang.String, java.lang.String)}.
     * @throws Exception 
     */
    @Test
    public void testSignStringString() throws Exception {

        // 生成公私玥
        Key[] keyPair = SecurityUtil.generateRSAKeyPair();
        //PEM格式保存公钥
        String pubKeyFile = "test.pem";
        SecurityUtil.savePublicKeyAsPemFormat((PublicKey) keyPair[0], pubKeyFile);
        // 读取PEM格式公钥
        String hexPubKey = SecurityUtil.loadPemPublicKeyAsHex(pubKeyFile);
        logger.info("输出读取的公钥（PEM格式）：" + hexPubKey);
        //保存十六进制私钥
        String privKeyFile = "test.priv.key";
        SecurityUtil.savePrivateKeyAsHexFormat((PrivateKey) keyPair[1], privKeyFile );
        // 读取十六进制私钥
        String hexPrivKey = SecurityUtil.loadPrivateKeyAsHex(privKeyFile);
        logger.info("输出读取的私钥（十六进制形式）：" + hexPrivKey);
        
        String sign = SecurityUtil.sign("你好", hexPrivKey);
        boolean b = SecurityUtil.verifySign("你好", sign,  hexPubKey);
        logger.info("签名校验结果:" + (b ? "Pass" : "Fail"));
    }

}
