package Model.Query.WhereFolder;

public class ColumnWhere extends WhereOperand {
    private String nomTaula;
    private String columna;
    private String nomColumnaEnunciat;
    private String nomTaulaEnunciat;

    public ColumnWhere(String nomTaula, String columna, String nomColumnaEnunciat, String nomTaulaEnunciat) {
        super("ColumnWhere");
        this.nomTaula = nomTaula;
        this.columna = columna;
        this.nomColumnaEnunciat = nomColumnaEnunciat;
        this.nomTaulaEnunciat = nomTaulaEnunciat;
    }

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

    public String getNomColumnaEnunciat() {
        return nomColumnaEnunciat;
    }

    public void setNomColumnaEnunciat(String nomColumnaEnunciat) {
        this.nomColumnaEnunciat = nomColumnaEnunciat;
    }

    public String getNomTaulaEnunciat() {
        return nomTaulaEnunciat;
    }

    public void setNomTaulaEnunciat(String nomTaulaEnunciat) {
        this.nomTaulaEnunciat = nomTaulaEnunciat;
    }

    @Override
    public String toString() {
        return nomTaula + "." + columna;
    }
}
