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

public class GestoreUtenti {
    //campi
    //lista che contiene i dati del JSON riguardo agli utenti
    private List<Utente> listaUtenti;
    private final String percorsoFileMemorizzato;
    private final Gson gson;
    //nel costruttore poi chiameremo il metodo carica che carica dal file json alla lista i miei utenti
    public GestoreUtenti(String nomeFileJson) {
        this.percorsoFileMemorizzato=nomeFileJson;
        this.gson= new GsonBuilder().setPrettyPrinting().create();
        File file= new File(nomeFileJson);
        if (file.exists() && file.length()>0) {
            try {
                //leggo contenuto del file e lo inserisco tutto in una string
                String contenutoJson = Files.readString(Path.of(nomeFileJson));
                Gson gson = new Gson();
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

    //metodo get
    public List<Utente> getListaUtenti() {
        return listaUtenti;
    }

    //metodo che dati username e password controlla se utente esiste e salva quale utente ha fatto l'accesso
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

    //aggiungo il nuovo utente alla mia lista e al file JSON
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

    public void modificaFileJsonUtenti(List<Utente> modifica){
        //cambio la lista in quella da nuova
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

