package az.com.cybernet;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class Edliyye1 {
  public static void main(String[] args) throws Exception {

    // Metni şifrele
    String plainText = "{2378122000185}";
    String sPublicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAip/uAw0m63af9tCK8xEjnxa1oL+4EpaYM9C3nt9NikSXaNZMFlDqx2K0ySQoMXs01+BKk94VDqLx9poCBtjbQFxq1TlOfALNoU7keipwhTKmH4O9SnowHTsCmVCqtYrUTHZywa/oO92LZ0oRPTWlImThd5cWBj6r13iGX5l3MDqXykpBla/vY7NyVT3lOGwCl7XEzzVnMk4d6YYK1r+kyo4QeIQdBpEV+8BFkRSphK7hJ0V2MSTRaKd70j8Mbk3ZSpmHybH8f/ulFW3w/FVQ1KmmFzas2oNj5U5U7HeJunaveuMDAznSGrOl2DsI0WaITQux81H1p5L5cv8EiMsU3e/6wcGTs9b+n7o9MxZl1JlYR1p/rAAoTfghdSgsCMhKeEzMt2Zoo56tiYPpunb2j8TK91ydYWIZN+xg/on2tftiQ2VMKZw++3AKr44nYaUB7AeJfjYRQ4VDjIvAaAFnQN0wGwqHkpO1j8KM2WEeWz93VfcRIlz22eEFrkk3MIFnMdjT6FuMxMLo/zbZK2mPZHMZW18iK3yqOfq2QVJ8vmFJAHw8D+eUaQfP3aNUTQadCPMgiwCjHiK+tyTa5zsYEAb1AIzaizUCpvhC3KNTO0bw7j5UkOSGBGdausw4IBpu3w2PwzEMVSmEpVfptE8RIUffPvDXYmPkAwLZ1m7BkWUCAwEAAQ==";

    byte[] encryptedText = encrypt(plainText, convertStringToPublicKey(sPublicKey));

    // Sonuçları göster
    System.out.println("Orjinal Metin: " + plainText);
    System.out.println("Şifrelenmiş Metin: " + Base64.getEncoder().encodeToString(encryptedText));
  }

  public static KeyPair generateKeyPair() throws Exception {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    return keyPairGenerator.generateKeyPair();
  }

  public static byte[] encrypt(String plainText, PublicKey publicKey) throws Exception {
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    return cipher.doFinal(plainText.getBytes());
  }

  public static String decrypt(byte[] encryptedText, PrivateKey privateKey) throws Exception {
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] decryptedBytes = cipher.doFinal(encryptedText);
    return new String(decryptedBytes);
  }

  private static PublicKey convertStringToPublicKey(String sPublicKey) {
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
    return publicKey;
  }
}
