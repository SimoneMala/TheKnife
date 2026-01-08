package theknife;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Classe con metodi per gestire le recensioni dei ristoranti
 * <p>
 * Contiene metodi per visualizzare, inserire, modificare, eliminare recensioni e rispondere alle recensioni
 * dei ristoranti.
 * @author Greta Giorgetti
 * @version 1.0
 */
public class GestoreRecensione {

    //CAMPI
    /**
     * Lista delle recensioni
     */
    private List<Recensione> recensioni;
    /**
     * Percorso del file JSON in cui sono memorizzate le recensioni
     */
    private final String percorsoFileMemorizzato;
    /**
     * Oggetto Gson per la serializzazione e deserializzazione JSON
     */
    private final Gson gson;

    /**
     * Costruttore della classe GestoreRecensione
     * <p>
     * Inizializza la lista delle recensioni leggendo dal file JSON specificato
     * @param nomeFileJson
     */
    public GestoreRecensione(String nomeFileJson) {
        this.percorsoFileMemorizzato=nomeFileJson;
        this.gson= new GsonBuilder().setPrettyPrinting().create();
        File file= new File(nomeFileJson);
        if (file.exists() && file.length()>0) {
            try {
                //leggo contenuto del file e lo inserisco tutto in una string, poi inserisco il contenuto in una lista
                String contenutoJson = Files.readString(Path.of(nomeFileJson));

                this.recensioni = gson.fromJson(contenutoJson, new TypeToken<ArrayList<Recensione>>() {
                }.getType());
            } catch (Exception e) {
                System.out.println("Impossibile caricare dal file recensioni");
            }
        }else{
            //creazione lista vuota se file non esiste o è vuoto
            this.recensioni= new ArrayList<>();
        }


    }

    //METODO GETTER
    /**
     * Restituisce la lista delle recensioni
     * @return lista delle recensioni
     */
    public List<Recensione> getRecensioni() {
        return recensioni;
    }

    /**
     * Salva la lista delle recensioni sul file JSON
     * @param modifica
     */
    private void salvaSuFile(List<Recensione> modifica) {
        this.recensioni = modifica;
        try {
            String json = gson.toJson(recensioni);
            Files.writeString(Path.of(percorsoFileMemorizzato), json);
        } catch (Exception e) {
            System.out.println("Errore nel salvataggio recensioni");
        }
    }

    /**
     * Risponde a una recensione specifica
     * <p>
     * @param rec
     * @param risposta
     */
    public void rispondiRecensione(Recensione rec, String risposta){
        for (Recensione tmp : recensioni) {
            if (tmp.equals(rec)) {
                rec = tmp;
                break;
            }
        }

        rec.setRispostaRecensione(risposta);
        salvaSuFile(recensioni);

    }

    /**
     * Inserisce una nuova recensione
     * <p>
     * @param ris
     * @param testo
     * @param stelle
     * @param username
     */
    public void inserisciRecensione(Ristorante ris, String testo, int stelle, String username) {
        Recensione nuova = new Recensione(ris.getNome(), testo, stelle, null, username);
        recensioni.add(nuova);
        salvaSuFile(recensioni);
    }

    /**
     * Modifica una recensione esistente
     * <p>
     * @param rec
     * @param testoMod
     * @param stelleMod
     * @param utente
     * @throws IllegalArgumentException
     */
    public void modificaRecensione(Recensione rec, String testoMod, int stelleMod, Utente utente) throws IllegalArgumentException {
        for (Recensione tmp : recensioni) {
            if (tmp.equals(rec) && utente.getUsername().equals(tmp.getUsername())) {
                //modifica il testo solo se inserito qualcosa di diverso da vuoto o spazi
                if (testoMod != null && !testoMod.trim().isEmpty()) {
                    tmp.setTesto(testoMod);
                }
                //modifica le stelle solo se diverso da 0
                if (stelleMod >= 1 && stelleMod <= 5) {
                    tmp.setStelle(stelleMod);
                }
                salvaSuFile(recensioni);
                return;
            }
        }
        throw new IllegalArgumentException("Recensione non trovata");
    }

    /**
     * Elimina una recensione specifica
     * <p>
     * @param rec
     * @throws IllegalArgumentException
     */
    public void eliminaRecensione(Recensione rec) throws IllegalArgumentException {
        if (!recensioni.remove(rec)) {
            throw new IllegalArgumentException("Recensione non trovata");
        }
        salvaSuFile(recensioni);
    }

    /**
     * Visualizza il riepilogo delle recensioni di un ristorante specifico
     * <p>
     * @param ris
     * @throws IllegalArgumentException
     */
    public void visualizzaRiepilogo(Ristorante ris) throws IllegalArgumentException {

        int cont = 0;
        for (Recensione rec : recensioni) {
            if (rec.getNomeRistorante().equals(ris.getNome())) {
                cont++;
            }
        }

        if (cont == 0) {
            throw new IllegalArgumentException("Nessuna recensione presente per questo ristorante.");
        } else {
            double media = ris.getStelle();
            System.out.println("Numero di recensioni: " + cont);
            System.out.println("valutazione media: " + media);
        }
    }

    /**
     * Visualizza le recensioni lasciate da un utente specifico
     * <p>
     * @param u
     * @return lista delle recensioni dell'utente
     * @throws IllegalArgumentException
     */
    public List<Recensione> visualizzaRecensioniperUtente (Utente u) throws IllegalArgumentException {
        if (recensioni.isEmpty()) {
            throw new IllegalArgumentException("la lista è vuota");
        }
        List<Recensione> recensioniUtente = new ArrayList<>();
        for (Recensione rec : recensioni) {
            if (rec.getUsername().equals(u.getUsername())){
                recensioniUtente.add(rec);
            }
        }
        if (recensioniUtente.isEmpty()) {
            throw new IllegalArgumentException("Nessuna recensione presente per questo utente.");
        }
        return recensioniUtente;
    }

    /**
     * Visualizza le recensioni ricevute da un ristoratore specifico
     * <p>
     * @param ris
     * @return
     * @throws IllegalArgumentException
     */
    public List<Recensione> visualizzaRecensioniPerRistoratore(Ristorante ris) throws IllegalArgumentException {
        if (recensioni.isEmpty()) {
            throw new IllegalArgumentException("la lista è vuota");
        }
        List<Recensione> recensioniPerRistoratore = new ArrayList<>();
        for(Recensione rec : recensioni){
            if(ris.getNome().equals(rec.getNomeRistorante())){
                recensioniPerRistoratore.add(rec);
            }
        }
        return recensioniPerRistoratore;
    }

    /**
     * Controlla se un utente ha già lasciato una recensione per un ristorante specifico
     * <p>
     * @param u
     * @param ris
     * @return true se l'utente ha già lasciato una recensione, false altrimenti
     */
    public boolean haLasciatoRecensione(Utente u, Ristorante ris) {
        for (Recensione rec : recensioni) {
            if (rec.getUsername().equals(u.getUsername()) && rec.getNomeRistorante().equals(ris.getNome())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gestisce l'input opzionale dell'utente
     * <p>
     * @param msg
     * @param sc
     * @param blank
     * @return input dell'utente
     */
    public static String gestisciInputOpzionale(String msg, Scanner sc, boolean blank) {
        System.out.println(msg);
        return sc.nextLine();
    }

}
