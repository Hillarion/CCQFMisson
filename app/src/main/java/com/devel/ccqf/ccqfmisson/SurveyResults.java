package com.devel.ccqf.ccqfmisson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.devel.ccqf.ccqfmisson.Adapters.CustomSurveyResultsAdapter;
import com.devel.ccqf.ccqfmisson.Database.interfaceDB;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyObject;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyObjectResults;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SurveyResults extends AppCompatActivity {
//    public final static String PAR_KEY = "com.devel.ccqf.ccqfmisson.PAR_KEY";
//    public final static String PAR_KEY2 = "com.devel.ccqf.ccqfmisson.PAR_KEY2";
    private ListView listResults;
    private ArrayList<SurveyObjectResults> l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_results);

        l = new ArrayList<>();
        interfaceDB iDb = new interfaceDB(this);
        System.out.print("CCQF Mission SurveyResults iDb = " + iDb + "\n\n");
        System.out.flush();
        if(iDb != null) {
            l = iDb.readSurveyResults(1);
            System.out.print("CCQF Mission SurveyResults l = " + l + "\n\n");
            System.out.flush();
        }
        else
            l = dummyList();
        listResults = (ListView)findViewById(R.id.listViewSurveyResults);
        listResults.setAdapter(new CustomSurveyResultsAdapter(SurveyResults.this, l));

        listResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SurveyResults.this, SurveyResultDetails.class);
/*                Bundle donnees = new Bundle();
                donnees.putString(PAR_KEY, l.get(position).getQuestion()   );
                donnees.putParcelableArrayList(PAR_KEY2, l.get(position).getAnswersAndHit());
                i.putExtra("Question", donnees);
*/
                startActivity(i);
            }
        });

    }
    public ArrayList<SurveyObjectResults> dummyList(){
        ArrayList<SurveyObjectResults> list = new ArrayList<>();
        for(int i = 0;i < 5;i++){
            list.add(new SurveyObjectResults("jhgjhg",i));
        }
        return  list;
    }
}
