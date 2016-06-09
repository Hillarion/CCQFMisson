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

            if (iDb.isNetAccessible()) {
                try {
                    URL url = new URL(params[0] + "/" + params[2]);

                    File docBaseDir = new File(params[1]);
                    if (!docBaseDir.exists())
                        docBaseDir.mkdirs();
                    File fileMenu = new File(params[1], params[2]);
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
                        byte[] buffer = new byte[Math.min(totalSize + 1, MEGABYTE)];
                        int bufferLength = 0;
                        while ((bufferLength = inputStream.read(buffer)) > 0) {
                            fileOutputStream.write(buffer, 0, bufferLength);
                        }
                        fileOutputStream.close();
                    }

                    Scanner fSc = new Scanner(fileMenu);
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
                } catch (MalformedURLException e) {
                    e.printStackTrace();
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
