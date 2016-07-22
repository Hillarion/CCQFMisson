package com.devel.ccqf.ccqfmisson;

import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyObjectResults;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyPair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class SurveyResultDetails extends CCQFBaseActivity {
    private TextView txtQuestion;
    private TextView txtDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_result_details);
        txtDetails = (TextView)findViewById(R.id.txtAnswerDetails);
        txtQuestion = (TextView)findViewById(R.id.txtQuestionResultDetails);
        Intent i = getIntent();

        Bundle bundle = i.getExtras();

        String q = bundle.getString(SurveyResults.PAR_KEY);

        ArrayList<SurveyPair> p = bundle.getParcelableArrayList(SurveyResults.PAR_KEY2);

        txtQuestion.setText(q);

        StringBuilder s = new StringBuilder();
        for(int idx = 0; idx < p.size(); idx++) {
            s.append(("\n" + p.get(idx).getAnswer() + " :      " + p.get(idx).getHits()));
            txtDetails.setText(s);
        }
    }
/*
    private SurveyObjectResults dummyObject(){
        SurveyPair p1 = new SurveyPair("A", "3");
        SurveyPair p2 = new SurveyPair("B", "4");
        SurveyPair p3 = new SurveyPair("C", "2");

        ArrayList<SurveyPair> p = new ArrayList<>();
        p.add(p1);
        p.add(p2);
        p.add(p3);

        SurveyObjectResults sor = new SurveyObjectResults("Hola",p);
        return sor;
    }
    private String textDetails(){
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < dummyObject().getAnswersAndHit().size(); i++){
            s.append(("\n"+dummyObject().getAnswersAndHit().get(i).getAnswer()+
                    " "+dummyObject().getAnswersAndHit().get(i).getHits()));
        }
        String string = s.toString();
        return string;
    }*/

}
