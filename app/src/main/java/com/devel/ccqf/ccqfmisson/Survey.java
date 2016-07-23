package com.devel.ccqf.ccqfmisson;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devel.ccqf.ccqfmisson.Adapters.CustomSurveyAdapter;
import com.devel.ccqf.ccqfmisson.Database.InterfaceDB;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyAnswer;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyGroup;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Survey extends CCQFBaseActivity {
    private List<SurveyObject> listAnswers;
    private List<List<SurveyObject>> listList;
    private ListView listViewAnswers;
    private TextView txtQuestion;
    private ImageButton btnNext;
    private ImageButton btnBack;
    private int surveyQuestionIndex = 0;
    private List<String> selectedAnswers;
    private List<String> checkedItems;
    private SparseIntArray selectedPosition;
    private SurveyGroup workingSurvey;
    private static List<SurveyObject> listSurveyGroup = null;
    private InterfaceDB iDb = null;
    private ArrayList<Integer> surveyListe = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        iDb = new InterfaceDB(this);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        surveyListe = bundle.getIntegerArrayList("surveyListe");
        Collections.sort(surveyListe);

        listAnswers = new ArrayList<>();
        selectedAnswers = new ArrayList<>();
        checkedItems = new ArrayList<>();
        selectedPosition = new SparseIntArray();

        listViewAnswers = (ListView)findViewById(R.id.listAnswer);
        txtQuestion = (TextView)findViewById(R.id.txtQuestionSurvey);

        Iterator<Integer> iter = surveyListe.iterator();
        Integer itg = null;
        while(iter.hasNext()){
            int idx = iDb.getLastSurveyIndex();
            itg = iter.next();
            if(itg > idx)
                break;
        }

        //Affichage de la premiere question et ses choix de réponses
        new GetSurveyAsyncTask().execute(itg);

        listViewAnswers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedId = ""+(position+1);
                CheckedTextView check = ((CheckedTextView) view.findViewById(R.id.txtAnswerSurvey));

                if(selectedAnswers.contains(selectedId)){ //UN-CHECK
                    selectedAnswers.remove(selectedId);
                    check.toggle();
                }
                else if (selectedAnswers.size() < 1){ //CHECK
                    selectedAnswers.add(selectedId);
                    check.toggle();
                }

            }
        });


        btnNext = (ImageButton)findViewById(R.id.btnNextSurvey);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Send Answer
                SurveyAnswer [] sAnswer = new SurveyAnswer[1];
                int srvID = workingSurvey.getId();
                SurveyObject.surveyType type = listSurveyGroup.get(surveyQuestionIndex).getType();
                if(selectedAnswers.size() > 0) {
                    String tmpText = selectedAnswers.get(0);
                    int respIdxVal = 0;
                    if (tmpText.matches("[0-9]*"))
                        respIdxVal = Integer.parseInt(tmpText);

                    sAnswer[0] = new SurveyAnswer(srvID, type, respIdxVal - 1);
                    new SendAnswerAsyncTask().execute(sAnswer);

                    //Reload list
                    if (surveyQuestionIndex < listSurveyGroup.size() - 1) {
                        surveyQuestionIndex++;
                        listViewAnswers.setAdapter(null);
                        setListAnswerAdapter(listSurveyGroup.get(surveyQuestionIndex).getChoixReponse());
                        txtQuestion.setText(listSurveyGroup.get(surveyQuestionIndex).getQuestionTexte());
                        selectedAnswers = new ArrayList<String>();
                    } else {
                        txtQuestion.setText("All caught up !");
                        listViewAnswers.setVisibility(View.GONE);
                        iDb.setLastSurveyIndex(srvID);
                    }
                }
            }
        });

      /*  btnBack = (Button)findViewById(R.id.btnBackSurvey);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(surveyQuestionIndex > 0){
                   surveyQuestionIndex--;
                   listViewAnswers.setAdapter(null);
                   setListAnswerAdapter(listList.get(surveyQuestionIndex));

                       View view = getViewByPosition(selectedPosition.get(surveyQuestionIndex), listViewAnswers);
                       CheckedTextView check = (CheckedTextView) view.findViewById(R.id.txtAnswerSurvey);
                       check.toggle();
                       Toast.makeText(Survey.this, "selectedPosition.get(surveyQuestionIndex) = " + selectedPosition.get(surveyQuestionIndex)
                               + "\n check.txt =" + check.getText().toString(), Toast.LENGTH_SHORT).show();
                       // NE coche pas les cases quand on revien d'une autre question mais le toast s'affiche quand meme
                       voir notifyDataSetChanged();
               }
                else{}
            }
        });*/
    }

    public void setListAnswerAdapter(ArrayList<String> answers){
        listViewAnswers.setAdapter(new CustomSurveyAdapter(this, answers));
    }



    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        }
        else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    //Méthode pour "checker" les case qui devraient l'être
    public void setCheckedMarks(int i){
        Toast.makeText(Survey.this, "In setCheckedMrks", Toast.LENGTH_SHORT).show();
           View v = getViewByPosition(Integer.parseInt(String.valueOf(selectedPosition.get(i))),
                   listViewAnswers);
            CheckedTextView check = (CheckedTextView)v.findViewById(R.id.txtAnswerSurvey);
            check.toggle();

    }

    private class GetSurveyAsyncTask extends AsyncTask<Integer, Void, SurveyGroup> {
        @Override
        protected SurveyGroup doInBackground(Integer... surveyIDTable) {
            SurveyGroup srvGrp = null;
            if(iDb != null)
                srvGrp = iDb.readSurvey(surveyIDTable[0].intValue());
            return srvGrp;
        }

        @Override
        protected void onPostExecute(SurveyGroup sGrp) {
            // execution of result of Long time consuming operation
            if(sGrp != null) {
                workingSurvey = sGrp;
                listSurveyGroup = sGrp.getQuestions();
            }
            else
               // listSurveyGroup = dummySurveyGroup();
            surveyQuestionIndex=0;
            if(listSurveyGroup != null) {
                if(surveyQuestionIndex < listSurveyGroup.size()) {
                    setListAnswerAdapter(listSurveyGroup.get(surveyQuestionIndex).getChoixReponse());
                    txtQuestion.setText(listSurveyGroup.get(surveyQuestionIndex).getQuestionTexte());
                }
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

    private class SendAnswerAsyncTask extends AsyncTask<SurveyAnswer,Void,Void>{
        @Override
        protected Void doInBackground(SurveyAnswer... answ) {
            InterfaceDB iDb = new InterfaceDB(Survey.this);
            if(iDb != null)
                iDb.answerSurveyQuestion(answ[0]);
            return null;
        }

        protected void onPostExecute(Void... unused) {

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
