package Utils.XML;

import Controller.DBLogic.DBConnection;
import Controller.Logic.GenerateCSV;
import Model.EStudyXML;
import Model.ParametersOfQuestion.Answer;
import Model.ParametersOfQuestion.Question;
import Model.ParametersOfQuestion.TemplateQuestion;
import Model.ProgramConfig;
import Model.Query.Select;
import Model.TablesData;
import Utils.Base64Encode;
import Utils.SQLLiteUtils;

import java.io.IOException;
import java.util.List;

public class ConvertAnswerToXML {
    private TemplateQuestion question;
    private String schema;

    public ConvertAnswerToXML(TemplateQuestion question, String schema) {
        this.question = question;
        this.schema = schema;
    }

    public String convert () {
        try {
            EStudyXML eStudyXML = new EStudyXML();
            int numQuestionInEstudy = 1;

            if (question.getAnswers().size() > 0) {
                for(Answer answerToAdd : question.getAnswers()) {
                    if (answerToAdd.isSelected()) {
                        eStudyXML.addQuestion("Pregunta " + question.getId() + " - Alternativa " + numQuestionInEstudy,  answerToAdd.getStatement() + schema + "\n", answerToAdd.getQuery(), answerToAdd.getAnswer(), Base64Encode.encodeFileToBase64Binary(question.getProject().getPathToDbFile()));
                        numQuestionInEstudy++;
                    }
                }

            } else {
                System.out.println("Per a la pregunta no s'han pogut generar preguntes!");
            }
            System.out.println("----------------------------------------------------------------------------");


            return eStudyXML.exportToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
