package az.com.cybernet.services;

import java.security.*;
import java.util.*;

public interface UtilService {
  String getRandomAesKey();
  String encryptTextUsingAES(String plainText, String aesKeyString);
  String decryptTextUsingAES(String encryptedText, String aesKeyString);
  Map<String, String> getNewRsaPublicPrivateKey();
  String decryptAESKey(String encryptedAESKey, PrivateKey privateKey);
  String encryptAESKey(String plainAESKey, PublicKey publicKey);
  Map<String, Object> jsonStringToMap(String json);
  String encryptAESKey(String plainAESKey, String sPublicKey);
  String encryptAESKeyEdliyye(String reqest, String sPublicKey);
}
