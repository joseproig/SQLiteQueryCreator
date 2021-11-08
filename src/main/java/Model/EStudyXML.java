package Model;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EStudyXML {
    Document document;
    Element root;

    public EStudyXML() {
        document = new Document();
        root = new Element("quiz");
    }

    public void addQuestion (String questionName, String question, String solution, String answer) {
        Element questionToAdd = new Element("question");
        questionToAdd.setAttribute("type", "coderunner");

        //Nom de la pregunta
        Element name = new Element("name");
        addElement (name, "text", questionName);
        questionToAdd.addContent(name);

        //Contigut pregunta
        Element questionText = new Element("questiontext");
        questionText.setAttribute("type","html");
        addElement (questionText, "text", question);
        questionToAdd.addContent(questionText);

        //Default settings
        Element generalFeedback = new Element("generalFeedback");
        generalFeedback.setAttribute("type","html");
        addElement (generalFeedback, "text", "");
        questionToAdd.addContent(generalFeedback);


        addElement (questionToAdd, "defaultgrade", "1.0000000");

        addElement (questionToAdd, "penalty", "0.0000000");

        addElement (questionToAdd, "hidden", "0");

        addElement (questionToAdd, "idnumber", " ");

        addElement (questionToAdd, "coderunnertype", "sql");

        addElement (questionToAdd, "prototypetype", "0");

        addElement (questionToAdd, "allornothing", "0");

        addElement (questionToAdd, "penaltyregime", "0");

        addElement (questionToAdd, "precheck", "0");

        addElement (questionToAdd, "hidecheck", "0");

        addElement (questionToAdd, "showsource", "0");

        addElement (questionToAdd, "answerboxlines", "18");

        addElement (questionToAdd, "answerboxcolumns", "100");

        addElement (questionToAdd, "answerpreload", " ");

        addElement (questionToAdd, "globalextra", " ");

        addElement (questionToAdd, "useace", " ");

        addElement (questionToAdd, "resultcolumns", " ");

        addElement (questionToAdd, "template", " ");

        addElement (questionToAdd, "iscombinatortemplate", " ");

        addElement (questionToAdd, "allowmultiplestdins", " ");

        addElement (questionToAdd, "answer", solution);

        root.addContent(questionToAdd);
    }


    private void addElement (Element elementDestiny, String nameOfPropertyXML, String defaultValue) {
        Element newElement = new Element(nameOfPropertyXML);
        newElement.addContent(defaultValue);
        elementDestiny.addContent(newElement);
    }

    public void exportToFile () throws IOException {
        document.setRootElement(root);
        XMLOutputter outter=new XMLOutputter();
        outter.setFormat(Format.getPrettyFormat());
        outter.output(document, new FileWriter(new File("/Users/joseproigtorres/Desktop/TFG_BBDD/src/fileUtils/estudy.xml")));
    }
}
