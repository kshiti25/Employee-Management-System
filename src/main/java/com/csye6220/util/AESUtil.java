package com.csye6220.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.util.*;

import java.security.SecureRandom;
import java.util.Base64;

public class AESUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int KEY_LENGTH = 256;
    private static final int ITERATION_COUNT = 65536;
    private static final int IV_LENGTH_BYTES = 16;

    public static String encrypt(String dataToEncrypt, String password) throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[IV_LENGTH_BYTES];
        random.nextBytes(salt);

        IvParameterSpec ivParameterSpec = new IvParameterSpec(salt);
        SecretKeySpec secretKeySpec = createSecretKey(password, salt);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encryptedData = cipher.doFinal(dataToEncrypt.getBytes());
        byte[] encryptedDataWithSalt = concatenate(salt, encryptedData);

        return Base64.getEncoder().encodeToString(encryptedDataWithSalt);
    }

    public static String decrypt(String dataToDecrypt, String password) throws Exception {
        byte[] decodedData = Base64.getDecoder().decode(dataToDecrypt);
        byte[] salt = extractSalt(decodedData);
        byte[] encryptedData = extractEncryptedData(decodedData);

        IvParameterSpec ivParameterSpec = new IvParameterSpec(salt);
        SecretKeySpec secretKeySpec = createSecretKey(password, salt);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] decryptedData = cipher.doFinal(encryptedData);
        return new String(decryptedData);
    }

    private static SecretKeySpec createSecretKey(String password, byte[] salt) throws Exception {
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM);
        return new SecretKeySpec(secretKeyFactory.generateSecret(pbeKeySpec).getEncoded(), ALGORITHM);
    }

    private static byte[] concatenate(byte[] salt, byte[] data) {
        byte[] result = new byte[salt.length + data.length];
        System.arraycopy(salt, 0, result, 0, salt.length);
        System.arraycopy(data, 0, result, salt.length, data.length);
        return result;
    }

    private static byte[] extractSalt(byte[] data) {
        return Arrays.copyOfRange(data, 0, IV_LENGTH_BYTES);
    }

    private static byte[] extractEncryptedData(byte[] data) {
        return Arrays.copyOfRange(data, IV_LENGTH_BYTES, data.length);
    }
}
