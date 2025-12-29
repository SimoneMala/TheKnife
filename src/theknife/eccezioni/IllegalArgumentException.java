package theknife.eccezioni;

public class IllegalArgumentException extends RuntimeException {
    public IllegalArgumentException(String message) {
        super(message);
    }
}  //eccezione da lanciare in caso di argomenti non validi
