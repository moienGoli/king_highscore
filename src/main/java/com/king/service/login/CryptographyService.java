package com.king.service.login;

import com.king.exception.AppException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Base64;

/**
 * provides two way encryption with base64.
 * <p>
 * Created by moien on 9/11/17.
 */

public class CryptographyService {

    private static final byte[] SALT = {
            (byte) 0xdd, (byte) 0x54, (byte) 0x40, (byte) 0x11,
            (byte) 0xde, (byte) 0x13, (byte) 0x58, (byte) 0x23,
    };

    private static final String PBE_MD5_DES = "PBEWithMD5AndDES";
    private static final char[] PASSWORD_PHRASE = "Go2y$MarAChOZa2DMa2daRPas$imo2NShoDValICha2rENabo2DE".toCharArray();


    public String encrypt(String property) {

        if (property == null || property.isEmpty()) {
            return property;
        }

        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBE_MD5_DES);
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD_PHRASE));
            Cipher pbeCipher = Cipher.getInstance(PBE_MD5_DES);
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
            return base64UrlEncode(pbeCipher.doFinal(property.getBytes("UTF-8")));
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            throw new AppException("There was a problem encrypting", e);
        }
    }

    public String decrypt(String property) throws GeneralSecurityException, IOException {

        if (property == null || property.isEmpty()) {
            return property;
        }

        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBE_MD5_DES);
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD_PHRASE));
            Cipher pbeCipher = Cipher.getInstance(PBE_MD5_DES);
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
            return new String(pbeCipher.doFinal(base64UrlDecode(property)), "UTF-8");
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            throw new AppException("There was a problem decrypting:" + property, e);
        }
    }

    private String base64UrlEncode(byte[] bytes) {
        return Base64.getUrlEncoder().encodeToString(bytes);
    }

    private byte[] base64UrlDecode(String property) {
        return Base64.getUrlDecoder().decode(property);
    }


}
