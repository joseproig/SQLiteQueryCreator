package Model.Query;

import Model.Columna;

public class ColumnaResult {
    private Columna columna;
    private String nomTaula;

    public ColumnaResult(Columna columna, String nomTaula) {
        this.columna = columna;
        this.nomTaula = nomTaula;
    }

    public Columna getColumna() {
        return columna;
    }

    public void setColumna(Columna columna) {
        this.columna = columna;
    }

    public String getNomTaula() {
        return nomTaula;
    }

    public void setNomTaula(String nomTaula) {
        this.nomTaula = nomTaula;
    }

    public String toString () {
        return nomTaula + "." + columna.getColumnName();
    }
}
