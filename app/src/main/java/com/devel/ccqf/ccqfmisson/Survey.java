package com.devel.ccqf.ccqfmisson;

import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devel.ccqf.ccqfmisson.Adapters.CustomSurveyAdapter;
import com.devel.ccqf.ccqfmisson.Database.interfaceDB;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyAnswer;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyGroup;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyObject;

import java.util.ArrayList;
import java.util.List;

public class Survey extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        listAnswers = new ArrayList<>();
      //  listList = new ArrayList<>();
        selectedAnswers = new ArrayList<>();
        checkedItems = new ArrayList<>();
        selectedPosition = new SparseIntArray();

        listViewAnswers = (ListView)findViewById(R.id.listAnswer);
        txtQuestion = (TextView)findViewById(R.id.txtQuestionSurvey);

        //Affichage de la premiere question et ses choix de réponses
        new GetSurveyAsyncTask().execute(3);

        listViewAnswers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*String selectedId = ((TextView) view.findViewById(R.id.txtIDSurvey))
                        .getText().toString();*/
                String selectedId = ""+(position+1);
                CheckedTextView check = ((CheckedTextView) view.findViewById(R.id.txtAnswerSurvey));
               //String checkedId = String.valueOf(position);

                if(selectedAnswers.contains(selectedId)){ //UN-CHECK
                    selectedAnswers.remove(selectedId);
                   // selectedPosition.delete(surveyQuestionIndex);
                   // checkedItems.remove(String.valueOf(position));
                    check.toggle();
                   // Toast.makeText(Survey.this, "Un-check", Toast.LENGTH_SHORT).show();

                }
                else if (selectedAnswers.size() < 1){ //CHECK
                    selectedAnswers.add(selectedId);
                   // selectedPosition.put(surveyQuestionIndex, position);
                   // checkedItems.add(checkedId);
                    check.toggle();
                    /*Toast.makeText(Survey.this, "Check "+selectedId,
                            Toast.LENGTH_SHORT).show();*/
                }

            }
        });


        btnNext = (ImageButton)findViewById(R.id.btnNextSurvey);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Send Answer
/*                new SendAnswerAsyncTask().execute(new SurveyAnswer(workingSurvey.getId(),
                                                listSurveyGroup.get(surveyQuestionIndex).getType(),
                                                selectedAnswers));*/
                Toast.makeText(Survey.this, "Sent (not)", Toast.LENGTH_SHORT).show();
                //Reload list
                if(surveyQuestionIndex < listSurveyGroup.size()-1){
                    surveyQuestionIndex++;
                    listViewAnswers.setAdapter(null);
                    setListAnswerAdapter(listSurveyGroup.get(surveyQuestionIndex).getChoixReponse());
                    txtQuestion.setText(listSurveyGroup.get(surveyQuestionIndex).getQuestionTexte());
                    selectedAnswers = new ArrayList<String>();
                }
                else {
                    txtQuestion.setText("All caught up !");
                    listViewAnswers.setVisibility(View.GONE);
                }
               /* if (surveyQuestionIndex < listList.size()-1) {
                    surveyQuestionIndex++;
                    listViewAnswers.setAdapter(null);
                    setListAnswerAdapter(listList.get(surveyQuestionIndex));
                 }
                else{
                   // Toast.makeText(Survey.this, surveyQuestionIndex+" nope", Toast.LENGTH_SHORT).show();
                }*/
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

    //Liste Temporaire
    public List<SurveyObject> dummySurveyGroup(){
        ArrayList<SurveyObject> dummy = new ArrayList<>();
        ArrayList<String> a =new ArrayList<>();
        a.add("Vert");
        a.add("Bleu");
        a.add("Gris");
        dummy.add(new SurveyObject(1, "Couleur préféré ?",a));

        ArrayList<String> b =new ArrayList<>();
        b.add("Oui");
        b.add("Non");
        dummy.add(new SurveyObject(2, "Oui ou Non ?",b));

        ArrayList<String> c =new ArrayList<>();
        c.add("1");
        c.add("2");
        c.add("3");
        c.add("4");
        dummy.add(new SurveyObject(3, "1 a 4 ?",c));
        return dummy;
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
        protected SurveyGroup doInBackground(Integer... adr) {
            SurveyGroup srvGrp = null;
            interfaceDB iDb = new interfaceDB(Survey.this);
            if(iDb != null)
                srvGrp = iDb.readSurvey(adr[0].intValue());
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
                listSurveyGroup = dummySurveyGroup();
            surveyQuestionIndex=0;
            setListAnswerAdapter(listSurveyGroup.get(surveyQuestionIndex).getChoixReponse());
            txtQuestion.setText(listSurveyGroup.get(surveyQuestionIndex).getQuestionTexte());
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
            interfaceDB iDb = new interfaceDB(Survey.this);
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
