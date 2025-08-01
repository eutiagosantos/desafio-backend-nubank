package spring.boot.nubank.exceptions;

public class ClientNotExistsException extends RuntimeException {
    public ClientNotExistsException(){
        super("O cliente nao existe");
    }
}
