package info.xiaomo.gengine.utils;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RSA加密方法
 * @author xiaomo
 */
public class EncryptRcaUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptRcaUtil.class);

    /**
     * 加密
     *
     * @param publicKey publicKey
     * @param msg       telegram
     * @return byte[]
     * @throws NoSuchAlgorithmException  NoSuchAlgorithmException
     * @throws NoSuchPaddingException    NoSuchPaddingException
     * @throws InvalidKeyException       InvalidKeyException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws BadPaddingException       BadPaddingException
     */
    public static byte[] encrypt(RSAPublicKey publicKey, String msg) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] srcBytes = msg.getBytes();
        //Cipher负责完成加密或解密工作，基于RSA
        Cipher cipher = Cipher.getInstance("RSA");
        //根据公钥，对Cipher对象进行初始化
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(srcBytes);
    }

    /**
     * 解密
     *
     * @param privateKey privateKey
     * @param srcBytes   srcBytes
     * @return byte[]
     * @throws NoSuchAlgorithmException  NoSuchAlgorithmException
     * @throws NoSuchPaddingException    NoSuchPaddingException
     * @throws InvalidKeyException       InvalidKeyException
     * @throws IllegalBlockSizeException IllegalBlockSizeException
     * @throws BadPaddingException       BadPaddingException
     */
    public static byte[] decrypt(RSAPrivateKey privateKey, byte[] srcBytes) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        if (privateKey != null) {
            //Cipher负责完成加密或解密工作，基于RSA
            Cipher cipher = Cipher.getInstance("RSA");
            //根据公钥，对Cipher对象进行初始化
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(srcBytes);
        }
        return null;
    }

    public static SshKey createSSHKey() throws NoSuchAlgorithmException {
        SshKey sshKey = new SshKey();

        //KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        //初始化密钥对生成器，密钥大小为1024位
        keyPairGen.initialize(1024);
        //生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        //得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        sshKey.setRsaPublicKey(publicKey);
        sshKey.setRsaPrivateKey(privateKey);
        return sshKey;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        String msg = "郭XX-精品相声";
        SshKey sshKey = EncryptRcaUtil.createSSHKey();

        //用公钥加密
        byte[] resultBytes = EncryptRcaUtil.encrypt(sshKey.getRsaPublicKey(), msg);

        //用私钥解密
        byte[] decBytes = EncryptRcaUtil.decrypt(sshKey.getRsaPrivateKey(), resultBytes);

        LOGGER.info("明文是:" + msg);
        LOGGER.info("加密后是:" + new String(resultBytes));
        LOGGER.info("解密后是:" + new String(decBytes));
    }


    public static class SshKey {
        private RSAPrivateKey rsaPrivateKey;
        private RSAPublicKey rsaPublicKey;

        RSAPrivateKey getRsaPrivateKey() {
            return rsaPrivateKey;
        }

        void setRsaPrivateKey(RSAPrivateKey rsaPrivateKey) {
            this.rsaPrivateKey = rsaPrivateKey;
        }

        RSAPublicKey getRsaPublicKey() {
            return rsaPublicKey;
        }

        void setRsaPublicKey(RSAPublicKey rsaPublicKey) {
            this.rsaPublicKey = rsaPublicKey;
        }
    }
}
