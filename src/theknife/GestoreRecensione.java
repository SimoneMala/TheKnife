package theknife;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class GestoreRecensione {
    //campi
    //lista che contiene i dati del JSON sulle recensioni
    private List<Recensione> recensioni;
    private String percorsoFileMemorizzato;

    //nel costruttore includiamo il metodo che carica le recensioni dal file JSON
    public GestoreRecensione(String nomeFileJson) {

        try {
            //leggo contenuto del file e lo inserisco tutto in una string
            String contenutoJson = Files.readString(Path.of(nomeFileJson));
            Gson gson = new Gson();

            //inserisco il contenuto nella mia lista
            this.recensioni = gson.fromJson(contenutoJson, new TypeToken<ArrayList<Recensione>>() {
            }.getType());

        } catch (Exception e) {
            System.err.println("Impossibile caricare le recensioni");
        }

        if (this.recensioni == null) {
            recensioni = new ArrayList<>();
        }
    }

    //metodo get
    public List<Recensione> getRecensioni() {
        return recensioni;
    }

    //metodi JSON
    private void salvaSuFile() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(recensioni);
            Files.writeString(Path.of(percorsoFileMemorizzato), json);
        } catch (Exception e) {
            System.err.println("Errore nel salvataggio recensioni");
        }
    }


    //mostra recensioni del theknife.Ristorante
    public String visualizzaRecensioni(Ristorante ris, Utente u) {

        if (recensioni.isEmpty()) {
            throw new IllegalArgumentException("la lista è vuota");
        }
        String recensioneRis = "";
        boolean trovato = false;

        for (Recensione rec : recensioni) {
            if (rec.getNomeRistorante().equals(ris.getNome())) {
                trovato = true;

                recensioneRis += "Nome Ristorante: " + rec.getNomeRistorante()
                        + "\nStelle: " + rec.getStelle()
                        + "\nTesto: " + rec.getTesto();
                        + "\nUsername: " + rec.getUsername();

                if (rec.getRispostaRecensione() != null) {
                    recensioneRis += "\nRisposta: " + rec.getRispostaRecensione();
                }
            }
        }

            if (!trovato) {
                throw new IllegalArgumentException("Nessuna recensione presente per questo ristorante.");
        }
        return recensioneRis;
    }

    //inserisce risposta alla recensione
    public void rispondiRecensione(Recensione rec, String risposta) {
        if (rec.getRispostaRecensione() != null) {
            throw new IllegalArgumentException("Risposta già inserita.");
        } else {
            rec.setRispostaRecensione(risposta);
            salvaSuFile();
        }
    }

    //inserisce nuova recensione
    public void inserisciRecensione(Ristorante ris, String testo, int stelle, String username) {
        Recensione nuova = new Recensione(ris.getNome(), testo, stelle, null, username);
        recensioni.add(nuova);
        salvaSuFile();
    }

    //modifica recensione esistente
    public void modificaRecensione(Recensione rec, String testoMod, int stelleMod) {
        for (Recensione tmp : recensioni) {
            if (tmp.equals(rec)) {
                tmp.setTesto(testoMod);
                tmp.setStelle(stelleMod);
                salvaSuFile();
                return;
            }
        }
        throw new IllegalArgumentException("Recensione non trovata");
    }

    //elimina recensione
    public void eliminaRecensione(Recensione rec) {
        if (!recensioni.remove(rec)) {
            throw new IllegalArgumentException("Recensione non trovata");
        }
        salvaSuFile();
    }

    //riepilogo recensioni ristorante
    public void visualizzaRiepilogo(Ristorante ris) {
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
            System.out.println("Numero di recensioni:" + cont);
            System.out.println("valutazione media:" + media);
        }
    }

    public void visualizzaRecensioniperUtente (Utente u, Ristorante r) {
        for (Recensione rec : recensioni) {
            if (rec.getUsername().equals(u.getUsername()) && rec.getNomeRistorante().equals(r.getNome()) {
                return rec;
            } else {
                throw new IllegalArgumentException("Nessuna recensione trovata per questo ristorante.");
            }
        }
    }
    //metodo per controllare input utente
    private static String gestisciInput(String msg, Scanner sc, boolean blank) {
        String input = "";
        do {
            System.out.println(msg);
            input = sc.nextLine();
        } while (input.isEmpty() && blank);
        return input;
    }


    //metodo di navigazione ristoranti
    private static void NavigazioneRistoranti(Utente u, Scanner s, List<Ristorante> listaRistoranti) {
        if (listaRistoranti == null || listaRistoranti.isEmpty()) {
            System.out.println("Nessun ristorante trovato");
            return;
        }
    }


}
