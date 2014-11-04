package com.crimsonsky.remotainment.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.net.util.Base64;

public class Encrypter {
    private static String ALGORITHM = "AES";
    private static Key key = null;
    private static Cipher cipher = null;
    
    public Encrypter() throws Exception 
    {
    	setUp();
    }
    
    private static void setUp() throws Exception 
    {
    	cipher = Cipher.getInstance(ALGORITHM);
        byte[] raw = { (byte) 0xA5, (byte) 0x01, (byte) 0x7B, (byte) 0xE5,
				(byte) 0x23, (byte) 0xCA, (byte) 0xD4, (byte) 0xD2,
				(byte) 0xC6, (byte) 0x5F, (byte) 0x7D, (byte) 0x8B,
				(byte) 0x0B, (byte) 0x9A, (byte) 0x3C, (byte) 0xF1 };
        key = new SecretKeySpec(raw, ALGORITHM);
    }
    
    public String encrypt(String input)
            throws InvalidKeyException, 
                   BadPaddingException,
                   IllegalBlockSizeException, 
                   UnsupportedEncodingException 
    {
    	cipher.init(Cipher.ENCRYPT_MODE, key);
    	byte[] utf8 = input.getBytes("UTF8");
    	byte[] encryptedData = cipher.doFinal(utf8);
    	return Base64.encodeBase64String(encryptedData);
    }
    
    public String decrypt(String input)
            throws InvalidKeyException, 
                   BadPaddingException,
                   IllegalBlockSizeException, 
                   UnsupportedEncodingException 
    {
    	cipher.init(Cipher.DECRYPT_MODE, key);
    	byte[] decodedData = Base64.decodeBase64(input);
    	byte[] utf8 = cipher.doFinal(decodedData);
        String recovered = new String(utf8, "UTF8");
        return recovered;
    }
}
