package gambler.commons.util.security;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PEMWriter;

/**
 * @author Martin
 */
public class SecurityUtil {

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
     * 生成RSA公私钥
     * 
     * @return 一对公私钥
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        return generator.generateKeyPair();
    }

    /**
     * 把秘钥保存成pem格式的文件<br/>
     * 底层步骤：key's encoded bytes - > base 64 encode -> pem format<br/>
     * 
     * @param key
     * @param pem
     * @throws IOException
     */
    public static void saveKeyAsPemFormat(Key key, File pem) throws IOException {
        logger.debug("开始保存秘钥到文件：" + pem.getAbsolutePath() + " 。。。 ");
        PEMWriter pemWriter = new PEMWriter(new FileWriter(pem));
        pemWriter.writeObject(key);
        pemWriter.flush();
        pemWriter.close();
        logger
            .debug("秘钥Hex:" + String.valueOf(Hex.encodeHex(key.getEncoded())));
        logger.debug("秘钥Base64:" + Base64.encodeBase64String(key.getEncoded()));
        logger.debug("秘钥以PEM格式成功写入到文件 : " + pem.getAbsolutePath());
    }

    /**
     * 读取PEM格式的公钥
     * 
     * @param pem
     * @return pem object - PrivateKey, PublicKey etc.,
     * @throws IOException
     */
    public static Object loadPemFormatKey(File pem) throws IOException {
        PEMReader pemReader = new PEMReader(new FileReader(pem));
        return pemReader.readObject();
    }

    /**
     * 用SHA1withRSA生成签名
     * 
     * @see #sign(String, String, PrivateKey)
     */
    public static String sign(String src, PrivateKey privateKey)
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
    public static String sign(String src, String algorithm,
        PrivateKey privateKey) throws GeneralSecurityException {
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(src.getBytes());
        return String.valueOf(Hex.encodeHex(signature.sign()));
    }

    /**
     * 公钥验证签名，签名算法：SHA1withRSA
     * 
     * @see #verifySign(String, String, String, PublicKey)
     */
    public static boolean verifySign(String src, String sign,
        PublicKey publicKey) throws GeneralSecurityException, DecoderException {
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
     */
    public static boolean verifySign(String src, String algorithm, String sign,
        PublicKey publicKey) throws GeneralSecurityException, DecoderException {
        Signature signature = Signature.getInstance(algorithm);
        signature.initVerify(publicKey);
        signature.update(src.getBytes());
        byte[] sign1 = Hex.decodeHex(sign.toCharArray());
        return signature.verify(sign1);
    }

}
