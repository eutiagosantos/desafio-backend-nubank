package spring.boot.nubank.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import spring.boot.nubank.dto.ClientDto;
import spring.boot.nubank.dto.ClientResponseDto;
import spring.boot.nubank.dto.ContactResponseDto;
import spring.boot.nubank.service.impl.ClientServiceImpl;

@RestController
@RequestMapping("/client")
public class ClientController {
 
    private final ClientServiceImpl clientService;

    public ClientController(ClientServiceImpl clienteServiceImpl){
        this.clientService = clienteServiceImpl;
    }

    @PostMapping
    public ResponseEntity<ClientDto> save(@RequestBody ClientDto clientDto){
        return ResponseEntity.ok(this.clientService.save(clientDto));
    }

    @GetMapping
    public ResponseEntity<List<ClientResponseDto>> listAll(){
        return ResponseEntity.ok(this.clientService.listAll());
    }

    @GetMapping("/{id}/contacts")
    public ResponseEntity<List<ContactResponseDto>> listContacts(@PathVariable String id){
        return ResponseEntity.ok(this.clientService.listContacts(id));
    }
}
