package com.devel.ccqf.ccqfmisson.SurveyStruct;

import android.support.v4.util.Pair;
import java.util.ArrayList;

/**
 * Created by jo on 5/12/16.
 */
public class SurveyObjectResults {
    private String question;
    private int totalHit;
    private ArrayList<Pair> answersAndHit;

    public SurveyObjectResults(String question, int totalHit, ArrayList<Pair> answersAndHit) {
        this.question = question;
        this.totalHit = totalHit;
        this.answersAndHit = answersAndHit;
    }

    public SurveyObjectResults(String question, int totalHit) {
        this.question = question;
        this.totalHit = totalHit;
    }

    public SurveyObjectResults(String question,  ArrayList<Pair> answersAndHit) {
        this.question = question;
        this.answersAndHit = answersAndHit;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getTotalHit() {
        return totalHit;
    }

    public void setTotalHit(int totalHit) {
        this.totalHit = totalHit;
    }

    public ArrayList<Pair> getAnswersAndHit() {
        return answersAndHit;
    }

    public void setAnswersAndHit(ArrayList<Pair> answersAndHit) {
        this.answersAndHit = answersAndHit;
    }

    @Override
    public String toString() {
        return "SurveyObjectResults{" +
                "question='" + question + '\'' +
                ", totalHit=" + totalHit +
                ", answersAndHit=" + answersAndHit +
                '}';
    }
}
