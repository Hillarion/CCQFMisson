package com.devel.ccqf.ccqfmisson.SurveyStruct;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.Pair;
import java.util.ArrayList;

/**
 * Created by jo on 5/12/16.
 */
public class SurveyObjectResults /*implements Parcelable*/ {
    private String question;
    private int totalHit;
    private ArrayList<SurveyPair> answersAndHit;


    public SurveyObjectResults() {
        this("", 0, null);
    }

    public SurveyObjectResults(String question, int totalHit, ArrayList<SurveyPair> answersAndHit) {
        this.question = question;
        this.totalHit = totalHit;
        this.answersAndHit = answersAndHit;
    }

    public SurveyObjectResults(String question, int totalHit) {
        this.question = question;
        this.totalHit = totalHit;
    }

    public SurveyObjectResults(String question,  ArrayList<SurveyPair> answersAndHit) {
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

    public ArrayList<SurveyPair> getAnswersAndHit() {
        return answersAndHit;
    }

    public void setAnswersAndHit(ArrayList<SurveyPair> answersAndHit) {
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
/*
    public static final Parcelable.Creator<SurveyObjectResults> CREATOR =
            new Creator<SurveyObjectResults>() {
                @Override
                public SurveyObjectResults createFromParcel(Parcel source) {
                    SurveyObjectResults sor = new SurveyObjectResults();
                    sor.question = source.readString();
                    sor.totalHit = source.readInt();
                    sor.answersAndHit = source.readArrayList();
                    return sor;
                }

                @Override
                public SurveyObjectResults[] newArray(int size) {
                    return new SurveyObjectResults[0];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeInt(totalHit);
        dest.writeList(answersAndHit);
    }*/
}
