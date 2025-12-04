import java.util.ArrayList;
import java.util.List;

public class GestorePreferiti {

    //campi
    private List<Preferito> ristorantiPreferiti;

    //costruttore a cui poi aggiungere il metodo carica() che carica dal file json i ristoranti nella lista
    public GestorePreferiti(){
        ristorantiPreferiti = new ArrayList<Preferito>();
    }

    //metodo get
    public List<Preferito> getRistorantiPreferiti() {
        return ristorantiPreferiti;
    }

    //metodo che aggiunge un ristorante alla lista di preferiti inerente a un utente
    //nel main controllo che utente e ristorante esistono
    public void aggiungiPreferiti(Utente utente, Ristorante ristorante) throws NullPointerException{
        if (utente==null || ristorante==null){
            throw new NullPointerException("Utente o ristorante non corretti");
        }
        Preferito p= new Preferito(utente.getUsername(), ristorante.getNome());

        this.ristorantiPreferiti.add(p);

    }

    //metodo per eliminare un ristorante dalla mia lista
    public void cancellaPreferiti(Utente utente, Ristorante ristorante) throws NullPointerException{
        if (utente==null || ristorante==null){
            throw new NullPointerException("Utente o ristorante non corretti");
        }
        if (this.ristorantiPreferiti.size()==0){
            throw new NullPointerException("La lista preferiti è già vuota");
        }
        Preferito p= new Preferito(utente.getUsername(), ristorante.getNome());
        for(Preferito p1: this.ristorantiPreferiti){
            if(p1.equals(p)){
                this.ristorantiPreferiti.remove(p);
            }
        }
    }

    //per visualizzare la lista nel main la stampo
}
