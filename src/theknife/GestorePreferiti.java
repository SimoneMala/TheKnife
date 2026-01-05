package theknife;

import com.google.gson.GsonBuilder;
import theknife.eccezioni.ListaVuotaException;
import com.google.gson.Gson;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import com.google.gson.reflect.TypeToken;
import java.util.*;
import java.util.List;

public class GestorePreferiti {

    //campi
    private List<Preferito> ristorantiPreferiti;
    private final String percorsoFileMemorizzato;
    private final Gson gson;
    //costruttore a cui poi aggiungere il metodo carica() che carica dal file json i ristoranti nella lista
    public GestorePreferiti(String nomeFile){
        this.percorsoFileMemorizzato = nomeFile;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        File file= new File(nomeFile);
        if(file.exists() && file.length() > 0) {
            try {
                //leggo contenuto del file
                String contenutoJson = Files.readString(Path.of(nomeFile));
                //metto contenuto del file nella lista
                this.ristorantiPreferiti = gson.fromJson(contenutoJson, new TypeToken<ArrayList<Preferito>>() {
                }.getType());
            } catch (Exception e) {
                System.err.println("Impossibile caricare i dati dal file Preferiti");
            }
        }else{
            //se il file risulta vuoto creo comunque una lista nuova
            this.ristorantiPreferiti= new ArrayList<>();
        }
    }

    //metodo get
    public List<Preferito> getRistorantiPreferiti() {
        return ristorantiPreferiti;
    }

    //metodo che aggiunge un ristorante alla lista di preferiti inerente a un utente
    //nel main controllo che utente e ristorante esistono
    public void aggiungiPreferiti(Utente utente, Ristorante ristorante) throws NullPointerException{
        if (utente==null || ristorante==null){
            throw new NullPointerException("theknife.Utente o ristorante non corretti");
        }
        Preferito p= new Preferito(utente.getUsername(), ristorante.getNome());

        this.ristorantiPreferiti.add(p);

        ModificaFileJsonPreferiti(this.ristorantiPreferiti);
    }

    //metodo per eliminare un ristorante dalla mia lista
    public void cancellaPreferiti(Utente utente, Ristorante ristorante) throws NullPointerException, ListaVuotaException{
        if (utente==null || ristorante==null){
            throw new NullPointerException("theknife.Utente o ristorante non corretti");
        }
        if (this.ristorantiPreferiti.isEmpty()){
            throw new ListaVuotaException("La lista preferiti è già vuota");
        }
        Preferito p= new Preferito(utente.getUsername(), ristorante.getNome());
        for(Preferito p1: this.ristorantiPreferiti){
            if(p1.equals(p)){
                this.ristorantiPreferiti.remove(p);
            }
        }
        ModificaFileJsonPreferiti(this.ristorantiPreferiti);
    }

    //restituisce la lista di preferiti per l'utente, per visualizzare la lista nel main la stampo
    public List<Preferito> visualizzaPreferiti(Utente utente) throws ListaVuotaException{
        if (this.ristorantiPreferiti.isEmpty()){
            throw new ListaVuotaException("Non sono presenti preferiti");
        }
        String nomeUtente= utente.getUsername();
        List<Preferito> tmp= new ArrayList<>();
        for(Preferito p: this.ristorantiPreferiti){
            if(p.getUsername().equals(nomeUtente)){
                tmp.add(p);
            }
        }
        if(tmp.isEmpty()){
            throw new ListaVuotaException("Non sono presenti preferiti per utente: " +  nomeUtente);
        }
        return tmp;
    }

    public void ModificaFileJsonPreferiti(List<Preferito> modifica){
        this.ristorantiPreferiti=modifica;
        try{
            String contenutoArray= gson.toJson(modifica);
            Files.writeString(Path.of(percorsoFileMemorizzato), contenutoArray);
        }catch(Exception e){
            System.err.println("Impossibile modificare il file Preferiti");
        }
    }
}
