package gambler.commons.util.security;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.log4j.Logger;
import org.bouncycastle.openssl.PEMWriter;

/**
 * @author Martin
 */
public class SecurityUtil {

    private static final String NEW_LINE = System.getProperty("line.separator");

    private static Logger logger = Logger.getLogger(SecurityUtil.class);

    // PEM格式私钥的一般样式 注意并不是唯一样式
    //
    // -----BEGIN RSA PRIVATE KEY-----
    // MIICXQIBAAKBgQDyx3ejX4C4f9TMEeamgR1SB9ntZH7+D4E5YbDegysxaIiZLcDR
    // k6uXUcxE9bqWRkCvEyO4BkZsFjI7YSRGif3Ocz1PgLuYGFA05UYGprKY5/16P/GL
    // DdRzMIgILjt4zZ2UEIC0v/7dFRDNenvpVgGoikKpRXaXhbLwbYnEdEYMPQIDAQAB
    // AoGBALeCADN0H7bVoQJzUXVvyLrVhmtC+B6Pl6zI48j9OqvJCN9HzyRay2vAf3ds
    // ihHGnmP3Qpt52scea++oOKQ876MhW1MZ3PDXUEVYZgD/DIB5BhzKa1l1Vdveslo/
    // oi9iVj+SkmsUMzss4G452oVX1e52vwREuzOBagopo7JvAUJhAkEA/LyHIkxGo44v
    // gDkyGgYoxb6eBTsGRW/YBRpBYhv5HCdbcJd8TOvOKm67F1WfIAobyYhoiV0w/qrP
    // kU1rEaI2GQJBAPXqBg/PliVZGuULtPFQojEq01E1OYpbPv+C4I8mhEOZrFLRJ9Y6
    // fK+2FMtHKV4ENaKDh8dPoCVXSq92UQ0VI8UCQCwBW3oiU85YjHbD/rA/UEwWA4ef
    // nljqv28sAiRmAHrhc29oQPFXwX7r5tGKS/mVKP9vK9x3CHffH+MJ2tGoL8kCQQCJ
    // kBlF/PTb/aV63Xqhx16DrJY7hk+i4voxys+9pGk2u59XUKM9Rsh7LpshP7SXYDl0
    // qKlqyFMosFUaJFCgxeOVAkBcCaPyOT9oa/72N0DMwWGSTylS70hSmwz2OTqLVZde
    // PL8zfVQ3Sc/s3YQG/fjSViQKNwu16qxqIyUNjr+4/0tW
    // -----END RSA PRIVATE KEY-----
    // public static String readPriKeyFromPem(String path) throws IOException {
    // return readKeyPem(path, false);
    // }

