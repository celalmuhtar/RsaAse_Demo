package az.com.cybernet;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

public class MyClass {
    public static final String KEY_ALG_RSA = "RSA";
    public static final String CIPHER_RSA = "RSA/ECB/PKCS1Padding";

    public static void main(String[] args) throws Exception {
        String publicKeyString = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAip/uAw0m63af9tCK8xEjnxa1oL+4EpaYM9C3nt9NikSXaNZMFlDqx2K0ySQoMXs01+BKk94VDqLx9poCBtjbQFxq1TlOfALNoU7keipwhTKmH4O9SnowHTsCmVCqtYrUTHZywa/oO92LZ0oRPTWlImThd5cWBj6r13iGX5l3MDqXykpBla/vY7NyVT3lOGwCl7XEzzVnMk4d6YYK1r+kyo4QeIQdBpEV+8BFkRSphK7hJ0V2MSTRaKd70j8Mbk3ZSpmHybH8f/ulFW3w/FVQ1KmmFzas2oNj5U5U7HeJunaveuMDAznSGrOl2DsI0WaITQux81H1p5L5cv8EiMsU3e/6wcGTs9b+n7o9MxZl1JlYR1p/rAAoTfghdSgsCMhKeEzMt2Zoo56tiYPpunb2j8TK91ydYWIZN+xg/on2tftiQ2VMKZw++3AKr44nYaUB7AeJfjYRQ4VDjIvAaAFnQN0wGwqHkpO1j8KM2WEeWz93VfcRIlz22eEFrkk3MIFnMdjT6FuMxMLo/zbZK2mPZHMZW18iK3yqOfq2QVJ8vmFJAHw8D+eUaQfP3aNUTQadCPMgiwCjHiK+tyTa5zsYEAb1AIzaizUCpvhC3KNTO0bw7j5UkOSGBGdausw4IBpu3w2PwzEMVSmEpVfptE8RIUffPvDXYmPkAwLZ1m7BkWUCAwEAAQ==";
        String plainText = "{2378122000185}";
        String encryptedText = RsaEncryptWithPublic(plainText, publicKeyString);
        System.out.println(encryptedText);
    }

    public static String RsaEncryptWithPublic(String clearText, String publicKey) throws Exception {
        publicKey = "-----BEGIN PUBLIC KEY-----" + publicKey + "-----END PUBLIC KEY-----";

        byte[] bytesToEncrypt = clearText.getBytes(StandardCharsets.UTF_8);
        Cipher encryptCipher = Cipher.getInstance(CIPHER_RSA);

        try (StringReader txtreader = new StringReader(publicKey)) {
            PemReader pemReader = new PemReader(txtreader);
            PemObject pemObject = pemReader.readPemObject();

            if (pemObject == null) {
                throw new RuntimeException("Public key content could not be read.");
            }

            byte[] keyBytes = pemObject.getContent();
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALG_RSA);
            PublicKey publicKey2 = keyFactory.generatePublic(keySpec);

            if (publicKey2 == null) {
                throw new RuntimeException("Failed to generate public key.");
            }

            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey2);
        }

        byte[] encryptedBytes = encryptCipher.doFinal(bytesToEncrypt);
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
}