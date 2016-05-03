package com.devel.ccqf.ccqfmisson.SurveyStruct;

/**
 * Created by thierry on 03/05/16.
 */
public class SurveyText extends SurveyObject{

    public SurveyText(){
        super();
    }

    public SurveyText(String question){
        super(question);
        setType(surveyType.SURVEY_TYPE_TEXT);
    }
}
