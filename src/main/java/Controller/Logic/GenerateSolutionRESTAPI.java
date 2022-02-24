package Controller.Logic;

import Controller.DBLogic.DBConnection;
import Model.ParametersOfQuestion.Question;
import Model.ProgramConfig;
import Model.Query.Select;
import Model.RestAPI.Solution;
import Model.TablesData;
import Utils.SQLLiteUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GenerateSolutionRESTAPI extends State {

    @Override
    void doYourFunction(String string) throws IOException {
        int numQuestion  = 1;
        int numQuestionInEstudy = 1;
        for (Question question : ProgramConfig.getInstance().getFilterParams().getQuestions()) {
            if (TablesData.getInstance().getPossibleQueries().get(numQuestion - 1).size() > 0) {
                for (Select select : TablesData.getInstance().getPossibleQueries().get(numQuestion - 1)) {
                    //ProcessBuilder builder = new ProcessBuilder("sqlite3", ".open " + DBConnection.getInstance("").getPathFile(),".mode column",TablesData.getInstance().getPossibleQueries().get(numQuestion-1).get(0).toString());
                    String answer = SQLLiteUtils.getSolutionForOneSelect(select);
                    select.setAnswer(answer);
                    numQuestionInEstudy++;
                }
            }
            numQuestion++;
        }
        Solution.getInstance().setPossibleQueries(TablesData.getInstance().getPossibleQueries());
    }
}
