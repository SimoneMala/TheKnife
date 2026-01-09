package theknife;

import theknife.eccezioni.ListaVuotaException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * La classe <code>GestoreRistorante</code> si occupa della gestione dei dati relativi ai ristoranti.
 * <p>
 *     Questa classe gestisce la conversione da CSV a JSON, il caricamento(da JSON), il salvataggio, la ricerca
 *     e il filraggio dei ristoranti.
 *     Utilizza la librearia Gson per la serializzazione e deserializzazione dei dati in formato JSON.
 * </p>
 *
 * @author Simone Malacrida
 */
public class GestoreRistorante {
    //campi

    /** La lista contenente tutti gli oggetti Ristorante caricati in memoria.
     * Memorizzata nell'attributo <code>elencoRistoranti</code>.
     */
    private List<Ristorante> elencoRistoranti;

    /** Il percorso del file JSON utilizzato per la persistenza dei dati.
     * Memorizzato nell'attributo <code>percorsoFileMemorizzato</code>.
     */
    private final String percorsoFileMemorizzato;//salvo il percorso del file json

    /** L'istanza di Gson utilizzata per le operazioni di serializzazione e deserializzazione su file JSON.
     * Memorizzata nell'attributo <code>gson</code>.
     */
    private final Gson gson;

    //costruttore

