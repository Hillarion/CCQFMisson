package com.devel.ccqf.ccqfmisson;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.devel.ccqf.ccqfmisson.Adapters.CustomSurveyAnswerAdapter;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyObject;

import java.util.ArrayList;

public class SurveyCreate extends AppCompatActivity {
    private Button btnNewAnswer;
    private EditText txtNewQuestion;
    private Button btnOK;
    private ListView listViewNewAnswers;
    SurveyObject newSurvey;
    ArrayList <String> arrayListNewAnswers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_create);

        txtNewQuestion = (EditText) findViewById(R.id.txtNewQuestion);
        arrayListNewAnswers = new ArrayList<>();
        listViewNewAnswers = (ListView)findViewById(R.id.listViewNewAnswers);

        //Ajout d'un nouveau choix de réponse, c'est aussi la que l'on determinera le type de question
        btnNewAnswer = (Button)findViewById(R.id.btnNewAnswer);
        btnNewAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newAnswer();
            }
        });

        //Modification ou effacement d'un choix de réponse
        listViewNewAnswers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editAnswer(view);
            }
        });

        //Envois du nouveau Survey
        btnOK = (Button)findViewById(R.id.btnNewSurveyDone);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public void newAnswer(){
        final Dialog d = new Dialog(SurveyCreate.this);
        d.setContentView(R.layout.new_answer_dialog);
        d.setTitle("Nouvelle Réponses");
        final EditText txtNewAnswer = (EditText)d.findViewById(R.id.txtDialogNewAnswer);
        Button btnAddAnswer = (Button)d.findViewById(R.id.btnDialogAjouter);
        Button btnDone = (Button)d.findViewById(R.id.btnDialogFin);
        d.show();

        btnAddAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = txtNewAnswer.getText().toString();
                arrayListNewAnswers.add(answer);
                txtNewAnswer.setText("");
                Toast.makeText(SurveyCreate.this, answer+" ajouté", Toast.LENGTH_SHORT).show();
                d.dismiss();

                if(arrayListNewAnswers.size() > 0){
                   refreshListView();
                }
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SurveyCreate.this, arrayListNewAnswers+"", Toast.LENGTH_SHORT).show();
                d.dismiss();
            }
        });
    }

    public void editAnswer(View view){
        TextView text = (TextView)view.findViewById(R.id.txtNewAnswerSurvey);
        final String answer = text.getText().toString();

        final Dialog d = new Dialog(SurveyCreate.this);
        d.setContentView(R.layout.edit_answer_dialog);
        final EditText txtAnswer = (EditText)d.findViewById(R.id.txtEditDialogAnswer);
        Button btnEdit = (Button)d.findViewById(R.id.btnEditDialogEdit);
        Button btnDelete = (Button)d.findViewById(R.id.btnEditDialogDelete);
        Button btnFin = (Button)d.findViewById(R.id.btnEditDialogFin);
        d.show();

        txtAnswer.setText(answer);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newAnswer = txtAnswer.getText().toString();
                int position = getPositionInArray(answer);
                if(position != -1){
                    if(!newAnswer.equals("")){
                        arrayListNewAnswers.set(position, newAnswer);
                        refreshListView();
                        Toast.makeText(SurveyCreate.this, "Modifié", Toast.LENGTH_SHORT).show();
                       // d.dismiss();
                    }
                    else{
                        Toast.makeText(SurveyCreate.this, "Can't modify to null",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else{}
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getPositionInArray(answer);
                if(position != -1){
                    arrayListNewAnswers.remove(position);
                    refreshListView();
                    d.dismiss();
                }
            }
        });
        btnFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
    }

    public void newSurveyObject(){
        String question = txtNewQuestion.getText().toString();
        newSurvey = new SurveyObject(1,question, arrayListNewAnswers);
    }

    public int getPositionInArray(String string){
        int i = -1;
        for(i = 0 ; i < arrayListNewAnswers.size() ; i++){
            if(arrayListNewAnswers.get(i).equals(string)){
                return i;
            }
            else {
                return -1;
            }
        }
        return i;
    }

    public void refreshListView(){
        listViewNewAnswers.setAdapter(null);
        listViewNewAnswers.setAdapter(new CustomSurveyAnswerAdapter
                (SurveyCreate.this, arrayListNewAnswers));
    }
}

