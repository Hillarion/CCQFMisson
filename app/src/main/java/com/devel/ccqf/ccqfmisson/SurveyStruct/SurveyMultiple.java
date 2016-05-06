package com.devel.ccqf.ccqfmisson.SurveyStruct;

/**
 * Created by thierry on 03/05/16.
 */
public class SurveyMultiple extends SurveyObject{

    SurveyMultiple(){
        super();
    }

    SurveyMultiple(String question, int nChoice, String[] choix){
        super(question);
        setType(surveyType.SURVEY_TYPE_MULTIPLECHOICE);
        setnReponse(nChoice);
        for(int idx = 0; idx<choix.length; idx++)
            addReponseToChoice(choix[idx]);
    }
}
