package com.devel.ccqf.ccqfmisson;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

public class Doc extends AppCompatActivity {
    private ListView listViewDoc;
    ActionMenuView amvMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);

       // actionBar();

        listViewDoc = (ListView)findViewById(R.id.listViewDoc);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (Doc.this, R.layout.doc_row_layout, dummyList());
        listViewDoc.setAdapter(arrayAdapter);
        listViewDoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Doc.this, DocDetails.class);
                startActivity(i);
            }
        });




    }
    public String[] dummyList(){
        String[] list = new String[]{"Documents", "Information Floride",
                                     "List Participants", "Mot du président"};
        return list;
    }

    public void actionBar(){
        Drawable logo = null;
        logo = getResources().getDrawable(R.mipmap.ccqf_logo);

        getSupportActionBar().setLogo(logo);
    }
}
