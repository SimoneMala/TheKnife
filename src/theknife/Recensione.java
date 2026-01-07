package theknife;

import java.util.Objects;

/**
 * Classe che rappresenta l'oggetto <code>Recensione</code> di un ristorante
 * <p>
 * contiene i campi principali di una recensione
 * @author Greta Giorgetti
 * @version 1.0
 */
public class Recensione {

    //CAMPI
    /**
     * Nome del ristorante cui la recensione si riferisce
     */
    private String nomeRistorante;

    /**
     * Testo della recensione
     */
    private String testo;

    /** Valutazione in stelle della recensione
     */
    private int stelle;

    /*
    * Risposta alla recensione da parte del ristorante
     */
    private String rispostaRecensione;

    /**
     * Username dell'utente che ha scritto la recensione
     */
    private String username;

    /**
     * Costruttore della classe <code>Recensione</code>
     * <p>
     * inizializza i campi della recensione e controlla il testo e le stelle
     * @param nomeRistorante nome del ristorante cui la recensione si riferisce
     * @param testo testo della recensione
     * @param stelle valutazione in stelle della recensione (da 1 a 5)
     * @param rispostaRecensione risposta alla recensione da parte del ristorante
     * @param username username dell'utente che ha scritto la recensione
     * @throws IllegalArgumentException se il testo è null o più lungo di 250 caratteri, o se le stelle non sono tra 1 e 5
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
     * Restituisce il nome del ristorante cui la recensione si riferisce
     * @param nomeRistorante
     * @return nome del ristorante
     */
    public String getNomeRistorante(){ return nomeRistorante; }

    /**
     * Imposta il nome del ristorante cui la recensione si riferisce
     * @param nomeRistorante nome del ristorante
     */
    public void setNomeRistorante(String nomeRistorante) { this.nomeRistorante=nomeRistorante; }

    /**
     * Restituisce il testo della recensione
     * @param testo
     * @return testo della recensione
     */
    public String getTesto() {
        return testo;
    }

    /**
     * Imposta il testo della recensione
     * @param testo
     */
    public void setTesto(String testo){
        this.testo=testo;
    }

    /**
     * Restituisce la valutazione in stelle della recensione
     * @param stelle
     * @return valutazione in stelle della recensione
     */
    public int getStelle(){
        return stelle;
    }

    /**
     * Imposta la valutazione in stelle della recensione
     * @param stelle
     */
    public void setStelle(int stelle){
        this.stelle=stelle;
    }

    /**
     * Restituisce la risposta alla recensione da parte del ristorante
     * @param rispostaRecensione
     * @return risposta alla recensione
     */
    public String getRispostaRecensione(){
        return rispostaRecensione;
    }

    /**
     * Imposta la risposta alla recensione da parte del ristorante
     * @param rispostaRecensione
     */
    public void setRispostaRecensione(String rispostaRecensione){
         this.rispostaRecensione=rispostaRecensione;
    }

    /**
     * Restituisce l'username dell'utente che ha scritto la recensione
     * @param username
     * @return username dell'utente
     */
    public String getUsername() { return username; }

    /**
     * Imposta l'username dell'utente che ha scritto la recensione
     * @param username
     */
    public void setUsername(String username) { this.username = username; }

    // METODI EQUALS, TOSTRING, HASHCODE
    /**
     * Metodo equals
     * <p>
     * confronta due recensioni in base a username e nome del ristorante
     * @param obj
     * @return true se le recensioni sono uguali, false altrimenti
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
     * Metodo toString
     * <p>
     * @return stringa con le informazioni principali della recensione
     */
    @Override
    public String toString(){
        String recensione= "Ristorante:" + nomeRistorante +
                "\nUsername:" + username +
                "\nTesto recensione:" + testo +
                "\nValutazione in stelle:" + stelle;
        if(this.rispostaRecensione!=null) {
            return recensione + "\nRisposta alla recensione:" + rispostaRecensione;
        }
        return recensione;
    }

    /**
     * Metodo hashCode
     * <p>
     * Calcola l'hashcode della recensione in base a username e nome del ristorante
     * @return hashcode della recensione
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, nomeRistorante);
    }

}
