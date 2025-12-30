package theknife;

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
        gestoreRecensione= new GestoreRecensione("Recensioni.json")

        //chiamo la pagina home, inizio dell'applicazione
        paginaHome();

        //quando pagina home finisce, quindi quando scelgo l'opzione esci, faccio la chiusura applicazione
        System.out.println("Chiusura dell'applicazione, a presto!");
        scanner.close();
        System.exit(0);

    }

    public static void paginaHome(){
        //stampe di benvenuto

        //parte il ciclo:
        //stampe di scelta dell'operazione: login, registrazione, entrare come guest, chiudere l'applicazione

        //posso creare 2 pagine a parte che gestiscono login e registrazione così le riutilizzo anche per guest
        /*switch case, in base alla scelta:
        -login= chiede username e password, fa i controlli che noon esista username, fa hash della password,
        poi entra nella pagina di Cliente o Ristoratore in base a quale ruolo ha (chiamata a quel metodo)
        -registrazione= chiede tutti i valori necessari, fa i controlli, hash password, poi entra nella pagina di
        Cliente o Ristoratore in base a quale ruolo ha
        -guest= chiede di inserire una string del luogo, poi chiama pagina del guest
        -chiusura= return
        */
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