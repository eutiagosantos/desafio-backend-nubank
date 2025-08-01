package spring.boot.nubank.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import spring.boot.nubank.dto.ContactDto;
import spring.boot.nubank.exceptions.ClientNotExistsException;
import spring.boot.nubank.exceptions.ContactAlreadyExistsException;
import spring.boot.nubank.model.Client;
import spring.boot.nubank.model.Contact;
import spring.boot.nubank.repository.ClientRepository;
import spring.boot.nubank.repository.ContactRepository;
import spring.boot.nubank.service.ContactService;

@Service
public class ContactServiceImpl implements ContactService{

    private final ContactRepository contactRepository;
    private final ClientRepository clientRepository;

    public ContactServiceImpl(ContactRepository contactRepository, ClientRepository clientRepository){
        this.contactRepository = contactRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public ContactDto save(ContactDto contactDto) {
        Optional<Contact> contactExists = this.contactRepository.findByPhone(contactDto.phone());
        Optional<Client> clientExists = this.clientRepository.findByEmail(contactDto.clientEmail());
        
        if(contactExists.isPresent()){
            throw new ContactAlreadyExistsException();
        }

        if(clientExists.isEmpty()){
            throw new ClientNotExistsException();
        }

        Client client = clientExists.get();
        Contact contact = Contact.builder()
            .name(contactDto.name())
            .uuid(UUID.randomUUID().toString())
            .phone(contactDto.phone())
            .client(client)
            .build();
        
        this.contactRepository.save(contact);

        return new ContactDto(contact.getUuid(),contact.getName(),contact.getPhone(), contact.getClient().getEmail());
    }
    
}