    // PEM格式公钥的一般样式 注意并不是唯一样式
    //
    // -----BEGIN PUBLIC KEY-----
    // MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC7z3PdApJhLsKuj1kQ8YNMoIRF
    // TVcNHGyJQtcId0pbO8g5kwNsBzLmPUo/bMpyaZRKWnuBmkZK33TurbCq6d1VaC0U
    // mGr2roog/ZDjzJVxo6/vGAOz+KzX99tDdd8D8LuqZPAiJ/KuxtFDXEwLJz9PbDAm
    // VPrVczMw6OZao+IzhwIDAQAB
    // -----END PUBLIC KEY-----
    // NOTE:结尾处有一个换行符
    /**
     * <p>
     * 读取的是十六进制字符串形式存储的私钥<br/>
     * NOTE:<br/>
     * 最后的换行符不能剔除</br>
     * </p>
     * 
     * @param privKeyPath
     * @return
     * @throws IOException
     */
    public static String loadPrivateKeyAsHex(String privKeyPath)
        throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(privKeyPath));
            String line;
            while (null != (line = reader.readLine())) {
                buffer.append(line).append(NEW_LINE);
            }
            return buffer.toString();
        } finally {
            reader.close();
        }

    }

    /**
     * 1. 读取PEM格式的公钥<br/>
     * 2. BASE64解码<br/>
     * 3. 字节转十六字符串<br/>
     * 
     * @param pemFile
     * @return 十六进制字符串
     * @throws IOException
     */
    public static String loadPemPublicKeyAsHex(String pemFile)
        throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(pemFile));
            StringBuilder sb = new StringBuilder();
            String line;
            while (null != (line = reader.readLine())) {
                if ((line.contains("BEGIN PUBLIC KEY") || line
                    .contains("END PUBLIC KEY")))
                    continue;
                sb.append(line);
            }
            return byte2hex(Base64Util.decode2byte(sb.toString()));
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    /**
     * 生成RSA公私钥
     * 
     * @return 一个对象数组，依次是[公钥，私钥]
     * @throws NoSuchAlgorithmException
     */
    public static Key[] generateRSAKeyPair() throws NoSuchAlgorithmException {
        Key[] keys = new Key[2];
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();

        keys[0] = keyPair.getPublic();
        keys[1] = keyPair.getPrivate();
        return keys;
    }

    /**
     * 保存成十六进制私钥文件
     * 
     * @param pubicKey
     * @param outputPath
     * @throws IOException
     */
    public static void savePrivateKeyAsHexFormat(PrivateKey privateKey,
        String outputPath) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(outputPath));
        out.write(byte2hex(privateKey.getEncoded()));
        out.close();
        logger.info("私钥以十六进制字符串形式成功写入文件" + outputPath);
        logger.info("十六进制:" + byte2hex(privateKey.getEncoded()));
    }

    /**
     * 保存成pem格式的公钥文件
     * 
     * @param pubicKey
     * @param outputPath
     * @throws IOException
     */
    public static void savePublicKeyAsPemFormat(PublicKey publicKey,
        String outputPath) throws IOException {
        PEMWriter pubWriter = new PEMWriter(new FileWriter(outputPath));
        pubWriter.writeObject(publicKey);
        pubWriter.flush();
        pubWriter.close();

        logger.info("公钥以PEM格式成功写入文件" + outputPath);
        logger.info("十六进制:" + byte2hex(publicKey.getEncoded()));
        logger.info("Base64:" + Base64Util.encode(publicKey.getEncoded()));
    }

    public static String byte2hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b: bytes)
            sb.append(Integer.toString(b >>> 4 & 0xF, 16)).append(
                Integer.toString(b & 0xF, 16));
        return sb.toString();
    }

    public static final byte[] hex2byte(String s) {
        byte[] bytes = new byte[s.length() >> 1];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(
                s.substring(i << 1, (i + 1) << 1), 16);
        }
        return bytes;
    }

    /**
     * 用SHA1withRSA生成签名
     * 
     * @see #sign(String, String, String)
     */
    public static String sign(String src, String privateKey)
        throws GeneralSecurityException {
        return sign(src, "SHA1withRSA", privateKey);
    }

    /**
     * 私钥生成签名<br/>
     * 公私钥采用RSA算法生成<br/>
     * 
     * @param src
     *            - 原始待签名数据
     * @param algorithm
     *            - 签名算法
     * @param privateKey
     *            - 十六进制格式
     * @return 十六进制格式签名字符串
     * @throws GeneralSecurityException
     */
    public static String sign(String src, String algorithm, String privateKey)
        throws GeneralSecurityException {
        Signature signature = Signature.getInstance(algorithm);
        byte[] pribyte = hex2byte(privateKey.trim());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pribyte);
        KeyFactory fac = KeyFactory.getInstance("RSA");
        PrivateKey privKeyObj = fac.generatePrivate(keySpec);
        signature.initSign(privKeyObj);
        signature.update(src.getBytes());
        return byte2hex(signature.sign());
    }

    /**
     * 公钥验证签名，签名算法：SHA1withRSA
     * 
     * @see #verifySign(String, String, String, String)
     */
    public static boolean verifySign(String src, String sign, String publicKey)
        throws GeneralSecurityException {
        return verifySign(src, "SHA1withRSA", sign, publicKey);
    }

    /**
     * 公钥验证签名<br/>
     * 公私钥采用RSA算法生成<br/>
     * 
     * @param src
     *            - 签名的原始数据
     * @param algorithm
     *            - 签名算法
     * @param sign
     *            - 签名
     * @param publicKey
     *            - 十六进制格式
     * @return true表示签名验证通过, false表示签名验证不通过
     * @throws GeneralSecurityException
     */
    public static boolean verifySign(String src, String algorithm, String sign,
        String publicKey) throws GeneralSecurityException {

        Signature sigEng = Signature.getInstance(algorithm);
        byte[] pubbyte = hex2byte(publicKey.trim());

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubbyte);
        KeyFactory fac = KeyFactory.getInstance("RSA");
        RSAPublicKey pubKey = (RSAPublicKey) fac.generatePublic(keySpec);

        sigEng.initVerify(pubKey);
        sigEng.update(src.getBytes());

        byte[] sign1 = hex2byte(sign);
        return sigEng.verify(sign1);
    }

}
