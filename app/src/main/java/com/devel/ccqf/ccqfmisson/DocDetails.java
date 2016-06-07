package com.devel.ccqf.ccqfmisson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.devel.ccqf.ccqfmisson.Adapters.CCQFDocumentAdapter;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyYesNo;

import java.util.ArrayList;
import java.util.List;

public class DocDetails extends CCQFBaseActivity/*AppCompatActivity*/ {
    private ListView listViewDocDetail;
    private List<CCQFDocument> liste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_details);

        listViewDocDetail = (ListView)findViewById(R.id.listViewDocDetails);
        Intent i = getIntent();

        System.out.print("CCQF DocDetails Intent = " + i + "\n\n");
        System.out.flush();
        Bundle bundle = i.getExtras();
        System.out.print("CCQF DocDetails bundle = " + bundle + "\n\n");
        System.out.flush();

        String categorie = bundle.getString(Doc.DOC_PARCEL_KEY);
        liste =  bundle.getParcelableArrayList(Doc.DOC_PARCEL_KEY2);


//        String s = bundle.getString(Doc.DOC_PARCEL_KEY);
//        System.out.print("CCQF DocDetails s = " + s + "\n\n");
        System.out.print("CCQF DocDetails categorie = " + categorie + "\n\n");
        System.out.flush();

        CCQFDocumentAdapter arrayAdapter = new CCQFDocumentAdapter(DocDetails.this, liste);
        listViewDocDetail.setAdapter(arrayAdapter);

    }
}

