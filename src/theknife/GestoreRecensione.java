package theknife;

import java.util.*;

public class GestoreRecensione {

    private List<Recensione> recensioni;

    public GestoreRecensione() {
        recensioni = new ArrayList<>();
    }

    //mostra recensioni del theknife.Ristorante
    public String visualizzaRecensioni(Ristorante ris) {
        boolean trovato = false;
        if (recensioni.isEmpty()) {
            throw new IllegalArgumentException("la lista è vuota");
        }
        String recensioneRis = "";
        for (Recensione rec : recensioni) {
            if (rec.getNomeRistorante().equals(ris.getNome())) {
                trovato = true;

                if (risposta == null) {
                    throw new IllegalArgumentException("non ci sono risposte");
                    recensioneRis = "Nome Ristorante:" + getNomeRistorante() + "\nStelle:" + getStelle() + "\nTesto" + getTesto();
                } else {
                    recensioneRis = "Nome Ristorante:" + getNomeRistorante() + "\nStelle:" + getStelle() + "\nTesto" + getTesto()
                            + "\nRisposta:" + getRispostaRecensione();
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
        }
    }

    //inserisce nuova recensione
    public void inserisciRecensione(Ristorante ris, String testo, int stelle) {
        Recensione nuova = new Recensione(ris.getNome(), testo, stelle, null);
        recensioni.add(nuova);
    }

    //modifica recensione esistente
    public void modificaRecensione(Recensione rec, String testoMod, int stelleMod) {
        for (Recensione tmp : recensioni) {
            if (tmp.equals(rec)) {
                tmp.setTesto(testoMod);
                tmp.setStelle(stelleMod);
                break;
            } else {
                throw new IllegalArgumentException("Recensione non trovata");
            }
        }
    }

    //elimina recensione
    public void eliminaRecensione(Recensione rec) {
        if (!recensioni.remove(rec)) {
            throw new IllegalArgumentException("Recensione non trovata");
        }
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
            System.out.println("Nessuna recensione presente per questo ristorante.");
        } else {
            double media = (double) sommaStelle / cont;
            System.out.println("Numero di recensioni:" + cont);
            System.out.println("valutazione media:" + media);
        }
    }
}
