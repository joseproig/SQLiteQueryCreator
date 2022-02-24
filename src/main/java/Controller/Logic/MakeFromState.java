package Controller.Logic;

import Controller.Communication.EQSplainClient;
import Controller.DBLogic.DBConnection;
import Controller.DBLogic.MySQLConnector;
import Model.*;

import Model.EQSPlain.ResponseEqsPlain;
import Model.ParametersOfQuestion.FilterInSelect;
import Model.ParametersOfQuestion.FilterOptions.ColumnFilterOption;
import Model.ParametersOfQuestion.FilterOptions.LiteralValue;
import Model.ParametersOfQuestion.Question;
import Model.ParametersOfQuestion.SelectFolder.ColumnInSelect;
import Model.ParametersOfQuestion.SelectFolder.ColumnsInSelect;
import Model.Query.*;
import Model.Query.WhereFolder.*;
import gr.uoa.di.db2nl.q2nl.CommandInterface;
import okhttp3.Route;
import org.sqlite.core.DB;
import retrofit2.Response;


import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MakeFromState extends State{
    //TODO: Doble relacions entre dos taules --> Ex: Treballador <--> Departament
    //TODO: Evitar for de for (Columna c : t1.getForeignKeys()) {
    //TODO: Select amb una classe, no crear string directament



    @Override
    void doYourFunction(String string) throws IOException {
        List <Select> results = new ArrayList<>();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                //includeOrderByInResults(results);
            }


        }
        TablesData.getInstance().addNewListOfPossibleQueries(results);

        int numOfQuestion = Integer.parseInt(string) + 1;
        if (numOfQuestion == ProgramConfig.getInstance().getFilterParams().getQuestions().size()) {
            MySQLConnector.getInstance().close();
            context.changeState(new GenerateSolutionRESTAPI());
        } else {
            context.changeState(new FilterTables());
            string = "" + numOfQuestion;
        }
        context.doStateFunction(string);
    }

    private void includeOrderByInResults (List <Select> results, int idQuestion, List<Integer> indexes, List<String> indexesString, HashMap<String,List<HashMap<String, Columna>>> possibleOrganizationForOneQuestion) {

        HashMap<String, Integer> hashmapWithIndexs = new HashMap<>();
        int h = 0;
        for (ColumnInSelect s : ProgramConfig.getInstance().getFilterParams().getQuestions().get(idQuestion).getStructure().getColumnsToOrderBy()) {
            if (!hashmapWithIndexs.containsKey(s.getTableReference())) {
                h = 0;
                for (String tableName : indexesString) {
                    if (tableName.equals(s.getTableReference())) {
                        hashmapWithIndexs.put(s.getTableReference(),h);
                        break;
                    }
                    h++;
                }
            }
        }
        //Anem guardant en una llista els diferents objectes que poden apareixer al Order By
        List <ColumnWhere> columnsToIncludeInSelect = new ArrayList<>();
        for (ColumnInSelect s : ProgramConfig.getInstance().getFilterParams().getQuestions().get(idQuestion).getStructure().getColumnsToOrderBy()) {
            HashMap<String, Columna> hashMapWithColumnsOfTableToOrderBy = possibleOrganizationForOneQuestion.get(s.getTableReference()).get(indexes.get(hashmapWithIndexs.get(s.getTableReference())));
            columnsToIncludeInSelect.add(new ColumnWhere(hashMapWithColumnsOfTableToOrderBy.get(s.getColumnReference()).getTableNameInFrom(), hashMapWithColumnsOfTableToOrderBy.get(s.getColumnReference()).getColumnName()));
        }

        //Anem resultat a resultat, que previament hauran passat pel WHERE, i generarem les diferents opcions de Order By
        for (Select result : results) {
            result.setOrder(new Order(columnsToIncludeInSelect));
        }

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
                ArrayList<Select> resultsOfOneCausistic = new ArrayList<>();
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
                        boolean firstFilterInSelect =  true;
                        for (FilterInSelect filterInSelect : ProgramConfig.getInstance().getFilterParams().getQuestions().get(idQuestion).getStructure().getColumnsToFilterInSelect()) {
                            int t1 = -2;
                            int t2 = -2;
                            int h = 0;
                            for (String tableName : indexesString) {
                                if (tableName.equals(((ColumnFilterOption) filterInSelect.getFilterOption1()).getTableReference()) && t1 == -2) {
                                    t1 = h;
                                    if (t2 != -2) {
                                        //Per no continuar iterant
                                        break;
                                    }
                                }
                                if (filterInSelect.getFilterOption2().getType().equals("ColumnFilterOption")) {
                                    if (tableName.equals(((ColumnFilterOption) filterInSelect.getFilterOption2()).getTableReference()) && t2 == -2) {
                                        t2 = h;
                                        if (t1 != -2) {
                                            //Per no continuar iterant
                                            break;
                                        }
                                    }
                                } else {
                                    if (filterInSelect.getFilterOption2().getType().equals("LiteralValue") && t2 == -2) {
                                        t2 = -1;
                                    }
                                }
                                h++;
                            }
                            HashMap<String, Columna> hashMapT1 = possibleOrganizationForOneQuestion.get(((ColumnFilterOption) filterInSelect.getFilterOption1()).getTableReference()).get(indexes.get(t1));
                            ColumnFilterOption columnFilterOption = (ColumnFilterOption) filterInSelect.getFilterOption1();
                            ColumnWhere columnWhere = new ColumnWhere(hashMapT1.get(columnFilterOption.getColumnRference()).getTableNameInFrom(), hashMapT1.get(columnFilterOption.getColumnRference()).getColumnName(), ((ColumnFilterOption) filterInSelect.getFilterOption1()).getColumnRference(), ((ColumnFilterOption) filterInSelect.getFilterOption1()).getTableReference());

                            List<WhereOperand> secondOperands = new ArrayList<>();
                            if (t2 >= 0) {
                                HashMap<String, Columna> hashMapT2 = possibleOrganizationForOneQuestion.get(((ColumnFilterOption) filterInSelect.getFilterOption2()).getTableReference()).get(indexes.get(t2));
                                ColumnFilterOption columnFilterOption2 = (ColumnFilterOption) filterInSelect.getFilterOption2();
                                secondOperands.add(new ColumnWhere(hashMapT2.get(columnFilterOption2.getColumnRference()).getTableNameInFrom(), hashMapT2.get(columnFilterOption2.getColumnRference()).getColumnName(), ((ColumnFilterOption) filterInSelect.getFilterOption2()).getColumnRference(), ((ColumnFilterOption) filterInSelect.getFilterOption2()).getTableReference()));

                            } else {
                                if (t2 == -1){
                                    LiteralValue columnFilterOption2 = (LiteralValue) filterInSelect.getFilterOption2();
                                    String typeColumnOne = hashMapT1.get(columnFilterOption.getColumnRference()).getType();
                                    if(typeColumnOne.equalsIgnoreCase("integer")){
                                        secondOperands.add(new LiteralIntegerWhere(DBConnection.getInstance(null).generateRandomOfIntegerColumn(hashMapT1.get(columnFilterOption.getColumnRference()).getColumnName(),hashMapT1.get(columnFilterOption.getColumnRference()).getTableName())));
                                    } else {
                                        if (typeColumnOne.equalsIgnoreCase("date")) {
                                            secondOperands.add(new LiteralDateWhere(DBConnection.getInstance(null).generateRandomOfDateColumn(hashMapT1.get(columnFilterOption.getColumnRference()).getColumnName(),hashMapT1.get(columnFilterOption.getColumnRference()).getTableName())));
                                        } else {
                                            if (typeColumnOne.toLowerCase().contains("varchar") || typeColumnOne.toLowerCase().contains("text")) {
                                                secondOperands.add(new LiteralVarcharWhere(DBConnection.getInstance(null).generateRandomOfVarcharColumn(hashMapT1.get(columnFilterOption.getColumnRference()).getColumnName(),hashMapT1.get(columnFilterOption.getColumnRference()).getTableName())));
                                            }
                                        }
                                    }
                                }
                            }
                            //Si hi ha més d'una combinació s'hauran d'afegir resultats, fem això per evitar que en un mateix where apareguin combinacions de la mateix relacio
                            int w = 0;
                            ArrayList<Select> newResultsToAdd = new ArrayList<>();
                            for (String operand: generatePossibleOperandsBetweenColumns(hashMapT1.get(columnFilterOption.getColumnRference()).getType())){
                                if (secondOperands.size() > 0 && operand.equals("LIKE") && secondOperands.get(0) instanceof LiteralVarcharWhere) {
                                    LiteralVarcharWhere secondOperand = (LiteralVarcharWhere) secondOperands.get(0);
                                    secondOperands.remove(0);
                                    Random r = new Random();
                                    if (secondOperand.getVarchar().length() > 5) {
                                        secondOperands.add(new LiteralVarcharWhere(secondOperand.getVarchar().substring(0, r.nextInt(secondOperand.getVarchar().length() - 2) + 1) + "%"));
                                        secondOperands.add(new LiteralVarcharWhere("%" + secondOperand.getVarchar().substring(r.nextInt(secondOperand.getVarchar().length() - 3))));
                                        int num1 = r.nextInt(secondOperand.getVarchar().length() - 4) + 1; //73
                                        int num2 = r.nextInt(secondOperand.getVarchar().length() - num1 - 1) + num1 + 1; //75-73-1 = 1 + 1 + 73
                                        secondOperands.add(new LiteralVarcharWhere("%" + secondOperand.getVarchar().substring(num1,num2) + "%"));
                                        StringBuilder stringBuilder = new StringBuilder(secondOperand.getVarchar());
                                        stringBuilder.setCharAt(r.nextInt(secondOperand.getVarchar().length() - 1), '_');
                                        secondOperands.add(new LiteralVarcharWhere(stringBuilder.toString()));
                                    } else {
                                        continue;
                                    }
                                    //((LiteralVarcharWhere)secondOperand).setVarchar(((LiteralVarcharWhere)secondOperand).getVarchar().substring(0, 2) + "%");
                                }

                                for (WhereOperand secondOperand : secondOperands) {
                                    Expression expression = new Expression(columnWhere, operand, secondOperand);

                                    if(firstFilterInSelect) {
                                        try {
                                            Where where = new Where();
                                            where.addExpression(expression);
                                            Select select = new Select(columnsInSelectResult, from);
                                            select.setWhere(where);
                                            //Fem la comprovacio de la inversa per descartar queryes que no te sentit fer el WHERE
                                            Select inverseSelectToAdd = new Select(columnsInSelectResult, from);
                                            inverseSelectToAdd.setWhere((Where)where.clone());
                                            inverseSelectToAdd.getWhere().setNegateExpression(true);
                                            if (DBConnection.getInstance("").testIfQueryHasResults(select.toString()) && DBConnection.getInstance("").testIfQueryHasResults(inverseSelectToAdd.toString())) {
                                                resultsOfOneCausistic.add(select);
                                                //Si es un WHERE d'una única AND, cosa poc probable però que pot passar, generem ja a l'enunciat, ja que no entrarà al else.
                                                /*if (ProgramConfig.getInstance().getFilterParams().getQuestions().get(idQuestion).getStructure().getColumnsToFilterInSelect().size() == 1) {
                                                    generateTextForAResult (select, possibleOrganizationForOneQuestion, indexes, indexesString, idQuestion);
                                                }*/
                                            }
                                        } catch (CloneNotSupportedException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        for (Select resultat : resultsOfOneCausistic) {
                                            try {
                                                Select selectToAdd = new Select(resultat.getColumnaResult(),resultat.getFrom(),resultat.getWhere());
                                                Where where = (Where) selectToAdd.getWhere();
                                                where.addExpression(expression);
                                                selectToAdd.setWhere(where);
                                                //Fem la comprovacio de la inversa per descartar queryes que no te sentit fer el WHERE
                                                Select inverseSelectToAdd = new Select(resultat.getColumnaResult(),resultat.getFrom(),(Where)resultat.getWhere().clone());
                                                inverseSelectToAdd.getWhere().setNegateExpression(true);
                                                if (DBConnection.getInstance("").testIfQueryHasResults(selectToAdd.toString()) && DBConnection.getInstance("").testIfQueryHasResults(inverseSelectToAdd.toString())) {
                                                    newResultsToAdd.add(selectToAdd);
                                                }
                                            } catch (CloneNotSupportedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    w++;
                                }
                            }
                            //Si a la primera iteracio no hem obtingut cap resultat no val la pena seguir
                            if(firstFilterInSelect && resultsOfOneCausistic.size() == 0){
                                break;
                            } else {
                                if (!firstFilterInSelect) {
                                    resultsOfOneCausistic.clear();
                                    resultsOfOneCausistic.addAll(newResultsToAdd);
                                }
                            }
                            firstFilterInSelect = false;
                        }
                        /*for (ColumnInSelect columnInSelect: ProgramConfig.getInstance().getFilterParams().getQuestions().get(idQuestion).getStructure().getColumnsToOrderBy()) {
                            for (Select select: results) {

                            }
                        }*/
                    }
                    includeOrderByInResults (resultsOfOneCausistic, idQuestion, indexes,  indexesString,  possibleOrganizationForOneQuestion);
                    for (Select s : resultsOfOneCausistic) {
                        generateTextForAResult (s, possibleOrganizationForOneQuestion, indexes, indexesString, idQuestion, results);
                    }
                    results.addAll(resultsOfOneCausistic);
                }
            }
        }

        for (Select select: results) {
            System.out.println(select);
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
                if (type.toLowerCase().contains("varchar") || type.toLowerCase().contains("text") ) {
                    operators.add("=");
                    operators.add("LIKE");
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
                        //NEW: Només incloem en el resultat aquelles columnes que no siguin pk o fk
                        if (!columna.getValue().getPK() && (columna.getValue().getFK() == null || !columna.getValue().getFK())) {
                            for (String columnReference : columns) {
                                List<HashMap<String, Columna>> subArrayResultsToAdd = new ArrayList<>();
                                for (HashMap<String, Columna> newResultToAdd : newResultsToAdd) {
                                    if (!newResultToAdd.containsKey(columnReference)) {
                                        if (!checkIfColumnExistInPossibleResult(newResultToAdd, columna.getValue().getColumnName())) {
                                            HashMap<String, Columna> shallowCopy = new HashMap<String, Columna>(newResultToAdd);
                                            shallowCopy.put(columnReference, columna.getValue());
                                            subArrayResultsToAdd.add(shallowCopy);
                                        }
                                    }
                                }
                                HashMap<String, Columna> newHashmapToAdd = new HashMap<>();
                                newHashmapToAdd.put(columnReference, columna.getValue());
                                newResultsToAdd.add(newHashmapToAdd);
                                newResultsToAdd.addAll(subArrayResultsToAdd);
                            }
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

    private void generateTextForAResult (Select select, HashMap<String,List<HashMap<String, Columna>>> possibleOrganizationForOneQuestion, List<Integer> indexes, List<String> indexesString, int idQuestion, List<Select> results) {
        select.addQuestion(generateTextForSolution (possibleOrganizationForOneQuestion, indexes, indexesString, idQuestion,select), Select.TEMPLATE);
        if(!ProgramConfig.getInstance().getFilterParams().getQuestions().get(idQuestion).isDeactivateEQSPlain()) {
            int LIMIT_OF_QUESTIONS_EQSPLAIN = 10;
            if (results.size() < LIMIT_OF_QUESTIONS_EQSPLAIN) {
                List<String> responseFromEQSPlain = callEQSPlainToGenerateMoreTextsForSolution(select);
                if (responseFromEQSPlain != null) {
                    select.addQuestions(responseFromEQSPlain, Select.EQSPLAIN);
                }
            }
        }
        if (!ProgramConfig.getInstance().getFilterParams().getQuestions().get(idQuestion).isDeactivateLogos()) {
            String question = callLogosToGenerateTextsForSolution(select);
            if (question != null) {
                select.addQuestion(question, Select.LOGOS);
            }
        }
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
        for (ColumnInSelect columnInSelect: ProgramConfig.getInstance().getFilterParams().getQuestions().get(actualQuestion).getStructure().getColumnsToOrderBy()) {
            Integer numOfColumns = necessaryColumnsForEachTable.get(columnInSelect.getTableReference());
            if (numOfColumns == null) {
                Integer numOfColumnsRef = 1;
                List<String> array = new ArrayList<>();
                array.add(columnInSelect.getColumnReference());
                columnsForEachTable.put(columnInSelect.getTableReference(), array);
                necessaryColumnsForEachTable.put(columnInSelect.getTableReference(), numOfColumnsRef);
            } else {
                boolean encountered = false;
                for (String c2 : columnsForEachTable.get(columnInSelect.getTableReference())) {
                    if (columnInSelect.getColumnReference().equals(c2)) {
                        encountered = true;
                        break;
                    }
                }
                if (!encountered) {
                    numOfColumns = necessaryColumnsForEachTable.get(columnInSelect.getTableReference()) + 1;
                    columnsForEachTable.get(columnInSelect.getTableReference()).add(columnInSelect.getColumnReference());
                    necessaryColumnsForEachTable.put(columnInSelect.getTableReference(), numOfColumns);
                }
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
            //NEW: Aquest if el que aconsegueix es que les primary keys no es tinguin amb compte
            Columna columna = TablesData.getInstance().getTaules().get(realnameOfTable).getColumnes().get(columnaKey);
            if (!columna.getPK() && (columna.getFK() == null || !columna.getFK())) {
                ColumnaResult columnaResult = new ColumnaResult(columna, nameOfTable);
                columnaResults.add(columnaResult);
            }
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

    private String changeTextOfColumnsInSelectObjects (String question, ColumnsInSelect columnsInSelect, String regexPattern, Question questionStructure,HashMap<String,List<HashMap<String, Columna>>> possibleOrganizationForOneQuestion, List<Integer> indexes, List<String> indexesString) {
        for (String key : columnsInSelect.getTablesWithHisRespectiveColumns().keySet()) {
            for (ColumnInSelect columnInSelect: columnsInSelect.getTablesWithHisRespectiveColumns().get(key)){
                List<HashMap<String, Columna>> optionsOfOneTable = possibleOrganizationForOneQuestion.get(columnInSelect.getTableReference());
                int i=0;
                for (String s: indexesString) {
                    if (s.equals(columnInSelect.getTableReference())){
                        break;
                    }
                    i++;
                }
                HashMap<String, Columna> actualCombination = optionsOfOneTable.get(i);

                question = question.replaceAll("(?i)"+ regexPattern.replace("?c",columnInSelect.getColumnReference()).replace("?t", columnInSelect.getTableReference()), optionsOfOneTable.get(indexes.get(i)).get(columnInSelect.getColumnReference()).getColumnName().replace("_"," "));
                question = question.replaceAll("(?i)"+ "\\{" + columnInSelect.getTableReference() + "\\}", optionsOfOneTable.get(i).get(columnInSelect.getColumnReference()).getTableName());
            }
        }
        return question;
    }

    private String changeTextOfFilterCheck (List<FilterInSelect> filtersInSelect, String question, HashMap<String,List<HashMap<String, Columna>>> possibleOrganizationForOneQuestion, List<Integer> indexes, List<String> indexesString, Select select) {
        for (FilterInSelect filterInSelect : filtersInSelect) {

            int i = 0;
            int t1 = -1;
            int t2 = -1;

            for (String s : indexesString) {
                if (filterInSelect.getFilterOption1().getType().equals("ColumnFilterOption") && ((ColumnFilterOption) filterInSelect.getFilterOption1()).getTableReference().equals(s)) {
                    t1 = i;
                } else {
                    if (!filterInSelect.getFilterOption1().getType().equals("ColumnFilterOption") && filterInSelect.getFilterOption2().getType().equals("LiteralValue")) {
                        t1 = -2;
                    }
                }
                if (filterInSelect.getFilterOption2().getType().equals("ColumnFilterOption") && ((ColumnFilterOption) filterInSelect.getFilterOption2()).getTableReference().equals(s)) {
                    t2 = i;
                } else {
                    if (!filterInSelect.getFilterOption2().getType().equals("ColumnFilterOption") && filterInSelect.getFilterOption2().getType().equals("LiteralValue")) {
                        t2 = -2;
                    }
                }
                if (t1 != -1 && t2 != -1) {
                    break;
                }
                i++;
            }
            String textComparacio =  "";
            if (t1 != -2 && t2 == -2) {
                List<HashMap<String, Columna>> optionsOfOneTable = possibleOrganizationForOneQuestion.get(((ColumnFilterOption) filterInSelect.getFilterOption1()).getTableReference());
                String value = "";
                //Busquem el simbol de la operació i adjuntem text de comparacio
                for (Expression expression : select.getWhere().getExpression()) {
                    if (((ColumnWhere)expression.getExpression()).getColumna().equals(optionsOfOneTable.get(indexes.get(t1)).get(((ColumnFilterOption) filterInSelect.getFilterOption1()).getColumnRference()).getColumnName()) && ((ColumnWhere)expression.getExpression()).getNomTaula().equals(optionsOfOneTable.get(indexes.get(t1)).get(((ColumnFilterOption) filterInSelect.getFilterOption1()).getColumnRference()).getTableNameInFrom())) {
                        value = expression.getExpression_2().toString();
                        if (expression.getOperator().equalsIgnoreCase("equals") || expression.getOperator().equals("=")) {
                            textComparacio = " is equal to the ";
                        } else {
                            if (expression.getOperator().equalsIgnoreCase(">")) {
                                textComparacio = " is greater than ";
                            } else {
                                if (expression.getOperator().equals("<")) {
                                    textComparacio = " is less than ";
                                } else{
                                    if (expression.getOperator().equals("LIKE")) {
                                        //\\d+(?:\\.\\d+)?%
                                        String regularExpression3 = "^'([a-zA-Z]+%)'$";
                                        Pattern pat3 = Pattern.compile(regularExpression3);
                                        Matcher matches3 = pat3.matcher(value);
                                        if (matches3.find()) {
                                            textComparacio = " starts with ";
                                            value = value.substring(0, value.indexOf('%')).replace("'","");
                                            break;
                                        }
                                        String regularExpression4 = "(^'%[a-zA-Z]+%)'$";
                                        Pattern pat4 = Pattern.compile(regularExpression4);
                                        Matcher matches4 = pat4.matcher(value);
                                        if (matches4.find()) {
                                            textComparacio = " contains ";
                                            value = value.substring(value.indexOf("%") + 1);
                                            value = value.substring(0, value.indexOf("%")).replace("'","");
                                            break;
                                        }

                                        String regularExpression5 = "^'(%[a-zA-Z]+)'$";
                                        Pattern pat5 = Pattern.compile(regularExpression5);
                                        Matcher matches5 = pat5.matcher(value);
                                        if (matches5.find()) {
                                            textComparacio = " ends with ";
                                            value = value.substring(value.indexOf('%')+1).replace("'","");
                                            break;
                                        }

                                        if (value.contains("_")) {
                                            textComparacio = " follows this expression " + value.replace('_','?').replace("'","") + " where  ? is any character";
                                            value = "";
                                        }

                                    }
                                }
                            }
                        }

                        break;
                    }
                }

                question = question.replaceFirst("(?i)\\{(" + ((ColumnFilterOption) filterInSelect.getFilterOption1()).getColumnRference() + "_" + ((ColumnFilterOption) filterInSelect.getFilterOption1()).getTableReference() + ")(>=|<=|>|<|==|!=)literal\\}", optionsOfOneTable.get(indexes.get(t1)).get(((ColumnFilterOption) filterInSelect.getFilterOption1()).getColumnRference()).getColumnName().replace("_"," ") + textComparacio + value);
                question = question.replaceAll("(?i)"+ "\\{" + ((ColumnFilterOption) filterInSelect.getFilterOption1()).getTableReference() + "\\}", optionsOfOneTable.get(indexes.get(t1)).get(((ColumnFilterOption) filterInSelect.getFilterOption1()).getColumnRference()).getTableName());
            } else {
                if (t1 != -2 && t2 != -2) {
                    List<HashMap<String, Columna>> optionsOfOneTable1 = possibleOrganizationForOneQuestion.get(((ColumnFilterOption) filterInSelect.getFilterOption1()).getTableReference());
                    List<HashMap<String, Columna>> optionsOfOneTable2 = possibleOrganizationForOneQuestion.get(((ColumnFilterOption) filterInSelect.getFilterOption2()).getTableReference());
                    //Busquem operador:
                    for (Expression expression : select.getWhere().getExpression()) {
                        String s = optionsOfOneTable1.get(indexes.get(t1)).get(((ColumnFilterOption) filterInSelect.getFilterOption1()).getColumnRference()).getColumnName();
                        if ((((ColumnWhere)expression.getExpression()).getColumna().equalsIgnoreCase(optionsOfOneTable1.get(indexes.get(t1)).get(((ColumnFilterOption) filterInSelect.getFilterOption1()).getColumnRference()).getColumnName()) && ((ColumnWhere)expression.getExpression()).getNomTaula().equalsIgnoreCase(optionsOfOneTable1.get(indexes.get(t1)).get(((ColumnFilterOption) filterInSelect.getFilterOption1()).getColumnRference()).getTableNameInFrom())) && (((ColumnWhere)expression.getExpression_2()).getColumna().equalsIgnoreCase(optionsOfOneTable2.get(indexes.get(t2)).get(((ColumnFilterOption) filterInSelect.getFilterOption2()).getColumnRference()).getColumnName()) && ((ColumnWhere)expression.getExpression_2()).getNomTaula().equalsIgnoreCase(optionsOfOneTable2.get(indexes.get(t2)).get(((ColumnFilterOption) filterInSelect.getFilterOption2()).getColumnRference()).getTableNameInFrom()))) {
                            if (expression.getOperator().equalsIgnoreCase("equals") || expression.getOperator().equals("=")) {
                                textComparacio = " is equal to the ";
                            }
                            else {
                                if (expression.getOperator().equalsIgnoreCase(">")) {
                                    textComparacio = " is greater than ";
                                } else {
                                    if (expression.getOperator().equals("<")) {
                                        textComparacio = " is less than ";
                                    }
                                }
                            }
                            break;
                        }
                    }


                    question = question.replaceAll("(?i)\\{(" + ((ColumnFilterOption) filterInSelect.getFilterOption1()).getColumnRference() + "_" + ((ColumnFilterOption) filterInSelect.getFilterOption1()).getTableReference() + ")(>=|<=|>|<|==|!=)(" + ((ColumnFilterOption) filterInSelect.getFilterOption2()).getColumnRference() + "_" + ((ColumnFilterOption) filterInSelect.getFilterOption2()).getTableReference() + ")\\}", optionsOfOneTable1.get(indexes.get(t1)).get(((ColumnFilterOption) filterInSelect.getFilterOption1()).getColumnRference()).getColumnName().replace("_"," ") + textComparacio + optionsOfOneTable2.get(indexes.get(t2)).get(((ColumnFilterOption) filterInSelect.getFilterOption2()).getColumnRference()).getColumnName().replace("_"," "));
                    question = question.replaceAll("(?i)"+ "\\{" + ((ColumnFilterOption) filterInSelect.getFilterOption1()).getTableReference() + "\\}", optionsOfOneTable1.get(indexes.get(t1)).get(((ColumnFilterOption) filterInSelect.getFilterOption1()).getColumnRference()).getTableName());
                    question = question.replaceAll("(?i)"+ "\\{" + ((ColumnFilterOption) filterInSelect.getFilterOption1()).getTableReference() + "\\}", optionsOfOneTable2.get(indexes.get(t2)).get(((ColumnFilterOption) filterInSelect.getFilterOption2()).getColumnRference()).getTableName());
                }
            }


        }
        return question;
    }



    private String changeTextOfOrderBy (List<ColumnInSelect> columnsInSelects, String question, HashMap<String,List<HashMap<String, Columna>>> possibleOrganizationForOneQuestion, List<Integer> indexes, List<String> indexesString){
        for (ColumnInSelect columnInSelect: columnsInSelects){
            List<HashMap<String, Columna>> optionsOfOneTable = possibleOrganizationForOneQuestion.get(columnInSelect.getTableReference());
            int i=0;
            for (String s: indexesString) {
                if (s.equals(columnInSelect.getTableReference())){
                    break;
                }
                i++;
            }


            question = question.replaceAll("(?i)\\{" + columnInSelect.getColumnReference() + "_" + columnInSelect.getTableReference() + "_o\\}", optionsOfOneTable.get(indexes.get(i)).get(columnInSelect.getColumnReference()).getColumnName().replace("_"," "));
            question = question.replaceAll("(?i)"+ "\\{" + columnInSelect.getTableReference() + "\\}", optionsOfOneTable.get(indexes.get(i)).get(columnInSelect.getColumnReference()).getTableName());
        }
        return question;
    }

    private String generateTextForSolution (HashMap<String,List<HashMap<String, Columna>>> possibleOrganizationForOneQuestion, List<Integer> indexes, List<String> indexesString, int actualQuestion, Select select) {
        //A possibleOrganizationForOneQuestion tens les possibles organitzacions, i a indexesString tens el nom de cada taula, en relacio a indexes, on tens quina combinacio usar en cada taula.
        //Mirar que fer amb les literals!!
        Question questionStructure = ProgramConfig.getInstance().getFilterParams().getQuestions().get(actualQuestion);
        String question = questionStructure.getQuestion();
        // questionStructure.getStructure().getColumnsToSeeInSelect()
        question = changeTextOfColumnsInSelectObjects (question, questionStructure.getStructure().getColumnsToSeeInSelect(), "\\{?c\\_?t\\_s\\}",questionStructure, possibleOrganizationForOneQuestion,  indexes,  indexesString);
        question = changeTextOfColumnsInSelectObjects (question, questionStructure.getStructure().getColumnsToTakeIntoAccountInSelect(), "\\{?c\\_?t}",questionStructure, possibleOrganizationForOneQuestion,  indexes,  indexesString);
        question = changeTextOfFilterCheck (questionStructure.getStructure().getColumnsToFilterInSelect(), question, possibleOrganizationForOneQuestion,indexes,  indexesString, select);
        question = changeTextOfOrderBy ( questionStructure.getStructure().getColumnsToOrderBy(), question,  possibleOrganizationForOneQuestion, indexes, indexesString);

        return question;
    }

    private List<String> callEQSPlainToGenerateMoreTextsForSolution (Select select) {
        try {
            List<String> responses = new ArrayList<>();
            Response<ResponseEqsPlain> responseEqsPlain = EQSplainClient.getInstance().getListOfQuestionsForOneQuery(select.toString(),"database",2).execute();
            if (responseEqsPlain.isSuccessful()) {
                ResponseEqsPlain textsEQSPlain = responseEqsPlain.body();
                responses.addAll(textsEQSPlain.getExplanations());
            } else {
                System.out.println("EQSPlain no ha pogut resoldre una de les querys generades");
            }
            return responses;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String callLogosToGenerateTextsForSolution (Select select) {
        boolean dontHaveOnlyLiteralComparations = true;
        for (Expression expression : select.getWhere().getExpression()) {
            if (!(expression.getExpression() instanceof LiteralVarcharWhere || expression.getExpression_2() instanceof LiteralVarcharWhere || expression.getExpression() instanceof LiteralDateWhere || expression.getExpression_2() instanceof LiteralDateWhere || expression.getExpression() instanceof LiteralIntegerWhere || expression.getExpression_2() instanceof LiteralIntegerWhere )) {
                dontHaveOnlyLiteralComparations = false;
                break;
            }
        }
        if (dontHaveOnlyLiteralComparations) {
            String ipDirectionAndPort = "localhost:3306";
            String database = DBConnection.getInstance(null).getDatabaseName(); // Database name
            String username = MysqlConfig.getInstance().getMysql_user();
            String password = MysqlConfig.getInstance().getMysql_passwd();
            String dbType = "MySQL"; // or MySQL
            String[] dbSchemas = {"AC3"};

            String question = null;
            try {
                question = CommandInterface.translate(select.toString(), ipDirectionAndPort, database, username, password, dbType, dbSchemas);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return question;
        }
        return null;
    }
}
