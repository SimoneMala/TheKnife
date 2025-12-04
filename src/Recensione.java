public class Recensione {

    //campi
    private String nomeRistorante;
    private String testo;
    private int stelle;
    private String rispostaRecensione;

    //costruttore
    public Recensione(String nomeRistorante, String testo, int stelle, String rispostaRecensione){
        this.nomeRistorante=nomeRistorante;
        this.testo=testo;
        this.stelle=stelle;
        this.rispostaRecensione=rispostaRecensione;
    }

    //metodi getter e setter
    public String getNomeRistorante(){ return nomeRistorante; }

    public setNomeRistorante(String nomeRistorante) { return this.nomeRistorante=nomeRistorante; }

    public String getTesto() {
        return testo;
    }

    public setTesto(String testo){
        return this.testo=testo;
    }

    public int getStelle(){
        return stelle;
    }

    public setStelle(int stelle){
        return this.stelle=stelle;
    }

    public String getRispostaRecensione(){
        return rispostaRecensione;
    }
    public setRispostaRecensione(String rispostaRecensione){
        return this.rispostaRecensione=rispostaRecensione;
    }

    @Override
    public String toString(){
        return "Ristorante:" + nomeRistorante +
                "\nTesto recensione:" + testo +
                "\nValutazione in stelle:" + stelle +
                "\nRisposta alla recensione:" + rispostaRecensione;
    }

}
