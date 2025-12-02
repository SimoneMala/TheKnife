public class Recensione {

    //campi
    private String testo;
    private int stelle;
    private String rispostaRecensione;

    //costruttore
    public Recensione(String testo, int stelle, String rispostaRecensione){
        this.testo=testo;
        this.stelle=stelle;
        this.rispostaRecensione=rispostaRecensione;
    }

    //metodi getter e setter
    public String getTesto() {
        return testo;
    }

    public String setTesto(String testo){
        return this.testo=testo;
    }

    public int getStelle(){
        return stelle;
    }

    public int setStelle(int stelle){
        return this.stelle=stelle;
    }

    public String getRispostaRecensione(){
        return rispostaRecensione;
    }
    public String setRispostaRecensione(String rispostaRecensione){
        return this.rispostaRecensione=rispostaRecensione;
    }

}
