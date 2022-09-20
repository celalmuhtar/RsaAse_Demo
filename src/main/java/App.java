import dto.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.web.client.*;
import services.*;

import java.util.*;

@RequiredArgsConstructor
public class App {
  public static void main(String[] args) throws Exception {
    /*------------------Bu hissə standart java api düzəltdiyim üçün məcbur əlavə etmişəm-----------*/
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.scan("services");
    context.refresh();
    UtilService service = context.getBean(UtilService.class);
    /*---------------son--------------*/

    String aesKey = service.getRandomAesKey();
    System.out.println(aesKey);

    String str = "{\"voen\":\"9900050571\"}";
    String encryptedPayload = service.encryptTextUsingAES(str, aesKey);
    System.out.println(encryptedPayload);

    String encryptedKey = service.encryptAESKey(aesKey, "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKtAUsccrWd6trAMxnhWbvGnl1Dp1olalRfZhBKI9EWwtxBdw494fDAYCb3WkLVUfeKHvKeu7fgfLmJ1yKivyz0CAwEAAQ==");
    System.out.println(encryptedKey);


    RestTemplate restTemplate = new RestTemplate();
    String url = "http://dvxintegenc.vn.local/v1/api/integ/proxyapi/callDvxService";

    PrimaryRequestDto dto = new PrimaryRequestDto();
    dto.setClientId("76ca7091-7361-4270-86e8-64019043e25c");
    dto.setOperation("WS_ITS_GET_TAXPAYER_INFO");
    dto.setEncryptedKey(encryptedKey);
    dto.setEncryptedPayload(encryptedPayload);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");

    HttpEntity<PrimaryRequestDto> request = new HttpEntity<>(dto, headers);

    ResponseEntity<String> result = restTemplate.postForEntity(url, request, String.class);

    System.out.println(result);

    /*----------*/
    context.close();
    /*----son---*/
  }

}
