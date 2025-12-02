public class Utente {

    public enum Ruolo{
        CLIENTE, RISTORATORE;
    }

    //campi
    private String nome;
    private String cognome;
    private String password;
    private String domicilio;
    private Ruolo ruolo;

    //costruttore
    public Utente(String nome, String cognome, String password, String domicilio, Ruolo ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.password = password;
        this.domicilio = domicilio;
        this.ruolo = ruolo;
    }

    //metodi getter
    public String getNome() {
        return nome;
    }
    public String getCognome() {
        return cognome;
    }
    public String getPassword() {
        return password;
    }
    public String getDomicilio() {
        return domicilio;
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
    public void setPassword(String password) {
        this.password = password;
    }
    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }
    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }


}
