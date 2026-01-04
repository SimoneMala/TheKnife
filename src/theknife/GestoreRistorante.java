package theknife;

import theknife.eccezioni.ListaVuotaException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
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
            //se il file non esiste o è vuoto creo una lista vuota
            this.elencoRistoranti= new ArrayList<>();
        }
    }

    //metodo ricerca con filtri
    public List<Ristorante> cercaRistoranti(String tipoCucina, String citta, Double prezzoMin, Double prezzoMax,
                                            boolean delivery, boolean prenotazione, Double stelle) throws ListaVuotaException {
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
            String contenutoArrayRistorantti = gson.toJson(this.elencoRistoranti);
            Files.writeString(Path.of(this.percorsoFileMemorizzato), contenutoArrayRistorantti);
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
}
