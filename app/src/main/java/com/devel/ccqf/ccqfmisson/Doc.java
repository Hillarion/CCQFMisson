package com.devel.ccqf.ccqfmisson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

public class Doc extends AppCompatActivity {
    private ListView listViewDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);
        listViewDoc = (ListView)findViewById(R.id.listViewDoc);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (Doc.this, R.layout.doc_row_layout, dummyList());
        listViewDoc.setAdapter(arrayAdapter);



    }
    public String[] dummyList(){
        String[] list = new String[]{"Documents", "Information Floride",
                                     "List Participants", "Mot du président"};
        return list;
    }
}
