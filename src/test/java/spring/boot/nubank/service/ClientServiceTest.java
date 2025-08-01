package spring.boot.nubank.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import spring.boot.nubank.dto.ClientDto;
import spring.boot.nubank.model.Client;
import spring.boot.nubank.model.Contact;
import spring.boot.nubank.repository.ClientRepository;
import spring.boot.nubank.service.impl.ClientServiceImpl;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    
    @Mock
    ClientRepository clientRepository;

    @InjectMocks
    ClientServiceImpl clientService;

    private ClientDto clientDto;
    private Client client;
    
    @BeforeEach
    void setup(){
        clientDto = new ClientDto("John Doe", "31 9999-9999", "johnDoe@gmail.com", new ArrayList<Contact>() );
    }

    @Test
    @DisplayName("When save client is OK")
    void whenSaveClientOk(){
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientDto client = clientService.save(clientDto);

        assertNotNull(client.id());
        assertEquals(clientDto.email(), client.email());
        verify(clientRepository, atLeastOnce()).save(any(Client.class));
    }

    @Test
    @DisplayName("When list all clients is ok")
    void whenListClientsOk(){
        when(clientRepository.findAll()).thenReturn(new ArrayList<Client>());
        
    }
}
