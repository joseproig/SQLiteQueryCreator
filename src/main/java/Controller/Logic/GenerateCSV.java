package Controller.Logic;

import Model.ParametersOfQuestion.Question;
import Model.ProgramConfig;
import Model.Query.Select;
import Model.TablesData;
import Utils.CSV.CSVLogicWrite;

import java.io.IOException;

public class GenerateCSV extends State{
    private final static String[] HEADERS = {"Query","Template","Logos","EQSPlain"};
    @Override
    void doYourFunction(String string) throws IOException {
        CSVLogicWrite csvLogicWrite = new CSVLogicWrite("src/fileUtils/output.csv");
        csvLogicWrite.addHeader(HEADERS);
        int numQuestion  = 1;
        for (Question question : ProgramConfig.getInstance().getFilterParams().getQuestions()) {
            if (TablesData.getInstance().getPossibleQueries().get(numQuestion - 1).size() > 0) {
                for (Select selectToAddInXML : TablesData.getInstance().getPossibleQueries().get(numQuestion - 1)) {
                    String[] rowOfCSV = new String[4];
                    rowOfCSV[0] = selectToAddInXML.toString();
                    String textTemplate = selectToAddInXML.printQuestions(Select.TEMPLATE);
                    rowOfCSV[1] =(!textTemplate.equals(""))?textTemplate:"Cannot generate text with Template";
                    String textLogos = selectToAddInXML.printQuestions(Select.LOGOS);
                    rowOfCSV[2] =(!textLogos.equals(""))?textLogos:"Cannot generate text with Logos";
                    String textEqsplain = selectToAddInXML.printQuestions(Select.EQSPLAIN);
                    rowOfCSV[3] = (!textEqsplain.equals(""))?textEqsplain:"Cannot generate text with EQSPlain";
                    csvLogicWrite.addContent(rowOfCSV);
                }
            }
            numQuestion++;
        }
        csvLogicWrite.finishAndCloseCSV();

    }
}
