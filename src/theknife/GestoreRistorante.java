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

public class GestoreRistorante {
    //campi
    private List<Ristorante> elencoRistoranti;
    private final String percorsoFileMemorizzato; //salvo il percorso del file json
    private final Gson gson;

    //costruttore
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
            String csvFile = "Dati" + File.separator + "RistorantiTheKnife.csv";

            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                String riga;
                br.readLine(); // Salta l'intestazione

                while ((riga = br.readLine()) != null) {
                    // --- MODIFICA FONDAMENTALE ---
                    // Questa Regex significa: "Splitta per virgola, ma solo se segue un numero pari di virgolette"
                    // In pratica: ignora le virgole dentro i testi "..."
                    String[] v = riga.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                    if (v.length < 12) continue;

                    // Usiamo un metodo 'clean' per togliere le virgolette e gli spazi extra
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
    public List<Ristorante> cercaRistoranti(String tipoCucina, String citta, Double prezzoMin, Double prezzoMax,
                                            Boolean delivery, Boolean prenotazione, Double stelle) throws ListaVuotaException {
        List<Ristorante> risultati = new ArrayList<>();

        for(Ristorante r: elencoRistoranti){
            boolean vaBene = true;

            if (tipoCucina != null && !r.getTipoCucina().equals(tipoCucina)) {vaBene = false;}
            if(!r.getCitta().equals(citta)){vaBene = false;}
            if (prezzoMin != null && r.getPrezzoMedio() < prezzoMin) {vaBene = false;}
            if(prezzoMax != null && r.getPrezzoMedio() > prezzoMax){vaBene = false;}
            if(delivery && !r.getDelivery()){vaBene = false;}
            if(prenotazione && !r.getPrenotazione()){vaBene = false;}
            //Ricordarsi che se passa null di farlo diventare 0 nel maim
            if(stelle != null && r.getStelle() < stelle){vaBene = false;}

            if(vaBene){risultati.add(r);}
        }
        if(risultati.isEmpty()){
            throw new ListaVuotaException("Nessun ristorante trovato");
        } else
            return risultati;
    }

    //cerco ristorante e visualizzo dettagli
    public Ristorante visualizzaRistorante(String nome, String citta)throws NullPointerException{
        for(Ristorante r: elencoRistoranti){
            if(r.getNome().equals(nome) && r.getCitta().equals(citta)){return r;}
        }
        throw new ListaVuotaException("theknife.Ristorante non trovato");
    }

    //Aggiungi ristorante al file json
    public void aggiungiRistorante(Ristorante ristorante){
        this.elencoRistoranti.add(ristorante); //aggiorno la lista in memroia
        //aggiorno il file json
        modificaFileJsonRistoranti(elencoRistoranti);
    }

    //Modifica diretta del FileJson
    public void modificaFileJsonRistoranti(List<Ristorante> modifica) {
        this.elencoRistoranti = modifica;
        try {
            String contenutoArrayRistoranti = gson.toJson(this.elencoRistoranti);
            Files.writeString(Path.of(this.percorsoFileMemorizzato), contenutoArrayRistoranti);
        } catch(Exception e) {
            System.err.println("Impossibile caricare dal file Ristoranti");
        }
    }

    public List<Ristorante> getRistoranteDi(String nomeProprietario){
        List<Ristorante> risultati = new ArrayList<>();
        for(Ristorante r: elencoRistoranti){
            if(r.getNomeProprietario().equals(nomeProprietario)){
                risultati.add(r);
            }
        }
        return risultati;
    }

    public List<Ristorante> getElencoRistoranti() {
        return elencoRistoranti;
    }

    //Tutti i tipi di cucina presenti(senza duplicati)
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
