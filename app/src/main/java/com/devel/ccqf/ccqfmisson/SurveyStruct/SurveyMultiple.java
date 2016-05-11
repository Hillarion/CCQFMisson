package com.devel.ccqf.ccqfmisson.SurveyStruct;

/**
 * Created by thierry on 03/05/16.
 */
public class SurveyMultiple extends SurveyObject{

    public SurveyMultiple(){
        super();
    }

    public SurveyMultiple(int id, String question, int nChoice, String[] choix){
        super(question);
        setQuestionId(id);
        setType(surveyType.SURVEY_TYPE_MULTIPLECHOICE);
        for(int idx = 0; idx<choix.length; idx++)
            addReponseToChoice(choix[idx]);
    }

    public SurveyMultiple(String question, int nChoice, String[] choix){
        this(-1, question, nChoice, choix);
    }

}
