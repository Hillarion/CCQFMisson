package com.devel.ccqf.ccqfmisson;

import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyObjectResults;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class SurveyResultDetails extends AppCompatActivity {
    private TextView txtQuestion;
    private TextView txtDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_result_details);
        txtDetails = (TextView)findViewById(R.id.txtAnswerDetails);
        txtQuestion = (TextView)findViewById(R.id.txtQuestionResultDetails);

        txtQuestion.setText(dummyObject().getQuestion());
        ArrayList<Pair> p = dummyObject().getAnswersAndHit();
        txtDetails.setText(jsndfkjhsdf());
    }

    private SurveyObjectResults dummyObject(){
        Pair p1 = new Pair<>("A", 3);
        Pair p2 = new Pair<>("B", 4);
        Pair p3 = new Pair<>("C", 2);

        ArrayList<Pair> p = new ArrayList<>();
        p.add(p1);
        p.add(p2);
        p.add(p3);

        SurveyObjectResults sor = new SurveyObjectResults("Hola",p);
        return sor;
    }
    private String jsndfkjhsdf(){ //Il était 3h
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < dummyObject().getAnswersAndHit().size(); i++){
            s.append(("\n"+dummyObject().getAnswersAndHit().get(i).first+
                    " "+dummyObject().getAnswersAndHit().get(i).second));
        }
        String string = s.toString();
        return string;
    }

}
