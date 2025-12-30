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
        gestoreRecensione= new GestoreRecensione("Recensioni.json");

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
            System.out.println("1: Login \n2: Registrazione \n3: Accedi come guest \n4: Esci da TheKnife" );
            System.out.println("Digitare il numero dell'operazione scelta:");
            //controlli sull'inserimento di un intero
            int op= IntInput();

            switch (op){
                //scelta login
                case 1:
                    paginaLogin();
                    break;
                //scelta registrazione
                case 2:
                    paginaRegistrazione();
                    break;
                //scelta accesso come guest
                case 3:
                    System.out.println("Inserire un luogo vicino a lei:");

                    //controlli sull'inserimento del luogo
                    String luogo= StringInput();

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
        boolean successo= true;
        try {
            utente = gestoreUtenti.login(username, hashPw);
        } catch (NullPointerException e){
            System.err.println(e.getMessage());
            successo= false;
        } catch (UtenteInesistente e) {
            System.err.println(e.getMessage());
            successo= false;
        }
        if(successo){
            System.out.println("Login eseguito con successo! \nBenvenuto:" + utente);
        }else{
            System.out.println("Il login non è andato a buon fine, siamo spiacenti");
            return;
        }
        //controllo del ruolo per mandarlo alla pagina giusta
        if (utente.getRuolo().equals(Utente.Ruolo.CLIENTE)){
            //paginaCliente(utente)
        }else{
            //paginaRistoratore(utente)
        }
    }

    //richiesta di informazioni per creare l'utente da registrare
    public static void paginaRegistrazione() {
        Utente u= null;
        System.out.println("\nRegistrazione");
        System.out.println("Inserire le informazioni necessarie alla registrazione:");
        System.out.println("Nome:");
        scanner.nextLine();
        String nome= StringInput();
        System.out.println("Cognome:");
        String cognome= StringInput();
        System.out.println("Username:");
        //controllo che non esistano altri username uguali
        boolean corretto= true;
        ArrayList<Utente> utenti = (ArrayList<Utente>) gestoreUtenti.getListaUtenti();
        String username= "";
        do {
            username = StringInput();
            for (Utente tmp : utenti) {
                if (tmp.getUsername().equals(username)) {
                    System.out.println("Questo username è già in utilizzo, sceglierne uno diverso:");
                    corretto= false;
                    break;
                }
                corretto= true;
            }
        }while(!corretto);
        System.out.println("Password:");
        String password= StringInput();
        //hash della password, sarà quella inserita all'interno dell'utente
        //...
        String hashPw = "";
        System.out.println("Domicilio:");
        String domicilio= StringInput();
        System.out.println("Ruolo: selezionare 1 per Ristoratore, 2 per Cliente");
        boolean ruoloCorretto= true;
        do {
            int ruolo = IntInput();
            //assegno il ruolo scelto
            if(ruolo==1){
                u= new Utente(nome, cognome, username, hashPw, domicilio, Utente.Ruolo.RISTORATORE);
                ruoloCorretto= true;
            }else if (ruolo==2){
                u= new Utente(nome, cognome, username, hashPw, domicilio, Utente.Ruolo.CLIENTE);
                ruoloCorretto= true;
            }else{ //se numero non identifica ruolo, lo richiedo
                System.out.println("L'input inserito non si riferisce a nessun ruolo, inserire un intero tra quelli mostrati " +
                        "\n(1=ristoratore, 2= cliente):");
                ruoloCorretto= false;
            }
        }while(!ruoloCorretto);
        //registro l'utente
        boolean successo= true;
        try {
            gestoreUtenti.registrazioneUtente(u);
        }catch (NullPointerException e){
            System.err.println(e.getMessage());
            successo= false;
        }
        //pagina login automatica dopo la registrazione
        if(successo) {
            System.out.println("Registrazione effettuata con successo! \nVerrai reindirizzato alla pagina di login");
            paginaLogin();
        }else{
            System.out.println("La registrazione non è andata a buon fine, siamo spiacenti!");
        }
    }

    public static void paginaGuest(String luogo){
        //stampe per pagina guest
        System.out.println("Benvenuto nell'area Guest!");
        //scelta dell'operazione
        while (true){
            System.out.println("1: Login \n2: Registrazione \n3: Visualizza ristoranti vicini");
        }
        //stampe per la scelta: login, registrazione, visualizza ristoranti vicini, ricerca
        /*switch case in base alla scelta:
        -login= chiama paginaLogin
        -registrazione= chiama paginaRegistrazione
        -visualizza ristoranti vicini= chiama paginaVisualizzaVicini(luogo), che chiamerà ricerca con solo luogo diverso da null
        -ricerca= chiama paginaFiltriRicerca, chiede al cliente che filtri vuole mettere
         */

    }

    //metodi di controllo dell'input
    //controllo che sia una stringa
    public static String StringInput(){
        while (!scanner.hasNextLine()) {
            System.out.println("Input non valido, inserire una stringa!");
            scanner.nextLine();
        }
        String input = scanner.nextLine();

        return input;
    }

    //controllo che sia un intero
    public static int IntInput(){
        while(!scanner.hasNextInt()) {
            System.out.println("Input non valido, inserire un intero!");
            scanner.nextInt();
        }
        int input = scanner.nextInt();

        return input;
    }

}