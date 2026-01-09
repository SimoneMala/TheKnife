package theknife;

import com.google.gson.GsonBuilder;
import org.mindrot.jbcrypt.BCrypt;
import theknife.eccezioni.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Gestisce la logica legata agli utenti registrati.
 * <p>
 *     Questa classe mantiene la lista degli utenti registrati, si occupa del caricamento e salvataggio
 *     su file JSON dedicato e fornisce metodo per autenticazione e registrazione all'applicazione.
 * </p>
 * @author Aurora Sarno 763021 VA
 */
public class GestoreUtenti {
     /**
     * Lista completa degli utenti caricati in memoria.
     */
    private List<Utente> listaUtenti;

    /**
     * Percorso per trovare il file dove sono caricati gli utenti registrati.
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
     * Costruisce e riempie la <code>listaUtenti</code> con i dati del file JSON.
     * <p>
     *     La lista <code>listaUtenti</code> viene riempita dal costruttore,
     *     ma nel caso il file JSON risulti vuoto o inesistente, viene comunque creata una lista vuota
     *     e stampato il messaggio dell'errore.
     * </p>
     * @param nomeFileJson Il nome del file JSON dove si trovano i dati utenti.
     */
    public GestoreUtenti(String nomeFileJson) {
        this.percorsoFileMemorizzato=nomeFileJson;
        this.gson= new GsonBuilder().setPrettyPrinting().create();
        File file= new File(nomeFileJson);
        if (file.exists() && file.length()>0) {
            try {
                //leggo contenuto del file e lo inserisco tutto in una string
                String contenutoJson = Files.readString(Path.of(nomeFileJson));
                //inserisco il contenuto nella mia lista
                this.listaUtenti = gson.fromJson(contenutoJson, new TypeToken<ArrayList<Utente>>() {
                }.getType());
            } catch (Exception e) {
                System.err.println("Impossibile caricare dal file utenti");
            }
        }else{
            //se il mio file risulta vuoto creo comunque una lista vuota
            this.listaUtenti= new ArrayList<>();
        }


    }

    /**
     * Restituisce la lista che contiene gli utenti.
     * @return La lista che contiene gli utenti.
     */
    public List<Utente> getListaUtenti() {
        return listaUtenti;
    }

    /**
     * Controlla le credenziali dell'utente per permettere l'accesso all'applicazione.
     * <p>
     *     Il metodo cerca nella <code>listaUtenti</code> è presente l'utente che sta tentando il login.
     *     La verifica della password viene effettuata tramite il metodo <code>checkpw</code> della classe <code>BCrypt</code>
     *     che confronta la password inserita con l'hash salvato.
     * </p>
     * @param username Lo username univoco dell'utente.
     * @param password La password in chiaro da verificare.
     * @return L'oggetto <code>Utente</code> che ha effettuato il login se le credenziali sono corrette.
     * @throws NullPointerException Se la lista è vuota o i parametri sono <code>null</code>.
     * @throws UtenteInesistente Se le credenziali sono errate.
     */
    public Utente login(String username, String password) throws NullPointerException, UtenteInesistente {
        if (listaUtenti.isEmpty() || username == null || password == null) {
            throw new NullPointerException("Non è possibile eseguire il login");
        }

        for (Utente u : listaUtenti) {
            if (u.getUsername().equals(username)) {
                String hashPw= u.getPassword();
                if (BCrypt.checkpw(password, hashPw)) {
                 return u;
                }
            }
        }

        throw new UtenteInesistente("Credenziali errate");

    }

    /**
     * Aggiunge alla <code>listaUtenti</code> il nuovo utente registrato.
     * @param utente L'utente che ha eseguito la registrazione.
     * @throws NullPointerException Se l'utente inserito è <code>null</code>.
     */
    public void registrazioneUtente(Utente utente) throws NullPointerException {
        //controllo che utente non sia vuoto
        if (utente == null) {
            throw new NullPointerException("Utente non valido");
        }
        //aggiungo l'utente alla lista
        this.listaUtenti.add(utente);

        //aggiungo il mio nuovo utente anche su JSON
        modificaFileJsonUtenti(listaUtenti);


    }

    /**
     * Aggiorna la lista interna e salva le modifiche sul file JSON.
     * <p>
     *     Questo metodo sovrascrive il contenuto del file con la nuova lista passata come parametro,
     *     serializzandola tramite l'istanza di <code>Gson</code>.
     *     In caso di errore di scrittura, viene stampato un messaggio di errore.
     * </p>
     * @param modifica La nuova lista di utenti da salvare nel file JSON.
     */
    public void modificaFileJsonUtenti(List<Utente> modifica){
        //cambio la lista in quella nuova
        this.listaUtenti = modifica;
        try{
            //metto in una stringa il contenuto della lista in formato Json
            String contenutoArray= gson.toJson(modifica);
            //metto nel file Json la stringa con il nuovo contenuto
            Files.writeString(Path.of(percorsoFileMemorizzato), contenutoArray);
        }catch (Exception e){
            System.err.println("Errore del caricamento sul file utenti");
        }
    }
}

