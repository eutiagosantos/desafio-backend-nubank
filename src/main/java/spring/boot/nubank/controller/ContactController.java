package spring.boot.nubank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import spring.boot.nubank.dto.ContactDto;
import spring.boot.nubank.service.impl.ContactServiceImpl;

@RestController
@RequestMapping("/contact")
public class ContactController {
    
    private final ContactServiceImpl contactService;

    public ContactController(ContactServiceImpl contactServiceImpl){
        this.contactService = contactServiceImpl;
    }

    @PostMapping
    public ResponseEntity<ContactDto> save(@RequestBody ContactDto contactDto){
        return ResponseEntity.ok(this.contactService.save(contactDto));
    }
}
