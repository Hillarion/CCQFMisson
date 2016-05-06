package com.devel.ccqf.ccqfmisson.SurveyStruct;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thierry on 03/05/16.
 *
 * exprimé en JSON, la classe aura la forme ;
 *    {
 *      id : QuestionId,
 *      type : surveyType,
 *      question : questionTexte,
 *      choix : choixReponse
 *    }
 */
public class SurveyObject {
    public enum surveyType {SURVEY_TYPE_TRUEFALSE, SURVEY_TYPE_MULTIPLECHOICE, SURVEY_TYPE_TEXT }
    protected int QuestionId;
    protected surveyType type;
    protected String questionTexte;
    protected ArrayList<String> choixReponse;

    public SurveyObject(){
        super();
    }

    public SurveyObject(String question){
        super();
        questionTexte = question;
        choixReponse = new ArrayList<String>();
    }
    public SurveyObject(int id, String question){
        super();
        QuestionId = id;
        questionTexte = question;
        choixReponse = new ArrayList<String>();
    }

    public int getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(int id) {
        this.QuestionId = id;
    }

    public surveyType getType() {
        return type;
    }

    public void setType(surveyType type) {
        this.type = type;
    }

    public String getQuestionTexte() {
        return questionTexte;
    }

    public void setQuestionTexte(String questionTexte) {
        this.questionTexte = questionTexte;
    }

    public int getnReponse() {
        return choixReponse.size();
    }


    public List<String> getChoixReponse() {
        return choixReponse;
    }

    public void setChoixReponse(ArrayList<String> choixReponse) {
        this.choixReponse = choixReponse;
    }

    public void addReponseToChoice(String rep){
        choixReponse.add(rep);
    }
}
