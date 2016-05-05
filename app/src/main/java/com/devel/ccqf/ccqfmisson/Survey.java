package com.devel.ccqf.ccqfmisson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devel.ccqf.ccqfmisson.Adapters.CustomSurveyAdapter;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

public class Survey extends AppCompatActivity {
    private List<SurveyObject> listAnswers;
    private List<List<SurveyObject>> listList;
    private ListView listViewAnswers;
    private Button btnNext;
    private Button btnBack;
    private int i = 0;
    private List<String> selectedAnswers;
    private List<String> checkedItems;
    private SparseIntArray selected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        listAnswers = new ArrayList<>();
        listList = new ArrayList<>();
        selectedAnswers = new ArrayList<>();
        checkedItems = new ArrayList<>();
        selected = new SparseIntArray();

        listList.add(dummyList());
        listList.add(dummyList1());
        listList.add(dummyList2());

        listViewAnswers = (ListView)findViewById(R.id.listAnswer);
        setListAnswerAdapter(listList.get(i));
        listViewAnswers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedId = ((TextView) view.findViewById(R.id.txtIDSurvey))
                        .getText().toString();
                CheckedTextView check = ((CheckedTextView) view.findViewById(R.id.txtAnswerSurvey));
               //String checkedId = String.valueOf(position);

                if(selectedAnswers.contains(selectedId)){ //UN-CHECK
                    selectedAnswers.remove(selectedId);
                    selected.delete(i);
                   // checkedItems.remove(String.valueOf(position));
                    check.toggle();
                    Toast.makeText(Survey.this, "Un-check", Toast.LENGTH_SHORT).show();

                }
                else if (selectedAnswers.size() < 1){ //CHECK
                    selectedAnswers.add(selectedId);
                    selected.put(i, position);
                    Toast.makeText(Survey.this, "Check "+selected.get(i), Toast.LENGTH_SHORT).show();
                   // checkedItems.add(checkedId);
                    check.toggle();
                }

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
                 }
                else{
                   // Toast.makeText(Survey.this, i+" nope", Toast.LENGTH_SHORT).show();
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

                   View view = getViewByPosition(1, listViewAnswers);
                   CheckedTextView check = (CheckedTextView)view.findViewById(R.id.txtAnswerSurvey);
                   check.toggle();
                   Toast.makeText(Survey.this, "select"+selected.get(i), Toast.LENGTH_SHORT).show();
                    // NE coche pas les cases quand on revien d'une autre question mais le toast s'affiche quand meme

               }
                else{}
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

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public void setCheckedMarks(int i){
        Toast.makeText(Survey.this, "In setCheckedMrks", Toast.LENGTH_SHORT).show();
           View v = getViewByPosition(Integer.parseInt(String.valueOf(selected.get(i))),
                   listViewAnswers);
            CheckedTextView check = (CheckedTextView)v.findViewById(R.id.txtAnswerSurvey);
            check.toggle();

    }
}
