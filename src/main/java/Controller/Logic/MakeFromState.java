package Controller.Logic;

import Controller.DBLogic.DBConnection;
import Model.*;
import Model.ParametersOfQuestion.FilterInSelect;
import Model.ParametersOfQuestion.FilterOptions.ColumnFilterOption;
import Model.ParametersOfQuestion.FilterOptions.FilterOption;
import Model.ParametersOfQuestion.FilterOptions.LiteralValue;
import Model.ParametersOfQuestion.SelectFolder.ColumnInSelect;
import Model.Query.*;
import Model.Query.WhereFolder.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakeFromState extends State{
    //TODO: Doble relacions entre dos taules --> Ex: Treballador <--> Departament
    //TODO: Evitar for de for (Columna c : t1.getForeignKeys()) {
    //TODO: Select amb una classe, no crear string directament



    @Override
    void doYourFunction(String string) throws IOException {
        List <Select> results = new ArrayList<>();
        System.out.println("----------------------------Pregunta " + (Integer.parseInt(string) + 1) + "--------------------------------------");
        for (Long key:TablesData.getInstance().getCaminsPossiblesSolucions().keySet()) {
            TableLayer tableLayer = TablesData.getInstance().getCaminsPossiblesSolucions().get(key).getConnections();
            TableFrom tableFrom = new TableFrom("t" + 0, TablesData.getInstance().getTaulesById().get(tableLayer.getTableNum()).getNomTaula());
            for (Map.Entry<String,Columna> entry : TablesData.getInstance().getTaulesById().get(tableLayer.getTableNum()).getColumnes().entrySet()) {
                entry.getValue().setTableNameInFrom("t" + 0);
            }
            makeFrom(tableLayer,new IntegerRef(1),tableFrom);
            From from = new From(tableFrom);

            //List<ColumnaResult> columnesPossibles = columnsOfOneTable(from.getTableFrom().getTableRealName(),from.getTableFrom().getTableName());
            //returnSelectColumns(columnesPossibles,from.getTableFrom());
            HashMap<String,List<String>> columnsForEachTable = new HashMap<>();
            HashMap<String,Integer>  necessaryColumnsForEachTable = getNecessaryColumnsForEachTable (Integer.parseInt(string),columnsForEachTable);


            List<List<String>> tablesOfTheQuestionThatEveryTableAdapts = new ArrayList<>();

            List<String> nameOfEveryTableInTablesOfTheQuestionThatEveryTableAdapts = new ArrayList<>();
            nameOfEveryTableInTablesOfTheQuestionThatEveryTableAdapts.add(from.getTableFrom().getTableRealName());
            getTablesOfTheQuestionThatEveryTableAdapts(necessaryColumnsForEachTable,from.getTableFrom(), tablesOfTheQuestionThatEveryTableAdapts,nameOfEveryTableInTablesOfTheQuestionThatEveryTableAdapts);

            if (necessaryColumnsForEachTable.size() <=  tablesOfTheQuestionThatEveryTableAdapts.size()) {
                List<HashMap<String,String>> possibleFormsOfOrganization = new ArrayList<>();
                generateAllTablePossibilities(tablesOfTheQuestionThatEveryTableAdapts,possibleFormsOfOrganization,nameOfEveryTableInTablesOfTheQuestionThatEveryTableAdapts);
                List<HashMap<String,List<HashMap<String, Columna>>>> possibleOrganizationsForOneQuestion = generateQueryWithDifferentPossibilities (possibleFormsOfOrganization,columnsForEachTable);
                filterConditionsCheck(possibleOrganizationsForOneQuestion,Integer.parseInt(string), results, from);
            }


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

    private void filterConditionsCheck (List<HashMap<String,List<HashMap<String, Columna>>>> possibleOrganizationsForOneQuestion, int idQuestion, List <Select> results, From from) {
        List<FilterInSelect> filtersInSelect = ProgramConfig.getInstance().getFilterParams().getQuestions().get(idQuestion).getStructure().getColumnsToFilterInSelect();

        for (HashMap<String,List<HashMap<String, Columna>>> possibleOrganizationForOneQuestion : possibleOrganizationsForOneQuestion) {
            List<Integer> indexes = new ArrayList<>();
            List<String> indexesString = new ArrayList<>();
            for (Map.Entry<String, List<HashMap<String, Columna>>> entry : possibleOrganizationForOneQuestion.entrySet()) {
                indexes.add(0);
                indexesString.add(entry.getKey());
            }
            boolean finished = false;
            indexes.set(0, -1);
            while(!finished) {
                int i = 0;
                indexes.set(0, indexes.get(0) + 1);
                for (Integer index : indexes) {
                    if (index == (possibleOrganizationForOneQuestion.get(indexesString.get(i)).size())) {
                        if ((i + 1) == indexes.size()) {
                            finished = true;
                        } else {
                            indexes.set(i + 1, indexes.get(i + 1) + 1);
                            indexes.set(i, 0);
                        }
                    }
                    i++;
                }
                if (!finished) {
                    boolean combinationCorrectForResult = true;
                    for (FilterInSelect filterInSelect : filtersInSelect) {
                        if (filterInSelect.getFilterOption1().getType().equals("ColumnFilterOption") && filterInSelect.getFilterOption2().getType().equals("ColumnFilterOption")) {
                            //Busquem index de la taula 1 i taula 2
                            int t1 = 0;
                            int t2 = 0;
                            int h = 0;
                            for (String tableName : indexesString) {
                                if (tableName.equals(((ColumnFilterOption) filterInSelect.getFilterOption1()).getTableReference())) {
                                    t1 = h;
                                    if (t2 != 0) {
                                        //Per no continuar iterant
                                        break;
                                    }
                                }
                                if (tableName.equals(((ColumnFilterOption) filterInSelect.getFilterOption2()).getTableReference())) {
                                    t2 = h;
                                    if (t1 != 0) {
                                        //Per no continuar iterant
                                        break;
                                    }
                                }
                                h++;
                            }
                            HashMap<String, Columna> hashMapT1 = possibleOrganizationForOneQuestion.get(((ColumnFilterOption) filterInSelect.getFilterOption1()).getTableReference()).get(indexes.get(t1));
                            HashMap<String, Columna> hashMapT2 = possibleOrganizationForOneQuestion.get(((ColumnFilterOption) filterInSelect.getFilterOption2()).getTableReference()).get(indexes.get(t2));
                            if (!hashMapT1.get(((ColumnFilterOption) filterInSelect.getFilterOption1()).getColumnRference()).getType().equals(hashMapT2.get(((ColumnFilterOption) filterInSelect.getFilterOption2()).getColumnRference()).getType())) {
                                combinationCorrectForResult = false;
                            }
                        }
                    }
                    //Si es una combinacio correcta significa que es apta pel resultat
                    if (combinationCorrectForResult) {
                        HashMap<String, ColumnaResult> columnsInSelectResult = new HashMap<>();
                        for (Map.Entry<String, List<ColumnInSelect>> tableWithHisRespectiveColumns : ProgramConfig.getInstance().getFilterParams().getQuestions().get(idQuestion).getStructure().getColumnsToSeeInSelect().getTablesWithHisRespectiveColumns().entrySet()) {
                            int t = 0;
                            for (String tableName : indexesString) {
                                if (tableName.equals(tableWithHisRespectiveColumns.getKey())) {
                                    break;
                                }
                                t++;
                            }
                            HashMap<String, Columna> hashMapT = possibleOrganizationForOneQuestion.get(tableWithHisRespectiveColumns.getKey()).get(indexes.get(t));
                            for (ColumnInSelect columnInSelect : tableWithHisRespectiveColumns.getValue()) {
                                ColumnaResult newColumnaResult = new ColumnaResult(hashMapT.get(columnInSelect.getColumnReference()), hashMapT.get(columnInSelect.getColumnReference()).getTableNameInFrom());
                                columnsInSelectResult.put(columnInSelect.generateName(), newColumnaResult);
                            }
                        }

                        for (FilterInSelect filterInSelect : ProgramConfig.getInstance().getFilterParams().getQuestions().get(idQuestion).getStructure().getColumnsToFilterInSelect()) {
                            int t1 = 0;
                            int t2 = 0;
                            int h = 0;
                            for (String tableName : indexesString) {
                                if (tableName.equals(((ColumnFilterOption) filterInSelect.getFilterOption1()).getTableReference())) {
                                    t1 = h;
                                    if (t2 != 0) {
                                        //Per no continuar iterant
                                        break;
                                    }
                                }
                                if (filterInSelect.getFilterOption2().getType().equals("ColumnFilterOption")) {
                                    if (tableName.equals(((ColumnFilterOption) filterInSelect.getFilterOption2()).getTableReference())) {
                                        t2 = h;
                                        if (t1 != 0) {
                                            //Per no continuar iterant
                                            break;
                                        }
                                    }
                                } else {
                                    if (filterInSelect.getFilterOption2().getType().equals("LiteralValue")) {
                                        t2 = -1;
                                    }
                                }
                                h++;
                            }
                            HashMap<String, Columna> hashMapT1 = possibleOrganizationForOneQuestion.get(((ColumnFilterOption) filterInSelect.getFilterOption1()).getTableReference()).get(indexes.get(t1));
                            ColumnFilterOption columnFilterOption = (ColumnFilterOption) filterInSelect.getFilterOption1();
                            ColumnWhere columnWhere = new ColumnWhere(hashMapT1.get(columnFilterOption.getColumnRference()).getTableNameInFrom(), hashMapT1.get(columnFilterOption.getColumnRference()).getColumnName());

                            WhereOperand secondOperand = null;
                            if (t2 > 0) {
                                HashMap<String, Columna> hashMapT2 = possibleOrganizationForOneQuestion.get(((ColumnFilterOption) filterInSelect.getFilterOption2()).getTableReference()).get(indexes.get(t2));
                                ColumnFilterOption columnFilterOption2 = (ColumnFilterOption) filterInSelect.getFilterOption2();
                                secondOperand = new ColumnWhere(hashMapT2.get(columnFilterOption2.getColumnRference()).getTableNameInFrom(), hashMapT2.get(columnFilterOption2.getColumnRference()).getColumnName());

                            } else {
                                if (t2 == -1){
                                    LiteralValue columnFilterOption2 = (LiteralValue) filterInSelect.getFilterOption2();
                                    String typeColumnOne = hashMapT1.get(columnFilterOption.getColumnRference()).getType();
                                    if(typeColumnOne.equalsIgnoreCase("integer")){
                                        secondOperand = new LiteralIntegerWhere(DBConnection.getInstance(null).generateRandomOfIntegerColumn(hashMapT1.get(columnFilterOption.getColumnRference()).getColumnName(),hashMapT1.get(columnFilterOption.getColumnRference()).getTableName()));
                                    } else {
                                        if (typeColumnOne.equalsIgnoreCase("date")) {
                                            secondOperand = new LiteralDateWhere(DBConnection.getInstance(null).generateRandomOfDateColumn(hashMapT1.get(columnFilterOption.getColumnRference()).getColumnName(),hashMapT1.get(columnFilterOption.getColumnRference()).getTableName()));
                                        } else {
                                            if (typeColumnOne.toLowerCase().contains("varchar")) {
                                                secondOperand = new LiteralVarcharWhere(DBConnection.getInstance(null).generateRandomOfVarcharColumn(hashMapT1.get(columnFilterOption.getColumnRference()).getColumnName(),hashMapT1.get(columnFilterOption.getColumnRference()).getTableName()));
                                            }
                                        }
                                    }
                                }
                            }
                            for (String operand: generatePossibleOperandsBetweenColumns(hashMapT1.get(columnFilterOption.getColumnRference()).getType())){
                                Expression expression = new Expression(columnWhere,operand,secondOperand);
                                Where where = new Where();
                                where.addExpression(expression);
                                Select select = new Select(columnsInSelectResult, from);
                                select.setWhere(where);
                                if (DBConnection.getInstance("").testIfQueryHasResults(select.toString())) {
                                    System.out.println(select);
                                    results.add(select);
                                }
                            }

                        }
                    }
                }
            }

        }
    }

    private List<String> generatePossibleOperandsBetweenColumns (String type) {
        List<String> operators = new ArrayList<>();
        if(type.equalsIgnoreCase("integer")){
            operators.add(">");
            operators.add("<");
            operators.add("=");
        } else {
            if (type.equalsIgnoreCase("date")) {
                operators.add(">");
                operators.add("<");
                operators.add("=");
            } else {
                if (type.toLowerCase().contains("varchar")) {
                    operators.add("=");
                }
            }
        }
        return operators;
    }



    private List<HashMap<String,List<HashMap<String, Columna>>>> generateQueryWithDifferentPossibilities(List<HashMap<String,String>> possibleFormsOfOrganization, HashMap<String,List<String>>  necessaryColumnsForEachTable) {
        List<HashMap<String,List<HashMap<String, Columna>>>> possibleOrganizationsForOneQuestion = new ArrayList<>();
        for (HashMap<String,String> possibleFormOfOrganization : possibleFormsOfOrganization) {
            if (possibleFormOfOrganization.size() == necessaryColumnsForEachTable.size()) {
                HashMap<String,List<HashMap<String, Columna>>> possibleCombinationsInsideOneSolution = new HashMap<>();
                for (Map.Entry<String,String> tableRelation : possibleFormOfOrganization.entrySet()) {
                    Taula t1 = TablesData.getInstance().getTaules().get(tableRelation.getValue());
                    List<HashMap<String, Columna>> newResultsToAdd = new ArrayList<>();
                    List<String> columns = necessaryColumnsForEachTable.get(tableRelation.getKey());
                    for (Map.Entry<String, Columna> columna : t1.getColumnes().entrySet()) {
                        for (String columnReference : columns) {
                            List<HashMap<String, Columna>> subArrayResultsToAdd = new ArrayList<>();
                            for (HashMap<String,Columna> newResultToAdd: newResultsToAdd) {
                                if (!newResultToAdd.containsKey(columnReference)){
                                    if (!checkIfColumnExistInPossibleResult (newResultToAdd,columna.getValue().getColumnName())) {
                                        HashMap<String, Columna> shallowCopy = new HashMap<String, Columna>(newResultToAdd);
                                        shallowCopy.put(columnReference,columna.getValue());
                                        subArrayResultsToAdd.add(shallowCopy);
                                    }
                                }
                            }
                            HashMap<String,Columna> newHashmapToAdd = new HashMap<>();
                            newHashmapToAdd.put(columnReference,columna.getValue());
                            newResultsToAdd.add(newHashmapToAdd);
                            newResultsToAdd.addAll(subArrayResultsToAdd);
                        }
                    }
                    //Podem aquells resultats que no ens interessen
                    List<HashMap<String, Columna>> resultatsPodats = new ArrayList<>();
                    for(HashMap<String, Columna> newResultToAdd : newResultsToAdd) {
                        if (newResultToAdd.size() == columns.size()){
                            resultatsPodats.add(newResultToAdd);
                        }
                    }
                    possibleCombinationsInsideOneSolution.put(tableRelation.getKey(),resultatsPodats);
                }
                possibleOrganizationsForOneQuestion.add(possibleCombinationsInsideOneSolution);
            }
        }
        return possibleOrganizationsForOneQuestion;
    }

    private boolean checkIfColumnExistInPossibleResult (HashMap<String,Columna> hashMapRelationBetweenReferenceAndColumn, String columnRealName) {
        for (Map.Entry<String, Columna> entry : hashMapRelationBetweenReferenceAndColumn.entrySet()) {
            if (entry.getValue().getColumnName().equals(columnRealName)) {
                return true;
            }
        }
        return false;
    }

    private void generateAllTablePossibilities (List<List<String>> tablesOfTheQuestionThatEveryTableAdapts, List<HashMap<String,String>> possibleFormsOfOrganization, List<String>nameOfEveryTableInTablesOfTheQuestionThatEveryTableAdapts) {
        int i = 0;
        for (List<String> tablesOfTheQuestionThatTableAdapts : tablesOfTheQuestionThatEveryTableAdapts) {
            List <HashMap<String,String>> newResultsToAdd = new ArrayList<>();
            for (String tableOfTheQuestionThatTableAdapts: tablesOfTheQuestionThatTableAdapts) {
                for (HashMap<String,String> possibleFormOfOrganization: possibleFormsOfOrganization) {
                    if (!possibleFormOfOrganization.containsKey(tableOfTheQuestionThatTableAdapts)) {
                        if (!checkIfTableExistInPossibleResult (possibleFormOfOrganization, nameOfEveryTableInTablesOfTheQuestionThatEveryTableAdapts.get(i))) {
                            HashMap<String, String> shallowCopy = new HashMap<String, String>(possibleFormOfOrganization);
                            shallowCopy.put(tableOfTheQuestionThatTableAdapts, nameOfEveryTableInTablesOfTheQuestionThatEveryTableAdapts.get(i));
                            newResultsToAdd.add(shallowCopy);
                        }
                    }
                }
                HashMap<String,String> newHashmap = new HashMap<>();
                newHashmap.put(tableOfTheQuestionThatTableAdapts,nameOfEveryTableInTablesOfTheQuestionThatEveryTableAdapts.get(i));
                newResultsToAdd.add(newHashmap);
            }
            possibleFormsOfOrganization.addAll(newResultsToAdd);
            i++;
        }
    }

    private boolean checkIfTableExistInPossibleResult (HashMap<String,String> hashMapRelationBetweenReferenceAndTable, String tableRealName) {
        for (Map.Entry<String, String> entry : hashMapRelationBetweenReferenceAndTable.entrySet()) {
            if (entry.getValue().equals(tableRealName)) {
                return true;
            }
        }
        return false;
    }

    private void getTablesOfTheQuestionThatEveryTableAdapts (HashMap<String,Integer>  necessaryColumnsForEachTable, TableFrom tableFrom, List<List<String>> tablesOfTheQuestionThatEveryTableAdapts, List<String> nameOfEveryTableInTablesOfTheQuestionThatEveryTableAdapts) {
        tablesOfTheQuestionThatEveryTableAdapts.add(new ArrayList<>());
        for (Map.Entry<String, Integer> tableOfQuestion : necessaryColumnsForEachTable.entrySet()) {
            if (tableOfQuestion.getValue() <= columnsOfOneTable(tableFrom.getTableRealName(), tableFrom.getTableName()).size()) {
                tablesOfTheQuestionThatEveryTableAdapts.get(tablesOfTheQuestionThatEveryTableAdapts.size()-1).add(tableOfQuestion.getKey());
            }
        }
        for (Relation relation: tableFrom.getRelations()) {
            nameOfEveryTableInTablesOfTheQuestionThatEveryTableAdapts.add(relation.getTableFrom().getTableRealName());
            getTablesOfTheQuestionThatEveryTableAdapts (necessaryColumnsForEachTable, relation.getTableFrom(), tablesOfTheQuestionThatEveryTableAdapts,nameOfEveryTableInTablesOfTheQuestionThatEveryTableAdapts);
        }
    }

    private HashMap<String,Integer>  getNecessaryColumnsForEachTable (int actualQuestion, HashMap<String,List<String>> columnsForEachTable) {
        HashMap<String,Integer> necessaryColumnsForEachTable = new HashMap<>();
        for (Map.Entry<String, List<ColumnInSelect>> entry : ProgramConfig.getInstance().getFilterParams().getQuestions().get(actualQuestion).getStructure().getColumnsToSeeInSelect().getTablesWithHisRespectiveColumns().entrySet()) {
            necessaryColumnsForEachTable.put(entry.getKey(),entry.getValue().size());
            List<String> array = new ArrayList<>();
            columnsForEachTable.put(entry.getKey(),array);
            for (ColumnInSelect c: entry.getValue()) {
                array.add(c.getColumnReference());
            }
        }
        for (Map.Entry<String, List<ColumnInSelect>> entry : ProgramConfig.getInstance().getFilterParams().getQuestions().get(actualQuestion).getStructure().getColumnsToTakeIntoAccountInSelect().getTablesWithHisRespectiveColumns().entrySet()) {
            Integer numOfColumns = necessaryColumnsForEachTable.get(entry.getKey());
            if (numOfColumns == null) {
                Integer numOfColumnsRef = 0;
                List<String> array = new ArrayList<>();
                columnsForEachTable.put(entry.getKey(),array);
                necessaryColumnsForEachTable.put(entry.getKey(),numOfColumnsRef);
            }
            numOfColumns = necessaryColumnsForEachTable.get(entry.getKey());
            for (ColumnInSelect c: entry.getValue()) {
                boolean encountered = false;
                for (String c2:columnsForEachTable.get(entry.getKey())){
                    if (c.getColumnReference().equals(c2)) {
                        encountered = true;
                        break;
                    }
                }
                if (!encountered) {
                    columnsForEachTable.get(entry.getKey()).add(c.getColumnReference());
                    numOfColumns++;
                    necessaryColumnsForEachTable.put(entry.getKey(),numOfColumns);
                }
            }
        }
        for (FilterInSelect filterInSelect: ProgramConfig.getInstance().getFilterParams().getQuestions().get(actualQuestion).getStructure().getColumnsToFilterInSelect()) {
            //necessaryColumnsForEachTable.put(entry.getKey(),entry.getValue().size());
            if (filterInSelect.getFilterOption1().getType().equals("ColumnFilterOption")) {
                ColumnFilterOption columnFilterOption = (ColumnFilterOption) filterInSelect.getFilterOption1();
                if (!necessaryColumnsForEachTable.containsKey(columnFilterOption.getTableReference())) {
                    necessaryColumnsForEachTable.put(columnFilterOption.getTableReference(),1);
                    List<String> array = new ArrayList<>();
                    columnsForEachTable.put(columnFilterOption.getTableReference(),array);
                    array.add(columnFilterOption.getColumnRference());
                } else {
                    boolean encontered = false;
                    for (String s: columnsForEachTable.get(columnFilterOption.getTableReference())) {
                        if (s.equals(((ColumnFilterOption) filterInSelect.getFilterOption1()).getColumnRference())) {
                            encontered = true;
                        }
                    }
                    if (!encontered) {
                        Integer numOfColumns = necessaryColumnsForEachTable.get(columnFilterOption.getTableReference());
                        numOfColumns++;
                        necessaryColumnsForEachTable.put(columnFilterOption.getTableReference(),numOfColumns);
                        List<String> array = columnsForEachTable.get(columnFilterOption.getTableReference());
                        array.add(((ColumnFilterOption) filterInSelect.getFilterOption1()).getColumnRference());
                    }
                }
            }
            if (filterInSelect.getFilterOption2().getType().equals("ColumnFilterOption")) {
                ColumnFilterOption columnFilterOption = (ColumnFilterOption) filterInSelect.getFilterOption2();
                if (!necessaryColumnsForEachTable.containsKey(columnFilterOption.getTableReference())) {
                    necessaryColumnsForEachTable.put(columnFilterOption.getTableReference(),1);
                    List<String> array = new ArrayList<>();
                    columnsForEachTable.put(columnFilterOption.getTableReference(),array);
                    array.add(columnFilterOption.getColumnRference());
                } else {
                    boolean encontered = false;
                    for (String s: columnsForEachTable.get(columnFilterOption.getTableReference())) {
                        if (s.equals(((ColumnFilterOption) filterInSelect.getFilterOption2()).getColumnRference())) {
                            encontered = true;
                        }
                    }
                    if (!encontered) {
                        Integer numOfColumns = necessaryColumnsForEachTable.get(columnFilterOption.getTableReference());
                        numOfColumns++;
                        necessaryColumnsForEachTable.put(columnFilterOption.getTableReference(),numOfColumns);
                        List<String> array = columnsForEachTable.get(columnFilterOption.getTableReference());
                        array.add(((ColumnFilterOption) filterInSelect.getFilterOption2()).getColumnRference());
                    }
                }
            }
        }
        return necessaryColumnsForEachTable;
    }



    private void generateAllPossibleCombonationsForAColumn (ColumnaResult columnToFilter, Select possibleResult,List <Select> possibleFromResults) {
        int j = 0;
        for (Expression e: generateExpressionsForAColumn(columnToFilter)) {
            if (j == (generateExpressionsForAColumn(columnToFilter).size() - 1)) {
                possibleResult.getWhere().addExpression(e);
            } else {
                try {
                    Select resultToAdd = (Select) possibleResult.clone();
                    resultToAdd.getWhere().addExpression(e);
                    possibleFromResults.add(resultToAdd);
                } catch (CloneNotSupportedException ex) {
                    ex.printStackTrace();
                }
            }
            j++;
        }
    }

    private List<Expression> generateExpressionsForAColumn (ColumnaResult columnToTreat) {
        List<Expression> expressions = new ArrayList<>();
        if(columnToTreat.getColumna().getType().equalsIgnoreCase("integer")){
            Integer integer = DBConnection.getInstance(null).generateRandomOfIntegerColumn(columnToTreat.getColumna().getColumnName(),columnToTreat.getColumna().getTableName());
            expressions.add(createExpressionBetweenColumnAndInteger (columnToTreat, ">", integer));
            expressions.add(createExpressionBetweenColumnAndInteger (columnToTreat, "=", integer));
            expressions.add(createExpressionBetweenColumnAndInteger (columnToTreat, "<", integer));

        } else {
            if(columnToTreat.getColumna().getType().equalsIgnoreCase("date")){
                String date =  DBConnection.getInstance(null).generateRandomOfDateColumn (columnToTreat.getColumna().getColumnName(), columnToTreat.getColumna().getTableName());
                expressions.add(createExpressionBetweenColumnAndDate (columnToTreat, ">", date));
                expressions.add(createExpressionBetweenColumnAndDate (columnToTreat, "=", date));
                expressions.add(createExpressionBetweenColumnAndDate (columnToTreat, "<", date));
            } else {
                if(columnToTreat.getColumna().getType().toLowerCase().contains("varchar")){
                    String varchar =  DBConnection.getInstance(null).generateRandomOfVarcharColumn (columnToTreat.getColumna().getColumnName(), columnToTreat.getColumna().getTableName());
                    expressions.add(createExpressionBetweenColumnAndDate (columnToTreat, "EQUALS", varchar));
                    expressions.add(createExpressionBetweenColumnAndDate (columnToTreat, "LIKE", varchar.charAt(0) + "%"));
                    expressions.add(createExpressionBetweenColumnAndDate (columnToTreat, "LIKE",  "%" + varchar.charAt(varchar.length()-1)));
                }
            }
        }
        return expressions;
    }

    private Expression createExpressionBetweenColumnAndInteger (ColumnaResult columnToTreat, String operator, int integerToCompare) {
        return new Expression(new ColumnWhere(columnToTreat.getNomTaula(),columnToTreat.getColumna().getColumnName()), operator,new LiteralIntegerWhere(integerToCompare));
    }

    private Expression createExpressionBetweenColumnAndDate (ColumnaResult columnToTreat, String operator, String date) {
        return new Expression(new ColumnWhere(columnToTreat.getNomTaula(),columnToTreat.getColumna().getColumnName()), operator,new LiteralDateWhere(date));
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

            //Recorrem totes les columnes de la taula 2 i ficar-los a l'atribut en concret el nom de la taula
            for (Map.Entry<String,Columna> entry : t2.getColumnes().entrySet()) {
                entry.getValue().setTableNameInFrom("t" + i.getInteger());
            }

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
