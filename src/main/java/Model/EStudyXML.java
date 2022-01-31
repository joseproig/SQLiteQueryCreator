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

    public void addQuestion (String questionName, String question, String solution, String answer, String fileEncodedInBase64) {
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
        Element generalFeedback = new Element("generalfeedback");
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

        addElement (questionToAdd, "answer",  solution);

        addElement(questionToAdd, "validateonsave","1");

        addElement(questionToAdd, "testsplitterre"," ");

        addElement(questionToAdd, "language"," ");

        addElement(questionToAdd, "acelang"," ");

        addElement(questionToAdd, "sandbox"," ");

        addElement(questionToAdd, "grader"," ");

        addElement(questionToAdd, "cputimelimitsecs"," ");

        addElement(questionToAdd, "memlimitmb"," ");

        addElement(questionToAdd, "sandboxparams"," ");

        addElement(questionToAdd, "templateparams"," ");

        addElement(questionToAdd, "hoisttemplateparams","1");

        addElement(questionToAdd, "templateparamslang","twig");

        addElement(questionToAdd, "templateparamsevalpertry","1");

        addElement(questionToAdd, "templateparamsevald","{}");

        addElement(questionToAdd, "twigall","0");

        addElement(questionToAdd, "uiplugin"," ");

        addElement(questionToAdd, "uiparameters"," ");

        addElement(questionToAdd, "attachments","0");

        addElement(questionToAdd, "attachmentsrequired","0");

        addElement(questionToAdd, "maxfilesize","10240");

        addElement(questionToAdd, "filenamesregex"," ");

        addElement(questionToAdd, "filenamesexplain"," ");

        addElement(questionToAdd, "displayfeedback","1");

        Element testCases = new Element("testcases");

        Element testCase = new Element("testcase");
        testCase.setAttribute("testtype","0");
        testCase.setAttribute("useasexample","0");
        testCase.setAttribute("hiderestiffail","0");
        testCase.setAttribute("mark","1.0000000");
        testCases.addContent(testCase);
        Element testCode = new Element("testcode");
        addElement(testCode, "text","");

        Element stdin = new Element("stdin");
        Element text2 = new Element("text");
        stdin.addContent(text2);
        testCase.addContent(stdin);
        Element expected = new Element("expected");
        Element textResult = new Element("text");
        textResult.addContent(answer);
        expected.addContent(textResult);
        testCase.addContent(expected);
        Element extra = new Element("extra");
        Element text3 = new Element("text");
        extra.addContent(text3);
        testCase.addContent(extra);
        Element display = new Element("display");
        addElement(display,"text","SHOW");
        testCase.addContent(display);

        Element file = new Element("file");
        file.setAttribute("name","database.db");
        file.setAttribute("path","/");
        file.setAttribute("encoding","base64");
        file.addContent(fileEncodedInBase64);
        testCases.addContent(file);

        questionToAdd.addContent(testCases);

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

