package theknife;

/**
 * Un oggetto della classe <code>Ristorante</code> rappresenta
 * un ristorante con le sue caratteristiche principali.
 * <p>
 * Le caratteristiche principali includono le informazioni anagrafiche, la locazione geografica,
 * i dettagli dei servizi offerti(come delivery e prenotazione), il tipo di cucina, la valutazione in stelle
 * e il nome del proprietario.
 * </p>
 *
 * @author Simone Malacrida 760997 VA
 */
public class Ristorante {
    //campi

    /**
     * Nome del ristorante.
     * Memorizzato nell'attributo <code>nome</code>.
     */
    private String nome;

    /**
     * Nazione in cui si trova il ristorante.
     * Memorizzato nell'attributo <code>nazione</code>.
     */
    private String nazione;

    /**
     * Città in cui si trova il ristorante.
     * Memorizzato nell'attributo <code>citta</code>.
     */
    private String citta;

    /** Indirizzo stradale del ristorante.
     * Memorizzato nell'attributo <code>indirizzo</code>.
     */
    private String indirizzo;

    /** Latitudine geografica del ristorante.
     * Memorizzato nell'attributo <code>latitudine</code>.
     */
    private double latitudine;

    /** Longitudine geografica del ristorante.
     * Memorizzato nell'attributo <code>longitudine</code>.
     */
    private double longitudine;

    /** Prezzo medio di spesa a persona nel ristorante.
     * Memorizzato nell'attributo <code>prezzoMedio</code>.
     */
    private int prezzoMedio;

    /** Indica se il ristorante offre servizio di consegna a domicilio.
     * Memorizzato nell'attributo <code>delivery</code>.
     */
    private Boolean delivery;

    /** Indica se il ristorante accetta prenotazioni online.
     * Memorizzato nell'attributo <code>prenotazione</code>.
     */
    private Boolean prenotazione;

    /** Tipo di cucina offerta dal ristorante(Italiana, cinese, ecc.).
     * Memorizzato nell'attributo <code>tipoCucina</code>.
     */
    private String tipoCucina;

    /** Media della valutazione del ristorante in stelle.
     * Memorizzato nell'attributo <code>stelle</code>.
     */
    private Double stelle;

    /** Nome del proprietario del ristorante.
     * Memorizzato nell'attributo <code>proprietario</code>.
     */
    private String proprietario;

    //costruttore

    /**
     * Costruisce un nuovo oggetto <code>Ristorante</code> inizializzando tutti i suoi campi.
     * @param nome  Il nome del ristorante.
     * @param nazione  La nazione di appartenenza.
     * @param citta  La città di appartenenza.
     * @param indirizzo  L'indirizzo civico.
     * @param latitudine  La latifudine geografica.
     * @param longitudine  La longitudine geografica.
     * @param prezzoMedio  Il prezzo medio di spesa a persona.
     * @param delivery  <code>true</code> se offre consegna a domicilio, altrimenti <code>false</code>.
     * @param prenotazione  <code>true</code> se accetta prenotazioni online, altrimenti <code>false</code>.
     * @param tipoCucina  Il tipo di cucina offerta.
     * @param stelle  La valutazione media del ristorante in stelle.
     * @param proprietario  Il nome del proprietario del ristorante.
     */
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

    /**
     * Restituisce il nome del ristorante.
     * @return Una stringa che rappresenta il nome del ristorante.
     */
    public String getNome() {return nome;}

    /**
     * Restituisce la nazione in cui si trova il ristorante.
     * @return Una stringa che rappresenta la nazione del ristorante.
     */
    public String getNazione(){return nazione;}

    /**
     * Restituisce la città in cui si trova il ristorante.
     * @return Una stringa che rappresenta la città del ristorante.
     */
    public String getCitta(){return citta;}

    /**
     * Restituisce l'indirizzo del ristorante.
     * @return Una stringa che rappresenta l'indirizzo del ristorante.
     */
    public String getIndirizzo(){return indirizzo;}

    /**
     * Restituisce la latitudine del ristorante.
     * @return Un valore double che rappresenta la latitudine del ristorante.
     */
    public double getLatitudine(){return latitudine;}

    /**
     * Restituisce la longitudine del ristorante.
     * @return Un valore double che rappresenta la longitudine del ristorante.
     */
    public double getLongitudine(){return longitudine;}

    /**
     * Restituisce il prezzo medio di spesa a persona nel ristorante.
     * @return Un valore intero che rappresenta il prezzo medio.
     */
    public int getPrezzoMedio(){return prezzoMedio;}

    /**
     * Restituisce se il ristorante offre servizio di consegna a domicilio.
     * @return <code>true</code> se offre consegna a domicilio, altrimenti <code>false</code>.
     */
    public Boolean getDelivery(){return delivery;}

