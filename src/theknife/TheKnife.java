package theknife;

import theknife.eccezioni.ListaVuotaException;
import theknife.eccezioni.UtenteInesistente;
//libreria per hashing
import org.mindrot.jbcrypt.BCrypt;
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
        Utente utente= null;
        boolean successo= true;
        try {
            utente = gestoreUtenti.login(username, password);
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
            paginaUtente(Utente u);
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
        String hashPw = BCrypt.hashpw(password, BCrypt.gensalt());
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
            System.out.println("1: Login \n2: Registrazione \n3: Visualizza ristoranti vicini \n4: Cerca ristoranti");
            int op= scanner.nextInt();

            switch (op){
                case 1:
                    paginaLogin();
                    break;
                case 2:
                    paginaRegistrazione();
                    break;
                case 3:
                    try {
                        ArrayList<Ristorante> ristorantiVicini = (ArrayList<Ristorante>) gestoreRistorante.cercaRistoranti
                                (null, luogo, null, null, false, false, null);
                        if(!ristorantiVicini.isEmpty()){
                            System.out.println("Ecco i ristoranti che si trovano vicino a te:");
                            int numero= 1;
                            //creo matrice che contiene tutti i ristoranti (nome =0, città= 1) che verrà passata al metodo
                            List<String[]> ristorantiTrovati= stampaRistoranti(ristorantiVicini);
                            Ristorante visto=dettagliRistorante(ristorantiTrovati);
                            if(visto!=null){
                                //utilizzo metodo visualizza recensioni per vedere recensioni in anonimo
                                //(all'interno del metodo serve boolean per capire se mostrarle in anonimo o no)
                            }
                        }
                    }catch(ListaVuotaException e){
                        System.err.println(e.getMessage());
                    }
                    break;
                //cerca ristoranti
                case 4:
                    ArrayList<Ristorante> ristorantiCercati= (ArrayList<Ristorante>) ricercaConFiltri();
                    List<String[]> ristorantiTrovati= stampaRistoranti(ristorantiCercati);
                    Ristorante visto=dettagliRistorante(ristorantiTrovati);
                    if (visto!=null){
                        //utilizzo metodo visualizza recensioni per vedere recensioni in anonimo
                    }
                    break;
                default:
                    System.out.println("Input non valido, inserire un intero che si riferisce a una delle operazioni descritte");
            }
        }

        //ricerca= chiama paginaFiltriRicerca, chiede al cliente che filtri vuole mettere

    }

    public static Ristorante dettagliRistorante(List<String[]> ristoranti){
        System.out.println("Vuoi vedere le informazioni di uno di questi ristoranti?");
        System.out.println("1: Sì \n2: No");
        int scelta= IntInput();
        if(scelta==1){
            int max= ristoranti.size();
            System.out.println("Digita il numero che si riferisce al ristorante di cui vuoi informazioni:");
            boolean corretto= true;
            int ristoranteScelto;
            do {
                ristoranteScelto = IntInput();
                if (ristoranteScelto > max) {
                    System.out.println("Il numero scelto non corrisponde a un ristorante, controlla la lista di ristoranti e riprova:");
                    corretto= false;
                }else{
                    corretto= true;
                }
            }while(!corretto);

            //recupero nome e città ristorante
            String[] riga= ristoranti.get(ristoranteScelto-1);
            Ristorante scelto=gestoreRistorante.visualizzaRistorante(riga[0], riga[1]);
            //bisogna ancora aggiungere il toString di ristoranti
            System.out.println(scelto.toString());
            return scelto;
        }else{
            return null;
        }
    }

    public static List<Ristorante> ricercaConFiltri(){
        String citta= null, tipologiaCucina= null;
        Double prezzoMassimo= null, prezzoMinimo= null, stelleMinimo= null;
        Boolean delivery= null, prenotazione= null;
        System.out.println("\nScegli i filtri che vuoi mettere! Rispondi con si o no");
        System.out.println("Filtro città:");
        boolean filtroCitta= siNoInput();
        System.out.println("Filtro tipo cucina:");
        boolean filtroCucina= siNoInput();
        System.out.println("Filtro prezzo minimo:");
        boolean filtroMinimo= siNoInput();
        System.out.println("Filtro prezzo massimo:");
        boolean filtroMassimo= siNoInput();
        System.out.println("Filtro delivery:");
        boolean filtroDelivery= siNoInput();
        System.out.println("Filtro prenotazione:");
        boolean filtroPrenotazione= siNoInput();
        System.out.println("Filtro stelle:");
        boolean filtroStelle= siNoInput();
        //con if ora gli faccio aggiungere i filtri veri e propri
        if(filtroCitta){
            System.out.println("Inserisci la città:");
            citta= StringInput();
        }
        if(filtroCucina){
            System.out.println("Inserisci la tipologia di cucina:");
            //stampo le tipologie presenti
            System.out.println("");
            int tipologia= IntInput();
            //if-else per dare poi valore corretto
        }
        if(filtroMinimo){
            System.out.println("Inserisci la prezzo minimo:");
            prezzoMinimo= doubleInput();
        }
        if(filtroMassimo){
            System.out.println("Inserisci la prezzo massimo:");
            prezzoMassimo= doubleInput();
        }
        if(filtroDelivery){
            System.out.println("Inserisci si o no se vuoi che delivery sia disponibile:");
            delivery= siNoInput();
        }
        if(filtroPrenotazione){
            System.out.println("Inserisci si o no se vuoi che prenotazione sia disponibile:");
            prenotazione= siNoInput();
        }
        if(filtroStelle){
            System.out.println("Inserisci il numero di stelle minimo che vuoi nel ristorante:");
            stelleMinimo= doubleInput();
        }
        List<Ristorante> ristorantiCercati= gestoreRistorante.cercaRistoranti(tipologiaCucina, citta, prezzoMinimo, prezzoMassimo, delivery, prenotazione, stelleMinimo);
        return ristorantiCercati;

    }

    public static List<String[]> stampaRistoranti(List<Ristorante> ristoranti){
        int numero= 1;
        List<String[]> matriceRistoranti= new ArrayList<>();
        for (Ristorante ris : ristoranti) {
            System.out.println(numero++ + ris.getNome() + ris.getCitta());
            //creo la mia riga che contiene nome e città (indice 0 = numero 1 stampato)
            String[] riga= new String[2];
            riga[0]= ris.getNome();
            riga[1]= ris.getCitta();
            matriceRistoranti.add(riga);
        }
        return matriceRistoranti;
    }
    //metodi di controllo dell'input
    //controllo che sia una stringa
    public static String StringInput(){
        while (!scanner.hasNextLine()) {
            System.out.println("Input non valido, inserire una stringa!");
            scanner.nextLine();
        }
        return scanner.nextLine();
    }
    //controllo che sia un intero
    public static int IntInput(){
        while(!scanner.hasNextInt()) {
            System.out.println("Input non valido, inserire un intero!");
            scanner.next();
        }
        return scanner.nextInt();
    }

    public static double doubleInput(){
        while (!scanner.hasNextDouble()) {
            System.out.println("Input non valido, inserire un numero!");
            scanner.next();
        }
        return scanner.nextDouble();
    }
    //controllo che sia o si o no e restituisco true se si, false se no
    public static boolean siNoInput(){
        do{
            String scelta= StringInput();
            if (scelta.equalsIgnoreCase("si")){
                return true;
            }else if (scelta.equalsIgnoreCase("no")){
                return false;
            }else{
                System.out.println("Input non valido, inserire o si o no!");
            }
        }while(true);
    }

    //accesso Utente
    public void paginaUtente(Utente u) {
        int scelta;

        do {
            System.out.println("\nTHE KNIFE - Login" + u.getUsername());
            System.out.println("1. Visualizza dati personali");
            System.out.println("2. Modifica dati personali");
            System.out.println("3. Visualizza ristoranti vicino a me");
            System.out.println("4. Cerca ristoranti per filtro");
            System.out.println("0. Esci");

            scelta = Integer.parseInt(gestisciInput("Inserisci la tua scelta: ", sc, false));

            switch (scelta) {
                //visualizzazione i dati utente
                case 1:
                    System.out.println("Nome:" + u.getNome() + "\nCognome:" + u.getCognome() + "\nUsername:"
                            + u.getUsername() + "\nDomicilio:" + u.getDomicilio());
                    break;

                //modifica dati utente
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

                //visualizzazione ristoranti vicini
                case 3:
                    try {
                        ArrayList<Ristorante> ristorantiVicini = (ArrayList<Ristorante>) gestoreRistorante.cercaRistoranti
                                (null, luogo, null, null, false, false, null);
                        if (!ristorantiVicini.isEmpty()) {
                            System.out.println("Ecco i ristoranti che si trovano vicino a te:");
                            int numero = 1;

                            List<String[]> ristorantiTrovati = stampaRistoranti(ristorantiVicini);
                            Ristorante visto = dettagliRistorante(ristorantiTrovati);

                        }
                    } catch (ListaVuotaException e) {
                        System.err.println(e.getMessage());
                    }
                    break;

                //ricerca ristoranti con filtri
                case 4:
                    ArrayList<Ristorante> ristorantiCercati = (ArrayList<Ristorante>) ricercaConFiltri();
                    List<String[]> ristorantiTrovati = stampaRistoranti(ristorantiCercati);
                    Ristorante visto = dettagliRistorante(ristorantiTrovati);

                    break;

                //uscita
                case 0:
                    System.out.println("Arrivederci!");
                    default -> System.out.println("Scelta non valida");
            }
        } while (scelta != 0);
    }

    public void ulterioriInformazioni (Utente u, Ristorante visto, List<Ristorante> ristorantiVicini, int ristoranteScelto){

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

            sceltaInterna = Integer.parseInt(gestisciInput("Inserisci la tua scelta: ", sc, false));

            switch (sceltaInterna) {

                //lascia recensione
                case 1:
                    System.out.println("Lascia il testo della recensione:");
                    testo = sc.nextLine();
                    System.out.println("Inserisci il numero di stelle (1-5):");
                    stelle = sc.nextInt();
                    inserisciRecensione(visto, testo, stelle, username);
                    System.out.println("Recensione inserita con successo.");
                    break;

                //vai al ristorante successivo
                case 2:
                    if (ristoranteScelto < ristorantiVicini.size() - 1) {
                        ristoranteScelto++;
                    } else {
                        System.out.println("Sei già all'ultimo ristorante.");
                    }
                    break;

                //vai al ristorante precedente
                case 3:
                    if (ristoranteScelto >= 0) {
                        ristoranteScelto--;
                    } else {
                        System.out.println("Sei già al primo ristorante.");
                    }
                    break;

                //aggiungi ai preferiti
                case 4:
                    System.out.println("Inserimento del ristorante tra i preferiti.");
                    aggiungiPreferiti(u, visto);
                    break;

                //rimozione dai preferiti
                case 5:
                    System.out.println("Rimozione del ristorante dai preferiti.");
                    cancellaPreferiti(u, visto);
                    break;

                //visualizza recensioni del ristorante lasciate dall'utente
                case 6:
                    System.out.println("Visualizza le recensioni che hai lasciato al ristorante.");
                    visualizzaRecensioniPerUtente(visto, u);

                    int sceltaRecensione;
                    System.out.println("Modifica o elimina le tue recensioni: premere 1 per modificare, 2 per eliminare, 0 per uscire.");
                    sceltaRecensione = sc.nextInt();
                    Recensione recensione = getRecensioneUtente(visto, u);
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

                //visualizza riepilogo recensioni ristorante
                case 7:
                    System.out.println("Visualizza il riepilogo delle recensioni del ristorante.");
                    visualizzaRecensioni(visto);
                    visualizzaRiepilogo(visto);
                    break;

                //esci
                case 8:
                    System.out.println("Uscita dalla visualizzazione del ristorante.");
                    break;
                default:
                    System.out.println("Scelta non valida. Riprova.");
            }
        }  while (sceltaInterna >= 1 && sceltaInterna <= 8);

    }

    public static void paginaRistoratore(Utente ristoratoreLoggato){
        int scelta;

        do {
            //stampe per pagina ristoratore
            System.out.println("Benvenuto" + ristoratoreLoggato.getNome() + " nell'area Ristoratore!");
            System.out.println("Scegli l'operazione da effettuare:");
            System.out.println("1. Aggiungi Ristorante");
            System.out.println("2. Visualizza Riepilogo Recensioni Ristorante");
            System.out.println("3. Visualizza Recensioni Ristorante");
            System.out.println("4. Rispondi a una Recensione");
            System.out.println("0. Logout/ Torna indietro");
            System.out.println("Funzione Scelta;");

            //Input scelta con gestione eccezioni
            try {
                scelta = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Input non valido, riprova");
                scanner.next(); //per pulire lo scanner
                scelta = -1; //valore di default per far ripartire il ciclo
            }

            switch (scelta) {
                case 1:
                    String nomeNuovoRistorante= "";
                    boolean trovatoDuplicato;
                    do{
                        System.out.println("Inserisci il nome del Ristorante:");
                        nomeNuovoRistorante = scanner.nextLine();
                        trovatoDuplicato = false;

                        for(Ristorante r : gestoreRistorante.getElencoRistoranti()){
                            if(r.getNome().equalsIgnoreCase(nomeNuovoRistorante)){
                                System.out.println("Nome" + nomeNuovoRistorante + "già presente, riprova.");
                                trovatoDuplicato = true;
                                break;
                            }
                        }
                    } while(trovatoDuplicato);
                    Ristorante r = datiRistorante(ristoratoreLoggato,nomeNuovoRistorante);
                    gestoreRistorante.aggiungiRistorante(r);
                    System.out.println("Ristorante aggiunto con successo.");
                    break;
                case 2:
                    System.out.println("---- Riepilogo Ristoranti ----");
                    List<Ristorante> ristorantiProprietario = gestoreRistorante.getRistoranteDi(ristoratoreLoggato.getNome());
                    for (Ristorante rist : ristorantiProprietario) {
                        System.out.println("Ristorante: " + rist.getNome());
                        try {
                            gestoreRecensione.visualizzaRiepilogo(rist);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    break;
                case 3:
                    System.out.println("Inserisci il nome del ristorante di cui vuoi visualizzare le recensioni:");
                    scanner.nextLine(); // Consumare la nuova linea rimasta
                    String nomeRistorante = scanner.nextLine();
                    for(Ristorante ristoranti : gestoreRistorante.getRistoranteDi(ristoratoreLoggato.getNome())) {
                        if(ristoranti.getNome().equals(nomeRistorante)) {
                            try {
                                List<Recensione> recensioni = gestoreRecensione.visualizzaRecensioni(ristoranti, ristoratoreLoggato);
                                for (Recensione rec : recensioni) {
                                    System.out.println(rec.toString());
                                }
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                    break;
                case 4:
                    System.out.println("Inserisci il nome del ristorante di cui vuoi rispondere a una recensione:");
                    scanner.nextLine(); // Consumare la nuova linea rimasta
                    String nomeRist = scanner.nextLine();
                    for(Ristorante ristoranti : gestoreRistorante.getRistoranteDi(ristoratoreLoggato.getNome())) {
                        if(ristoranti.getNome().equals(nomeRist)) {
                            try {
                                List<Recensione> recensioni = gestoreRecensione.visualizzaRecensioni(ristoranti, ristoratoreLoggato);
                                for (Recensione rec : recensioni) {
                                    System.out.println(rec.toString());
                                }
                                System.out.println("Inserisci nome utente della recensione a cui vuoi rispondere:");
                                String usernameRecensione = scanner.nextLine();
                                System.out.println("Inserisci la tua risposta:");
                                String risposta = scanner.nextLine();
                                for (Recensione rec : recensioni) {
                                    if (rec.getUsername().equals(usernameRecensione))
                                        gestoreRecensione.rispondiRecensione(rec, risposta);
                                }
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                    break;
                case 0:
                    System.out.println("Logout effettuato.");
                    break;
                default:
                    System.out.println("Scelta non valida, riprova.");
            }
        } while(scelta != 0);
    }

    public static Ristorante datiRistorante(Utente ristoratoreLoggato, String nomeControllato) {
        System.out.print("Nazione: ");
        String nazione = scanner.nextLine();
        System.out.print("Città: ");
        String citta = scanner.nextLine();
        System.out.print("Indirizzo: ");
        String indirizzo = scanner.nextLine();
        System.out.print("Latitudine: ");
        double latitudine = scanner.nextDouble();
        System.out.print("Longitudine: ");
        double longitudine = scanner.nextDouble();
        System.out.print("Prezzo Medio: ");
        double prezzoMedio = scanner.nextDouble();
        System.out.print("Delivery (true/false): ");
        boolean delivery = scanner.nextBoolean();
        System.out.print("Prenotazione (true/false): ");
        boolean prenotazione = scanner.nextBoolean();
        scanner.nextLine(); // Consumare la nuova linea rimasta
        System.out.print("Tipo di Cucina: ");
        String tipoCucina = scanner.nextLine();
        Double stelle = 0.0; // Nuovo ristorante inizia con 0 stelle
        String nomeProprietario = ristoratoreLoggato.getNome();
        return new Ristorante(nomeControllato, nazione, citta, indirizzo, latitudine, longitudine, prezzoMedio,
                delivery, prenotazione, tipoCucina, stelle, nomeProprietario);
    }

}