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
public class App {
  public static void main(String[] args) throws Exception {
    /*------------------Bu hissə standart java api düzəltdiyim üçün məcbur əlavə etmişəm-----------*/
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.scan("az.com.cybernet.services");
    context.refresh();
    UtilService service = context.getBean(UtilService.class);
    /*---------------son--------------*/

    String aesKey = service.getRandomAesKey();
    log.info("AES: "+aesKey);

    String str = "{\n" +
        "  \"voen\": \"9900050571\"\n" +
        "}";
    String encryptedPayload = service.encryptTextUsingAES(str, aesKey);
    log.info("encryptedPayload: "+encryptedPayload);

    /*public key-nizi əlavə edəcəksiniz*/
    String encryptedKey = service.encryptAESKey(aesKey, "Sizə təqdim etdiyimiz RSA public key daxil edirsiniz");
    log.info("encryptedKey: "+encryptedKey);

    RestTemplate restTemplate = new RestTemplate();
    /*Asan Bridg tərəfindən verilən url daxil edəcəksiniz*/
    //String url = "http://localhost:9090/v1/api/integ/proxyapi/callDvxService";
    String url = "bura asanbridg tərəfindən təqdim edilən linki daxil edirsiniz";

    PrimaryRequestDto dto = new PrimaryRequestDto();
    /*Sizə məxsus clientId əlavə edəcəksiniz*/
    dto.setClientId("XXXXXXXXXXXXXXXXXXXXX");
    /*sizə təqdim edilən servisin adını əlavə edəcəksiniz*/
    dto.setOperation("XXXXXXXXXXXXXXXXXX");
    dto.setEncryptedKey(encryptedKey);
    dto.setEncryptedPayload(encryptedPayload);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    headers.set("X-Bridge-Authorization", "asanbridg tərəfindən təqdim edilən token daxil edirsiniz");

    HttpEntity<PrimaryRequestDto> request = new HttpEntity<>(dto, headers);

    ResponseEntity<String> result = restTemplate.postForEntity(url, request, String.class);

    log.info(result.getBody());


    /*----------*/
    context.close();
    /*----son---*/
  }

}
