package uniba.tesi.magicwand.Model;

import java.io.Serializable;

public class Question implements Serializable {

    private int id;
    private String question;
    private String opt_a;
    private String opt_b;
    private String opt_c;
    private String opt_d;
    private String answer;


    public Question(int id, String question, String opt_a, String opt_b, String opt_c, String opt_d, String answer) {

        this.id = id;
        this.question = question;
        this.opt_a = opt_a;
        this.opt_b = opt_b;
        this.opt_c = opt_c;
        this.opt_d = opt_d;
        this.answer = answer;
    }

    public Question() {
    }

    //getter and setter

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpt_a() {
        return opt_a;
    }

    public void setOpt_a(String opt_a) {
        this.opt_a = opt_a;
    }

    public String getOpt_b() {
        return opt_b;
    }

    public void setOpt_b(String opt_b) {
        this.opt_b = opt_b;
    }

    public String getOpt_c() {
        return opt_c;
    }

    public void setOpt_c(String opt_c) {
        this.opt_c = opt_c;
    }

    public String getOpt_d() {
        return opt_d;
    }

    public void setOpt_d(String opt_d) {
        this.opt_d = opt_d;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
