package com.flab.marketgola.user.util;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordEncrypter {

    private PasswordEncrypter() {
    }

    public static String encrypt(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        //다이제스트(Password-Based-Encryption Key)에 대한 스펙 정의하기
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 4);

        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] digest = secretKeyFactory.generateSecret(spec).getEncoded();

        // 추후 하드웨어 성능 향상으로 인해 iteration이 더 커지더라도
        // 이전 iteration으로 암호화된 비밀번호를 알아내기 위하여 iteration과 digest를 같이 보관
        return iterations + ":" + toHex(salt) + ":" + toHex(digest);
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        return bi.toString(16);
    }
}
