package theknife;

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

    private static void NavigazioneRistoranti(Cliente u, Scanner s, List<Ristorante> listaRistoranti){
        if (listaRistoranti == null || listaRistoranti.isEmpty()) {
            System.out.println("Nessun ristorante trovato");
            return;
        }

        int indice = 0;
        boolean visualizza = true;

        while (visualizza) {
            if (listaRistoranti == null || listaRistoranti.isEmpty()) {
                System.out.println("Non ci sono ristoranti da visualizzare.");
                break;
            }

            Ristorante ristoranteCorrente = listaRistoranti.get(indice);
            System.out.println("\n--- Ristorante " + (indice + 1) + " di " + listaRistoranti.size() + " ---");
            System.out.println(ristoranteCorrente.visualizzaRistorante());

            System.out.println(""" Scegli un'opzione:
                            1. Lascia una recensione
                            2. Vai al ristorante successivo
                            3. Torna al ristorante precedente
                            4. Inserisci il ristorante tra i preferiti
                            5. Rimuovi il ristorante dai preferiti
                            6. Visualizza e modifica le recensioni che hai lasciato al ristorante\s
                            7. Visualizza tutte le recensioni lasciate al ristorante\s
                            8. Esci dalla visualizzazione
                    """ );

    }


    public void mostraMenu(Utente u) {
        int scelta;

        do {
            System.out.println("\nTHE KNIFE - CLIENTE");
            System.out.println("1. Visualizza dati personali");
            System.out.println("2. Modifica dati personali");
            System.out.println("3. Cerca ristoranti");
            System.out.println("4. Cerca ristoranti per filtro");
            System.out.println("0. Esci");

            gestisciInput("Inserisci la tua scelta: ", sc, false);

            sc.nextLine(); // pulizia buffer

            switch (scelta) {
                case1: System.out.println("Nome:" + u.getNome() + "\nCognome:" + u.getCognome() + "\nUsername:"
                        + u.getUsername() + "\nDomicilio:" + u.getDomicilio());
                 break;
                case2:
                case3:
                case4:
                System.out.println("Arrivederci!");
                default -> System.out.println("Scelta non valida");
            }
        } while (scelta != 0);

