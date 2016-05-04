package com.devel.ccqf.ccqfmisson.SurveyStruct;

/**
 * Created by thierry on 03/05/16.
 */
public class SurveyObject {
    public enum surveyType {SURVEY_TYPE_TRUEFALSE, SURVEY_TYPE_MULTIPLECHOICE, SURVEY_TYPE_TEXT }
    protected int surveyId;
    protected surveyType type;
    protected String questionTexte;
    protected int nReponse;
    protected String [] choixReponse;

    public SurveyObject(){
        super();
    }

    public SurveyObject(String question){
        super();
        questionTexte = question;
    }
    public SurveyObject(int id, String question){
        super();
        surveyId = id;
        questionTexte = question;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int id) {
        this.surveyId = id;
    }

    public surveyType getType() {
        return type;
    }

    public void setType(surveyType type) {
        this.type = type;
    }

    public String getQuestionTexte() {
        return questionTexte;
    }

    public void setQuestionTexte(String questionTexte) {
        this.questionTexte = questionTexte;
    }

    public int getnReponse() {
        return nReponse;
    }

    public void setnReponse(int nReponse) {
        this.nReponse = nReponse;
    }

    public String[] getChoixReponse() {
        return choixReponse;
    }

    public void setChoixReponse(String[] choixReponse) {
        this.choixReponse = choixReponse;
    }
}
