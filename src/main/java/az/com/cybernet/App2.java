package az.com.cybernet;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import java.util.Base64;

public class Edliyye2 {
    public static void main(String[] args) throws Exception {
        String publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhygXgkSJTMClk9e829f34ldLyLHsg01N5ycRi7j5kpTq5LIKrX4IC6oUVDVX4MG3GYI7leBwx4ZHpccSle9XFfBzdnm03EwOPAc" +
                "8UpwkMzdB9Z34AjBgWH6gWb8NumY4Ukd8GvNb9zOPPsHoSJGxwcYlemMmrJb4nQ/fogW807lyvovF278sSBIDE1p2XNvjx9uGA91KypJ2vixXWEucci/lKGodTrb8Udh5aXx0TqrLfRhqHQyxQ" +
                "AezPlcxB1JP69x/uEE/dT4UpQtVJHI62DS02JSRTEanWh+TiRe6laY+Wf1IzE/cFWf4ljVpu0lx54qVYEo+MS4BfHtKmIq8/QIDAQAB";

        // String'i PublicKey nesnesine dönüştür
        PublicKey publicKey = getPublicKeyFromString(publicKeyString);

        // Metni şifrele
        //String plainText = "2378122000185";
        String plainText = "{\"StartDate\":\"01.01.2024\", \"EndDate\":\"02.01.2024\"}";
        byte[] encryptedText = encrypt(plainText, publicKey);

        // Şifrelenmiş metni Base64'e çevir
        String base64EncryptedText = encodeBase64(encryptedText);

        // Sonuçları göster
        System.out.println("Orjinal Metin: " + plainText);
        System.out.println("Şifrelenmiş Metin (Base64): " + base64EncryptedText);
    }

    public static PublicKey getPublicKeyFromString(String keyString) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(keyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
    public static final String CIPHER_RSA = "RSA/ECB/PKCS1Padding";
    public static byte[] encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    }

    public static String encodeBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
}
