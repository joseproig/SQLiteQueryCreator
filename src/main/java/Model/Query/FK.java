package Model.Query;

public class FK {
    private String nomOrigen;
    private String nomDesti;

    public FK(String nomOrigen, String nomDesti) {
        this.nomOrigen = nomOrigen;
        this.nomDesti = nomDesti;
    }

    public String getNomOrigen() {
        return nomOrigen;
    }

    public void setNomOrigen(String nomOrigen) {
        this.nomOrigen = nomOrigen;
    }

    public String getNomDesti() {
        return nomDesti;
    }

    public void setNomDesti(String nomDesti) {
        this.nomDesti = nomDesti;
    }
}
