package Controller.Logic;

import Model.*;
import Model.Query.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MakeQueryState extends State{
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
            Select result = new Select();
            result.setFrom(new From(tableFrom));

            results.add(result);
            System.out.println(result.toString());
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
