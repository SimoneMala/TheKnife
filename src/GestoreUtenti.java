import Eccezioni.*;
import java.util.*;

public class GestoreUtenti {
    //campi
    //lista che contiene i dati del JSON riguardo agli utenti
    private List<Utente> listaUtenti;

    //nel costruttore poi chiameremo il metodo carica che carica dal file json alla lista i miei utenti
    public GestoreUtenti() {
        this.listaUtenti = new ArrayList<Utente>();
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
        if (utente == null) {
            throw new NullPointerException("Utente non vaido");
        }
        this.listaUtenti.add(utente);

        //aggiungo il mio nuovo utente anche su JSON

    }

}

