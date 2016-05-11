package com.devel.ccqf.ccqfmisson.SurveyStruct;

/**
 * Created by thierry on 03/05/16.
 */
public class SurveyYesNo extends SurveyObject{

    public SurveyYesNo(){
        super();
    }

    public SurveyYesNo(int id, String question){
        super(question);
        setQuestionId(id);
        setType(surveyType.SURVEY_TYPE_TRUEFALSE);
    }

    public SurveyYesNo(String question){
        this(-1, question);
    }
}
