package com.devel.ccqf.ccqfmisson;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.devel.ccqf.ccqfmisson.Adapters.CCQFCategorieAdapter;
import com.devel.ccqf.ccqfmisson.Database.InterfaceDB;
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

public class Doc extends CCQFBaseActivity {

    private ListView listViewDoc;
    final static String dirUrl = "http://thierrystpierre.ddns.net:81/CCQFMission/Documents";
    final static String DOC_PARCEL_KEY = "com.devel.ccqf.ccqfmisson.Doc.PARCEL_KEY";
    final static String DOC_PARCEL_KEY2 = "com.devel.ccqf.ccqfmisson.Doc.PARCEL_KEY2";
    private static String baseApplicationFilesPath;
    private CCQFCategorieAdapter arrayAdapter = null;
    private ArrayList<CCQFCategorie> menu =  null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);

        listViewDoc = (ListView)findViewById(R.id.listViewDoc);
        listViewDoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(Doc.this, DocDetails.class);
                Bundle bundle = new Bundle();
                CCQFCategorie categorie = menu.get(position);
                bundle.putString(DOC_PARCEL_KEY, categorie.getNom());
                bundle.putParcelableArrayList(DOC_PARCEL_KEY2, categorie.getItemsList());

                i.putExtras(bundle);
                startActivity(i);
            }
        });
        baseApplicationFilesPath = "" + Environment.getExternalStorageDirectory() + "/" +
                getPackageName() + "/Documents";

        String [] params = {dirUrl, baseApplicationFilesPath, "menu.csv"};
        new GetMenuAsyncTask().execute(params);
        listViewDoc.setAdapter(arrayAdapter);
    }

    private class GetMenuAsyncTask extends AsyncTask<String, Void, List<CCQFCategorie>> {
        private static final int  MEGABYTE = 1024 * 1024;
        private boolean uploadFile = false;

        @Override
        protected List<CCQFCategorie> doInBackground(String... params) {
            long date = 0;
            InterfaceDB iDb = new InterfaceDB(Doc.this);

            FileDownLoader fd = new FileDownLoader(params[2], params[1], params[0]);

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

            File file = fd.getFileHandle();
            if (file.exists()) {
                try {
                    Scanner fSc = new Scanner(fd.getFileHandle());
                    fSc.useDelimiter("\n");
                    CCQFCategorie cat = null;
                    menu = new ArrayList<>();
                    do {
                        String line = fSc.next();
                        String[] linetbl = line.split(";");
                        int t = 0;
                        for (t = 0; t < menu.size(); t++) {
                            if (linetbl[0].equals(menu.get(t).getNom()))
                                break;
                        }
                        if (t >= menu.size()) {
                            cat = new CCQFCategorie(linetbl[0]);
                            menu.add(cat);
                        }
                        CCQFDocument doc = new CCQFDocument(linetbl[1], linetbl[2], params[1], params[0]);
                        cat.addDocument(doc);
                    } while (fSc.hasNext());
                    fSc.close();
                } catch (FileNotFoundException fnfe) {
                    fnfe.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return menu;
        }

        @Override
        protected void onPostExecute(List<CCQFCategorie> liste) {
            super.onPostExecute(liste);
            arrayAdapter = new CCQFCategorieAdapter(Doc.this.getBaseContext(), liste);
            listViewDoc.setAdapter(arrayAdapter);
        }
    }
}
