package com.devel.ccqf.ccqfmisson.SurveyStruct;

/**
 * Created by thierry on 03/05/16.
 */
public class SurveyText extends SurveyObject{

    public SurveyText(){
        super();
    }

    public SurveyText(int id, String question){
        super(question);
        setQuestionId(id);;
        setType(surveyType.SURVEY_TYPE_TEXT);
    }


    public SurveyText(String question){
        this(-1, question);
    }
}
