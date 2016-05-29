package com.devel.ccqf.ccqfmisson;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.devel.ccqf.ccqfmisson.Adapters.CustomSurveyResultsAdapter;
import com.devel.ccqf.ccqfmisson.Database.InterfaceDB;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyObjectResults;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyPair;

import java.util.ArrayList;

public class SurveyResults extends AppCompatActivity {
    public final static String PAR_KEY = "com.devel.ccqf.ccqfmisson.SurveyResults.PAR_KEY";
    public final static String PAR_KEY2 = "com.devel.ccqf.ccqfmisson.SurveyResults.PAR_KEY2";
    private ListView listResults;
    private ArrayList<SurveyObjectResults> l;
    private ArrayList<SurveyPair> listPair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_results);

        new GetSurveyResultsAsyncTask().execute(1);
        listResults = (ListView)findViewById(R.id.listViewSurveyResults);

        listResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SurveyResults.this, SurveyResultDetails.class);
                Bundle donnees = new Bundle();
                SurveyObjectResults sor = l.get(position);
                donnees.putString(PAR_KEY, sor.getQuestion());
                donnees.putParcelableArrayList(PAR_KEY2, sor.getAnswersAndHit());
                i.putExtras(donnees);

                startActivity(i);
            }
        });

    }
    public ArrayList<SurveyObjectResults> dummyList(){
        ArrayList<SurveyObjectResults> list = new ArrayList<>();
        for(int i = 0;i < 5;i++){
            list.add(new SurveyObjectResults("jhgjhg", i));
        }
        return  list;
    }


    private class GetSurveyResultsAsyncTask extends AsyncTask<Integer, Void, ArrayList<SurveyObjectResults> > {
        @Override
        protected ArrayList<SurveyObjectResults>  doInBackground(Integer... adr) {
            ArrayList<SurveyObjectResults> responsesList = null;
            InterfaceDB iDb = new InterfaceDB(SurveyResults.this);
            if(iDb != null)
                responsesList = iDb.readSurveyResults(adr[0].intValue());
            return responsesList;
        }

        @Override
        protected void onPostExecute(ArrayList<SurveyObjectResults>  sList) {
            // execution of result of Long time consuming operation
            if(sList != null) {
                l = sList;
                listResults.setAdapter(new CustomSurveyResultsAdapter(SurveyResults.this, sList));
            }
        }

        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }

        @Override
        protected void onProgressUpdate(Void... text) {
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }

    }

}
