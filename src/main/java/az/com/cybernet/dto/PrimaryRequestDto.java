package az.com.cybernet.dto;

import lombok.*;

@Data
public class PrimaryRequestDto {
  String clientId;
  String operation;
  String encryptedKey;
  String encryptedPayload;
}
