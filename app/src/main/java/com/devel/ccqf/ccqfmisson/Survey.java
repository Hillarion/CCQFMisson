package com.devel.ccqf.ccqfmisson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.devel.ccqf.ccqfmisson.Adapters.CustomSurveyAdapter;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyObject;

import java.util.ArrayList;
import java.util.List;

public class Survey extends AppCompatActivity {
    private List<SurveyObject> listAnswers;
    private List<List<SurveyObject>> listList;
    private ListView listViewAnswers;
    private Button btnNext;
    private Button btnBack;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        listAnswers = new ArrayList<>();
        listList = new ArrayList<>();

        listList.add(dummyList());
        listList.add(dummyList1());
        listList.add(dummyList2());

        listViewAnswers = (ListView)findViewById(R.id.listAnswer);
        setListAnswerAdapter(listList.get(i));
        listViewAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnNext = (Button)findViewById(R.id.btnNextSurvey);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i < listList.size()-1) {
                    i++;
                    listViewAnswers.setAdapter(null);
                    setListAnswerAdapter(listList.get(i));
                    Toast.makeText(Survey.this, i+"", Toast.LENGTH_SHORT).show();
                 }
                else{
                    Toast.makeText(Survey.this, i+" nope", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack = (Button)findViewById(R.id.btnBackSurvey);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i > 0){
                    i--;
                    listViewAnswers.setAdapter(null);
                    setListAnswerAdapter(listList.get(i));
                    Toast.makeText(Survey.this, i+"", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Survey.this, i+" nope", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void setListAnswerAdapter(List<SurveyObject> answers){
        listViewAnswers.setAdapter(new CustomSurveyAdapter(this, answers));
    }

    public List<SurveyObject> dummyList(){
        List<SurveyObject> dummy = new ArrayList<>();
        dummy.add(new SurveyObject(1, "Bleu"));
        dummy.add(new SurveyObject(2, "Jaune"));
        dummy.add(new SurveyObject(3, "Vert"));
        return dummy;
    }

    public List<SurveyObject> dummyList1(){
        List<SurveyObject> dummy = new ArrayList<>();
        dummy.add(new SurveyObject(1, "Blue"));
        dummy.add(new SurveyObject(2, "Green"));
        dummy.add(new SurveyObject(3, "Red"));
        return dummy;
    }

    public List<SurveyObject> dummyList2(){
        List<SurveyObject> dummy = new ArrayList<>();
        dummy.add(new SurveyObject(1, "Oui"));
        dummy.add(new SurveyObject(2, "Non"));
        return dummy;
    }
}
