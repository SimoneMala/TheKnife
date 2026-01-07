package theknife.eccezioni;

/**
 * Eccezione lanciata quando si tenta di accedere a un utente non presente in memoria.
 * @author Aurora Sarno
 */
public class UtenteInesistente extends RuntimeException {
    /**
     * Costruisce l'eccezione con un messaggio di dettaglio.
     * @param message Il messaggio che spiega dove si è verificato il problema.
     */
    public UtenteInesistente(String message) {
        super(message);
    }
}
