package services.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;
import services.*;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UtilServiceImpl implements UtilService {
  @Override
  public String getRandomAesKey() {
    KeyGenerator keyGen = null;
    try {
      keyGen = KeyGenerator.getInstance("AES");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    keyGen.init(128);
    SecretKey secretKey = keyGen.generateKey();
    byte[] raw = secretKey.getEncoded();
    return Base64.getEncoder().encodeToString(raw);
  }

  // Decrypt text using AES key
  @Override
  public String decryptTextUsingAES(String encryptedText, String aesKeyString) {
    byte[] decodedKey = Base64.getDecoder().decode(aesKeyString);
    SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

    // AES defaults to AES/ECB/PKCS5Padding in Java 7
    Cipher aesCipher = null;
    byte[] bytePlainText = null;
    try {
      aesCipher = Cipher.getInstance("AES");

      aesCipher.init(Cipher.DECRYPT_MODE, originalKey);
      bytePlainText = aesCipher.doFinal(Base64.getDecoder().decode(encryptedText));
    } catch (InvalidKeyException
             | IllegalBlockSizeException
             | BadPaddingException
             | NoSuchPaddingException
             | NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    return new String(bytePlainText);
  }

  // Encrypt text using AES key
  @Override
  public String encryptTextUsingAES(String plainText, String aesKeyString) {
    byte[] decodedKey = Base64.getDecoder().decode(aesKeyString);
    SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

    // AES defaults to AES/ECB/PKCS5Padding in Java 7
    Cipher aesCipher = null;
    byte[] byteCipherText = null;
    try {
      aesCipher = Cipher.getInstance("AES");

      aesCipher.init(Cipher.ENCRYPT_MODE, originalKey);
      byteCipherText = aesCipher.doFinal(plainText.getBytes());
    } catch (InvalidKeyException
             | IllegalBlockSizeException
             | BadPaddingException
             | NoSuchPaddingException
             | NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    return Base64.getEncoder().encodeToString(byteCipherText);
  }

  // Decrypt AES Key using RSA public key
  @Override
  public String decryptAESKey(String encryptedAESKey, PrivateKey privateKey) {
    Cipher cipher = null;
    String decryptedKey = null;
    try {
      cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      decryptedKey = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedAESKey)));
    } catch (InvalidKeyException
             | IllegalBlockSizeException
             | BadPaddingException
             | NoSuchPaddingException
             | NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    return decryptedKey;
  }

  // Encrypt AES Key using RSA private key
  @Override
  public String encryptAESKey(String plainAESKey, PublicKey publicKey) {
    Cipher cipher = null;
    String encryptedKey = null;
    try {
      cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      encryptedKey = Base64.getEncoder().encodeToString(cipher.doFinal(plainAESKey.getBytes()));
    } catch (InvalidKeyException
             | IllegalBlockSizeException
             | BadPaddingException
             | NoSuchPaddingException
             | NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
    return encryptedKey;
  }

  @Override
  public Map<String, Object> jsonStringToMap(String json) {
    Map<String, Object> mapping = new HashMap<>();
    try {
      mapping = new ObjectMapper().readValue(json, HashMap.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return mapping;
  }

  @Override
  public String encryptAESKey(String plainAESKey, String sPublicKey) {
    PublicKey publicKey = null;
    try {
      byte[] privateKeyBytes = Base64.getDecoder().decode(sPublicKey.getBytes("utf-8"));
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(privateKeyBytes);
      KeyFactory fact = KeyFactory.getInstance("RSA");
      publicKey = fact.generatePublic(keySpec);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    } catch (InvalidKeySpecException e) {
      throw new RuntimeException(e);
    }
    return encryptAESKey(plainAESKey, publicKey);
  }

  @Override
  public Map<String, String> getNewRsaPublicPrivateKey() {
    Map<String, String> response = new HashMap<>();
    try {
      Cipher cipher = Cipher.getInstance("RSA");
      KeyPairGenerator darken = KeyPairGenerator.getInstance("RSA");
      darken.initialize(512);
      KeyPair keyPair = darken.generateKeyPair();

      Key publicKey = keyPair.getPublic();
      Key privateKey = keyPair.getPrivate();

      String sPublicKey = new String(Base64.getEncoder().encode(publicKey.getEncoded()));
      response.put("PUBLIC", sPublicKey);
      System.out.println("Public-key: "+sPublicKey);
      String sPrivateKey = new String(Base64.getEncoder().encode(privateKey.getEncoded()));
      response.put("PRIVATE", sPrivateKey);
      System.out.println("Private-key: "+sPrivateKey);

    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    } catch (NoSuchPaddingException e) {
      throw new RuntimeException(e);
    }
    return response;
  }
}
