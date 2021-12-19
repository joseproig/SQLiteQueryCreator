package Model.Query.WhereFolder;

public class ColumnWhere extends WhereOperand {
    private String nomTaula;
    private String columna;

    public ColumnWhere(String nomTaula, String columna) {
        super("ColumnWhere");
        this.nomTaula = nomTaula;
        this.columna = columna;
    }

    public String getNomTaula() {
        return nomTaula;
    }

    public void setNomTaula(String nomTaula) {
        this.nomTaula = nomTaula;
    }

    public String getColumna() {
        return columna;
    }

    public void setColumna(String columna) {
        this.columna = columna;
    }

    @Override
    public String toString() {
        return nomTaula + "." + columna;
    }
}
