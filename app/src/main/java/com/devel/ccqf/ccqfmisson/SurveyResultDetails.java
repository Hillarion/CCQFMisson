package com.devel.ccqf.ccqfmisson;

import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyObjectResults;

import java.util.ArrayList;

public class SurveyResultDetails extends AppCompatActivity {
    private TextView txtQuestion;
    private TextView txtDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_result_details);
        txtDetails = (TextView)findViewById(R.id.txtAnswerDetails);
        txtQuestion = (TextView)findViewById(R.id.txtQuestionResultDetails);

    }
    private SurveyObjectResults dummyObject(){
        Pair pair = new Pair<>(1, 3);
        ArrayList<Pair> p = new ArrayList<>();
        p.add(pair);

        SurveyObjectResults sor = new SurveyObjectResults("Hola",p);
        return sor;
    }
}
