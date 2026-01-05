package theknife;

public class Utente {

    public enum Ruolo{
        CLIENTE, RISTORATORE;
    }

    //campi
    private String nome;
    private String cognome;
    private String username;
    private String password;
    private String via;
    private String citta;
    private Ruolo ruolo;

    //costruttore
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
    public String getNome() {
        return nome;
    }
    public String getCognome() {
        return cognome;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getVia() {
        return via;
    }
    public String getCitta() {
        return citta;
    }
    public Ruolo getRuolo() {
        return ruolo;
    }

    //metodi setter
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setVia(String via) {
        this.via = via;
    }
    public void setCitta(String citta) {this.citta = citta;}
    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }

    public String toString() {
        return "Utente: "+ username + " " + password;
    }

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(o==null || getClass() != o.getClass()) return false;
        Utente that = (Utente) o;
        return that.getUsername().equals(this.getUsername());
    }

}
