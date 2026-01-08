package theknife;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Classe con metodi per gestire l'oggetto <code>Recensione</code> dei ristoranti.
 * <p>
 *      Contiene metodi per visualizzare, inserire, modificare, eliminare recensioni e
 *      rispondere alle recensioni dei ristoranti.
 * </p>
 * @author Greta Giorgetti
 * @version 1.0
 */
public class GestoreRecensione {

    //CAMPI
    /**
     * Lista delle recensioni.
     */
    private List<Recensione> recensioni;

    /**
     * Percorso del file <code>JSON</code> in cui sono memorizzate le recensioni.
     */
    private final String percorsoFileMemorizzato;

    /**
     * Oggetto <code>GSON</code> per la serializzazione e deserializzazione JSON.
     * <p>
     *     Viene inizializzato nel costruttore con l'opzione "Pretty Printing"
     *     per garantire che i file vengano salvati con una formattazione leggibile.
     * </p>
     */
    private final Gson gson;

    /**
     * Costruttore della classe <code>GestoreRecensione</code>.
     * <p>
     *      Inizializza la lista delle recensioni leggendo dal file <code>JSON</code> specificato.
     *      In caso il file non esista o sia vuoto, viene creata una lista vuota.
     * </p>
     * @param nomeFileJson Il nome del file <code>JSON</code> da cui caricare le recensioni.
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
     * Restituisce la lista delle recensioni.
     * @return la lista (<code>ArrayList</code>) delle recensioni.
     */
    public List<Recensione> getRecensioni() {
        return recensioni;
    }

    /**
     * Il metodo salva la lista delle recensioni sul file <code>JSON</code>.
     * @param modifica La lista delle recensioni da salvare.
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
     * Il metodo permette al ristoratore di rispondere a una recensione specifica.
     * <p>
     *     Viene prima crecata la recensione nella lista delle recensioni,
     *     poi viene controllato se la recensione ha già una risposta.
     *     Se non ha una risposta, viene salvata la risposta.
     * </p>
     * @param rec Recensione a cui rispondere.
     * @param risposta Risposta del ristoratore alla recensione.
     * @throws IllegalArgumentException Se la recensione ha già una risposta.
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
     * Il metodo inserisce una nuova recensione.
     * <p>
     *     Viene creato un nuovo oggetto <code>Recensione</code> con i dati passati come parametri
     *     e viene aggiunto alla lista delle recensioni, salvandolo poi sul file <code>JSON</code>..
     * </p>
     * @param ris Ristorante a cui è associata la recensione.
     * @param testo Testo della recensione.
     * @param stelle Valutazione in stelle della recensione.
     * @param username Username dell'utente che ha scritto la recensione.
     */
    public void inserisciRecensione(Ristorante ris, String testo, int stelle, String username) {
        Recensione nuova = new Recensione(ris.getNome(), testo, stelle, null, username);
        recensioni.add(nuova);
        salvaSuFile(recensioni);
    }

    /**
     * Il metodo modifica una recensione esistente.
     * <p>
     *     Viene cercata la recensione nella lista delle recensioni,
     *     se trovata e l'utente corrisponde a quello che ha scritto la recensione,
     *     viene modificato il testo e/o le stelle della recensione
     *     (se i nuovi valori sono validi) e viene salvata la lista sul file <code>JSON</code>.
     *     I valori possono anche essere lasciati invariati.
     * </p>
     * @param rec Recensione da modificare.
     * @param testoMod Il testo modificato dall'utente (opzionale).
     * @param stelleMod Le stelle modificate dall'utente (opzionale).
     * @param utente Utente che sta effettuando la modifica.
     * @throws IllegalArgumentException Se la recensione non viene trovata o l'utente non è autorizzato a modificarla.
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
     * Il metodo elimina una recensione specifica.
     * <p>
     *     Viene rimossa la recensione dalla lista delle recensioni
     *     e viene salvata la lista aggiornata sul file <code>JSON</code>.
     * </p>
     * @param rec Recensione da eliminare.
     * @throws IllegalArgumentException Se la recensione non viene trovata.
     */
    public void eliminaRecensione(Recensione rec) throws IllegalArgumentException {
        if (!recensioni.remove(rec)) {
            throw new IllegalArgumentException("Recensione non trovata");
        }
        salvaSuFile(recensioni);
    }

    /**
     * Il metodo mostra il riepilogo delle recensioni di un ristorante specifico.
     * <p>
     *     Calcola e visualizza il numero totale di recensioni e la valutazione media in stelle
     *     per il ristorante passato come parametro.
     * </p>
     * @param ris Ristorante di cui visualizzare il riepilogo delle recensioni.
     * @throws IllegalArgumentException Se non sono presenti recensioni per il ristorante.
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
     * Il metodo visualizza le recensioni lasciate da un utente specifico.
     * <p>
     *     Controlla se la lista delle recensioni è vuota, poi crea una lista temporanea
     *     per memorizzare le recensioni dell'utente passato come parametro.
     *     Se non sono presenti recensioni per l'utente, viene lanciata un'eccezione.
     * </p>
     * @param u Utente di cui visualizzare le recensioni.
     * @return lista delle recensioni dell'utente.
     * @throws IllegalArgumentException Se la lista delle recensioni è vuota o se non sono presenti recensioni per l'utente.
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
     * Il metodo visualizza le recensioni ricevute da un ristoratore specifico.
     * <p>
     *     Controlla se la lista delle recensioni è vuota, poi crea una lista temporanea
     *     per memorizzare le recensioni del ristorante passato come parametro.
     * </p>
     * @param ris Ristorante di cui visualizzare le recensioni.
     * @return lista delle recensioni del ristorante.
     * @throws IllegalArgumentException Se la lista delle recensioni è vuota.
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
     * Il metodo controlla se un utente ha già lasciato una recensione per un ristorante specifico.
     * <p>
     *     Scorre la lista delle recensioni e verifica se esiste una recensione
     *     associata all'utente e al ristorante passati come parametri.
     * </p>
     * @param u Utente che può aver lasciato una recensione.
     * @param ris Ristorante per cui verificare la recensione.
     * @return <code>true</code> se l'utente ha già lasciato una recensione, <code>false</code> altrimenti.
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
     * Il metodo gestisce l'input opzionale dell'utente.
     * <p>
     *     Stampa il messaggio passato come parametro e legge l'input dell'utente.
     *     L'input può essere una stringa vuota se l'utente non desidera modificare il valore.
     * </p>
     * @param msg Messaggio da mostrare all'utente.
     * @param sc Scanner per leggere l'input dell'utente.
     * @param blank Indica se l'input può essere vuoto.
     * @return input dell'utente come <code>String</code>.
     */
    public static String gestisciInputOpzionale(String msg, Scanner sc, boolean blank) {
        System.out.println(msg);
        return sc.nextLine();
    }

}
