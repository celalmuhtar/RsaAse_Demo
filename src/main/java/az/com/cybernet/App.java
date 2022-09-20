package az.com.cybernet;

import az.com.cybernet.dto.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.web.client.*;
import az.com.cybernet.services.*;

@Slf4j
@RequiredArgsConstructor
public class App {
  public static void main(String[] args) throws Exception {
    /*------------------Bu hissə standart java api düzəltdiyim üçün məcbur əlavə etmişəm-----------*/
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.scan("az.com.cybernet.services");
    context.refresh();
    UtilService service = context.getBean(UtilService.class);
    /*---------------son--------------*/

    String aesKey = service.getRandomAesKey();
    log.info(aesKey);

    String str = "{\"voen\":\"1234567890\"}";
    String encryptedPayload = service.encryptTextUsingAES(str, aesKey);
    log.info(encryptedPayload);

    /*public key-nizi əlavə edəcəksiniz*/
    String encryptedKey = service.encryptAESKey(aesKey, "");
    log.info(encryptedKey);

    RestTemplate restTemplate = new RestTemplate();
    /*Asan Bridg tərəfindən verilən url daxil edəcəksiniz*/
    String url = "";

    PrimaryRequestDto dto = new PrimaryRequestDto();
    /*Sizə məxsus clientId əlavə edəcəksiniz*/
    dto.setClientId("");
    /*sizə təqdim edilən servisin adını əlavə edəcəksiniz*/
    dto.setOperation("");
    dto.setEncryptedKey(encryptedKey);
    dto.setEncryptedPayload(encryptedPayload);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");

    HttpEntity<PrimaryRequestDto> request = new HttpEntity<>(dto, headers);

    ResponseEntity<String> result = restTemplate.postForEntity(url, request, String.class);

    log.info(result.getBody());


    /*----------*/
    context.close();
    /*----son---*/
  }

}
