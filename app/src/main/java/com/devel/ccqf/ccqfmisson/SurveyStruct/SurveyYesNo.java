package com.devel.ccqf.ccqfmisson.SurveyStruct;

/**
 * Created by thierry on 03/05/16.
 */
public class SurveyYesNo extends SurveyObject{

    public SurveyYesNo(){
        super();
    }

    public SurveyYesNo(String question){
        super(question);
        setType(surveyType.SURVEY_TYPE_TRUEFALSE);
    }

}
