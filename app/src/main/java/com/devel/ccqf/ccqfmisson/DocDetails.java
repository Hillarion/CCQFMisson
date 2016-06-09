package com.devel.ccqf.ccqfmisson;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.devel.ccqf.ccqfmisson.Adapters.CCQFCategorieAdapter;
import com.devel.ccqf.ccqfmisson.Adapters.CCQFDocumentAdapter;
import com.devel.ccqf.ccqfmisson.Database.InterfaceDB;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyYesNo;

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
            long date = 0;
            File fileMenu = null;
            InterfaceDB iDb = new InterfaceDB(DocDetails.this);


            if (iDb.isNetAccessible()) {
                try {
                    URL url = new URL(params[0].getFullURL());

                    fileMenu = new File(params[0].getFilePath(), params[0].getNomFichier());
                    if (fileMenu.exists()) {
                        HttpURLConnection.setFollowRedirects(false);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        date = con.getLastModified();
                        long dateLocal = fileMenu.lastModified();
                        if (dateLocal < date)
                            uploadFile = true;
                    } else
                        uploadFile = true;

                    if (uploadFile) {
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setDoOutput(true);
                        urlConnection.connect();
                        InputStream inputStream = urlConnection.getInputStream();
                        fileMenu.createNewFile();
                        FileOutputStream fileOutputStream = new FileOutputStream(fileMenu);
                        int totalSize = urlConnection.getContentLength();
                        byte[] buffer = new byte[totalSize + 1/*MEGABYTE*/];
                        int bufferLength = 0;
                        while ((bufferLength = inputStream.read(buffer)) > 0) {
                            fileOutputStream.write(buffer, 0, bufferLength);
                        }
                        fileOutputStream.close();
                    }
                } catch (FileNotFoundException fnfe) {

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return fileMenu;
        }

        @Override
        protected void onPostExecute(File fichier) {
            super.onPostExecute(fichier);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(fichier), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }
    }


}

