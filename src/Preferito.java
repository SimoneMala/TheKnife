public class Preferito {
    //campi
    private String username;
    private String nomeRist;

    //costruttore
    public Preferito(String username, String nomeRist){
        this.username = username;
        this.nomeRist = nomeRist;
    }

    //metodi getter
    public String getUsername(){
        return username;
    }
    public String nomeRist(){
        return nomeRist;
    }

    //metodi setter
    public void setUsername(String username){
        this.username = username;
    }
    public void setnomeRist(String nomeRist){
        this.nomeRist= nomeRist;
    }

}
