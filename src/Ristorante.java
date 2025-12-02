public class Ristorante {
    //campi
    private String nome;
    private String nazione;
    private String citta;
    private String indirizzo;
    private double latitudine;
    private double longitudine;
    private double prezzoMedio;
    private boolean delivery;
    private boolean prenotazione;
    private String tipoCucina;

    //costruttore
    public Ristorante(String nome, String nazione, String citta, String indirizzo, double latitudine,
                      double longitudine, double prezzoMedio, boolean delivery, boolean prenotazione, String tipoCucina){
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
    }


    //getter
    public String getNome() {return nome;}
    public String getNazione(){return nazione;}
    public String getCitta(){return citta;}
    public String getIndirizzo(){return indirizzo;}
    public double getLatitudine(){return latitudine;}
    public double getLongitudine(){return longitudine;}
    public double getPrezzoMedio(){return prezzoMedio;}
    public boolean getDelivery(){return delivery;}
    public boolean getPrenotazione(){return prenotazione;}
    public String getTipoCucina(){return tipoCucina;}

    //setter
    public void setNome(String nome){this.nome = nome;}
    public void setNazione(String nazione){this.nazione = nazione;}
    public void setCitta(String citta){this.citta = citta;}
    public void setIndirizzo(String indirizzo){this.indirizzo = indirizzo;}
    public void setLatitudine(double latitudine) {this.latitudine = latitudine;}
    public void setLongitudine(double longitudine){this.longitudine = longitudine;}
    public void setPrezzoMedio(double prezzoMedio){this.prezzoMedio = prezzoMedio;}
    public void setDelivery(boolean delivery) {this.delivery = delivery;}
    public void setPrenotazione(boolean prenotazione) {this.prenotazione = prenotazione;}
    public void setTipoCucina(String tipoCucina){this.tipoCucina = tipoCucina;}

}
