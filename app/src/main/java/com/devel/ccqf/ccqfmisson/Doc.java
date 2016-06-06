package com.devel.ccqf.ccqfmisson;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
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

public class Doc extends CCQFBaseActivity {

    private ListView listViewDoc;
    final static String dirUrl = "http://thierrystpierre.ddns.net:81/CCQFMission/Documents";
    private static String baseApplicationFilesPath;
    private File fileMenu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc);

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
        baseApplicationFilesPath = "" + Environment.getDataDirectory().getPath() + "/data/" +
                getPackageName() + "/files/Documents";
        File docBaseDir = new File(baseApplicationFilesPath);
        fileMenu = new File(baseApplicationFilesPath, "menu.csv");
/*        if(!docBaseDir.exists()) {
            docBaseDir.mkdirs();
*/
            new GetMenuAsyncTask().execute(null, null);
/*        }
        else if(fileMenu.exists()){

        }*/
    }

    public String[] dummyList(){
        String[] list = new String[]{"Documents", "Information Floride",
                                     "List Participants", "Mot du président"};
        return list;
    }




    private class GetMenuAsyncTask extends AsyncTask<Void, Void, List<String>> {
        private static final int  MEGABYTE = 1024 * 1024;
        private String baseApplicationFilesPath = null;

        @Override
        protected List<String> doInBackground(Void... unused) {
            ArrayList<String> menu =  null;

            System.out.print("CCQF Doc GetMenuAsyncTask");
            System.out.flush();

            try {
                URL url = new URL(dirUrl+"/menu.csv");
                System.out.print("CCQF Doc GetMenuAsyncTask url = " + url + "\n\n");
                System.out.flush();
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                System.out.print("CCQF Doc GetMenuAsyncTask urlConnection = " + urlConnection + "\n\n");
                System.out.flush();
                urlConnection.setRequestMethod("GET");
                System.out.print("CCQF Doc GetMenuAsyncTask 2");
                System.out.flush();
                urlConnection.setDoOutput(true);
                System.out.print("CCQF Doc GetMenuAsyncTask 3");
                System.out.flush();
                urlConnection.connect();
                System.out.print("CCQF Doc GetMenuAsyncTask 4");
                System.out.flush();
                InputStream inputStream = urlConnection.getInputStream();
                System.out.print("CCQF Doc GetMenuAsyncTask 5");
                System.out.flush();
                if(!fileMenu.exists())
                    fileMenu.createNewFile();
                System.out.print("CCQF Doc GetMenuAsyncTask fileCopy (2) = " + fileMenu + "\n\n");
                System.out.flush();
                FileOutputStream fileOutputStream = new FileOutputStream(fileMenu);
                System.out.print("CCQF Doc GetMenuAsyncTask 6");
                System.out.flush();
                int totalSize = urlConnection.getContentLength();
                System.out.print("CCQF Doc GetMenuAsyncTask fileCopy (3) = " + fileMenu + "\n\n");
                System.out.flush();
                byte[] buffer = new byte[MEGABYTE];
                int bufferLength = 0;
                while((bufferLength = inputStream.read(buffer))>0 ){
                    fileOutputStream.write(buffer, 0, bufferLength);
//                    menu.add(new String(buffer));
                    System.out.write(buffer, 0, bufferLength);
                    System.out.flush();
                }
                fileOutputStream.close();
            }catch (FileNotFoundException fnfe){

            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return menu;
        }

    }
}
