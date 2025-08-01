package spring.boot.nubank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spring.boot.nubank.dto.ContactDto;
import spring.boot.nubank.model.Client;
import spring.boot.nubank.model.Contact;
import spring.boot.nubank.exceptions.ClientNotExistsException;
import spring.boot.nubank.exceptions.ContactAlreadyExistsException;
import spring.boot.nubank.repository.ClientRepository;
import spring.boot.nubank.repository.ContactRepository;
import spring.boot.nubank.service.impl.ContactServiceImpl;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ContactServiceImpl contactService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should save contact successfully when phone does not exist and client exists")
    void shouldSaveContactSuccessfully() {
        String phone = "31 91234-5678";
        String clientEmail = "client@email.com";
        String name = "Contact Name";
        String uuid = UUID.randomUUID().toString();

        ContactDto contactDto = new ContactDto(uuid, name, phone, clientEmail);

        when(contactRepository.findByPhone(phone)).thenReturn(Optional.empty());
        Client client = Client.builder().email(clientEmail).build();
        when(clientRepository.findByEmail(clientEmail)).thenReturn(Optional.of(client));
        when(contactRepository.save(any(Contact.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ContactDto saved = contactService.save(contactDto);

        assertNotNull(saved);
        assertEquals(name, saved.name());
        assertEquals(phone, saved.phone());
        assertEquals(clientEmail, saved.clientEmail());
        verify(contactRepository).findByPhone(phone);
        verify(clientRepository).findByEmail(clientEmail);
        verify(contactRepository).save(any(Contact.class));
    }

    @Test
    @DisplayName("Should throw ContactAlreadyExistsException when contact with phone already exists")
    void shouldThrowContactAlreadyExistsException() {
        String phone = "31 91234-5678";
        String clientEmail = "client@email.com";
        String name = "Contact Name";
        String uuid = UUID.randomUUID().toString();

        ContactDto contactDto = new ContactDto(uuid, name, phone, clientEmail);

        when(contactRepository.findByPhone(phone)).thenReturn(Optional.of(Contact.builder().build()));

        assertThrows(ContactAlreadyExistsException.class, () -> contactService.save(contactDto));
        verify(contactRepository).findByPhone(phone);
        verify(contactRepository, never()).save(any(Contact.class));
    }

    @Test
    @DisplayName("Should throw ClientNotExistsException when client does not exist")
    void shouldThrowClientNotExistsException() {
        String phone = "31 91234-5678";
        String clientEmail = "client@email.com";
        String name = "Contact Name";
        String uuid = UUID.randomUUID().toString();

        ContactDto contactDto = new ContactDto(uuid, name, phone, clientEmail);

        when(contactRepository.findByPhone(phone)).thenReturn(Optional.empty());
        when(clientRepository.findByEmail(clientEmail)).thenReturn(Optional.empty());

        assertThrows(ClientNotExistsException.class, () -> contactService.save(contactDto));
        verify(contactRepository).findByPhone(phone);
        verify(clientRepository).findByEmail(clientEmail);
        verify(contactRepository, never()).save(any(Contact.class));
    }
}

