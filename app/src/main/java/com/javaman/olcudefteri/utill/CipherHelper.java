package com.javaman.olcudefteri.utill;

import android.util.Base64;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CipherHelper {

    private static final String cipherSecret="dfkjdfjdfn34343.fgfg944rnfjyutgf3dfgı3jk3m845nf94.";
    private static final String aes="AES";
    private static final String algorithm="SHA-256";
    private static final String character_encoding="UTF-8";

    public static String decrypt(String outputString) throws Exception {
        SecretKeySpec key=generateKey(cipherSecret);
        Cipher c= Cipher.getInstance(aes);
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decVal1= Base64.decode(outputString,Base64.DEFAULT);
        byte[] decVal2=c.doFinal(decVal1);
        String decValString=new String(decVal2);
        return decValString;

    }

    public static String encrypt(String data) throws Exception{
        SecretKeySpec key=generateKey(cipherSecret);
        Cipher c= Cipher.getInstance(aes);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal=c.doFinal(data.getBytes());
        String encryptedValue= Base64.encodeToString(encVal,Base64.DEFAULT);
        return encryptedValue;
    }

    private static  SecretKeySpec generateKey(String password) throws Exception {
        final MessageDigest messageDigest=MessageDigest.getInstance(algorithm);
        byte[] bytes=password.getBytes(character_encoding);
        messageDigest.update(bytes,0,bytes.length);
        byte[] key=messageDigest.digest();
        SecretKeySpec secretKeySpec=new SecretKeySpec(key,aes);
        return secretKeySpec;
    }
}
