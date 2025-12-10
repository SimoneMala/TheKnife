package theknife;

import theknife.eccezioni.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class GestoreUtenti {
    //campi
    //lista che contiene i dati del JSON riguardo agli utenti
    private List<Utente> listaUtenti;
    private String percorsoFileMemorizzato;

    //nel costruttore poi chiameremo il metodo carica che carica dal file json alla lista i miei utenti
    public GestoreUtenti(String nomeFileJson) {

        try {
            //leggo contenuto del file e lo inserisco tutto in una string
            String contenutoJson = Files.readString(Path.of(nomeFileJson));
            Gson gson = new Gson();
            //inserisco il contenuto nella mia lista
            this.listaUtenti= gson.fromJson(contenutoJson, new TypeToken<ArrayList<Utente>>(){}.getType());
        } catch (Exception e) {
            System.err.println("Impossibile caricare dal file utenti");
        }
        //se il mio file risulta vuoto, creo comunque una lista vuota
        if(this.listaUtenti==null) {
            this.listaUtenti = new ArrayList<Utente>();
        }
    }

    //metodo get
    public List getListaUtenti() {
        return listaUtenti;
    }

    //metodo che dati username e password controlla se utente esiste e salva quale utente ha fatto l'accesso
    public Utente login(String username, String password) throws NullPointerException, UtenteInesistente {
        if (listaUtenti.isEmpty() || username == null || password == null) {
            throw new NullPointerException("Non è possibile eseguire il login");
        }

        for (Utente u : listaUtenti) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }

        throw new UtenteInesistente("Credenziali errate");

    }

    //aggiungo il nuovo utente alla mia lista e al file JSON
    public void registrazioneUtente(Utente utente) throws NullPointerException {
        //controllo che utente non sia vuoto
        if (utente == null) {
            throw new NullPointerException("theknife.Utente non vaido");
        }
        //aggiungo l'utenten alla lista
        this.listaUtenti.add(utente);

        //aggiungo il mio nuovo utente anche su JSON
        modificaFileJsonUtenti(listaUtenti);


    }

    public void modificaFileJsonUtenti(List modifica){
        //cambio la lista in quella da nuova
        this.listaUtenti = modifica;

        try{
            Gson gson = new Gson();
            //metto in una stringa il contenuto della lista in formato Json
            String contenutoArray= gson.toJson(modifica);
            //metto nel file Json la stringa con il nuovo contenuto
            Files.writeString(Path.of(percorsoFileMemorizzato), contenutoArray);
        }catch (Exception e){
            System.err.println("Errore del caricamento sul file utenti");
        }
    }
}

