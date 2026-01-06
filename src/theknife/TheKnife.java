package theknife;

import theknife.eccezioni.ListaVuotaException;
import theknife.eccezioni.UtenteInesistente;
//libreria per hashing
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.util.*;
public class TheKnife {

    //inizializzo tutti i gestori e scanner all'inizio in modo da poterli usare in tutti i metodi
    private static Scanner scanner;
    private static GestoreRecensione gestoreRecensione;
    private static GestoreRistorante gestoreRistorante;
    private static GestorePreferiti gestorePreferiti;
    private static GestoreUtenti gestoreUtenti;

    public static void main(String[] args) {

        //creo i gestori, che mi serviranno per gestire tutti i dati
        scanner = new Scanner(System.in);
        gestoreUtenti= new GestoreUtenti("Dati"+ File.separator + "Utenti.json");
        gestorePreferiti= new GestorePreferiti("Dati"+ File.separator + "Preferiti.json");
        //se è il primo accesso il gestore ristorante metterà il contenuto del csv all'interno del file json
        gestoreRistorante= new GestoreRistorante("Dati"+ File.separator + "Ristoranti.json");
        gestoreRecensione= new GestoreRecensione("Dati"+ File.separator + "Recensioni.json");

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
        System.out.println("Login");
        System.out.println("Inserire lo username:");
        String username = StringInput();
        System.out.println("Inserire la password:");
        String password = StringInput();
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
            System.out.println("Login eseguito con successo! \nBenvenuto:" + utente.getUsername());
        }else{
            System.out.println("Il login non è andato a buon fine, siamo spiacenti");
            return;
        }
        //controllo del ruolo per mandarlo alla pagina giusta
        if (utente.getRuolo().equals(Utente.Ruolo.CLIENTE)){
            paginaUtente(utente);
        }else{
            paginaRistoratore(utente);
        }
    }

    //richiesta di informazioni per creare l'utente da registrare
    public static void paginaRegistrazione() {
        Utente u= null;
        System.out.println("\nRegistrazione");
        System.out.println("Inserire le informazioni necessarie alla registrazione:");
        System.out.println("Nome:");
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
        System.out.println("Inserire la via:");
        String via= StringInput();
        System.out.println("Inserire la città:");
        String citta= StringInput();
        System.out.println("Ruolo: selezionare 1 per Ristoratore, 2 per Cliente");
        boolean ruoloCorretto= true;
        do {
            int ruolo = IntInput();
            //assegno il ruolo scelto
            if(ruolo==1){
                u= new Utente(nome, cognome, username, hashPw, via, citta, Utente.Ruolo.RISTORATORE);
                ruoloCorretto= true;
            }else if (ruolo==2){
                u= new Utente(nome, cognome, username, hashPw, via, citta, Utente.Ruolo.CLIENTE);
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
            System.out.println("1: Login \n2: Registrazione \n3: Visualizza ristoranti vicini \n4: Cerca ristoranti \n0: Torna alla home");
            int op= IntInput();

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
                                recensioniAnonime(visto);
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
                        recensioniAnonime(visto);
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Input non valido, inserire un intero che si riferisce a una delle operazioni descritte");
            }
        }
    }

    public static void recensioniAnonime(Ristorante ristorante){
        System.out.println("Vuoi visualizzare le recensioni del ristorante " + ristorante.getNome() + "?");
        boolean scelta= siNoInput();
        ArrayList<Recensione> recensioniRistorante;
        if(scelta){
            System.out.println("Recensioni per il ristorante " + ristorante.getNome());
            recensioniRistorante= (ArrayList<Recensione>) gestoreRecensione.visualizzaRecensioni(ristorante);
            int count=1;
            for(Recensione r: recensioniRistorante){
                if(r.getRispostaRecensione()!=null) {
                    System.out.println(count++ + "\nTesto:" + r.getTesto() + "\nStelle:" + r.getStelle() + "\nRisposta" + r.getRispostaRecensione());
                }else{
                    System.out.println(count++ +"\nTesto:" + r.getTesto() + "\nStelle:" + r.getStelle());
                }
            }
        }
    }

    public static Ristorante dettagliRistorante(List<String[]> ristoranti){
        System.out.println("Vuoi vedere le informazioni di uno di questi ristoranti (Digita 1 o 2)?");
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
            System.out.println("Scegli la tipologia di cucina tra quelle presenti:");
            //stampo le tipologie presenti
            ArrayList<String> tipiCucina= (ArrayList<String>) gestoreRistorante.getTipiCucinaLista();
            for(String tipo: tipiCucina){
                System.out.println(tipo);
            }
            tipologiaCucina= StringInput();
            while(!tipiCucina.contains(tipologiaCucina)){
              System.out.println("Hai scritto in maniera errata la tipologia, riprova (stai attento a maiuscole e spazi)!");
              tipologiaCucina= StringInput();
            }
        }
        if(filtroMinimo){
            System.out.println("Inserisci il prezzo minimo:");
            prezzoMinimo= doubleInput();
        }
        if(filtroMassimo){
            System.out.println("Inserisci il prezzo massimo:");
            prezzoMassimo= doubleInput();
        }
        if(filtroDelivery){
            System.out.println("Inserisci si o no se vuoi che delivery sia disponibile:");
            delivery= siNoInput();
        }
        if(filtroPrenotazione){
            System.out.println("Inserisci si o no se vuoi che la prenotazione online sia disponibile:");
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
            System.out.println(numero++ + " " + ris.getNome() + " " + ris.getCitta());
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
            System.out.println("Input non valido, inserire un numero senza virgola!");
            scanner.next();
        }
        int numero= scanner.nextInt();
        scanner.nextLine();
        return numero;
    }

    public static double doubleInput(){
        while (!scanner.hasNextDouble()) {
            System.out.println("Input non valido, inserire un numero!");
            scanner.next();
        }
        double numero= scanner.nextDouble();
        scanner.nextLine();
        return numero;
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

    private static String gestisciInput(String msg, boolean blank) {
        String input = "";
        do {
            System.out.println(msg);
            input = scanner.nextLine();
        } while (input.isEmpty() && blank);
        return input;
    }

    //accesso Utente
    public static void paginaUtente(Utente u) {
        int scelta;

        while(true) {
            System.out.println("\nLogin" + u.getUsername());
            System.out.println("1. Visualizza dati personali");
            System.out.println("2. Modifica dati personali");
            System.out.println("3. Visualizza ristoranti vicino a me");
            System.out.println("4. Cerca ristoranti per filtro");
            System.out.println("5. Visualizza le tue recensioni");
            System.out.println("0. Torna alla home");

            scelta = Integer.parseInt(gestisciInput("Inserisci la tua scelta: ", false));

            switch (scelta) {
                //visualizzazione i dati utente
                case 1:
                    System.out.println("Nome:" + u.getNome() + "\nCognome:" + u.getCognome() + "\nUsername:"
                            + u.getUsername() + "\nVia:" + u.getVia() + "\nCitta:" + u.getCitta());
                    break;

                //modifica dati utente
                case 2:
                    String nuovoNome = gestisciInput("Inserisci il nuovo nome: ", true);
                    String nuovoCognome = gestisciInput("Inserisci il nuovo cognome: ", true);
                    String nuovaVia = gestisciInput("Inserisci la nuova via: ", true);
                    String nuovaCitta =gestisciInput("Inserisci la nuova citta: ", true);

                    for (Utente utente: gestoreUtenti.getListaUtenti()) {
                        if (u.equals(utente)) {
                            if (!nuovoNome.isEmpty()) {
                                u.setNome(nuovoNome);
                            }
                            if (!nuovoCognome.isEmpty()) {
                                u.setCognome(nuovoCognome);
                            }
                            if(!nuovaVia.isEmpty()) {
                                u.setVia(nuovaVia);
                            }
                            if(!nuovaCitta.isEmpty()) {
                                u.setCitta(nuovaCitta);
                            }
                        }
                    }
                    gestoreUtenti.modificaFileJsonUtenti(gestoreUtenti.getListaUtenti());

                    System.out.println("Dati aggiornati con successo.");
                    break;

                //visualizzazione ristoranti vicini
                case 3:
                    try {
                        ArrayList<Ristorante> ristorantiVicini = (ArrayList<Ristorante>) gestoreRistorante.cercaRistoranti
                                (null, u.getCitta(), null, null, false, false, null);
                        if (!ristorantiVicini.isEmpty()) {
                            System.out.println("Ecco i ristoranti che si trovano vicino a te:");
                            int numero = 1;

                            List<String[]> ristorantiTrovati = stampaRistoranti(ristorantiVicini);
                            Ristorante visto = dettagliRistorante(ristorantiTrovati);

                            ulterioriInformazioni(u, visto);

                        }
                    } catch (ListaVuotaException e) {
                        System.err.println("Non ci sono ristoranti vicino a te.");
                    }
                    break;

                //ricerca ristoranti con filtri
                case 4:
                    ArrayList<Ristorante> ristorantiCercati = (ArrayList<Ristorante>) ricercaConFiltri();
                    List<String[]> ristorantiTrovati = stampaRistoranti(ristorantiCercati);
                    Ristorante visto = dettagliRistorante(ristorantiTrovati);
                    ulterioriInformazioni(u, visto);

                    break;

                //visualizza recensioni
                case 5:
                    List<Recensione> recensioniUtente= gestoreRecensione.visualizzaRecensioniperUtente(u);
                    int cont=1;
                    for(Recensione rec:  recensioniUtente) {
                        System.out.println(cont++ + rec.toString());
                    }
                    System.out.println("Modifica o elimina le tue recensioni: premere 1 per modificare, 2 per eliminare, 0 per uscire.");
                    int sceltaOperazione= IntInput();
                    while(sceltaOperazione!=0 || sceltaOperazione!=1 || sceltaOperazione!=2) {
                        sceltaOperazione = IntInput();
                    }
                    System.out.println("Scegli su quale recensione fare l'operazione (inserisci il numero della recensione):");
                    int sceltaRecensione = IntInput();
                    int contatore=1;
                    Recensione recensione= null;
                    for(Recensione rec:  recensioniUtente) {
                        if(sceltaRecensione==contatore){
                            recensione = rec;
                        }
                        contatore ++;
                    }

                    if (sceltaOperazione == 1) {
                        System.out.println("Inserisci il testo modificato della recensione (PREMERE INVIO PER NON MODIFICARE):");
                        String testoModificato = scanner.nextLine();
                        System.out.println("Inserisci il numero di stelle modificato (PREMERE INVIO PER NON MODIFICARE):");
                        int stelleModificate = scanner.nextInt();
                        gestoreRecensione.modificaRecensione(recensione, testoModificato, stelleModificate, u);
                        System.out.println("Recensione modificata con successo.");
                    } else if (sceltaOperazione == 2) {
                        gestoreRecensione.eliminaRecensione(recensione);
                        System.out.println("Recensione rimossa con successo.");
                    } else if (sceltaOperazione == 0) {
                        System.out.println("Uscita dalla gestione delle recensioni.");
                    }
                    break;
                //uscita
                case 0:
                    System.out.println("Arrivederci!");
                    return;
                default: System.out.println("Scelta non valida");
            }
        }
    }

    public static void ulterioriInformazioni (Utente u, Ristorante visto){

        int sceltaInterna;

        while(true){
            System.out.println("Scegli un'opzione: \n1. Lascia una recensione \n2. Inserisci il ristorante tra i preferiti" +
                    " \n3. Rimuovi il ristorante dai preferiti" +
                    " \n4. Visualizza tutte le recensioni lasciate al ristorante \n5. Esci dalla visualizzazione");

            sceltaInterna = Integer.parseInt(gestisciInput("Inserisci la tua scelta: ", false));

            switch (sceltaInterna) {

                //lascia recensione
                case 1:
                    if (gestoreRecensione.haLasciatoRecensione(u, visto)) {
                        System.out.println("Hai già lasciato una recensione per questo ristorante.");
                        break;
                    }
                    System.out.println("Lascia il testo della recensione:");
                    String testo = StringInput();
                    System.out.println("Inserisci il numero di stelle (1-5):");
                    int stelle = IntInput();
                    gestoreRecensione.inserisciRecensione(visto, testo, stelle, u.getUsername());
                    System.out.println("Recensione inserita con successo.");
                    break;

                //aggiungi ai preferiti
                case 2:
                    System.out.println("Inserimento del ristorante tra i preferiti.");
                    gestorePreferiti.aggiungiPreferiti(u, visto);
                    break;

                //rimozione dai preferiti
                case 3:
                    System.out.println("Rimozione del ristorante dai preferiti.");
                    gestorePreferiti.cancellaPreferiti(u, visto);
                    break;

                //visualizza riepilogo recensioni ristorante
                case 4:
                    System.out.println("Visualizza il riepilogo delle recensioni del ristorante.");
                    gestoreRecensione.visualizzaRecensioni(visto);
                    gestoreRecensione.visualizzaRiepilogo(visto);
                    break;

                //esci
                case 5:
                    System.out.println("Uscita dalla visualizzazione del ristorante.");
                    return;
                default:
                    System.out.println("Scelta non valida. Riprova.");
            }
        }

    }

    public static void paginaRistoratore(Utente ristoratoreLoggato){
        int scelta;

        do {
            //stampe per pagina ristoratore
            System.out.println("Benvenuto " + ristoratoreLoggato.getNome() + " nell'area Ristoratore!");
            System.out.println("Scegli l'operazione da effettuare:");
            System.out.println("1. Aggiungi Ristorante");
            System.out.println("2. Visualizza Ristoranti e modificali");
            System.out.println("3. Visualizza Riepilogo Recensioni Ristorante");
            System.out.println("4. Visualizza Recensioni Ristorante");
            System.out.println("5. Rispondi a una Recensione");
            System.out.println("0. Logout/ Torna indietro");
            System.out.println("Operazione Scelta:");

            //Input scelta

            scelta = IntInput();

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
                    List<Ristorante> rist = gestoreRistorante.getRistoranteDi(ristoratoreLoggato.getNome());
                    if(rist.isEmpty()){
                        System.out.println("Non hai ristoranti registrati.");
                        break;
                    }
                    int count=1;
                    System.out.println("---- I tuoi ristoranti sono ----");
                    for(Ristorante ristorantiDelRistoratore : rist) {
                        System.out.println(count++ + ": " + ristorantiDelRistoratore.getNome());
                    }
                    System.out.println("Vuoi modificare i dati di uno di questi ristoranti?(1: Sì, 2: No)");
                    int risposta= IntInput();
                    if(risposta!=1) {
                        break;
                    }
                    System.out.println("Inserisci il numero del ristorante che vuoi modificare:");
                    int sceltaRisto = IntInput();
                    if(sceltaRisto<0 || sceltaRisto>rist.size()) {
                        System.out.println("Scelta non valida.");
                        break;
                    }
                    Ristorante ristoranteScelto = rist.get(sceltaRisto - 1);
                    // MODIFICA STRINGHE
                    ristoranteScelto.setNome(modificaStringa("Nome", ristoranteScelto.getNome()));
                    ristoranteScelto.setNazione(modificaStringa("Nazione", ristoranteScelto.getNazione()));
                    ristoranteScelto.setCitta(modificaStringa("Città", ristoranteScelto.getCitta()));
                    ristoranteScelto.setIndirizzo(modificaStringa("Indirizzo", ristoranteScelto.getIndirizzo()));

                    // MODIFICA DOUBLE (Validazione automatica inclusa!)
                    ristoranteScelto.setLatitudine(modificaDouble("Latitudine", ristoranteScelto.getLatitudine()));
                    ristoranteScelto.setLongitudine(modificaDouble("Longitudine", ristoranteScelto.getLongitudine()));

                    // MODIFICA INTERI
                    ristoranteScelto.setPrezzoMedio(modificaIntero("Prezzo Medio", ristoranteScelto.getPrezzoMedio()));

                    // MODIFICA BOOLEANI (Si/No)
                    ristoranteScelto.setDelivery(modificaSiNo("Delivery Disponibile", ristoranteScelto.getDelivery()));
                    ristoranteScelto.setPrenotazione(modificaSiNo("Prenotazione Online", ristoranteScelto.getPrenotazione()));
                    ristoranteScelto.setTipoCucina(modificaStringa("Tipo di Cucina", ristoranteScelto.getTipoCucina()));

                    // Salvataggio finale
                    gestoreRistorante.modificaFileJsonRistoranti(gestoreRistorante.getElencoRistoranti());
                    System.out.println("Ristorante modificato con successo.");
                    break;
                case 3:
                    System.out.println("---- Riepilogo Ristoranti ----");
                    List<Ristorante> ristorantiProprietario = gestoreRistorante.getRistoranteDi(ristoratoreLoggato.getNome());
                    if(ristorantiProprietario.isEmpty()){
                        System.out.println("Non hai ristoranti registrati.");
                        break;
                    }
                    for (Ristorante risto : ristorantiProprietario) {
                        System.out.println("Ristorante: " + risto.getNome());
                        try {
                            gestoreRecensione.visualizzaRiepilogo(risto);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    break;
                case 4:
                    List<Ristorante> ristorantiDelRistoratore = gestoreRistorante.getRistoranteDi(ristoratoreLoggato.getNome());
                    if(ristorantiDelRistoratore.isEmpty()){
                        System.out.println("Non hai ristoranti registrati.");
                        break;
                    }
                    int cont=1;
                    System.out.println("---- I tuoi ristoranti sono ----");
                    for(Ristorante ristoranti : ristorantiDelRistoratore) {
                        System.out.println(cont++ + ": " + ristoranti.getNome());
                    }
                    System.out.println("Inserisci il numero del ristorante di cui vuoi visulizzare le recensioni: ");
                    int sceltaRistorante = IntInput();
                    if(sceltaRistorante<0 || sceltaRistorante>ristorantiDelRistoratore.size()) {
                        System.out.println("Scelta non valida.");
                        break;
                    }
                    Ristorante ristoScelto = ristorantiDelRistoratore.get(sceltaRistorante - 1);
                            try {
                                List<Recensione> recensioni = gestoreRecensione.visualizzaRecensioniPerRistoratore(ristoScelto);
                                if(recensioni.isEmpty()){
                                    System.out.println("Non ci sono recensioni per questo ristorante.");
                                    break;
                                }
                                for (Recensione rec : recensioni) {
                                    System.out.println(rec.toString());
                                }
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }
                    break;
                case 5:
                    List<Ristorante> ristoDelRistoratore = gestoreRistorante.getRistoranteDi(ristoratoreLoggato.getNome());
                    if(ristoDelRistoratore.isEmpty()){
                        System.out.println("Non hai ristoranti registrati.");
                        break;
                    }
                    int conta=1;
                    System.out.println("I tuoi ristoranti sono:");
                    for(Ristorante ristoranti : ristoDelRistoratore) {
                        System.out.println(conta++ + ": " + ristoranti.getNome());
                        try {
                            gestoreRecensione.visualizzaRiepilogo(ristoranti);
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                        System.out.println("-------------------------");
                    }
                    System.out.println("Inserisci il numero del ristorante di cui vuoi visualizzare le recensioni:");
                    int sceltaRist = IntInput();
                    if(sceltaRist<0 || sceltaRist>ristoDelRistoratore.size()) {
                        System.out.println("Scelta non valida.");
                        break;
                    }
                    Ristorante rScelto = ristoDelRistoratore.get(sceltaRist - 1);
                            try {
                                List<Recensione> recensioni = gestoreRecensione.visualizzaRecensioniPerRistoratore(rScelto);
                                if(recensioni.isEmpty()){
                                    System.out.println("Non ci sono recensioni per questo ristorante.");
                                    break;
                                }
                                int contaRecensioni = 1;
                                for (Recensione rec : recensioni) {
                                    System.out.println(contaRecensioni++ + ": " + rec.toString());
                                }
                                System.out.println("Inserisci numero recensione a cui vuoi rispondere: ");
                                int sceltaRecensione = IntInput();
                                if(sceltaRecensione<0 || sceltaRecensione>recensioni.size()) {
                                    System.out.println("Scelta non valida.");
                                    break;
                                }
                                Recensione recensioneTarget = recensioni.get(sceltaRecensione - 1);
                                System.out.println("Inserisci la tua risposta:");
                                String rispostaRecensione = scanner.nextLine();
                                gestoreRecensione.rispondiRecensione(recensioneTarget, rispostaRecensione);
                                System.out.println("Risposta inviata con successo.");

                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
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
        double latitudine = doubleInput();
        System.out.print("Longitudine: ");
        double longitudine = doubleInput();
        System.out.print("Prezzo Medio: ");
        int prezzoMedio = IntInput();
        System.out.print("Delivery (Si/No): ");
        boolean delivery = siNoInput();
        System.out.print("Prenotazione (Si/No): ");
        boolean prenotazione = siNoInput();
        scanner.nextLine(); // Consumare la nuova linea rimasta
        System.out.print("Tipo di Cucina: ");
        String tipoCucina = scanner.nextLine();
        Double stelle = 0.0; // Nuovo ristorante inizia con 0 stelle
        String nomeProprietario = ristoratoreLoggato.getNome();
        return new Ristorante(nomeControllato, nazione, citta, indirizzo, latitudine, longitudine, prezzoMedio,
                delivery, prenotazione, tipoCucina, stelle, nomeProprietario);
    }

    // 1. PER LE STRINGHE (Nome, Città, ecc.)
    public static String modificaStringa(String messaggio, String vecchioValore) {
        System.out.print(messaggio + " (" + vecchioValore + "): ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            return vecchioValore; // Ritorna il vecchio se premi invio
        }
        return input; // Ritorna il nuovo
    }
    // 2. PER I NUMERI INTERI (Prezzo Medio)
    public static int modificaIntero(String messaggio, int vecchioValore) {
        while (true) {
            System.out.print(messaggio + " (" + vecchioValore + "): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return vecchioValore; // Mantiene il vecchio
            }

            try {
                int nuovoValore = Integer.parseInt(input);
                // Opzionale: controllo se negativo
                if (nuovoValore < 0) {
                    System.out.println("Errore: inserisci un numero positivo.");
                    continue;
                }
                return nuovoValore;
            } catch (NumberFormatException e) {
                System.out.println("Input non valido! Inserisci un numero intero (es: 30).");
            }
        }
    }
    // 3. PER I DOUBLE (Latitudine, Longitudine)
    public static double modificaDouble(String messaggio, double vecchioValore) {
        while (true) {
            System.out.print(messaggio + " (" + vecchioValore + "): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return vecchioValore;
            }

            try {
                // Sostituisce la virgola col punto per sicurezza
                input = input.replace(",", ".");
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Input non valido! Inserisci un numero decimale (es: 45.5).");
            }
        }
    }
    // 4. PER I BOOLEANI (Delivery, Prenotazione - Logica Si/No)
    public static boolean modificaSiNo(String messaggio, boolean vecchioValore) {
        while (true) {
            String labelVecchio = vecchioValore ? "sì" : "no";
            System.out.print(messaggio + " (" + labelVecchio + "): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return vecchioValore;
            }

            if (input.equalsIgnoreCase("si") || input.equalsIgnoreCase("sì")) {
                return true;
            } else if (input.equalsIgnoreCase("no")) {
                return false;
            } else {
                System.out.println("Input non valido! Scrivi 'si' o 'no'.");
            }
        }
    }


}