package az.com.cybernet;

import az.com.cybernet.dto.*;
import az.com.cybernet.services.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.web.client.*;

@Slf4j
@RequiredArgsConstructor
public class AppAsan2 {
  public static void main(String[] args) throws Exception {
    /*------------------Bu hissə standart java api düzəltdiyim üçün məcbur əlavə etmişəm-----------*/
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.scan("az.com.cybernet.services");
    context.refresh();
    UtilService service = context.getBean(UtilService.class);
    /*---------------son--------------*/

    String aesKey = "cIm48lwj7XEQ+m0P+S0H/w=="; //service.getRandomAesKey();
    log.info("AES: "+aesKey);

    String str = "{\n" +
        "  \"voen\": \"1304801101\"\n" +
        "}";
    String encryptedPayload = service.encryptTextUsingAES(str, aesKey);
    log.info("encryptedPayload: "+encryptedPayload);

    /*public key-nizi əlavə edəcəksiniz*/
    String encryptedKey = service.encryptAESKey(aesKey, "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKx/WgndzN5IU0S+LQFf+G2hG05LRq4NaeJwCQJ7kZg7iT9K/2SodX2aQFIUmNbZQzODo0jURTpRi/pXISKjSQ8CAwEAAQ==");
    log.info("encryptedKey: "+encryptedKey);

    RestTemplate restTemplate = new RestTemplate();
    /*Asan Bridg tərəfindən verilən url daxil edəcəksiniz*/
    //String url = "http://localhost:9090/v1/api/integ/proxyapi/callDvxService";
    String url = "https://bridge-proxy-taxes.e-gov.az/api/1/779868B2-B6F0-4BCA-AD6F-BA178F27EA3E/taxesSecurityService\n";

    PrimaryRequestDto dto = new PrimaryRequestDto();
    /*Sizə məxsus clientId əlavə edəcəksiniz*/
    dto.setClientId("fe32c50c-9f0f-4f39-a8a3-70f7dc3e1736");
    /*sizə təqdim edilən servisin adını əlavə edəcəksiniz*/
    dto.setOperation("WS_AKB_GET_TAX_PAYER_INFO");
    dto.setEncryptedKey(encryptedKey);
    dto.setEncryptedPayload(encryptedPayload);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    headers.set("X-Bridge-Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJvcmdhbml6YXRpb24iOiI5MUI1NTlERC0zNjU0LTRDNzAtQkFDNS1DMUJCRkI5Q0QwNUMiLCJzaG9ydF9uYW1lIjoidGF4ZXNfcHJvZCIsImlkZW50aXR5IjoiNzc5ODY4QjItQjZGMC00QkNBLUFENkYtQkExNzhGMjdFQTNFIiwiZXhwaXJlIjoxNjIxMjU2OTY4fQ.-3_Rrmw31CWeXRZ-ZmVwcudaiRQMbzTT8iw0N782i7s");

    HttpEntity<PrimaryRequestDto> request = new HttpEntity<>(dto, headers);

    ResponseEntity<String> result = restTemplate.postForEntity(url, request, String.class);

    log.info(result.getBody());


    /*----------*/
    context.close();
    /*----son---*/
  }

}
