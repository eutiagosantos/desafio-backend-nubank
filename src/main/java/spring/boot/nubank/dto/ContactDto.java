package spring.boot.nubank.dto;

public record ContactDto(String id, String name, String phone, String clientEmail) {
    public ContactDto(String name, String phone, String clientEmail) {
        this(null, name, phone, clientEmail);
    }
}
