package theknife;

public class Ristorante {
    //campi
    private String nome;
    private String nazione;
    private String citta;
    private String indirizzo;
    private double latitudine;
    private double longitudine;
    private int prezzoMedio;
    private Boolean delivery;
    private Boolean prenotazione;
    private String tipoCucina;
    private Double stelle;
    private String proprietario;

    //costruttore
    public Ristorante(String nome, String nazione, String citta, String indirizzo, double latitudine,
                      double longitudine, int prezzoMedio, boolean delivery, boolean prenotazione, String tipoCucina,
                      Double stelle, String proprietario){
        this.nome = nome;
        this.nazione = nazione;
        this.citta = citta;
        this.indirizzo = indirizzo;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.prezzoMedio = prezzoMedio;
        this.delivery = delivery;
        this.prenotazione = prenotazione;
        this.tipoCucina = tipoCucina;
        this.stelle = stelle;
        this.proprietario = proprietario;
    }


    //getter
    public String getNome() {return nome;}
    public String getNazione(){return nazione;}
    public String getCitta(){return citta;}
    public String getIndirizzo(){return indirizzo;}
    public double getLatitudine(){return latitudine;}
    public double getLongitudine(){return longitudine;}
    public int getPrezzoMedio(){return prezzoMedio;}
    public Boolean getDelivery(){return delivery;}
    public Boolean getPrenotazione(){return prenotazione;}
    public String getTipoCucina(){return tipoCucina;}
    public Double getStelle(){return stelle;}
    public String getNomeProprietario(){return proprietario;}

    //setter
    public void setNome(String nome){this.nome = nome;}
    public void setNazione(String nazione){this.nazione = nazione;}
    public void setCitta(String citta){this.citta = citta;}
    public void setIndirizzo(String indirizzo){this.indirizzo = indirizzo;}
    public void setLatitudine(double latitudine) {this.latitudine = latitudine;}
    public void setLongitudine(double longitudine){this.longitudine = longitudine;}
    public void setPrezzoMedio(int prezzoMedio){this.prezzoMedio = prezzoMedio;}
    public void setDelivery(Boolean delivery) {this.delivery = delivery;}
    public void setPrenotazione(Boolean prenotazione) {this.prenotazione = prenotazione;}
    public void setTipoCucina(String tipoCucina){this.tipoCucina = tipoCucina;}
    public void setStelle(Double stelle){this.stelle = stelle;}
    public void setProprietario(String proprietario){this.proprietario = proprietario;}

    public String toString(){
        return "Ristorante: " + nome +
                "\nNazione: " + nazione +
                "\nCittà: " + citta +
                "\nIndirizzo: " + indirizzo +
                "\nLatitudine: " + latitudine +
                "\nLongitudine: " + longitudine +
                "\nPrezzo Medio: " + prezzoMedio +
                "\nDelivery: " + delivery + //mettere si e no se è true o false
                "\nPrenotazione: " + prenotazione +
                "\nTipo di Cucina: " + tipoCucina +
                "\nValutazione in Stelle: " + stelle +
                "\nNome Proprietario: " + proprietario;
    }

}
