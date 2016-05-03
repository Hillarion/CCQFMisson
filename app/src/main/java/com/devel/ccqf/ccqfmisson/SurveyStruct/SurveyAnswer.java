package com.devel.ccqf.ccqfmisson.SurveyStruct;

import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyObject.*;
/**
 * Created by thierry on 03/05/16.
 */
public class SurveyAnswer {
    protected int surveyId;
    protected surveyType type;
    protected String reponseTexte;
    protected int reponseInt;

    public SurveyAnswer(){
    }

    public SurveyAnswer(int surveyID, surveyType type, int resp){
        this.surveyId = surveyID;
        this.type = type;
        this.reponseInt = resp;
        this.reponseTexte = null;
    }

    public SurveyAnswer(int surveyID, String resp){
        this.surveyId = surveyID;
        this.type = surveyType.SURVEY_TYPE_TEXT;
        this.reponseTexte = resp;
        this.reponseInt = -1;
    }
}
