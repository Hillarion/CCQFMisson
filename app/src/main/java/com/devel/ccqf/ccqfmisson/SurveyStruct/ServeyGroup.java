package com.devel.ccqf.ccqfmisson.SurveyStruct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thierry on 05/05/16.
 *
 * Cette classe n'a pour but que de faciliter le regroupement des question à la réception via
 *  internet. Le message internet, exprimé en JSON, aura la forme :
 *
 *  {survey :
 *      {
 *          id : n,
 *          questions : [SurveyObject, ...]
 *      }
 *  }
 */

public class ServeyGroup {
    private List<SurveyObject> surveyList;

    public ServeyGroup(){
        surveyList = new ArrayList<>();
    }

    public int addQuestion(SurveyObject question){
        surveyList.add(question);
        return surveyList.size();
    }

    public List<SurveyObject> getQuestions(){
        return surveyList;
    }

    public int getSurveyLength(){
        return surveyList.size();
    }
}
