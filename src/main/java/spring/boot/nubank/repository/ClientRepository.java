package spring.boot.nubank.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import spring.boot.nubank.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    
    Optional<Client> findByEmail(String email);
    Optional<Client> findByuuid(String uuid);
}
