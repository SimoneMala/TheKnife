package theknife;

import java.util.Objects;

/**
 * Classe che rappresenta l'oggetto <code>Recensione</code> di un ristorante.
 * <p>
 *      Contiene i campi principali di una <code>Recensione</code>, quali il nome del ristorante,
 *      il testo della recensione, la valutazione in stelle, la risposta del ristorante
 *      e l'username dell'utente che ha scritto la recensione.
 * * </p>
 * @author Greta Giorgetti 761628 VA
 * @version 1.0
 */
public class Recensione {

    //CAMPI
    /**
     * Nome del ristorante cui la recensione si riferisce.
     */
    private String nomeRistorante;

    /**
     * Testo della recensione.
     */
    private String testo;

    /**
     * Valutazione in stelle della recensione.
     */
    private int stelle;

    /**
     * Risposta alla recensione da parte del ristorante.
     */
    private String rispostaRecensione;

    /**
     * Username dell'utente che ha scritto la recensione.
     */
    private String username;

    /**
     * Costruttore della classe <code>Recensione</code>.
     * <p>
     *      Inizializza i campi dell'oggetto <code>Recensione</code> controllando il testo e le stelle.
     *      In caso di dati non validi, lancia un'eccezione <code>IllegalArgumentException</code>.
     * </p>
     * @param nomeRistorante Nome del ristorante cui la recensione si riferisce.
     * @param testo Testo della recensione.
     * @param stelle Valutazione in stelle della recensione (da 1 a 5) in <code>int</code>.
     * @param rispostaRecensione Risposta alla recensione da parte del ristorante.
     * @param username Username dell'utente che ha scritto la recensione.
     * @throws IllegalArgumentException Se il testo è null o più lungo di 250 caratteri, o se le stelle non sono tra 1 e 5.
     */

    public Recensione(String nomeRistorante, String testo, int stelle, String rispostaRecensione, String username) throws IllegalArgumentException {
        if (testo==null || testo.length()>250 || stelle<1 || stelle>5){
            throw new IllegalArgumentException("Dati recensione non validi");
        }
        this.nomeRistorante=nomeRistorante;
        this.testo=testo;
        this.stelle=stelle;
        this.rispostaRecensione=rispostaRecensione;
        this.username = username;
    }

    //METODI GETTER E SETTER
    /**
     * Restituisce il nome del ristorante cui la recensione si riferisce.
     * @return Il nome del ristorante.
     */
    public String getNomeRistorante(){ return nomeRistorante; }

    /**
     * Imposta il nome del ristorante cui la recensione si riferisce.
     * @param nomeRistorante Il nome del ristorante.
     */
    public void setNomeRistorante(String nomeRistorante) { this.nomeRistorante=nomeRistorante; }

    /**
     * Restituisce il testo della recensione.
     * @return Il testo in <code>String</code> della recensione.
     */
    public String getTesto() {
        return testo;
    }

    /**
     * Imposta il testo della recensione.
     * @param testo Il testo in <code>String</code> della recensione.
     */
    public void setTesto(String testo){
        this.testo=testo;
    }

    /**
     * Restituisce la valutazione in stelle della recensione (<code>int</code> tra 1 e 5).
     * @return La valutazione in stelle della recensione.
     */
    public int getStelle(){
        return stelle;
    }

    /**
     * Imposta la valutazione in stelle della recensione.
     * @param stelle La valutazione in stelle (<code>int</code> tra 1 e 5) della recensione.
     */
    public void setStelle(int stelle){
        this.stelle=stelle;
    }

    /**
     * Restituisce la risposta alla recensione da parte del ristoratore.
     * @return la risposta alla recensione da parte del ristorante.
     */
    public String getRispostaRecensione(){
        return rispostaRecensione;
    }

    /**
     * Imposta la risposta alla recensione da parte del ristorante.
     * @param rispostaRecensione la risposta alla recensione da parte del ristorante.
     */
    public void setRispostaRecensione(String rispostaRecensione){
         this.rispostaRecensione=rispostaRecensione;
    }

    /**
     * Restituisce l'username dell'utente che ha scritto la recensione
     * @return Il nome identificatiuvo dell'utente.
     */
    public String getUsername() { return username; }

    /**
     * Imposta l'username dell'utente che ha scritto la recensione
     * @param username Il nome identificativo dell'utente.
     */
    public void setUsername(String username) { this.username = username; }

    // METODI EQUALS, TOSTRING, HASHCODE
    /**
     * Metodo equals.
     * <p>
     *      Confronta due recensioni in base a username dell'utente che l'ha
     *      scritta e il nome del ristorante, grazie all'Override.
     * </p>
     * @param obj Oggetto da confrontare.
     * @return <code>true</code> se le recensioni sono uguali, <code>false</code> altrimenti.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Recensione rec = (Recensione) obj;

        return username.equals(rec.username) &&
                nomeRistorante.equals(rec.nomeRistorante);
    }

    /**
     * Metodo toString.
     * <p>
     *     Restituisce una stringa con le informazioni principali della recensione.
     * </p>
     * @return <code>String</code> con i dati della recensione.
     */
    @Override
    public String toString(){
        String recensione= "Ristorante: " + nomeRistorante +
                "\nUsername: " + username +
                "\nTesto recensione: " + testo +
                "\nValutazione in stelle: " + stelle;
        if(this.rispostaRecensione!=null) {
            return recensione + "\nRisposta alla recensione: " + rispostaRecensione;
        }
        return recensione;
    }

    /**
     * Metodo hashCode.
     * <p>
     *      Calcola l'hashcode della recensione in base a username e nome del ristorante.
     * </p>
     * @return Hashcode della recensione
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, nomeRistorante);
    }

}
