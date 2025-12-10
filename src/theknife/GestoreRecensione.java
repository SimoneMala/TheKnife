package theknife;

import java.util.*;

public class GestoreRecensione {

    private List<Recensione> recensioni;

    public GestoreRecensione() {
        recensioni = new ArrayList<>();
    }

    //mostra recensioni del theknife.Ristorante
    public void visualizzaRecensioni(Ristorante ris) {
        boolean trovato = false;
        for (Recensione rec : recensioni) {
            if (rec.getNomeRistorante().equals(ris.getNome())) {
                trovato = true;
                System.out.println("Testo:" + rec.getTesto());
                System.out.println("Stelle:" + rec.getStelle());
                String risposta = rec.getRispostaRecensione();
                if (risposta == null) {
                    System.out.println("Risposta: nessuna risposta alla recensione.");
                } else {
                    System.out.println("Risposta:" + risposta);
                }
            }
        }
        if (!trovato) {
            System.out.println("Nessuna recensione presente per questo ristorante.");
        }
    }

    //inserisce risposta alla recensione
    public void rispondiRecensione(Recensione rec, String risposta) {
        if (rec.getRispostaRecensione() != null) {
            System.out.println("Risposta già presente.");
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
                rec = tmp;
                break;
            }
        }
        rec.setTesto(testoMod);
        rec.setStelle(stelleMod);
    }

    //elimina recensione
    public void eliminaRecensione(Recensione rec) {
        recensioni.remove(rec);
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