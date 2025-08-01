package spring.boot.nubank.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ContactResponseDto {
    String name;
    String phone;
}