    /**
     * Restituisce se il ristorante accetta prenotazioni online.
     * @return <code>true</code> se accetta prenotazioni online, altrimenti <code>false</code>.
     */
    public Boolean getPrenotazione(){return prenotazione;}

    /**
     * Restituisce il tipo di cucina offerta dal ristorante.
     * @return Una stringa che rappresenta il tipo di cucina.
     */
    public String getTipoCucina(){return tipoCucina;}

    /**
     * Restituisce la valutazione media del ristorante in stelle.
     * @return Un valore double che rappresenta la valutazione in stelle.
     */
    public Double getStelle(){return stelle;}

    /**
     * Restituisce il nome del proprietario del ristorante.
     * @return Una stringa che rappresenta il nome del proprietario.
     */
    public String getNomeProprietario(){return proprietario;}

    //setter

    /**
     * Imposta il nome del ristorante.
     * @param nome  Il nuovo nome del ristorante.
     */
    public void setNome(String nome){this.nome = nome;}

    /**
     * Imposta la nazione del ristorante.
     * @param nazione  La nuova nazione del ristorante.
     */
    public void setNazione(String nazione){this.nazione = nazione;}

    /**
     * Imposta la città del ristorante.
     * @param citta  La nuova città del ristorante.
     */
    public void setCitta(String citta){this.citta = citta;}

    /**
     * Imposta l'indirizzo del ristorante.
     * @param indirizzo  Il nuovo indirizzo del ristorante.
     */
    public void setIndirizzo(String indirizzo){this.indirizzo = indirizzo;}

    /**
     * Imposta la latitudine del ristorante.
     * @param latitudine  La nuova latitudine del ristorante.
     */
    public void setLatitudine(double latitudine) {this.latitudine = latitudine;}

    /**
     * Imposta la longitudine del ristorante.
     * @param longitudine  La nuova longitudine del ristorante.
     */
    public void setLongitudine(double longitudine){this.longitudine = longitudine;}

    /**
     * Imposta il prezzo medio di spesa a persona nel ristorante.
     * @param prezzoMedio  Il nuovo prezzo medio.
     */
    public void setPrezzoMedio(int prezzoMedio){this.prezzoMedio = prezzoMedio;}

    /**
     * Imposta se il ristorante offre servizio di consegna a domicilio.
     * @param delivery  <code>true</code> se offre consegna a domicilio, altrimenti <code>false</code>.
     */
    public void setDelivery(Boolean delivery) {this.delivery = delivery;}

    /**
     * Imposta se il ristorante accetta prenotazioni online.
     * @param prenotazione  <code>true</code> se accetta prenotazioni online, altrimenti <code>false</code>.
     */
    public void setPrenotazione(Boolean prenotazione) {this.prenotazione = prenotazione;}

    /**
     * Imposta il tipo di cucina offerta dal ristorante.
     * @param tipoCucina  Il nuovo tipo di cucina.
     */
    public void setTipoCucina(String tipoCucina){this.tipoCucina = tipoCucina;}

    /**
     * Imposta la valutazione media del ristorante in stelle.
     * @param stelle  La nuova valutazione in stelle.
     */
    public void setStelle(Double stelle){this.stelle = stelle;}

    /**
     * Imposta il nome del proprietario del ristorante.
     * @param proprietario  Il nuovo nome del proprietario.
     */
    public void setProprietario(String proprietario){this.proprietario = proprietario;}

    /**
     * Restituisce una rappresentazione testuale dell'oggetto Ristorante.
     * Vengono formattati in modo leggibile i booleani e il prezzo medio.
     *
     * @return Una stringa con i dettagli del ristorante.
     */
    public String toString(){
        return "Ristorante: " + nome +
                "\nNazione: " + nazione +
                "\nCittà: " + citta +
                "\nIndirizzo: " + indirizzo +
                "\nPrezzo Medio: " + prezzoMedioZero(prezzoMedio) +
                "\nDelivery: " + formattoSieNo(delivery) +
                "\nPrenotazione: " + formattoSieNo(prenotazione) +
                "\nTipo di Cucina: " + tipoCucina +
                "\nValutazione in Stelle: " + stelle;
    }

    /**
     * Formatta un valore booleano in una stringa "Sì" o "No".
     *
     * @param valore Il booleano da convertire.
     * @return "Non specificato" se null, "Sì" se true, "No" se false.
     */
    public String formattoSieNo(Boolean valore){
        if(valore == null){
            return "Non spcecificato";}
        // Se è true restituisce "sì", se è false restituisce "no"
            return valore ? "Sì" : "No";
        }

    /**
     * Gestisce la formattazione del prezzo medio.
     *
     * @param prezzoMedio Il valore intero del prezzo.
     * @return Una stringa contente il prezzo medio o "Non specificato" se è 0.
     */
    public String prezzoMedioZero(int prezzoMedio){
        if(prezzoMedio == 0){
            return "Non specificato";}
        return Integer.toString(prezzoMedio);
    }
}

