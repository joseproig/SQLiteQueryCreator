package Controller.Logic;

import Model.*;
import Model.Query.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MakeFromState extends State{
    //TODO: Doble relacions entre dos taules --> Ex: Treballador <--> Departament
    //TODO: Evitar for de for (Columna c : t1.getForeignKeys()) {
    //TODO: Select amb una classe, no crear string directament



    @Override
    void doYourFunction(String string) throws IOException {
        List <Select> results = new ArrayList<>();
        System.out.println("----------------------------Pregunta " + (Integer.parseInt(string) + 1) + "--------------------------------------");
        for (Long key:TablesData.getInstance().getCaminsPossiblesSolucions().keySet()) {
            //ArrayList<TableLayer> arrayList = new ArrayList<>();
            //arrayList.add(TablesData.getInstance().getCaminsPossiblesSolucions().get(key).getConnections());
            //String result = makeQuery("SELECT * FROM ", arrayList) + " ;";
            TableLayer tableLayer = TablesData.getInstance().getCaminsPossiblesSolucions().get(key).getConnections();
            TableFrom tableFrom = new TableFrom("t" + 0, TablesData.getInstance().getTaulesById().get(tableLayer.getTableNum()).getNomTaula());
            makeFrom(tableLayer,new IntegerRef(1),tableFrom);
            From from = new From(tableFrom);

            List<ColumnaResult> columnesPossibles = columnsOfOneTable(from.getTableFrom().getTableRealName(),from.getTableFrom().getTableName());
            returnSelectColumns(columnesPossibles,from.getTableFrom());

            //TODO: Fer combinacions en bytes en una nova funcio i definir RESULTAT
            generateDifferentCombinations (results, columnesPossibles, from, Integer.parseInt(string));

            //System.out.println(result.toString());
        }
        System.out.println("----------------------------------------------------------------------------");
        TablesData.getInstance().setPossibleQueries(results);

        int numOfQuestion = Integer.parseInt(string) + 1;
        if (numOfQuestion == ProgramConfig.getInstance().getFilterParams().getQuestions().size()) {
            context.changeState(new GenerateXML());
        } else {
            context.changeState(new FilterTables());
            string = "" + numOfQuestion;
        }
        context.doStateFunction(string);
    }

    private void generateDifferentCombinations (List <Select> results, List<ColumnaResult> columnesPossibles, From from, int actualQuestion) {
        int numberOfColumnsToShow = ProgramConfig.getInstance().getFilterParams().getQuestions().get(actualQuestion).getStructure().getColumnsToSeeInSelect().size();
        HashMap<Integer, Boolean> isPresent = new HashMap<>();
        int possibleCombination = 0;
        for (int combinations = 0; combinations <= Math.pow(2,columnesPossibles.size());combinations++) {
            HashMap<String, ColumnaResult> columns = new HashMap<>();
            int i = 0;
            int j = 0;
            while((j != numberOfColumnsToShow) && (i < columnesPossibles.size())) {
                if ((combinations >> i & 1) == 1) {
                    possibleCombination = (possibleCombination | (1 << i));
                    columns.put(ProgramConfig.getInstance().getFilterParams().getQuestions().get(actualQuestion).getStructure().getColumnsToSeeInSelect().get(j), columnesPossibles.get(i));
                    j++;
                }
                i++;
            }
            if (j == numberOfColumnsToShow && isPresent.get(possibleCombination) == null) {
                Select select = new Select(columns,from);
                results.add(select);
                isPresent.put(possibleCombination, true);
                System.out.println(select);
            }
            possibleCombination = 0;
        }
    }

    private void returnSelectColumns (List <ColumnaResult> columnaResults, TableFrom tableFrom) {
        for (Relation relation: tableFrom.getRelations()) {
            columnaResults.addAll(columnsOfOneTable(relation.getTableFrom().getTableRealName(), relation.getTableFrom().getTableName()));
            returnSelectColumns(columnaResults,relation.getTableFrom());
        }
    }

    private List<ColumnaResult> columnsOfOneTable (String realnameOfTable, String nameOfTable) {
        List<ColumnaResult> columnaResults = new ArrayList<>();
        for (String columnaKey : TablesData.getInstance().getTaules().get(realnameOfTable).getColumnes().keySet()) {
            Columna columna = TablesData.getInstance().getTaules().get(realnameOfTable).getColumnes().get(columnaKey);
            ColumnaResult columnaResult = new ColumnaResult(columna,nameOfTable);
            columnaResults.add(columnaResult);
        }
        return columnaResults;
    }

    private void makeFrom (TableLayer tableLayer,IntegerRef i, TableFrom tableFrom) {
        for (TableLayer tl2 : tableLayer.getPointsTo()) {
            Taula t1 = TablesData.getInstance().getTaulesById().get(tableLayer.getTableNum());
            Taula t2 = TablesData.getInstance().getTaulesById().get(tl2.getTableNum());

            TableFrom tableDestiny = new TableFrom("t" + i.getInteger(), t2.getNomTaula());
            TypeRelation type = TypeRelation.INNER_JOIN;
            List<FK> fks =  new ArrayList<>();

            for (Columna c : t1.getForeignKeys()) {
                if (c.getTableReference().equals(t2.getNomTaula())) {
                    //relacionsEntreT1iT2.add(c);
                    fks.add(new FK(c.getColumnName(), c.getColumnReference()));
                }
            }
            for (Columna c : t2.getForeignKeys()) {
                if (c.getTableReference().equals(t1.getNomTaula())) {
                    fks.add(new FK(c.getColumnReference(), c.getColumnName()));
                }
            }
            tableFrom.addRelation(new Relation(fks,type,tableDestiny));
            i.incrementInteger();
            makeFrom(tl2,i,tableDestiny);
        }

    }

}