    /**
     * Costruisce un oggetto <code>GestoreRistorante</code> e inizializza i dati.
     * <p>
     *     Se il file JSON specificato esiste e contiene dati, carica i ristoranti da quel file.
     *     Altrimenti, i dati vengono importati da un file CSV predefinito("Dati/RistorantiTheKnife.csv")
     *     e salvati nel nuovo file JSON.
     * </p>
     * @param nomeFileJson Il percorso del file JSON da utilizzare per il salvataggio e il caricamento dei dati.
     */
    public GestoreRistorante(String nomeFileJson){ //passo il percorso del file json
        this.percorsoFileMemorizzato = nomeFileJson;
        this.gson= new GsonBuilder().setPrettyPrinting().create();
        File file= new File(nomeFileJson);
        if (file.exists() && file.length()>0) {
            try{
                //leggo contenuto del file in una stringa
                String contenutoJson = Files.readString(Path.of(nomeFileJson));
                //prendo la stringa e la converto in array di theknife.Ristorante
                this.elencoRistoranti = gson.fromJson(contenutoJson, new TypeToken<ArrayList<Ristorante>>(){}.getType());
            } catch(Exception e) {
                System.err.println("Impossibile caricare dal file Ristoranti");
            }
        }else{
            this.elencoRistoranti = new ArrayList<>();
            String csvFile = "data" + File.separator + "RistorantiTheKnife.csv";

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                String riga;
                br.readLine(); // Salta l'intestazione

                while ((riga = br.readLine()) != null) {
                    // --- MODIFICA FONDAMENTALE ---
                    // Questa Regex significa: "Splitta per virgola, ma solo se segue un numero pari di virgolette"
                    // In pratica: ignora le virgole dentro i testi "..."
                    String[] v = riga.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                    if (v.length < 12) continue;

                    // Usiamo il metodo 'clean' per togliere le virgolette e gli spazi extra
                    String name = clean(v[0]);
                    String nazione = clean(v[1]);
                    String citta = clean(v[2]);
                    String indirizzo = clean(v[3]);

                    // Parsing sicuro dei numeri
                    double latitudine = parseDoubleSafe(clean(v[4]));
                    double longitudine = parseDoubleSafe(clean(v[5]));

                    int prezzoMedio = Integer.parseInt(clean(v[6]));
                    Boolean delivery= Boolean.parseBoolean(clean(v[7]));
                    Boolean prenotazione= Boolean.parseBoolean(clean(v[8]));
                    String tipoCucina = clean(v[9]);
                    Double stelle = parseDoubleSafe(clean(v[10]));
                    String nomeProprietario = clean(v[11]);

                    Ristorante r = new Ristorante(name, nazione, citta, indirizzo,
                            latitudine, longitudine, prezzoMedio,
                            delivery, prenotazione, tipoCucina,
                            stelle, nomeProprietario);

                    elencoRistoranti.add(r);
                }

            } catch (Exception e) {
                System.out.println("Errore nel csv: " + e.getMessage());
            }
            modificaFileJsonRistoranti(elencoRistoranti);
        }
    }

    //metodo ricerca con filtri

    /**
     * Cerca ristoranti che soddisfino una serie di criteri filtro.
     * I parametri nulli vengono ignorati durante il filtraggio.
     * @param tipoCucina Il tipo dii cucina desiderato.(Può essere null)
     * @param citta La città in cui cercare(Obbliagatorio per la logica attuale)
     * @param prezzoMin Il prezzo minimo desiderato.(Può essere null)
     * @param prezzoMax Il prezzo massimo desiderato.(Può essere null)
     * @param delivery Se <code>true</code>, filtra solo ristoranti con delivery; altrimenti ignora questo filtro.
     * @param prenotazione Se <code>true</code>, filtra solo ristoranti che accettano prenotazioni online; altrimenti ignora questo filtro.
     * @param stelle La valutazione minima in stelle desiderata.(Può essere null)
     * @return Una lista di oggetti <code>Ristorante</code> che soddisfano i criteri di ricerca.
     * @throws ListaVuotaException Se nessun ristorante soddisfa i criteri di ricerca.
     */
    public List<Ristorante> cercaRistoranti(String tipoCucina, String citta, Double prezzoMin, Double prezzoMax,
                                            Boolean delivery, Boolean prenotazione, Double stelle) throws ListaVuotaException {
        List<Ristorante> risultati = new ArrayList<>();

        for(Ristorante r: elencoRistoranti){
            boolean vaBene = true;

            if (tipoCucina != null && !r.getTipoCucina().equalsIgnoreCase(tipoCucina)) {vaBene = false;}
            if(!r.getCitta().equalsIgnoreCase(citta)){vaBene = false;}
            if (prezzoMin != null && r.getPrezzoMedio() < prezzoMin){vaBene = false;}
            if(prezzoMax != null && r.getPrezzoMedio() > prezzoMax){vaBene = false;}
            if(delivery != null){
                if (delivery && !r.getDelivery()){vaBene = false;}}
            if(prenotazione != null){
                if(prenotazione && !r.getPrenotazione()){vaBene = false;}}
            if(stelle != null && r.getStelle() < stelle){vaBene = false;}

            if(vaBene){risultati.add(r);}
        }
        if(risultati.isEmpty()){
            throw new ListaVuotaException("Nessun ristorante trovato");
        } else
            return risultati;
    }

    //cerco ristorante e visualizzo dettagli

    /**
     * Trovo un ristorante specifico tramite nome e città.
     * @param nome Il nome del ristorante da cercare.
     * @param citta La città in cui si trova il ristorante.
     * @return L'oggetto <code>Ristorante</code> corrispondente ai parametri di ricerca.
     * @throws ListaVuotaException Se il ristorante non viene trovato.
     */
    public Ristorante visualizzaRistorante(String nome, String citta)throws ListaVuotaException {
        for(Ristorante r: elencoRistoranti){
            if(r.getNome().equals(nome) && r.getCitta().equals(citta)){return r;}
        }
        throw new ListaVuotaException("theknife.Ristorante non trovato");
    }

    //Aggiungi ristorante al file json

    /**
     * Aggiunge un nuovo ristorante alla lista e aggiorna il file JSON.
     * @param ristorante L'oggetto <code>Ristorante</code> da aggiungere.
     */
    public void aggiungiRistorante(Ristorante ristorante){
        this.elencoRistoranti.add(ristorante); //aggiorno la lista in memroia
        //aggiorno il file json
        modificaFileJsonRistoranti(elencoRistoranti);
    }

    //Modifica diretta del FileJson

    /**
     * Sovrascrive l'intero file JSON con la lista di ristoranti fornita.
     * @param modifica La nuova lista di oggetti <code>Ristorante</code> da salvare.
     */
    public void modificaFileJsonRistoranti(List<Ristorante> modifica) {
        this.elencoRistoranti = modifica;
        try {
            String contenutoArrayRistoranti = gson.toJson(this.elencoRistoranti);
            Files.writeString(Path.of(this.percorsoFileMemorizzato), contenutoArrayRistoranti);
        } catch(Exception e) {
            System.err.println("Impossibile caricare dal file Ristoranti");
        }
    }

    /**
     * Restituisce una lista di ristoranti di proprietà di uno specifico utente.
     * @param usernameProprietario Lo username del proprietario dei ristoranti da cercare.
     * @return Una lista di oggetti <code>Ristorante</code> appartenenti al proprietario specificato.
     */
    public List<Ristorante> getRistoranteDi(String usernameProprietario){
        List<Ristorante> risultati = new ArrayList<>();
        for(Ristorante r: elencoRistoranti){
            if(r.getNomeProprietario().equals(usernameProprietario)){
                risultati.add(r);
            }
        }
        return risultati;
    }

    /**
     * Restituisce l'elenco completo dei ristoranti.
     * @return La lista completa di oggetti <code>Ristorante</code>.
     */
    public List<Ristorante> getElencoRistoranti() {
        return elencoRistoranti;
    }

    //Tutti i tipi di cucina presenti(senza duplicati)

    /**
     * Restituisce una lista di tutte le tipologie di cucina presenti nel sistema, senza duplicati.
     * @return Una lista di stringhe rappresentanti i tipi di cucina unici.
     */
    public List<String> getTipiCucinaLista(){
        List<String> tipiCucina = new ArrayList<>();
        for(Ristorante r: elencoRistoranti){
            if(!tipiCucina.contains(r.getTipoCucina())){
            tipiCucina.add(r.getTipoCucina());}
        }
        return tipiCucina;
    }

    // Metodo helper per pulire le stringhe: toglie spazi e virgolette agli estremi
    // Es: "  \"Vienna\" " diventa "Vienna"

    /**
     * Pulisce una stringa rimuovendo spazi vuoti e virgolette agli estremi.
     * Rimuove gli spazi iniziali e finali, e se la stringa inizia e finisce con virgolette,
     * le rimuove.
     * @param input La stringa da pulire.
     * @return La stringa pulita.
     */
    private static String clean(String input) {
        if (input == null) return "";
        input = input.trim();
        // Se inizia e finisce con virgolette, le togliamo
        if (input.startsWith("\"") && input.endsWith("\"")) {
            return input.substring(1, input.length() - 1);
        }
        return input;
    }

    // Metodo helper per convertire i numeri senza crashare se sono vuoti

    /**
     * Converte una stringa in un valore double in modo sicuro.
     * Se la stringa è nulla o vuota, restituisce 0.0.
     * Se la conversione fallisce, stampa un messaggio di avviso e restituisce 0.0.
     * @param input La stringa da convertire in double.
     * @return Il valore double risultante, oppure 0.0 se la stringa è nulla, vuota o non valida.
     */
    private static double parseDoubleSafe(String input) {
        if (input == null || input.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("Attenzione: valore numerico non valido trovato: " + input);
            return 0.0; // Valore di default in caso di errore
        }
    }
}
