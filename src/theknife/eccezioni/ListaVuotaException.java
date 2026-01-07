package theknife.eccezioni;

/**
 * Eccezione lanciata quando si tenta di eseguire operazioni usando una lista priva di elementi.
 * @author Aurora Sarno
 */
public class ListaVuotaException extends RuntimeException {
    /**
     * Costruisce l'eccezione con un messaggio di dettaglio.
     * @param message Il messaggio che spiega dove si è verificato il problema.
     */
    public ListaVuotaException(String message) {
        super(message);
    }
}
