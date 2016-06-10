package com.devel.ccqf.ccqfmisson;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.devel.ccqf.ccqfmisson.Adapters.CustomB2BAdapter;
import com.devel.ccqf.ccqfmisson.Adapters.CustomProgramAdapter;
import com.devel.ccqf.ccqfmisson.AgendaObjects.Event;
import com.devel.ccqf.ccqfmisson.Database.InterfaceDB;
import com.devel.ccqf.ccqfmisson.Utilitairies.FileDownLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Program extends CCQFBaseActivity {
    private ListView listViewEvents;
    private ArrayList<Event> events;
    private CustomB2BAdapter adapter;
    final static String dirUrl = "http://thierrystpierre.ddns.net:81/CCQFMission/Programme";
    private static String baseApplicationFilesPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_agenda);
        InputStream inputStream = getResources().openRawResource(R.raw.program);
        listViewEvents = (ListView) findViewById(R.id.listViewEvents);
        //setListViewEvents(temp);
        baseApplicationFilesPath = "" + Environment.getDataDirectory().getPath() + "/data/" +
                getPackageName() + "/Files/Programme";
        Toast.makeText(Program.this, ""+baseApplicationFilesPath, Toast.LENGTH_LONG).show();
        new GetProgramAsyncTask().execute(dirUrl,baseApplicationFilesPath, "program.csv");

        Toast.makeText(Program.this, ""+events, Toast.LENGTH_SHORT).show();

        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  Intent i = new Intent(Program.this )
                //Toast.makeText(Program.this, temp.toString(), Toast.LENGTH_SHORT).show();


            }
        });
    }
    public void setListViewEvents(ArrayList<Event> alProgram) {
        CustomProgramAdapter adapter = new CustomProgramAdapter(this, dummyListDays());
       // CustomSurveyAnswerAdapter adapter = new CustomSurveyAnswerAdapter(Program.this, alProgram);
        listViewEvents.setAdapter(adapter);
    }
    public ArrayList<Event> dummyListDays(){
        ArrayList<Event> n = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy ");
        Date d = Calendar.getInstance().getTime();
        String date = df.format(d);

        for(int i = 0 ; i < 20 ; i++ ){
            n.add(new Event("Title", i+"h00", (i+1)+"h00"));
        }
        return n;
    }
    private class GetProgramAsyncTask extends AsyncTask<String, Void, ArrayList<Event>>{

        @Override
        protected ArrayList<Event> doInBackground(String... params) {
            InterfaceDB iDb = new InterfaceDB(Program.this);

            FileDownLoader fd = new FileDownLoader(params[2], params[1], params[0]);//dir, path, file

            if(iDb.isNetAccessible()){
                try{
                    if(!fd.isUptodate())
                        fd.getFileFromServer();

                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
            File f = new File(baseApplicationFilesPath);

            if(!f.exists()){
                f.mkdirs();
            }

            File file = fd.getFileHandle();
            if(file.exists()){
                try {
                    Scanner scanner = new Scanner(fd.getFileHandle());
                    scanner.useDelimiter("\n");
                    Event e = null;
                    events = new ArrayList<>();
                    do{
                        String line = scanner.next();
                        String[] linetbl = line.split(";");
                        e = new Event(linetbl[0], linetbl[1], linetbl[2], linetbl[3]);
                        events.add(e);
                      //  Toast.makeText(Program.this, "Event : "+e, Toast.LENGTH_SHORT).show();
                    }while(scanner.hasNext());
                    scanner.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            return events;
        }
        protected void onPostExecute(ArrayList<Event> liste){
            super.onPostExecute(liste);
            adapter = new CustomB2BAdapter(Program.this,liste);
            //listViewEvents.setAdapter(adapter);

        }
    }
}
