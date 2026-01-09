package theknife;

/**
 * Rappresenta l'oggetto utente registrato a TheKnife.
 * <p>
 *     Questa classe viene utilizzata per creare gli utenti registrati da salvare.
 * </p>
 * @author Aurora Sarno 763021 VA
 */
public class Utente {

    /**
     * Rappresenta i possibili ruoli che un <code>Utente</code> può assumere.
     * <p>
     *     Definisce i diversi livelli di accesso e le funzionalità per utenti (Cliente o Ristoratore).
     * </p>
     * @author Aurora Sarno 763021 VA
     */
    public enum Ruolo{
        /**
         * Identifica l'utente cliente che cerca i ristoranti, li recensisce e li aggiunge ai preferiti.
         */
        CLIENTE,
        /**
         * Identifica l'utente ristoratore che aggiunge i propri ristoranti e li gestisce.
         */
        RISTORATORE;
    }

    //campi
    /**
     * Il nome dell'utente registrato.
     */
    private String nome;

    /**
     * Il cognome dell'utente registrato.
     */
    private String cognome;

    /**
     * Lo username dell'utente registrato.
     */
    private String username;

    /**
     * La password dell'utente registrato.
     */
    private String password;

    /**
     * La via del domicilio dell'utente registrato.
     */
    private String via;

    /**
     * La città del domicilio dell'utente registrato.
     */
    private String citta;

    /**
     * Il ruolo assegnato all'utente registrato.
     */
    private Ruolo ruolo;

    //costruttore

    /**
     * Costruisce l' utente registrato.
     * @param nome Il nome dell'utente.
     * @param cognome Il cognome dell'utente.
     * @param username Lo username dell'utente.
     * @param password La password dell'utente.
     * @param via La via del domicilio dell'utente.
     * @param citta La città del domicilio dell'utente.
     * @param ruolo Il ruolo dell'utente.
     */
    public Utente(String nome, String cognome, String username, String password, String via, String citta, Ruolo ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.via = via;
        this.citta = citta;
        this.ruolo = ruolo;
    }

    //metodi getter

    /**
     * Restituisce il nome dell'utente.
     * @return Il nome associato all'utente.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     * @return Il cognome associato all'utente.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Restituisce lo username dell'utente.
     * @return Lo username associato all'utente.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Restituisce la password dell'utente.
     * @return la password associata all'utente.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Restituisce la via del domicilio dell'utente.
     * @return La via associata all'utente.
     */
    public String getVia() {
        return via;
    }

    /**
     * Restituisce la città dell'utente.
     * @return La città associata all'utente.
     */
    public String getCitta() {
        return citta;
    }

    /**
     * Restituisce il ruolo dell'utente.
     * @return Il ruolo associato all'utente.
     */
    public Ruolo getRuolo() {
        return ruolo;
    }

    //metodi setter

    /**
     * Cambia il nome associato all'utente.
     * @param nome Il nuovo valore da associare a <code>nome</code>.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Cambia il cognome associato all'utente.
     * @param cognome Il nuovo valore da associare a <code>cognome</code>.
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Cambia lo username associato all'utente.
     * @param username Il nuovo valore da associare a <code>username</code>.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Cambia la password associata all'utente.
     * @param password Il nuovo valore da associare a <code>password</code>.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Cambia la via associata all'utente.
     * @param via Il nuovo valore da associare a <code>via</code>.
     */
    public void setVia(String via) {
        this.via = via;
    }

    /**
     * Cambia la città associata all'utente.
     * @param citta Il nuovo valore da associare a <code>citta</code>.
     */
    public void setCitta(String citta) {this.citta = citta;}
    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    /**
     * Crea una stringa che descrive l'oggetto chiamante.
     * @return La stringa che contiene username e password dell'utente.
     */
    public String toString() {
        return "Utente: "+ username + " " + password;
    }

    /**
     * Verifica l'uguaglianza tra l'oggetto passato e l'oggetto chiamante.
     * <p>
     *     Due oggetti sono considerati uguali se: hanno lo stesso indicaore;
     *     hanno lo stesso <code>username</code>.
     * </p>
     * @param o   L'oggetto da confrontare con l'oggetto chiamante.
     * @return <code>true</code> se gli oggetti sono uguali, <code>false</code> altrimenti.
     */
    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(o==null || getClass() != o.getClass()) return false;
        Utente that = (Utente) o;
        return that.getUsername().equals(this.getUsername());
    }

}
