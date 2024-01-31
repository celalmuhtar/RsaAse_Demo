package az.com.cybernet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.TimeZone;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.core5.http.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Rasim R. İmanov
 */
public class NPTestClient {

    public static final String KEY_ALG_RSA = "RSA";
    public static final String CIPHER_RSA = "RSA/ECB/PKCS1Padding";
    public static final int RSA_KEY_SIZE = 2048;
    public static final int KEY_SIZE_AES = 128;
    public static final String KEY_ALG_AES = "AES";
    public static final String CIPHER_AES = "AES/CBC/PKCS5Padding";

    public static final int STREAM_BUFF_SIZE = 1024;

    public static final String API_BASE_URL = "url_text";
    private static final int OUTPUT_BUFFER_SIZE = 4096;

    private static final String CLIENT_ID = "client id";
    private static final String RSA_PUBLIC_KEY = "public key";
    private final static int REPORT_ACTION = 1;







    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidAlgorithmParameterException {

        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Baku"));
        //RSA Public key - base64 formatda, istifadəçiyə əvvəlcədən verilir
        final String rsaPublicKeyBase64 = RSA_PUBLIC_KEY;// loadResourceAsString("/public-key.txt");
        //ClientId - əvvəlcədən istifadəçiyə verilir
        final String clientId = CLIENT_ID;// loadResourceAsString("/client-id.txt");
        //Sorğu məqsədi
        final String purposeCode = "001";
        //Şəxsiyyət vəsiqəsinin nömrəsi
        final String documentNo = "AA1231231"; //və ya 12312311
        //Şəxsiyyət vəsiqəsinin FİNi
        final String pinCode = "1231231";

        //Public key yüklənir
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALG_RSA);

        X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(
                Base64.getDecoder().decode(rsaPublicKeyBase64.getBytes()));

        PublicKey publicKey = keyFactory.generatePublic(keySpecPublic);
        //
        //RSA alqoritmi üçün uyğun transformasiya seçilir (RSA/ECB/PKCS1Padding)
        Cipher cipherRsa = Cipher.getInstance(CIPHER_RSA);
        cipherRsa.init(Cipher.ENCRYPT_MODE, publicKey);

        //Sorğu parametrləri JSON formatında hazırlanır
        String reportRequestData = createReportRequest(clientId, purposeCode, documentNo, pinCode);

        System.out.println(reportRequestData);
        //Parametrlər RSA ilə şifrələnir
        byte[] requestDataRaw = cipherRsa.doFinal(reportRequestData.getBytes(StandardCharsets.UTF_8));
        //Serverə göndəriləcək sorğu hazırlanır.
        // {"clientId":"xxxxx-xx-xx", "data":"xxxxxxxxx.....xxxx"}
        String requestBody
                = createRequest(clientId,
                Base64.getEncoder().encodeToString(requestDataRaw));

        System.out.println("Request:" + requestBody);

        //Serverə POST sorğu göndərilir
        String targetUrl = API_BASE_URL;


    }

    private static String createReportRequest(String clientId, String purposeCode, String docNo, String pinCode) {
        return "{\"accepted\":\"true\","
                + "\"clientId\":\"" + clientId + "\","
                + "\"purposeCode\":\"" + purposeCode + "\","
                + "\"reason\":\"A04-LatinOnlyNoSpace/LatinNo2022.02-02\","
                + "\"docSerie\":\"AZE\","
                + "\"documentNo\":\"" + docNo + "\","
                + "\"pinCode\":\"" + pinCode + "\"}";
    }


    private static String createRequest(String clientId, String data) {
        return "{"
                + "\"clientId\":\"" + clientId.trim() + "\","
                + "\"action\": " + REPORT_ACTION + ","
                + "\"data\":\"" + data + "\"}";
    }

    private static void copyStream(Cipher ci, InputStream in, OutputStream out, int bufferSize)
            throws javax.crypto.IllegalBlockSizeException,
            javax.crypto.BadPaddingException,
            java.io.IOException {

        byte[] ibuf = new byte[bufferSize];
        int len;
        while ((len = in.read(ibuf)) != -1) {
            byte[] obuf = ci.update(ibuf, 0, len);
            if (obuf != null) {
                out.write(obuf);
            }
        }
        byte[] obuf = ci.doFinal();
        if (obuf != null) {
            out.write(obuf);
        }
    }
}