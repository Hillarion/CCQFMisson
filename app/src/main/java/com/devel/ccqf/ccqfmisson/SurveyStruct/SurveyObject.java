package com.devel.ccqf.ccqfmisson.SurveyStruct;

import com.devel.ccqf.ccqfmisson.Survey;

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
    protected int questionId;
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
    public SurveyObject(int id, String question, ArrayList<String> reponse){
        super();
        questionId = id;
        questionTexte = question;
        choixReponse = reponse;
       // choixReponse = new ArrayList<String>();
    }
    //Constructeur temporaire a fin de faire des test
    public SurveyObject(String question, int id){
        super();
        questionTexte = question;
        questionId = id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int id) {
        this.questionId = id;
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


    public ArrayList<String> getChoixReponse() {
        return choixReponse;
    }

    public void setChoixReponse(ArrayList<String> choixReponse) {
        this.choixReponse = choixReponse;
    }

    public void addReponseToChoice(String rep){
        choixReponse.add(rep);
    }
}
