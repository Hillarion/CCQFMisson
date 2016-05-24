package com.devel.ccqf.ccqfmisson.SurveyStruct;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jo on 5/24/16.
 */
public class SurveyPair implements Parcelable {
    String answer;
    String hits;

    public SurveyPair(String answer, String hits) {
        this.answer = answer;
        this.hits = hits;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public SurveyPair(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this.answer = data[0];
        this.hits = data[1];
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.answer, this.hits});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public SurveyPair createFromParcel(Parcel in) {
            return new SurveyPair(in);
        }

        public SurveyPair[] newArray(int size) {
            return new SurveyPair[size];
        }
    };

    @Override
    public String toString() {
        return "SurveyPair{" +
                "answer='" + answer + '\'' +
                ", hits='" + hits + '\'' +
                '}';
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }
}
