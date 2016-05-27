package com.devel.ccqf.ccqfmisson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class DocDetails extends AppCompatActivity {
    ListView listViewDocDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_details);

        listViewDocDetail = (ListView)findViewById(R.id.listViewDocDetails);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (DocDetails.this, R.layout.docdetail_row_layout, dummyList());
        listViewDocDetail.setAdapter(arrayAdapter);




    }
    public ArrayList<String> dummyList(){
        ArrayList<String> array = new ArrayList<>();
        for(int i = 0; i<50;i++){
            array.add("Document "+i);
        }
        return array;
    }
}

