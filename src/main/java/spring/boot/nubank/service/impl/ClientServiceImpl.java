package spring.boot.nubank.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import spring.boot.nubank.model.Client;
import spring.boot.nubank.model.Contact;
import jakarta.transaction.Transactional;
import spring.boot.nubank.dto.ClientDto;
import spring.boot.nubank.dto.ClientResponseDto;
import spring.boot.nubank.dto.ContactResponseDto;
import spring.boot.nubank.exceptions.ClientAlreadyExistsException;
import spring.boot.nubank.exceptions.ClientNotExistsException;
import spring.boot.nubank.repository.ClientRepository;
import spring.boot.nubank.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService{
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public ClientDto save(ClientDto clientDto) {
        Optional<spring.boot.nubank.model.Client> clientExists = this.clientRepository.findByEmail(clientDto.email());

        if(clientExists.isPresent()){
            throw new ClientAlreadyExistsException();
        }

        Client client = Client.builder()
        .name(clientDto.name())
        .uuid(UUID.randomUUID().toString())
        .phone(clientDto.phone())
        .email(clientDto.email())
        .contacts(new ArrayList<Contact>()).build();

        this.clientRepository.save(client);
        return new ClientDto(client.getUuid(), client.getName(), client.getPhone(), client.getEmail(), client.getContacts());
    }

    @Override
    public List<ClientResponseDto> listAll() {
        var clients = this.clientRepository.findAll();
        clients.sort(Comparator.comparing(Client::getId));
        return clients.stream()
            .map(this::convertClientToDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<ContactResponseDto> listContacts(String id) {
        Optional<Client> clientExists = this.clientRepository.findByuuid(id);

        if(clientExists.isEmpty()){
            throw new ClientNotExistsException();
        }

        Client client = clientExists.get();
        List<Contact> contacts = new ArrayList<>(client.getContacts());
        contacts.sort(Comparator.comparing(Contact::getName));
        return contacts.stream()
            .map(this::convertContactToDto)
            .collect(Collectors.toList());
    }    

    private ClientResponseDto convertClientToDto(Client client){
        ClientResponseDto dto = new ClientResponseDto();
        dto.setName(client.getName());
        dto.setEmail(client.getEmail());
        dto.setUuid(client.getUuid());

        List<Contact> contacts = client.getContacts();
        List<ContactResponseDto> contacstDto = contacts.stream().map(this::convertContactToDto).collect(Collectors.toList());
        dto.setContacts(contacstDto);
        return dto;
    }

    private ContactResponseDto convertContactToDto(Contact contact){
        ContactResponseDto dto = new ContactResponseDto();
        dto.setName(contact.getName());
        dto.setPhone(contact.getPhone());
        return dto;
    }
}