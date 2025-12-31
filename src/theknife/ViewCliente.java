package theknife;

import theknife.eccezioni.ListaVuotaException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ViewCliente {
    Scanner sc = new Scanner(System.in);

    private static String gestisciInput(String msg, Scanner sc, boolean blank) {
        String input = "";
        do {
            System.out.println(msg);
            input = scanner.nextLine();
        } while (input.isEmpty() && blank);
        return input;
    }


    private static void NavigazioneRistoranti(Utente u, Scanner s, List<Ristorante> listaRistoranti) {
        if (listaRistoranti == null || listaRistoranti.isEmpty()) {
            System.out.println("Nessun ristorante trovato");
            return;
        }
    }


    public void mostraMenu(Utente u) {
        int scelta;

        do {
            System.out.println("\nTHE KNIFE - Login" + u.getUsername());
            System.out.println("1. Visualizza dati personali");
            System.out.println("2. Modifica dati personali");
            System.out.println("3. Visualizza ristoranti vicino a me");
            System.out.println("4. Cerca ristoranti per filtro");
            System.out.println("0. Esci");

            gestisciInput("Inserisci la tua scelta: ", sc, false);


            switch (scelta) {
                case 1:
                    System.out.println("Nome:" + u.getNome() + "\nCognome:" + u.getCognome() + "\nUsername:"
                            + u.getUsername() + "\nDomicilio:" + u.getDomicilio());
                    break;

                case 2:
                    String nuovoNome = gestisciInput("Inserisci il nuovo nome: ", sc, true);
                    String nuovoCognome = gestisciInput("Inserisci il nuovo cognome: ", sc, true);
                    String nuovoDomicilio = gestisciInput("Inserisci il nuovo domicilio: ", sc, true);

                    if (!nuovoNome.isEmpty()) {
                        u.setNome(nuovoNome);
                    }
                    if (!nuovoCognome.isEmpty()) {
                        u.setCognome(nuovoCognome);
                    }
                    if (!nuovoDomicilio.isEmpty()) {
                        u.setDomicilio(nuovoDomicilio);
                    }
                    System.out.println("Dati aggiornati con successo.");
                    break;

                case 3:
                    try {
                        ArrayList<Ristorante> ristorantiVicini = (ArrayList<Ristorante>) gestoreRistorante.cercaRistoranti
                                (null, luogo, null, null, false, false, null);
                        if (!ristorantiVicini.isEmpty()) {
                            System.out.println("Ecco i ristoranti che si trovano vicino a te:");
                            int numero = 1;
                            //creo matrice che contiene tutti i ristoranti (nome =0, città= 1) che verrà passata al metodo
                            List<String[]> ristorantiTrovati = stampaRistoranti(ristorantiVicini);
                            Ristorante visto = dettagliRistorante(ristorantiTrovati);

                        }
                    } catch (ListaVuotaException e) {
                        System.err.println(e.getMessage());
                    }
                    break;

                case 4:
                    ArrayList<Ristorante> ristorantiCercati = (ArrayList<Ristorante>) ricercaConFiltri();
                    List<String[]> ristorantiTrovati = stampaRistoranti(ristorantiCercati);
                    Ristorante visto = dettagliRistorante(ristorantiTrovati);

                    break;


                case 0:
                    System.out.println("Arrivederci!");
                    default -> System.out.println("Scelta non valida");
            }
        } while (scelta != 0);
    }

    public void ulterioriInformazioni (Utente u, Ristorante visto){

    int sceltaInterna;

    do {
        System.out.println("""Scegli un'opzione:
            1. Lascia una recensione
            2. Vai al ristorante successivo
            3. Torna al ristorante precedente
            4. Inserisci il ristorante tra i preferiti
            5. Rimuovi il ristorante dai preferiti
            6. Visualizza, modifica o elimina le recensioni che hai lasciato al ristorante
            7. Visualizza tutte le recensioni lasciate al ristorante
            8. Esci dalla visualizzazione
        """);

        gestisciInput("Inserisci la tua scelta: ", sc, false);

        switch (sceltaInterna) {
            case 1:
                System.out.println("Lascia il testo della recensione:");
                testo = sc.nextLine();
                System.out.println("Inserisci il numero di stelle (1-5):");
                stelle = sc.nextInt();
                inserisciRecensione(visto, testo, stelle, username)
                System.out.println("Recensione inserita con successo.");
                break;
            case 2:
                // Codice per andare al ristorante successivo
                break;
            case 3:
                // Codice per tornare al ristorante precedente
                break;
            case 4:
                System.out.println("Inserimento del ristorante tra i preferiti.");
                aggiungiPreferiti(u, visto);
                break;
            case 5:
                System.out.println("Rimozione del ristorante dai preferiti.");
                cancellaPreferiti(u, visto);
                break;
            case 6:
                System.out.println("Visualizza le recensioni che hai lasciato al ristorante.");
                visualizzaRecensioniPerUtente(visto, u);

                int sceltaRecensione;
                System.out.println("Modifica o elimina le tue recensioni: premere 1 per modificare, 2 per eliminare, 0 per uscire.");
                sceltaRecensione = sc.nextInt();
                if (sceltaRecensione == 1) {
                    System.out.println("Inserisci il testo modificato della recensione:");
                    String testoModificato = sc.nextLine();
                    System.out.println("Inserisci il numero di stelle modificato (1-5):");
                    int stelleModificate = sc.nextInt();
                    modificaRecensione(recensione, testoModificato, stelleModificate);
                    System.out.println("Recensione modificata con successo.");
                } else if (sceltaRecensione == 2) {
                    rimuoviRecensione(visto, recensione);
                    System.out.println("Recensione rimossa con successo.");
                } else if (sceltaRecensione == 0) {
                    System.out.println("Uscita dalla gestione delle recensioni.");
                }
                break;

            case 7:
                System.out.println("Visualizza il riepilogo delle recensioni del ristorante.");
                visualizzaRecensioni(visto);
                visualizzaRiepilogo(visto);
                break;
            case 8:
                System.out.println("Uscita dalla visualizzazione del ristorante.");
                break;
            default:
                System.out.println("Scelta non valida. Riprova.");
        }


}


