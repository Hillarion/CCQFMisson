package com.devel.ccqf.ccqfmisson.SurveyStruct;

import java.util.ArrayList;
import java.util.Date;
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

public class SurveyGroup {
    private Date dateLimite;
    private int id;
    private List<SurveyObject> surveyList;

    public Date getDateLimite() {
        return dateLimite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SurveyGroup(){
        surveyList = new ArrayList<>();
    }

    public SurveyGroup(int id, Date date){
        dateLimite = date;
        this.id = id;
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
