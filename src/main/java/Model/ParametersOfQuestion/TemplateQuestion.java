package Model.ParametersOfQuestion;

import java.util.List;

public class TemplateQuestion {

    private int id;

    private String templateQuestion;

    private Project project;

    private List<Answer> answers;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTemplateQuestion() {
        return templateQuestion;
    }

    public void setTemplateQuestion(String templateQuestion) {
        this.templateQuestion = templateQuestion;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
