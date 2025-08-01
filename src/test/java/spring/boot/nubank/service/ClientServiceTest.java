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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import spring.boot.nubank.dto.ClientDto;
import spring.boot.nubank.dto.ClientResponseDto;
import spring.boot.nubank.dto.ContactResponseDto;
import spring.boot.nubank.exceptions.ClientNotExistsException;
import spring.boot.nubank.model.Client;
import spring.boot.nubank.model.Contact;
import spring.boot.nubank.repository.ClientRepository;
import spring.boot.nubank.service.impl.ClientServiceImpl;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    void whenListClientsOk() {
        Contact contato1 = Contact.builder()
                .id(1L)
                .uuid("uuid-contato-1")
                .name("Contato 1")
                .phone("31 98888-8888")
                .build();

        Contact contato2 = Contact.builder()
                .id(2L)
                .uuid("uuid-contato-2")
                .name("Contato 2")
                .phone("31 97777-7777")
                .build();

        Client cliente1 = Client.builder()
                .id(1L)
                .uuid("uuid-cliente-1")
                .name("Cliente 1")
                .phone("31 99999-9999")
                .email("cliente1@email.com")
                .contacts(List.of(contato1, contato2))
                .build();

        Client cliente2 = Client.builder()
                .id(2L)
                .uuid("uuid-cliente-2")
                .name("Cliente 2")
                .phone("31 96666-6666")
                .email("cliente2@email.com")
                .contacts(new ArrayList<>())
                .build();

        List<Client> clientes = List.of(cliente1, cliente2);

        when(clientRepository.findAll()).thenReturn(clientes);
        when(clientRepository.findAll()).thenReturn(new ArrayList<>(clientes));

        List<ClientResponseDto> clients = clientService.listAll();

        verify(clientRepository, atLeastOnce()).findAll();
        assertNotNull(clients);
        assertEquals(2, clients.size());

        ClientResponseDto dto1 = clients.get(0);
        assertEquals("Cliente 1", dto1.getName());
        assertEquals("cliente1@email.com", dto1.getEmail());
        assertEquals("uuid-cliente-1", dto1.getUuid());
        assertNotNull(dto1.getContacts());
        assertEquals(2, dto1.getContacts().size());
        assertEquals("Contato 1", dto1.getContacts().get(0).getName());
        assertEquals("Contato 2", dto1.getContacts().get(1).getName());

        ClientResponseDto dto2 = clients.get(1);
        assertEquals("Cliente 2", dto2.getName());
        assertEquals("cliente2@email.com", dto2.getEmail());
        assertEquals("uuid-cliente-2", dto2.getUuid());
        assertNotNull(dto2.getContacts());
        assertEquals(0, dto2.getContacts().size());
    }

    @Test
    @DisplayName("When list contacts is ok")
    void whenListContactsOk() {
        String uuidCliente = "uuid-cliente-1";
        Client cliente = Client.builder()
                .id(1L)
                .uuid(uuidCliente)
                .name("Cliente 1")
                .phone("31 99999-9999")
                .email("cliente1@email.com")
                .contacts(new ArrayList<>())
                .build();

        Contact contato1 = Contact.builder()
                .id(1L)
                .uuid("uuid-contato-1")
                .name("Contato A")
                .phone("31 98888-8888")
                .client(cliente)
                .build();

        Contact contato2 = Contact.builder()
                .id(2L)
                .uuid("uuid-contato-2")
                .name("Contato B")
                .phone("31 97777-7777")
                .client(cliente)
                .build();

        cliente.setContacts(List.of(contato1, contato2));

        when(clientRepository.findByuuid(uuidCliente)).thenReturn(Optional.of(cliente));

        List<ContactResponseDto> contatos = clientService.listContacts(uuidCliente);

        verify(clientRepository, atLeastOnce()).findByuuid(uuidCliente);
        assertNotNull(contatos);
        assertEquals(2, contatos.size());

        assertEquals("Contato A", contatos.get(0).getName());
        assertEquals("Contato B", contatos.get(1).getName());
        assertEquals("31 98888-8888", contatos.get(0).getPhone());
        assertEquals("31 97777-7777", contatos.get(1).getPhone());
    }

    @Test
    @DisplayName("When client not exists to list contacts, should throw ClientNotExistsException")
    void whenListContactsClientNotExists_thenThrowException() {
        String uuidInexistente = "uuid-inexistente";
        when(clientRepository.findByuuid(uuidInexistente)).thenReturn(Optional.empty());

        assertThrows(ClientNotExistsException.class, () -> clientService.listContacts(uuidInexistente));
        verify(clientRepository, atLeastOnce()).findByuuid(uuidInexistente);
    }

    @Test
    @DisplayName("When convert client to dto is ok")
    void whenConvertClientToDtoOk() throws Exception {
        Client cliente = Client.builder()
                .id(1L)
                .uuid("uuid-cliente-1")
                .name("Cliente Teste")
                .phone("31 91234-5678")
                .email("teste@email.com")
                .contacts(new ArrayList<>())
                .build();

        Contact contato = Contact.builder()
                .id(1L)
                .uuid("uuid-contato-1")
                .name("Contato Teste")
                .phone("31 90000-0000")
                .client(cliente)
                .build();

        cliente.setContacts(List.of(contato));

        var method = clientService.getClass().getDeclaredMethod("convertClientToDto", Client.class);
        method.setAccessible(true);

        ClientResponseDto dto = (ClientResponseDto) method.invoke(clientService, cliente);

        assertNotNull(dto);
        assertEquals("Cliente Teste", dto.getName());
        assertEquals("teste@email.com", dto.getEmail());
        assertEquals("uuid-cliente-1", dto.getUuid());
        assertNotNull(dto.getContacts());
        assertEquals(1, dto.getContacts().size());
        assertEquals("Contato Teste", dto.getContacts().get(0).getName());
        assertEquals("31 90000-0000", dto.getContacts().get(0).getPhone());
    }

    @Test
    @DisplayName("When convert contact to dto is ok")
    void whenConvertContactToDtoOk() throws Exception {
        Contact contato = Contact.builder()
                .id(1L)
                .uuid("uuid-contato-1")
                .name("Contato Teste")
                .phone("31 90000-0000")
                .build();

        var method = clientService.getClass().getDeclaredMethod("convertContactToDto", Contact.class);
        method.setAccessible(true);

        ContactResponseDto dto = (ContactResponseDto) method.invoke(clientService, contato);

        assertNotNull(dto);
        assertEquals("Contato Teste", dto.getName());
        assertEquals("31 90000-0000", dto.getPhone());
    }
}
