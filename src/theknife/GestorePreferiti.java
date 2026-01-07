package theknife;

import com.google.gson.GsonBuilder;
import theknife.eccezioni.IllegalArgumentException;
import theknife.eccezioni.ListaVuotaException;
import com.google.gson.Gson;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import com.google.gson.reflect.TypeToken;
import java.util.*;
import java.util.List;

/**
 * Gestisce la logica legata ai ristoranti preferiti aggiunti da ogni cliente.
 * <p>
 *     Questa classe mantiene la lista dei ristoranti preferiti di ogni utente, si occupa del caricamento e salvataggio
 *     su file JSON dedicato e fornisce metodi di modifica e visualizzazione della lista.
 * </p>
 * @author Aurora Sarno.
 */
public class GestorePreferiti {

    /**
     * Lista completa dei preferiti caricati in memoria.
     * <p>
     *     La lista viene inizializzata dal costruttore e riempita dagli oggetti del file JSON a ogni avvio dell'applicazione.
     * </p>
     */
    private List<Preferito> ristorantiPreferiti;

    /**
     * Percorso per trovare il file JSON dove sono caricati i preferiti in memoria.
     */
    private final String percorsoFileMemorizzato;

    /**
     * Istanza di <code>Gson</code> utilizzata per la lettura e scrittura dei dati JSON.
     * <p>
     *     Viene inizializzata nel costruttore con l'opzione "Pretty Printing"
     *     per garantire che i file vengano salvati con una formattazione leggibile.
     * </p>
     */
    private final Gson gson;

    /**
     * Costruisce e riempie la lista <code>ristorantiPreferiti</code> con i dati del file JSON.
     * <p>
     *     La lista <code>ristorantiPreferiti</code> viene riempita dal costruttore,
     *     ma nel caso il file JSON risulti vuoto o inesistente, viene comunque creata una lista vuota
     *     e stampato il messaggio dell'errore.
     * </p>
     * @param nomeFile Il nome del file JSON dove si trovano i dati preferito.
     */
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

    /**
     * Restituisce la lista <code>ristorantiPreferiti</code> che contiene i preferiti.
     * @return La lista che contiene i preferiti.
     */
    public List<Preferito> getRistorantiPreferiti() {
        return ristorantiPreferiti;
    }

    /**
     * Aggiunge un ristorante preferito alla lista <code>ristorantiPreferiti</code>.
     * <p>
     *     Il metodo controlla se è già presente nella lista <code>ristorantiPreferiti</code>
     *     un preferito con stesso username dell'utente e stesso nome del ristoratore associati,
     *     se non è presente viene aggiunto alla lista.
     * </p>
     * @param utente L'utente che sta aggiungendo il ristorante preferito.
     * @param ristorante Il ristorante preferito dall'utente.
     * @throws NullPointerException Se l'utente o il ristorante risultano <code>null</code>.
     * @throws IllegalArgumentException Se il ristorante fa già parte dei preferiti di questo utente
     */
    public void aggiungiPreferiti(Utente utente, Ristorante ristorante) throws NullPointerException, IllegalArgumentException {
        if (utente==null || ristorante==null){
            throw new NullPointerException("Utente o ristorante non corretti");
        }
        //controllo che non sia già presente
        for(Preferito p: ristorantiPreferiti){
            if(p.getUsername().equals(utente.getUsername()) && p.getNomeRist().equals(ristorante.getNome())){
                throw new IllegalArgumentException("Questo ristorante fa già parte dei tuoi ristoranti preferiti!");
            }
        }
        Preferito p= new Preferito(utente.getUsername(), ristorante.getNome());
        this.ristorantiPreferiti.add(p);
        ModificaFileJsonPreferiti(this.ristorantiPreferiti);
    }

    /**
     * Elimina un ristorante preferito dalla lista <code>ristorantiPreferiti</code>.
     * <p>
     *     Il metodo controlla che il preferito associato allo username di <code>utente</code> e
     *     al nome di <code>ristorante</code> sia presente nella lista <code>ristorantiPreferiti</code>,
     *     se è presente viene tolto alla lista.
     * </p>
     * @param utente L'utente che vuole eliminare un ristorante dai suoi preferiti.
     * @param ristorante Il ristorante preferito, aggiunto precedentemente, da rimuovere.
     * @throws NullPointerException Se l'utente o il ristorante risultano <code>null</code>.
     * @throws ListaVuotaException Se la lista <code>ristorantiPreferiti</code> risulta già vuota.
     */
    public void cancellaPreferiti(Utente utente, Ristorante ristorante) throws NullPointerException, ListaVuotaException{
        if (utente==null || ristorante==null){
            throw new NullPointerException("theknife.Utente o ristorante non corretti");
        }
        if (ristorantiPreferiti.isEmpty()){
            throw new ListaVuotaException("La lista preferiti è già vuota");
        }
        Preferito p= new Preferito(utente.getUsername(), ristorante.getNome());
        for(Preferito p1: ristorantiPreferiti){
            if(p1.equals(p)){
                ristorantiPreferiti.remove(p);
                break;
            }
        }
        ModificaFileJsonPreferiti(ristorantiPreferiti);
    }

    //restituisce la lista di preferiti per l'utente, per visualizzare la lista nel main la stampo

    /**
     * Restituisce una lista di preferiti associati allo username dell'utente passato.
     * <p>
     *     Il metodo crea la lista da restituire, cerca nella lista <code>ristorantiPreferiti</code>
     *     se sono presenti preferiti associati all'utente e li aggiunge alla lista da restituire.
     * </p>
     * @param utente L'utente che vuole visualizzare la sua lista di preferiti.
     * @return La lista di preferiti associati allo username dell'<code>utente</code> passato.
     * @throws ListaVuotaException Se la lista <code>ristorantiPreferiti</code> è vuota o se non sono presenti
     * preferiti associati all'utente nella lista.
     *
     */
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

    /**
     * Aggiorna la lista interna e salva le modifiche sul file JSON.
     * <p>
     *     Questo metodo sovrascrive il contenuto del file con la nuova lista passata come parametro,
     *     serializzandola tramite l'istanza di <code>Gson</code>.
     *     In caso di errore di scrittura, viene stampato un messaggio di errore.
     * </p>
     * @param modifica La nuova lista di preferiti da salvare nel file JSON.
     */
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
