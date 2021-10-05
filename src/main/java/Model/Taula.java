package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Taula {
    private int id;
    private String nomTaula;
    private HashMap<String,Columna> columnes;
    private List<Columna> foreignKeys;

    public Taula(int id, String nomTaula, HashMap<String, Columna> columnes, List<Columna> foreignKeys) {
        this.id = id;
        this.nomTaula = nomTaula;
        this.columnes = columnes;
        this.foreignKeys = foreignKeys;
    }

    public List<Columna> getForeignKeys() {
        return foreignKeys;
    }

    public void setForeignKeys(List<Columna> foreignKeys) {
        this.foreignKeys = foreignKeys;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomTaula() {
        return nomTaula;
    }

    public void setNomTaula(String nomTaula) {
        this.nomTaula = nomTaula;
    }

    public HashMap<String, Columna> getColumnes() {
        return columnes;
    }

    public void setColumnes(HashMap<String, Columna> columnes) {
        this.columnes = columnes;
    }

    private void addNewFK (Columna columna) {
        foreignKeys.add(columna);
    }


}
