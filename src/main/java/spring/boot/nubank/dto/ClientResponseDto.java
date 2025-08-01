package spring.boot.nubank.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ClientResponseDto {
    String uuid;
    String name;
    String email;
    List<ContactResponseDto> contacts;
}
