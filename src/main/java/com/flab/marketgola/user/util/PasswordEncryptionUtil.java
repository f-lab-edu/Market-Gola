package com.flab.marketgola.user.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordEncryptionUtil {

    public static final int ENOUGH_ITERATION_COUNT = 1000;
    public static final int KEY_LENGTH = 256;
    public static final String SEPERATION = ":";
    public static final int SALT_SIZE = 16;
    public static final String DIJEST_GENERATION_ALGORHYTHM = "PBKDF2WithHmacSHA256";
    public static final String RANDOM_NUMBER_GENERATOR_ALGORHYTHM = "SHA1PRNG";

    private PasswordEncryptionUtil() {
    }

    public static String encrypt(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();
        byte[] digest = makeDijest(ENOUGH_ITERATION_COUNT, chars, salt);

        // 추후 하드웨어 성능 향상으로 인해 iteration이 더 커지더라도
        // 이전 iteration으로 암호화된 비밀번호를 알아내기 위하여 iteration과 digest를 같이 보관
        return ENOUGH_ITERATION_COUNT + SEPERATION + toHex(salt) + SEPERATION + toHex(digest);
    }

    private static byte[] makeDijest(int iteration, char[] password, byte[] salt)
            throws InvalidKeySpecException, NoSuchAlgorithmException {

        PBEKeySpec spec = new PBEKeySpec(password, salt, iteration,
                KEY_LENGTH); //다이제스트(Password-Based-Encryption Key)에 대한 스펙 정의하기
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(
                DIJEST_GENERATION_ALGORHYTHM);
        return secretKeyFactory.generateSecret(spec).getEncoded();
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance(RANDOM_NUMBER_GENERATOR_ALGORHYTHM);
        byte[] salt = new byte[SALT_SIZE];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);

        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    public static boolean validatePassword(String storedPasswordCombi, String comparedPassword)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        String[] parts = storedPasswordCombi.split(SEPERATION);

        int iteration = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] storedPasswordDijest = fromHex(parts[2]);

        byte[] comparingPasswordDijest = makeDijest(iteration, comparedPassword.toCharArray(),
                salt);

        return MessageDigest.isEqual(storedPasswordDijest, comparingPasswordDijest);
    }

    private static byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
