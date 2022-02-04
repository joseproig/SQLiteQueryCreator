package Controller.Logic;

import Controller.DBLogic.DBConnection;
import Model.EStudyXML;
import Model.ParametersOfQuestion.Question;
import Model.ProgramConfig;
import Model.Query.Select;
import Model.TablesData;
import Utils.Base64Encode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GenerateXML extends State {
    @Override
    void doYourFunction(String string) throws IOException {
        EStudyXML eStudyXML = new EStudyXML();
        int numQuestion  = 1;
        int numQuestionInEstudy = 1;
        //Tanquem la connexio a la BBDD ja que no la necessitarem per més.
        DBConnection.getInstance("").closeConnection();
        for (Question question : ProgramConfig.getInstance().getFilterParams().getQuestions()) {
            if (TablesData.getInstance().getPossibleQueries().get(numQuestion-1).size() > 0) {
                for(Select selectToAddInXML : TablesData.getInstance().getPossibleQueries().get(numQuestion-1)) {
                    //ProcessBuilder builder = new ProcessBuilder("sqlite3", ".open " + DBConnection.getInstance("").getPathFile(),".mode column",TablesData.getInstance().getPossibleQueries().get(numQuestion-1).get(0).toString());
                    StringBuilder answer = new StringBuilder("");
                    //Hem de ficar la width minima de 10 en les columnes que es vaiguin a mostrar
                    StringBuilder widthCommand = new StringBuilder(".width");
                    for (String var:selectToAddInXML.getColumnaResult().keySet()) {
                        widthCommand.append(" 10");
                    }
                    ProcessBuilder builder = new ProcessBuilder("sqlite3", "", ".open " + DBConnection.getInstance("").getPathFile(), ".mode column",widthCommand.toString(), selectToAddInXML.toString() + ";", ".exit");

                    builder.redirectErrorStream(true);
                    Process p = builder.start();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        answer.append(line).append("\n");
                    }
                    eStudyXML.addQuestion("Pregunta " + numQuestionInEstudy, selectToAddInXML.printAllQuestions(), selectToAddInXML.toString(), answer.toString(), Base64Encode.encodeFileToBase64Binary(DBConnection.getInstance("").getPathFile()));
                    numQuestionInEstudy++;
                }

            } else {
                System.out.println("Per a la pregunta " + numQuestion + " no s'han pogut generar preguntes!");
            }
            System.out.println("----------------------------------------------------------------------------");
            numQuestion++;
        }
        eStudyXML.exportToFile();
    }
}
