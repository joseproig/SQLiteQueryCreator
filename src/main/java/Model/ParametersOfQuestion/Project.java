package Model.ParametersOfQuestion;

import java.util.List;

public class Project {

    private int id;


    private String pathToDbFile;


    private String name;


    private String description;


    //private List<TemplateQuestion> templateQuestions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPathToDbFile() {
        return pathToDbFile;
    }

    public void setPathToDbFile(String pathToDbFile) {
        this.pathToDbFile = pathToDbFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*public List<TemplateQuestion> getTemplateQuestions() {
        return templateQuestions;
    }

    public void setTemplateQuestions(List<TemplateQuestion> templateQuestions) {
        this.templateQuestions = templateQuestions;
    }*/
}
