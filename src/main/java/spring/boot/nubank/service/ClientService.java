package spring.boot.nubank.service;

import java.util.List;

import spring.boot.nubank.dto.ClientDto;
import spring.boot.nubank.dto.ClientResponseDto;
import spring.boot.nubank.dto.ContactResponseDto;

public interface ClientService {
    
    ClientDto save(ClientDto clientDto);
    List<ClientResponseDto> listAll();
    List<ContactResponseDto> listContacts(String id);
}
