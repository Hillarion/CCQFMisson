package com.devel.ccqf.ccqfmisson.SurveyStruct;

import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyObject.*;
/**
 * Created by thierry on 03/05/16.
 */
public class SurveyAnswer {
    protected int questionId;
    protected surveyType type;
    protected String reponseTexte;
    protected int reponseInt;

    public SurveyAnswer(){
    }

    public SurveyAnswer(int surveyID, surveyType type, int resp){
        this.questionId = surveyID;
        this.type = type;
        this.reponseInt = resp;
        this.reponseTexte = null;
    }

    public SurveyAnswer(int surveyID, String resp){
        this.questionId = surveyID;
        this.type = surveyType.SURVEY_TYPE_TEXT;
        this.reponseTexte = resp;
        this.reponseInt = -1;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public surveyType getType() {
        return type;
    }

    public void setType(surveyType type) {
        this.type = type;
    }

    public String getReponseTexte() {
        return reponseTexte;
    }

    public void setReponseTexte(String reponseTexte) {
        this.reponseTexte = reponseTexte;
    }

    public int getReponseInt() {
        return reponseInt;
    }

    public void setReponseInt(int reponseInt) {
        this.reponseInt = reponseInt;
    }
}
