package theknife;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class GestoreRecensione {
    //campi
    //lista che contiene i dati del JSON sulle recensioni
    private List<Recensione> recensioni;
    private final String percorsoFileMemorizzato;
    private final Gson gson;

    //nel costruttore includo il metodo che carica le recensioni dal file JSON
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

    //metodo get
    public List<Recensione> getRecensioni() {
        return recensioni;
    }

    //metodi JSON
    private void salvaSuFile(List<Recensione> modifica) {
        this.recensioni = modifica;
        try {
            String json = gson.toJson(recensioni);
            Files.writeString(Path.of(percorsoFileMemorizzato), json);
        } catch (Exception e) {
            System.out.println("Errore nel salvataggio recensioni");
        }
    }


    //mostra recensioni del theknife.Ristorante
    public List<Recensione> visualizzaRecensioni(Ristorante ris) throws IllegalArgumentException {

        if (recensioni.isEmpty()) {
            throw new IllegalArgumentException("la lista è vuota");
        }

        List<Recensione> recensioniRistorante = new ArrayList<>();

        boolean trovato = false;

        for (Recensione rec : recensioni) {
            if (rec.getNomeRistorante().equals(ris.getNome())) {
                trovato = true;
                recensioniRistorante.add(rec);
            }
        }

        if (!trovato) {
                throw new IllegalArgumentException("Nessuna recensione presente per questo ristorante.");
        }
        return recensioniRistorante;
    }

    //inserisce risposta alla recensione
    public void rispondiRecensione(Recensione rec, String risposta) throws IllegalArgumentException {
        for (Recensione tmp : recensioni) {
            if (tmp.equals(rec)) {
                rec = tmp;
                break;
            }
        }
        if (rec.getRispostaRecensione() != null) {
            throw new IllegalArgumentException("Risposta già inserita.");
        } else {
            rec.setRispostaRecensione(risposta);
            salvaSuFile(recensioni);
        }
    }

    //inserisce nuova recensione
    public void inserisciRecensione(Ristorante ris, String testo, int stelle, String username) {
        Recensione nuova = new Recensione(ris.getNome(), testo, stelle, null, username);
        recensioni.add(nuova);
        salvaSuFile(recensioni);
    }

    //modifica recensione esistente
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

    //elimina recensione
    public void eliminaRecensione(Recensione rec) throws IllegalArgumentException {
        if (!recensioni.remove(rec)) {
            throw new IllegalArgumentException("Recensione non trovata");
        }
        salvaSuFile(recensioni);
    }

    //riepilogo recensioni ristorante
    public void visualizzaRiepilogo(Ristorante ris) throws IllegalArgumentException {
        int sommaStelle = 0;
        int cont = 0;
        for (Recensione rec : recensioni) {
            if (rec.getNomeRistorante().equals(ris.getNome())) {
                cont++;
                sommaStelle += rec.getStelle();
            }
        }

        if (cont == 0) {
            throw new IllegalArgumentException("Nessuna recensione presente per questo ristorante.");
        } else {
            double media = (double) sommaStelle / cont;
            System.out.println("Numero di recensioni: " + cont);
            System.out.println("valutazione media: " + media);
        }
    }

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

    //metodo di navigazione ristoranti
    private static void NavigazioneRistoranti(List<Ristorante> listaRistoranti) {
        if (listaRistoranti == null || listaRistoranti.isEmpty()) {
            System.out.println("Nessun ristorante trovato");
        }
    }

    public boolean haLasciatoRecensione(Utente u, Ristorante ris) {
        for (Recensione rec : recensioni) {
            if (rec.getUsername().equals(u.getUsername()) && rec.getNomeRistorante().equals(ris.getNome())) {
                return true;
            }
        }
        return false;
    }

    //metodo per gestire input opzionale (PREMERE INVIO PER NON MODIFICARE)
    public static String gestisciInputOpzionale(String msg, Scanner sc, boolean blank) {
        System.out.println(msg);
        return sc.nextLine();
    }

}
