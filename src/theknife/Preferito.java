package theknife;

/**
 * Rappresenta la relazione tra l'utente e il ristorante salvato nei preferiti.
 * <p>
 *     Questa classe viene utilizzata per salvare per ogni utente i suoi ristoranti preferiti.
 * </p>
 * @author Aurora Sarno.
 */
public class Preferito {
    /**
     * Lo username del cliente che ha salvato il ristorante tra i preferiti.
     */
    private String username;
    /**
     * Il nome del ristorante che è stato salvato nei preferiti.
     */
    private String nomeRist;

    /**
     * Costruisce il nuovo oggetto preferito.
     * @param username Lo username del cliente.
     * @param nomeRist Il nome del ristorante.
     */
    public Preferito(String username, String nomeRist){
        this.username = username;
        this.nomeRist = nomeRist;
    }

    /**
     * Restituisce lo username del cliente.
     * @return Lo username associato al preferito.
     */
    public String getUsername(){
        return username;
    }

    /**
     * Restituisce il nome del ristorante.
     * @return Il nome del ristorante associato al preferito.
     */
    public String getNomeRist(){
        return nomeRist;
    }

    /**
     * Cambia lo username associato al preferito.
     * @param username Il nuovo valore da assegnare a username.
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * Cambia il nome del ristorante associato al preferito.
     * @param nomeRist Il nuovo valore da assegnare a nomeRist.
     */
    public void setnomeRist(String nomeRist){
        this.nomeRist= nomeRist;
    }

    /**
     * Verifica l'uguaglianza tra l'oggetto passato e l'oggetto chiamante.
     * <p>
     *     Due oggetti <code>Preferito</code> sono considerati uguali se: hanno lo stesso indicatore;
     *     hanno lo stesso <code>username</code> e stesso <code>nomeRist</code>.
     * </p>
     * @param o   L'oggetto da confrontare con l'oggetto chiamante.
     * @return <code>true</code> se gli oggetti sono uguali, <code>false</code> altrimenti.
     */
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Preferito that = (Preferito) o;
        return that.username.equals(this.username) && that.nomeRist.equals(this.nomeRist);
    }

}
