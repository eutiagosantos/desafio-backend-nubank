package spring.boot.nubank.dto;

import java.util.List;

import spring.boot.nubank.model.Contact;

public record ClientDto(String id, String name, String phone, String email, List<Contact> contacts) {
    public ClientDto(String name, String phone, String email, List<Contact> contacts) {
        this(null, name, phone, email, contacts);
    }
}
