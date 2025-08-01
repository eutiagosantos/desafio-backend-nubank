package spring.boot.nubank.exceptions;

public class ClientAlreadyExistsException extends RuntimeException{
    public ClientAlreadyExistsException(){
        super("O cliente já existe");
    }
}
