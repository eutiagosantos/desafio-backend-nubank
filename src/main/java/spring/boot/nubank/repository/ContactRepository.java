package spring.boot.nubank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.boot.nubank.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
    
    Optional<Contact> findByPhone(String phone);
}
