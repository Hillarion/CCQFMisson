package com.devel.ccqf.ccqfmisson;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.devel.ccqf.ccqfmisson.Adapters.CustomSurveyListAdapter;
import com.devel.ccqf.ccqfmisson.Database.InterfaceDB;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyGroup;

import java.util.ArrayList;

/**
 * Created by thierry on 19/07/16.
 */
public class SurveyList extends CCQFBaseActivity {
    public final static String KEY_SURVEY_ID = "com.devel.ccqf.ccqfmisson.SurveyList.KEY_SURVEY_ID";
    private ListView surveyListView;
    private InterfaceDB iDb;
    private ArrayList<SurveyGroup> surveyList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_results);
        iDb = new InterfaceDB(SurveyList.this);

        surveyListView = (ListView) findViewById(R.id.listViewSurveyResults);
        new GetSurveyListAsyncTask().execute();
        surveyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SurveyList.this, SurveyResults.class);
                if(surveyList != null) {
                    Bundle donnees = new Bundle();
                    donnees.putInt(KEY_SURVEY_ID, surveyList.get(position).getId());
                    i.putExtras(donnees);
                }
                startActivity(i);
            }
        });
    }

    private class GetSurveyListAsyncTask extends AsyncTask<Void, Void, ArrayList<SurveyGroup> >{
        @Override
        protected ArrayList<SurveyGroup> doInBackground(Void... unused){
            ArrayList<SurveyGroup> sList = null;
            if(iDb != null)
                sList = iDb.getSurveyList();
            return sList;
        }

        protected void onPostExecute(ArrayList<SurveyGroup> sList){
            surveyList = sList;

            surveyListView.setAdapter(new CustomSurveyListAdapter(SurveyList.this,surveyList));
        }
    }
}