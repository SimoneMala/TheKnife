import java.util.*;

public class GestoreRecensione {

    private List<Recensione> recensioni;

    public GestoreRecensione() {
        recensioni = new ArrayList<>();
    }

    //lo scanner non lo usiamo nelle classi ma solo nel main
    Scanner sc = new Scanner(System.in);

    //mostra recensioni del Ristorante
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
    //nel metodo tra i parametri hai già la string risposta, usiamo lo scanner dal main non dalla classe
    public void rispondiRecensione(Recensione rec) {
        if (rec.getRispostaRecensione() != null) {
            System.out.println("Risposta già presente.");
        }
        System.out.println("Inserire la risposta alla recensione:");
        String risposta = sc.nextLine();
        rec.setRispostaRecensione(risposta);
    }

    //inserisce nuova recensione
    //stesso discorso dello scanner, nei parametri avrai direttamente la recensione
    public void inserisciRecensione(Ristorante ris) {
        System.out.println("Inserire il testo della recensione:");
        String testo = sc.nextLine();
        while (testo.isEmpty() || testo.length() > 250) {
            System.out.println("Testo non valido (max 250 caratteri). Riprova:");
            testo = sc.nextLine();
        }

        System.out.println("Inserire la valutazione in stelle tra 1 e 5:");
        int stelle = sc.nextInt();
        while (stelle < 1 || stelle > 5) {
            System.out.println("Valutazione non valida. Inserire un valore tra 1 e 5:");
        } //manca controllo input non numerico: try catch o classe eccezione?
        Recensione nuova = new Recensione(ris.getNome(), testo, stelle, null);
        recensioni.add(nuova);
    }

    //modifica recensione esistente
    // stesso problema, avrai già nei parametri la recensione da cambiare e la recensione cambiata, trovala nella lista e cambiala, altrimenti la modifica non ci risulterà
    public void modificaRecensione(Recensione rec) {
        System.out.println("Modificare il testo della recensione:");
        String testoMod = sc.nextLine();
        while (testoMod.isEmpty() || testoMod.length() > 250) {
            System.out.println("Testo non valido (max 250 caratteri): Riprova:");
            testoMod = sc.nextLine();
        }

        System.out.println("Modificare la valutazione in stelle tra 1 e 5:");
        int stelleMod = sc.nextInt();
        while (stelleMod < 1 || stelleMod > 5) {
            System.out.println("Valutazione non valida. Inserire un valore tra 1 e 5:");
        } //manca controllo input non numerico: try catch o classe eccezione?

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