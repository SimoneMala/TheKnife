package theknife;

import theknife.eccezioni.UtenteInesistente;

import java.util.*;
public class TheKnife {

    //inizializzo tutti i gestori e scanner all'inizio in modo da poterli usare in tutti i metodi
    private static Scanner scanner;
    private static GestoreRecensione gestoreRecensione;
    private static GestoreRistorante gestoreRistorante;
    private static GestorePreferiti gestorePreferiti;
    private static GestoreUtenti gestoreUtenti;

    public static void main(String[] args) {
        //se è il primo avvio dell'applicazione, quindi il file json è vuoto, converto il csv in json
        //...

        //creo i gestori, che mi serviranno per gestire tutti i dati
        scanner = new Scanner(System.in);
        gestoreUtenti= new GestoreUtenti("Utenti.json");
        gestorePreferiti= new GestorePreferiti("Preferiti.json");
        gestoreRistorante= new GestoreRistorante("Ristoranti.json");
        //gestoreRecensione= new GestoreRecensione("Recensioni.json"); -> commento perchè finchè greta non aggiorna il gestore è errato

        //chiamo la pagina home, inizio dell'applicazione
        paginaHome();

        //quando pagina home finisce, quindi quando scelgo l'opzione esci, faccio la chiusura applicazione
        System.out.println("Chiusura dell'applicazione, a presto!");
        scanner.close();
        System.exit(0);

    }

    public static void paginaHome(){
        System.out.println("Benvenuto in TheKnife!");
        //scelta dell'operazione
        while(true){
            System.out.println("\n1: Login \n2: Registrazione \n3: Accedi come guest \n4: Esci da TheKnife" );
            System.out.println("Digitare il numero dell'operazione scelta:");
            //controlli sull'inserimento di un intero
            while (!scanner.hasNextInt()) {
                System.out.println("Input non valido, inserire un valore intero:");
                scanner.next();
            }
            int op= scanner.nextInt();

            switch (op){
                //scelta login
                case 1:
                   // paginaLogin();
                    break;
                //scelta registrazione
                case 2:
                    //paginaRegistrazione();
                    break;
                //scelta accesso come guest
                case 3:
                    System.out.println("Inserire un luogo vicino a lei:");

                    //controlli sull'inserimento del luogo
                    while (!scanner.hasNextLine()) {
                        System.out.println("Input non valido, inserire un luogo:");
                        scanner.next();
                    }
                    String luogo= scanner.nextLine();

                    paginaGuest(luogo);
                    break;
                //scelta di uscire dall'app
                case 4:
                    return;
                default:
                    System.out.println("Input non valido, inserire un valore che fornisce le operazioni mostrate:");
            }
        }
    }

    public static void paginaLogin() {
        System.out.println("/nLogin");
        System.out.println("Inserire lo username:");
        String username = scanner.nextLine();
        System.out.println("Inserire la password:");
        String password = scanner.nextLine();

        //hash della passowrd
        //...

        String hashPw = "";
        Utente utente= null;
        try {
            utente = gestoreUtenti.login(username, hashPw);
        } catch (NullPointerException e){
            System.err.println(e.getMessage());
            return;
        } catch (UtenteInesistente e) {
            System.err.println(e.getMessage());
            return;
        }

        System.out.println("Login eseguito con successo! \nBenvenuto:" + utente);

        //controllo del ruolo per mandarlo alla pagina giusta
        if (utente.getRuolo().equals(Utente.Ruolo.CLIENTE)){
            //paginaCliente(utente)
        }else{
            //paginaRistoratore(utente)
        }
    }

    public static void paginaRegistrazione() {

    }
    public static void paginaGuest(String luogo){
        //stampe per pagina guest

        //parte il ciclo
        //stampe per la scelta: login, registrazione, visualizza ristoranti vicini, ricerca
        /*switch case in base alla scelta:
        -login= chiama paginaLogin
        -registrazione= chiama paginaRegistrazione
        -visualizza ristoranti vicini= chiama paginaVisualizzaVicini(luogo), che chiamerà ricerca con solo luogo diverso da null
        -ricerca= chiama paginaFiltriRicerca, chiede al cliente che filtri vuole mettere
         */

    }

}