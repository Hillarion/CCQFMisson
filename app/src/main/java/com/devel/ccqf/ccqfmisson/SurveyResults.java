package com.devel.ccqf.ccqfmisson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.devel.ccqf.ccqfmisson.Adapters.CustomSurveyResultsAdapter;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SurveyResults extends AppCompatActivity {
    private ListView listResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_results);


        ArrayList<SurveyObject> l = new ArrayList<>();
        l = dummyList();
        Toast.makeText(SurveyResults.this, ""+l, Toast.LENGTH_SHORT).show();
        listResults = (ListView)findViewById(R.id.listViewSurveyResults);
        listResults.setAdapter(new CustomSurveyResultsAdapter(SurveyResults.this, dummyList()));

    }
    public ArrayList<SurveyObject> dummyList(){
        ArrayList<SurveyObject> list = new ArrayList<>();
        for(int i = 0;i < 5;i++){
            list.add(new SurveyObject("jhgjhg",i));
        }
        return  list;
    }
}