package com.devel.ccqf.ccqfmisson;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.devel.ccqf.ccqfmisson.Adapters.CustomB2BAdapter;
import com.devel.ccqf.ccqfmisson.Adapters.CustomEventAdapter;
import com.devel.ccqf.ccqfmisson.AgendaObjects.Event;
import com.devel.ccqf.ccqfmisson.Database.InterfaceDB;
import com.devel.ccqf.ccqfmisson.Database.LocaleDB;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MonAgenda extends CCQFBaseActivity { //B2B
    private ListView listViewEvents;
    private ArrayList<Event> events;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_agenda);
        LocaleDB lDb = new LocaleDB(MonAgenda.this);
        String user = lDb.getCurrentUserLogin();
        listViewEvents = (ListView) findViewById(R.id.listViewEvents);
        new getEventListAsyncTask().execute(user);
//        setListViewEvents(user);


    }

    public ArrayList<Event> filterEventListFromDb(ArrayList<Event> alEventRaw){

        ArrayList<Event> alEvent = new ArrayList<>();
        for(int i = 0;i < alEventRaw.size();i++){
            Event eTemp =  alEventRaw.get(i);
            String nom =  eTemp.getNom();
            String hDebut = eTemp.getDTStart();
            String hFin = eTemp.getDTEnd();
            String compagnie = eTemp.getCompagnie();
            String poste = eTemp.getPoste();
            String telephone = eTemp.getTelephone();
            String email = eTemp.getEmail();
            String adresse = eTemp.getAdresse();
            boolean ext = eTemp.isAutreBatiment();

            String heure = ""+hDebut+" "+hFin;
            String title = nom+", "+poste+", "+compagnie;
            String detail = telephone+", "+email;
            String location = ""+adresse+""+ext;

            /*if(ext){
                location = adresse+" ";
            }
            else if(!ext){
                location = adresse;
            }*/




            alEvent.add(new Event(title, heure, detail, location));
        }
        return alEvent;
    }

    private class getEventListAsyncTask extends AsyncTask<String, Void, ArrayList<Event>>{

        @Override
        protected ArrayList<Event> doInBackground(String... params) {
            ArrayList<Event>alEventRaw =null;
            InterfaceDB iDb = new InterfaceDB(MonAgenda.this);
            if(iDb != null)
                alEventRaw = iDb.getEventList(params[0]);
                ArrayList<Event> alEvent = filterEventListFromDb(alEventRaw);

            System.out.print("CCQF MonAgenda getEventListAsyncTask doInBackground ("+params[0] + ") alEventRaw = " + alEventRaw + "\n\n");
            System.out.flush();
            return alEvent;
        }
        @Override
        protected void onPostExecute(ArrayList<Event> eList){
            events = eList;
            CustomB2BAdapter adapter = new CustomB2BAdapter(MonAgenda.this, eList);
            listViewEvents.setAdapter(adapter);
        }
    }



}
