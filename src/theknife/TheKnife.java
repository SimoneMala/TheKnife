package theknife;

import theknife.eccezioni.ListaVuotaException;
import theknife.eccezioni.UtenteInesistente;
//libreria per hashing
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.util.*;

/**
 * Classe principale del progetto <code>TheKnife</code>.
 * <p>
 *     Questa classe contiene il metodo statico <code>main</code> che viene invocato dalla JVM all'avvio.
 *     Si occupa di inizializzare i gestori dei dati e di avviare l'interfaccia per l'interazione con il sistema
 * </p>
 * @author Aurora Sarno
 * @author Simone Malacrida
 * @author Greta Giorgetti
 */
public class TheKnife {

    //inizializzo tutti i gestori e scanner all'inizio in modo da poterli usare in tutti i metodi
    private static Scanner scanner;
    private static GestoreRecensione gestoreRecensione;
    private static GestoreRistorante gestoreRistorante;
    private static GestorePreferiti gestorePreferiti;
    private static GestoreUtenti gestoreUtenti;

    /**
     * Metodo principale che avvia l'esecuzione dell'applicazione.
     * <p>
     *     Inizializza l'ambiente di lavoro e istanzia gli oggetti necessari al funzionamento del programma,
     *     per poi passare il controllo al menù principale
     * </p>
     * @param args Argomenti da riga di comando, attualmente non utilizzati
     */
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

