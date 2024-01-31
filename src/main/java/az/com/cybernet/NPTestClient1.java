package az.com.cybernet;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.TimeZone;


/**
 *
 * @author Rasim R. İmanov
 */
public class NPTestClient1 {

    public static final String KEY_ALG_RSA = "RSA";
    public static final String CIPHER_RSA = "RSA/ECB/PKCS1Padding";
    private static final String RSA_PUBLIC_KEY = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAip/uAw0m63af9tCK8xEjnxa1oL+4EpaYM9C3nt9NikSXaNZMFlDqx2K0ySQoMXs01+BKk94VDqLx9poCBtjbQFxq1TlOfALNoU7keipwhTKmH4O9SnowHTsCmVCqtYrUTHZywa/oO92LZ0oRPTWlImThd5cWBj6r13iGX5l3MDqXykpBla/vY7NyVT3lOGwCl7XEzzVnMk4d6YYK1r+kyo4QeIQdBpEV+8BFkRSphK7hJ0V2MSTRaKd70j8Mbk3ZSpmHybH8f/ulFW3w/FVQ1KmmFzas2oNj5U5U7HeJunaveuMDAznSGrOl2DsI0WaITQux81H1p5L5cv8EiMsU3e/6wcGTs9b+n7o9MxZl1JlYR1p/rAAoTfghdSgsCMhKeEzMt2Zoo56tiYPpunb2j8TK91ydYWIZN+xg/on2tftiQ2VMKZw++3AKr44nYaUB7AeJfjYRQ4VDjIvAaAFnQN0wGwqHkpO1j8KM2WEeWz93VfcRIlz22eEFrkk3MIFnMdjT6FuMxMLo/zbZK2mPZHMZW18iK3yqOfq2QVJ8vmFJAHw8D+eUaQfP3aNUTQadCPMgiwCjHiK+tyTa5zsYEAb1AIzaizUCpvhC3KNTO0bw7j5UkOSGBGdausw4IBpu3w2PwzEMVSmEpVfptE8RIUffPvDXYmPkAwLZ1m7BkWUCAwEAAQ==";

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidAlgorithmParameterException {
        final String rsaPublicKeyBase64 = RSA_PUBLIC_KEY;
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALG_RSA);
        X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(Base64.getDecoder().decode(rsaPublicKeyBase64.getBytes()));
        PublicKey publicKey = keyFactory.generatePublic(keySpecPublic);
        Cipher cipherRsa = Cipher.getInstance(CIPHER_RSA);
        cipherRsa.init(Cipher.ENCRYPT_MODE, publicKey);
        //Sorğu parametrləri JSON formatında hazırlanır
        String reportRequestData = "2378122000185";
        System.out.println(reportRequestData);
        //Parametrlər RSA ilə şifrələnir
        byte[] requestDataRaw = cipherRsa.doFinal(reportRequestData.getBytes(StandardCharsets.UTF_8));
        String encodingData = Base64.getEncoder().encodeToString(requestDataRaw);
        System.out.println(encodingData);

    }
}