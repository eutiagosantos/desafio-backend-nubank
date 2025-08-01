package spring.boot.nubank.exceptions;

public class ContactAlreadyExistsException extends RuntimeException {
    public ContactAlreadyExistsException(){
        super("O contato com esse telefone ja existe");
    }
}
