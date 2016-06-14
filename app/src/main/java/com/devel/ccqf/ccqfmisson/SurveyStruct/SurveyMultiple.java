package com.devel.ccqf.ccqfmisson.SurveyStruct;

import java.util.ArrayList;

/**
 * Created by thierry on 03/05/16.
 */
public class SurveyMultiple extends SurveyObject{

    public SurveyMultiple(){
        super();
    }

    public SurveyMultiple(int id, int nChoice, String question,  ArrayList<String> choix){
        super(question);
        setQuestionId(id);
        setType(surveyType.SURVEY_TYPE_MULTIPLECHOICE);
        for(int idx = 0; idx<choix.size(); idx++)
            addReponseToChoice(choix.get(idx));
    }

    public SurveyMultiple(int id, String question, int nChoice, String[] choix){
        super(question);
        setQuestionId(id);
        setType(surveyType.SURVEY_TYPE_MULTIPLECHOICE);
        for(int idx = 0; idx<choix.length; idx++)
            addReponseToChoice(choix[idx]);
    }




    public SurveyMultiple(int question, String nChoice, ArrayList<String> choix){
        this(-1, question, nChoice, choix);
    }

}
