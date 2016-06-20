package com.devel.ccqf.ccqfmisson;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.devel.ccqf.ccqfmisson.Adapters.CCQFCategorieAdapter;
import com.devel.ccqf.ccqfmisson.Adapters.CCQFDocumentAdapter;
import com.devel.ccqf.ccqfmisson.Database.InterfaceDB;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyYesNo;
import com.devel.ccqf.ccqfmisson.Utilitairies.FileDownLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DocDetails extends CCQFBaseActivity {
    private ListView listViewDocDetail;
    private List<CCQFDocument> liste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_details);

        listViewDocDetail = (ListView)findViewById(R.id.listViewDocDetails);
        TextView txtTitrePage = (TextView)findViewById(R.id.txtTitrePage);
        Intent i = getIntent();

        Bundle bundle = i.getExtras();

        String categorie = bundle.getString(Doc.DOC_PARCEL_KEY);
        txtTitrePage.setText(categorie);
        liste =  bundle.getParcelableArrayList(Doc.DOC_PARCEL_KEY2);

        CCQFDocumentAdapter arrayAdapter = new CCQFDocumentAdapter(DocDetails.this, liste);
        listViewDocDetail.setAdapter(arrayAdapter);
        listViewDocDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new GetDocumentAsyncTask().execute(liste.get(position));
            }
        });
    }

    private class GetDocumentAsyncTask extends AsyncTask<CCQFDocument, Void, File> {
        private static final int  MEGABYTE = 1024 * 1024;
        private boolean uploadFile = false;

        @Override
        protected File doInBackground(CCQFDocument... params) {
            InterfaceDB iDb = new InterfaceDB(DocDetails.this);

            FileDownLoader fd = new FileDownLoader(params[0].getNomFichier(), params[0].getFilePath(), params[0].getBaseURL());
            if (iDb.isNetAccessible()) {
                try {
                    if (!fd.isUptodate())
                        fd.getFileFromServer();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return fd.getFileHandle();
        }

        @Override
        protected void onPostExecute(File fichier) {
            super.onPostExecute(fichier);
            if(fichier.exists()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(fichier), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
            else {
                dialogAlerteReseau();
            }
        }
    }

    private void dialogAlerteReseau() {
        final Dialog d = new Dialog(DocDetails.this);
        d.setContentView(R.layout.dialog_message);
        d.setTitle("Alerte!");
        Button btnOK = (Button) d.findViewById(R.id.btnNoNetOk);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }


}