    /**
     * Gestisce il menù principale (Home) dell'applicazione
     * <p>
     *     Questo metodo stampa le opzioni disponibili (Login, Registrazione, Accesso Guest e Uscita)
     *     e gestisce la navigazione verso le pagine successive tramite un ciclo continuo (fino alla scelta di uscita)
     *     tramite l'input inserito dall'utente.
     * </p>
     */
    public static void paginaHome(){
        System.out.println("Benvenuto in TheKnife!");
        //scelta dell'operazione
        while(true){
            System.out.println("\n1: Login \n2: Registrazione \n3: Accedi come guest \n4: Esci da TheKnife" );
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
                    System.out.println("Inserire un luogo vicino a te (città):");

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

    /**
     * Gestisce la procedura di Login per un utente già registrato.
     * <p>
     *     In questo metodo viene richiesto di inserire lo username e la password del proprio profilo utente,
     *     se sono corretti l'utente viene indirizzato alla sua pagina (Cliente o Ristoratore)
     * </p>
     */
    public static void paginaLogin() {
        System.out.println("\nLogin");
        System.out.println("Inserire lo username:");
        String username = StringInput();
        System.out.println("Inserire la password:");
        String password = StringInput();
        Utente utente= null;
        boolean successo= true;
        try {
            utente = gestoreUtenti.login(username, password);
        } catch (NullPointerException e){
            System.out.println(e.getMessage());
            successo= false;
        } catch (UtenteInesistente e) {
            System.out.println(e.getMessage());
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
    /**
     * Gestisce la procedura di registrazione per un nuovo utente.
     * <p>
     *     In questo metodo viene richiesto di inserire tutte le informazioni necessarie per registrare un nuovo utente.
     *     All'inserimento del nome viene fatto un controllo per assicurarsi che non esistano altri utenti
     *     con lo stesso username, se esistono viene chiesto un nuovo username.
     *     Per garantire la sicurezza dei dati, la password inserita viene memorizzata sotto forma di hash
     *     generati tramite il metodo <code>hashpw</code> della libreria <code>BCrypt</code>.
     *     Dopo la registrazione l'utente viene indirizzato direttamente alla pagina di login.
     * </p>
     */
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
        System.out.println("Via:");
        String via= StringInput();
        System.out.println("Città:");
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
            System.out.println(e.getMessage());
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

    /**
     * Gestisce la pagina dell'utente non registrato.
     * <p>
     *     Questo metodo stampa le opzioni disponibili (Login, registrazione, visualizzazione di ristoranti vicini,
     *     ricerca dei ristoranti, ritorno alla home) e gestisce la navigazione alle schermate relative
     *     in base all'input dell'utente.
     * </p>
     * @param luogo Il luogo inserito dal guest per eseguire la ricerca di ristoranti vicini.
     */
    public static void paginaGuest(String luogo){
        //stampe per pagina guest
        System.out.println("\nBenvenuto nell'area Guest!");
        //scelta dell'operazione
        while (true){
            System.out.println("\n1: Login \n2: Registrazione \n3: Visualizza ristoranti vicini \n4: Cerca ristoranti \n0: Torna alla home");
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
                            //creo matrice che contiene tutti i ristoranti (nome =0, città= 1) che verrà passata al metodo
                            List<String[]> ristorantiTrovati= stampaRistoranti(ristorantiVicini);
                            Ristorante visto=dettagliRistorante(ristorantiTrovati);
                            if(visto!=null){
                                recensioniAnonime(visto);
                            }
                        }
                    }catch(ListaVuotaException e){
                        System.out.println(e.getMessage());
                    }
                    break;
                //cerca ristoranti
                case 4:
                        ArrayList<Ristorante> ristorantiCercati = (ArrayList<Ristorante>) ricercaConFiltri();
                        if(ristorantiCercati!=null){
                            List<String[]> ristorantiTrovati = stampaRistoranti(ristorantiCercati);
                            Ristorante visto = dettagliRistorante(ristorantiTrovati);
                            if (visto != null) {
                                recensioniAnonime(visto);
                            }
                        }

                    break;
                case 0:
                    return;
                default:
                    System.out.println("Input non valido, inserire un intero che si riferisce a una delle operazioni descritte");
            }
        }
    }

    /**
     * Gestisce la visualizzazione delle recensioni in forma anonima (da parte del guest)
     * <p>
     *     Il metodo richiede se l'utente vuole visualizzare le recensioni del ristorante, in caso positivo
     *     cerca le recensioni associate al ristorante scelto tramite il metodo <code>visualizzaRecensioni</code> e,
     *     se trovate, stampa il testo, le stelle e l'eventuale risposta, ma non lo username dell'utente che l'ha scritta.
     * </p>
     * @param ristorante Il ristorante di cui si chiede se visualizzare le recensioni.
     */
    public static void recensioniAnonime(Ristorante ristorante){
        System.out.println("Vuoi visualizzare le recensioni del ristorante " + ristorante.getNome() + "? (si o no)");
        boolean scelta= siNoInput();
        ArrayList<Recensione> recensioniRistorante;
        if(scelta){
            try {
                System.out.println("Recensioni per il ristorante " + ristorante.getNome());
                recensioniRistorante = (ArrayList<Recensione>) gestoreRecensione.visualizzaRecensioniPerRistoratore(ristorante);
                if(recensioniRistorante.isEmpty()){
                    System.out.println("Non ci sono recensioni per questo ristorante.");
                    return;
                }
                int count = 1;
                for (Recensione r : recensioniRistorante) {
                    if (r.getRispostaRecensione() != null) {
                        System.out.println(count++ + "\nTesto:" + r.getTesto() + "\nStelle:" + r.getStelle() + "\nRisposta" + r.getRispostaRecensione());
                    } else {
                        System.out.println(count++ + "\nTesto:" + r.getTesto() + "\nStelle:" + r.getStelle());
                    }
                    System.out.println("-------------------------");
                }
            }catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Permette di visualizzare uno dei ristoranti della lista passata.
     * <p>
     *     Il metodo chiede all'utente se vuole vedere le informazioni di uno dei ristoranti della lista passata,
     *     in caso positivo vengono stampati tutti i nomi e città dei ristoranti, numerati, l'utente potrà scegliere
     *     quale visualizzare.
     * </p>
     * @param ristoranti I ristoranti disponibili alla visualizzazione.
     * @return Il ristorante scelto per la visualizzazione.
     */
    public static Ristorante dettagliRistorante(List<String[]> ristoranti){
        System.out.println("Vuoi vedere le informazioni di uno di questi ristoranti (Digita 1 o 2)?");
        System.out.println("1: Sì \n2: No");
        do {
            int scelta = IntInput();
            switch (scelta) {
                case 1:
                    int max = ristoranti.size();
                    System.out.println("Digita il numero che si riferisce al ristorante di cui vuoi informazioni:");
                    boolean corretto = true;
                    int ristoranteScelto;
                    do {
                        ristoranteScelto = IntInput();
                        if (ristoranteScelto > max) {
                            System.out.println("Il numero scelto non corrisponde a un ristorante, controlla la lista di ristoranti e riprova:");
                            corretto = false;
                        } else {
                            corretto = true;
                        }
                    } while (!corretto);

                    try {
                        //recupero nome e città ristorante
                        String[] riga = ristoranti.get(ristoranteScelto - 1);
                        Ristorante scelto = gestoreRistorante.visualizzaRistorante(riga[0], riga[1]);
                        //bisogna ancora aggiungere il toString di ristoranti
                        System.out.println(scelto.toString());
                        return scelto;
                    }catch(NullPointerException e){
                        System.out.println(e.getMessage());
                    }
                case 2:
                    return null;
                default:
                    System.out.println("Il numero inserito non corrisponde a nessuna scelta, riprova");

            }
        }while(true);
    }

    /**
     * Gestisce la ricerca con filtri dei risoranti
     * <p>
     *     Il metodo richiede l'inserimento dei filtri che si vogliono aggiungere alla ricerca (Città, tipologia di
     *     cucina, prezzo massimo, prezzo minimo, stelle minime, presenza del servizio di delivery e della prenotazione
     *     online).
     *     Viene eseguito un controllo sulla tipologia di cucina per vedere se esistono ristoranti con quella tipologia,
     *     in caso contrario viene richiesto all'utente se vuole usare una nuova tipologia o andare avanti senza.
     *     Inseriti i filtri vengono cercati i ristoranti tramite il metodo <code>cercaRistoranti</code>.
     * </p>
     * @return La lista di ristoranti che contiene i ristoranti filtrati.
     */
    public static List<Ristorante> ricercaConFiltri(){
        System.out.println("Inserisci i filtri che vuoi aggiungere, se non vuoi aggiungerli premi invio");
        System.out.println("Filtro città (obbligatorio, non puoi lasciarlo vuoto):");
        boolean cittaGiusto = false;
        String citta="";
        while(!cittaGiusto) {
            citta= scanner.nextLine();
            if(!citta.isEmpty()){
                cittaGiusto = true;
            }else{
                System.out.println("La città è obbligatoria, non puoi lasciarla vuota!");
            }
        }
        System.out.println("Filtro tipo cucina (inserisci il nome del tipo cucina che vuoi):");
        String tipologiaCucina= scanner.nextLine();
        boolean continua=true;
        while(continua) {
            if (tipologiaCucina.isEmpty()){
                continua=false;
            }else {
                ArrayList<String> tipiCucina = (ArrayList<String>) gestoreRistorante.getTipiCucinaLista();
                if (!tipiCucina.contains(tipologiaCucina)) {
                    System.out.println("La tipologia scritta non è presente tra i ristoranti, vuoi provare a inserirne un altra?(si o no)");
                    boolean scelta = siNoInput();
                    if (scelta) {
                        System.out.println("Prova a inserirla in inglese! Altrimenti inseriscine un altra:");
                        tipologiaCucina = StringInput();
                    } else {
                        tipologiaCucina="";
                        continua = false;
                    }
                } else
                    continua = false;
            }
        }
        System.out.println("I prezzi dei ristoranti sono prezzi medi, in cui un numero identifica quanto spenderai");
        System.out.println("Filtro prezzo massimo (inserisci un numero):");
        Double prezzoMassimo= doubleInputOrNull();
        System.out.println("Filtro prezzo minimo (inserisci un numero):");
        Double prezzoMinimo= doubleInputOrNull();
        System.out.println("Filtro stelle (inserisci il numero minimo di stelle che il ristorante deve avere)");
        Double stelleMinimo= doubleInputOrNull();
        System.out.println("Filtro delivery (inserisci si o no se vuoi che sia presente o meno il servizio):");
        Boolean delivery= booleanInputOrNull();
        System.out.println("Filtro prenotazione online (inserisci si o no se vuoi che sia presente o meno il servizio)");
        Boolean prenotazione= booleanInputOrNull();
        if(tipologiaCucina.equals("")){
            tipologiaCucina=null;
        }
        try {
            return gestoreRistorante.cercaRistoranti(tipologiaCucina, citta, prezzoMinimo, prezzoMassimo, delivery, prenotazione, stelleMinimo);
        }catch(ListaVuotaException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Metodo di stampa del nome e della città dei ristoranti appartenenti alla lista passata
     * <p>
     *     Il metodo permette di stampare solo i nomi e le città dei ristoranti passati per permettere
     *     una visualizzazione più efficace della lista.
     * </p>
     * @param ristoranti La lista di ristoranti passati.
     * @return La lista che contiene i nomi e le città dei ristoranti stampati.
     */
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

    /**
     * Gestisce il controllo dell'input.
     * <p>
     *     Il metodo gestisce il controllo dell'input, in modo non sia possibile lasciarlo vuoto andando a capo,
     *     attraverso un ciclio while che controlla se lo scanner è rimasto vuoto.
     * </p>
     * @return La <code>String</code> che contiene l'input dell'utente
     */
    public static String StringInput(){
        String input= scanner.nextLine();
        while (input.isEmpty()) {
            System.out.println("Input non valido, non puoi andare a capo lasciando vuoto!");
            input=scanner.nextLine();
        }
        return input;
    }
    //controllo che sia un intero

    /**
     * Gestisce il controllo dell'input per interi.
     * <p>
     *     Il metodo non permette di scrivere input diversi da interi attraverso un ciclo while che controlla
     *     se lo scanner ha un intero al suo interno.
     * </p>
     * @return L'<code>int</code> contenuto nell'input.
     */
    public static int IntInput(){
        while(!scanner.hasNextInt()) {
            System.out.println("Input non valido \nInserire un numero senza virgola!");
            scanner.next();
        }
        int numero= scanner.nextInt();
        scanner.nextLine();
        return numero;
    }

    /**
     * Gestisce il controllo dell'input per i double.
     * <p>
     *     Il metodo non permette di scrivere input diversi da double attraverso un ciclo while che controlla se lo
     *     scanner ha un double al suo interno.
     * </p>
     * @return Il <code>double</code> contenuto nell'input.
     */
    public static double doubleInput(){
        while (!scanner.hasNextDouble()) {
            System.out.println("Input non valido \nInserire un numero!");
            scanner.next();
        }
        double numero= scanner.nextDouble();
        scanner.nextLine();
        return numero;
    }

    /**
     * Gestisce il controllo dell'input per double in cui è permesso lasciare l'input vuoto.
     * <p>
     *     Il metodo permette di inserire solo double o lasciare vuoto l'input, tramite un ciclo while che controlla
     *     se è rimasto vuoto l'input o se contiene un double.
     * </p>
     * @return Il <code>Double</code> di valore <code>null</code> se l'input è rimasto vuoto,
     * altrimenti con il valore <code>double</code>.
     */
    public static Double doubleInputOrNull(){
        while (true){
            String input= scanner.nextLine().trim();
            if(input.isEmpty()){
                return null;
            }
            try{
                return Double.parseDouble(input);
            }catch(NumberFormatException e){
                System.out.println("Input non valido \nInserire un numero (es. 3.5) oppure premere invio per saltare!");
            }
        }
    }

    /**
     * Gestisce il controllo dell'input per interi in cui è permesso lasciare l'input vuoto.
     * <p>
     *     Il metodo permette di inserire solo interi o lasciare vuoto l'input, tramite un ciclo while che controlla
     *     se è rimasto vuoto l'input o se contiene un interi.
     * </p>
     * @return L'<code>Integer</code> di valore <code>null</code> se l'input è rimasto vuoto,
     * altrimenti con il valore <code>int</code>.
     */
    public static Integer intInputOrNull(){
        while (true){
            String input= scanner.nextLine().trim();
            if(input.isEmpty()){
                return null;
            }
            try{
                return Integer.parseInt(input);
            }catch(NumberFormatException e){
                System.out.println("Input non valido \nInserire un numero senza virgola oppure premere invio per saltare!");
            }
        }
    }

    /**
     * Gestisce il controllo dell'input per booleani in cui è permesso lasciare l'input vuoto.
     * <p>
     *     Il metodo permette di inserire solo <code>String</code> equivalenti a "si" o "no" o lasciare vuoto l'input,
     *     tramite un ciclo while che controlla se è rimasto vuoto l'input o se contiene la <code>String</code> corretta.
     * </p>
     * @return Il <code>Boolean</code> di valore <code>null</code> se l'input è rimasto vuoto,
     * con valore <code>true</code> se la <code>String</code> valeva "si", <code>false</code> se ha valore "no".
     */
    public static Boolean booleanInputOrNull(){
        while (true){
            String input= scanner.nextLine().trim();
            if(input.isEmpty()){
                return null;
            }
            if(input.equalsIgnoreCase("si") || input.equalsIgnoreCase("sì")){
                return true;
            }else if(input.equalsIgnoreCase("no")){
                return false;
            }else{
                System.out.println("Input non valido \nInserire si o no oppure premere invio per saltare!");
            }

        }
    }

    /**
     * Gestisce il controllo dell'input per booleani.
     * <p>
     *     Il metodo permette di inserire nell'input <code>String</code> con il valore "si" o "no",
     *     tramite un ciclo while che controlla il valore della <code>String</code>.
     * </p>
     * @return <code>true</code> se l'input contiene la <code>String</code> con valore si,
     * <code>false</code> se contiene no.
     */
    public static boolean siNoInput(){
        do{
            String scelta= scanner.nextLine();
            if (scelta.equalsIgnoreCase("si") || scelta.equalsIgnoreCase("sì")){
                return true;
            }else if (scelta.equalsIgnoreCase("no")){
                return false;
            }else{
                System.out.println("Input non valido \nInserire o si o no!");
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

            System.out.println("1. Visualizza dati personali");
            System.out.println("2. Modifica dati personali");
            System.out.println("3. Visualizza ristoranti vicino a me");
            System.out.println("4. Cerca ristoranti per filtro");
            System.out.println("5. Visualizza le tue recensioni");
            System.out.println("6. Visualizza i tuoi ristoranti preferiti");
            System.out.println("0. Torna alla home");

            System.out.println("Seleziona un'opzione:");
            scelta = IntInput();

            switch (scelta) {
                //visualizzazione i dati utente
                case 1:
                    System.out.println("---Ecco i tuoi dati personali--");
                    System.out.println("Nome:" + u.getNome() + "\nCognome:" + u.getCognome() + "\nUsername:"
                            + u.getUsername() + "\nVia:" + u.getVia() + "\nCitta:" + u.getCitta());
                    System.out.println("-------------------------");
                    break;

                //modifica dati utente
                case 2:
                    String nuovoNome = gestoreRecensione.gestisciInputOpzionale("Inserisci il nuovo nome (PREMERE INVIO PER NON MODIFICARE): ", scanner, true);
                    String nuovoCognome = gestoreRecensione.gestisciInputOpzionale("Inserisci il nuovo cognome (PREMERE INVIO PER NON MODIFICARE): ", scanner, true);
                    String nuovaVia = gestoreRecensione.gestisciInputOpzionale("Inserisci la nuova via (PREMERE INVIO PER NON MODIFICARE): ", scanner, true);
                    String nuovaCitta = gestoreRecensione.gestisciInputOpzionale("Inserisci la nuova citta (PREMERE INVIO PER NON MODIFICARE): ", scanner, true);

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
                    System.out.println("-------------------------");
                    break;

                //visualizzazione ristoranti vicini
                case 3:
                    try {
                        ArrayList<Ristorante> ristorantiVicini = (ArrayList<Ristorante>) gestoreRistorante.cercaRistoranti
                                (null, u.getCitta(), null, null, false, false, null);
                        if (!ristorantiVicini.isEmpty()) {
                            System.out.println("Ecco i ristoranti che si trovano vicino a te:");

                            List<String[]> ristorantiTrovati = stampaRistoranti(ristorantiVicini);
                            Ristorante visto = dettagliRistorante(ristorantiTrovati);

                            if (visto != null){
                                ulterioriInformazioni(u, visto);
                            }

                        }
                    } catch (ListaVuotaException e) {
                        System.out.println("Non ci sono ristoranti vicino a te.");
                    }
                    break;

                //ricerca ristoranti con filtri
                case 4:
                    ArrayList<Ristorante> ristorantiCercati = (ArrayList<Ristorante>) ricercaConFiltri();
                    if(ristorantiCercati!=null){
                        List<String[]> ristorantiTrovati = stampaRistoranti(ristorantiCercati);
                        Ristorante visto = dettagliRistorante(ristorantiTrovati);
                        if (visto != null) {
                            ulterioriInformazioni(u, visto);
                        }
                    }
                    break;

                //visualizza recensioni
                case 5:
                    try {
                        List<Recensione> recensioniUtente = gestoreRecensione.visualizzaRecensioniperUtente(u);
                        int cont = 1;
                        for (Recensione rec : recensioniUtente) {
                            System.out.println(cont++ + ") " + rec.toString());
                        }

                        System.out.println("1 per modificare, 2 per eliminare, 0 per uscire:");
                        int sceltaOperazione = IntInput();
                        if (sceltaOperazione == 0) {
                            System.out.println("Uscita in corso...");
                            break;
                        }
                        System.out.println("Scegli il numero della recensione:");
                        int sceltaRecensione = IntInput();


                        if (sceltaRecensione > 0 && sceltaRecensione <= recensioniUtente.size()) {
                            Recensione recensioneSelezionata = recensioniUtente.get(sceltaRecensione - 1);

                            if (sceltaOperazione == 1) {
                                System.out.println("Nuovo testo (Invio per non modificare):");
                                String testoModificato = scanner.nextLine();

                                System.out.println("Nuove stelle (es. 1-5):");
                                String stelleInput = scanner.nextLine();
                                int stelleModificate = stelleInput.isEmpty() ? recensioneSelezionata.getStelle() : Integer.parseInt(stelleInput);

                                gestoreRecensione.modificaRecensione(recensioneSelezionata, testoModificato, stelleModificate, u);
                                modificaStelleRistorante(recensioneSelezionata.getNomeRistorante());
                                System.out.println("Modificata!");
                            } else if (sceltaOperazione == 2) {
                                gestoreRecensione.eliminaRecensione(recensioneSelezionata);
                                modificaStelleRistorante(recensioneSelezionata.getNomeRistorante());
                                System.out.println("Eliminata!");
                            }
                        } else {
                            System.out.println("Selezione non valida.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Non hai recensioni.");
                    }
                    break;

                case 6:
                    try {
                        List<String[]> ristorantiScelti = stampaRistoranti(PreferitiInRistorante(gestorePreferiti.visualizzaPreferiti(u)));
                        Ristorante scelto = dettagliRistorante(ristorantiScelti);
                        if (scelto != null) {
                            ulterioriInformazioni(u, scelto);
                        }
                    } catch (ListaVuotaException e) {
                        System.out.println("Non hai ristoranti preferiti.");
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

    /**
     * Gestisce il cambio della media delle stelle del ristorante passato.
     * <p>
     *     Il metodo cambia la media delle stelle del ristorante passato, attraverso il metodo
     *     <code>visualizzaRecensioni</code> vengono prese le recensioni associate al ristorante.
     *     Viene eseguito il calcolo della media nuova da inserire tramite un ciclo for che scorre tutte le recensioni.
     *     Per apportare le modifiche anche al file JSON viene chiamato il metodo <code>modificaFileJsonRistoranti</code>.
     * </p>
     * @param nomeRistorante Il nome del ristorante a cui cambiare la media di stelle.
     */
    public static void modificaStelleRistorante(String nomeRistorante){
        List<Ristorante> ristoranti = gestoreRistorante.getElencoRistoranti();
        Ristorante risto= null;
        for (Ristorante r : ristoranti) {
            if (r.getNome().equalsIgnoreCase(nomeRistorante)) {
                risto= r;
            }
        }
        double nuovaMedia=0.0;
        if(risto!=null) {
            try {
                List<Recensione> recensioniRistorante = gestoreRecensione.visualizzaRecensioniPerRistoratore(risto);
                if (recensioniRistorante != null && !recensioniRistorante.isEmpty()) {
                    double somma = 0;
                    double cont = 0;
                    for (Recensione rec : recensioniRistorante) {
                        somma += rec.getStelle();
                        cont++;
                    }
                    nuovaMedia=somma/cont;
                }
                risto.setStelle(nuovaMedia);
                gestoreRistorante.modificaFileJsonRistoranti(ristoranti);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public static List<Ristorante> PreferitiInRistorante (List<Preferito> ristorantiPreferiti) {
        List<Ristorante> preferitiInRistorante = new ArrayList<>();
        List<Ristorante> tuttiIRistoranti = gestoreRistorante.getElencoRistoranti();
        for (Preferito p : ristorantiPreferiti) {
            for (Ristorante r : tuttiIRistoranti) {
                if (p.getNomeRist().equalsIgnoreCase(r.getNome())) {
                    preferitiInRistorante.add(r);
                }
            }
        }
            return preferitiInRistorante;
    }


    public static void ulterioriInformazioni (Utente u, Ristorante visto){

            int sceltaInterna;

            while (true) {
                System.out.println("Scegli un'opzione: \n1. Lascia una recensione \n2. Inserisci il ristorante tra i preferiti" +
                        " \n3. Rimuovi il ristorante dai preferiti" +
                        " \n4. Visualizza tutte le recensioni lasciate al ristorante \n5. Esci dalla visualizzazione");

                System.out.println("Operazione scelta:");
                sceltaInterna = IntInput();

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
                        modificaStelleRistorante(visto.getNome());
                        System.out.println("Recensione inserita con successo!");
                        break;

                    //aggiungi ai preferiti
                    case 2:
                        try {
                            gestorePreferiti.aggiungiPreferiti(u, visto);
                            System.out.println("Ristorante inserito con successo!");

                        } catch (NullPointerException e) {
                            System.out.println(e.getMessage());
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    case 3:
                        try {
                            gestorePreferiti.cancellaPreferiti(u, visto);
                            System.out.println("Ristorante rimosso con successo!");
                        } catch (NullPointerException e) {
                            System.out.println(e.getMessage());
                        } catch (ListaVuotaException e) {
                            System.out.println(e.getMessage());
                        }
                        break;

                    //visualizza riepilogo recensioni ristorante
                    case 4:
                        try {
                            System.out.println("Ecco le recensioni del ristorante:");
                            List<Recensione> recs = gestoreRecensione.visualizzaRecensioniPerRistoratore(visto);
                            if(recs.isEmpty()){
                                System.out.println("Non ci sono recensioni per questo ristorante.");
                                break;
                            }
                        for (Recensione r : recs) {
                            System.out.println(r + " ");
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
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

    /**
     * Gestisce il menu e le operazioni specifiche per l'utente con ruolo Ristoratore.
     * <p>
     *     Attraverso questo metodo, il ristoratore può aggiungere nuovi ristoranti, visualizzare e modificare i propri ristoranti,
     *     visualizzare il riepilogo delle recensioni, rispondere alle recensioni dei clienti.
     * </p>
     * @param ristoratoreLoggato L'utente attualmente loggato con ruolo Ristoratore.
     */
    public static void paginaRistoratore(Utente ristoratoreLoggato){
        int scelta;
        System.out.println("Benvenuto " + ristoratoreLoggato.getNome() + " nell'area Ristoratore!");
        do {
            //stampe per pagina ristoratore
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
                    System.out.println("Se non vuoi modificare un informazione premi invio");
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

    /**
     * Raccoglie gli input dell'utente per creare un nuovo oggetto Ristorante.
     * Richiede all'utente di inserire vari dettagli come nazione, città, indirizzo, latitudine, longitudine,
     * prezzo medio, disponibilità di delivery e prenotazione, tipo di cucina, e assegna 0 stelle iniziali.
     * @param ristoratoreLoggato L'utente ristoratore che sta creando il ristorante.
     * @param nomeControllato Il nome del ristorante da creare, già controllato per evitare duplicati.
     * @return Un nuovo oggetto Ristorante inizializzato con i dati forniti dall'utente Ristoratore.
     */
    public static Ristorante datiRistorante(Utente ristoratoreLoggato, String nomeControllato) {
        System.out.print("Nazione: ");
        String nazione = StringInput();
        System.out.print("Città: ");
        String citta = StringInput();
        System.out.print("Indirizzo: ");
        String indirizzo = StringInput();
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
        System.out.print("Tipo di Cucina: ");
        String tipoCucina = StringInput();
        Double stelle = 0.0; // Nuovo ristorante inizia con 0 stelle
        String nomeProprietario = ristoratoreLoggato.getNome();
        return new Ristorante(nomeControllato, nazione, citta, indirizzo, latitudine, longitudine, prezzoMedio,
                delivery, prenotazione, tipoCucina, stelle, nomeProprietario);
    }

    // 1. PER LE STRINGHE (Nome, Città, ecc.)

    /**
     * Permette la modifica di un campo stringa tramite input da tastiera.
     * Se l'utente preme invio senza inserire nulla, viene mantenuto il originale.
     * @param messaggio Campo del ristorante da modificare (es. "Nome", "Città").
     * @param vecchioValore Valore attuale del campo.
     * @return La nuova stringa inserita dall'utente oppure il vecchio valore.
     */
    public static String modificaStringa(String messaggio, String vecchioValore) {
        System.out.print(messaggio + " (" + vecchioValore + "): ");
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            return vecchioValore; // Ritorna il vecchio se premi invio
        }
        return input; // Ritorna il nuovo
    }

    // 2. PER I NUMERI INTERI (Prezzo Medio)

    /**
     * Permette la modifica di un valore intero tramite input da tastiera.
     * Continua a richiedere l'input finché non viene inserito un intero valido o si preme invio per mantenere il vecchio valore.
     * @param messaggio Campo del ristorante da modificare (es. "Prezzo Medio").
     * @param vecchioValore Valore attuale del campo.
     * @return Il nuovo intero inserito dall'utente oppure il vecchio valore.
     */
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

    /**
     * Permette la modifica di un valore double tramite input da tastiera.
     * Gestisce sia la virgola che il punto come separatori decimali. Con anche controllo finché non venga inserito un double valido
     * o si preme invio per mantenere il vecchio valore.
     * @param messaggio Campo del ristorante da modificare (es. "Latitudine", "Longitudine").
     * @param vecchioValore Valore attuale del campo.
     * @return Il nuovo double inserito dall'utente oppure il vecchio valore.
     */
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

    /**
     * Permette la modifica di un valore booleano tramite input testuale "si"/"no".
     * Continua a richiedere l'input finché non viene inserito un valore valido o si preme invio per mantenere il vecchio valore.
     * @param messaggio Campo del ristorante da modificare (es. "Delivery", "Prenotazione").
     * @param vecchioValore Valore attuale del campo.
     * @return Il nuovo booleano inserito dall'utente oppure il vecchio valore.
     */
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