package Controller.Logic;

import Model.EStudyXML;
import Model.ParametersOfQuestion.Question;
import Model.ProgramConfig;
import Model.TablesData;

import java.io.IOException;

public class GenerateXML extends State {
    @Override
    void doYourFunction(String string) throws IOException {
        EStudyXML eStudyXML = new EStudyXML();
        int numQuestion  = 1;
        for (Question question : ProgramConfig.getInstance().getFilterParams().getQuestions()) {
            //TODO: Cambiar Solution per lo que doni SQLite
            eStudyXML.addQuestion("Pregunta " + numQuestion++, question.getQuestion(), TablesData.getInstance().getPossibleQueries().get(0).toString(), "Solution");
        }
        eStudyXML.exportToFile();
    }
}
