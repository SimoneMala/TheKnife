package theknife;

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
    public String getNomeRist(){
        return nomeRist;
    }

    //metodi setter
    public void setUsername(String username){
        this.username = username;
    }
    public void setnomeRist(String nomeRist){
        this.nomeRist= nomeRist;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Preferito that = (Preferito) o;
        return that.username.equals(this.username) && that.nomeRist.equals(this.nomeRist);
    }

}
