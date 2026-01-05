package theknife;

import java.util.Objects;

public class Recensione {

    //campi
    private String nomeRistorante;
    private String testo;
    private int stelle;
    private String rispostaRecensione;
    private String username;

    //costruttore
    public Recensione(String nomeRistorante, String testo, int stelle, String rispostaRecensione, String username){
        if (testo==null || testo.length()>250 || stelle<1 || stelle>5){
            throw new IllegalArgumentException("Dati recensione non validi");
        }
        this.nomeRistorante=nomeRistorante;
        this.testo=testo;
        this.stelle=stelle;
        this.rispostaRecensione=rispostaRecensione;
        this.username = username;
    }

    //metodi getter e setter
    //metodi setter devono restituire void, non c'è return, vedi setRispostaRecensione
    public String getNomeRistorante(){ return nomeRistorante; }

    public void setNomeRistorante(String nomeRistorante) { this.nomeRistorante=nomeRistorante; }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo){
        this.testo=testo;
    }

    public int getStelle(){
        return stelle;
    }

    public void setStelle(int stelle){
        this.stelle=stelle;
    }

    public String getRispostaRecensione(){
        return rispostaRecensione;
    }
    public void setRispostaRecensione(String rispostaRecensione){
         this.rispostaRecensione=rispostaRecensione;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }


    //metodo equals per confrontare due recensioni
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Recensione rec = (Recensione) obj;

        return username.equals(rec.username) &&
                nomeRistorante.equals(rec.nomeRistorante);
    }

    //metodo toString
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

    //metodo hashcode
    @Override
    public int hashCode() {
        return Objects.hash(username, nomeRistorante);
    }

}
