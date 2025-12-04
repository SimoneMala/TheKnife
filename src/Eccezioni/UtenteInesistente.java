package Eccezioni;

public class UtenteInesistente extends RuntimeException {
    public UtenteInesistente(String message) {
        super(message);
    }
}
