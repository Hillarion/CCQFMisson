package com.devel.ccqf.ccqfmisson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

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

public class MonAgenda extends CCQFBaseActivity {
    private ListView listViewEvents;
    private ArrayList<Event> events;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_agenda);
        LocaleDB lDb = new LocaleDB(MonAgenda.this);
        String user = lDb.getCurrentUserLogin();
        listViewEvents = (ListView) findViewById(R.id.listViewEvents);
        setListViewEvents(user);


    }
        //get current user
    public void setListViewEvents(String user) {
        CustomEventAdapter adapter = new CustomEventAdapter(this, getEventListFromDb(user));
       /* InterfaceDB iDb = new InterfaceDB(this);
        CustomEventAdapter adapter = new CustomEventAdapter(this, iDb.getEventList(user));*/
        listViewEvents.setAdapter(adapter);
    }
    public ArrayList<Event> dummyList(){
        ArrayList<Event> n = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date d = Calendar.getInstance().getTime();
        String date = df.format(d);

        for(int i = 0 ; i < 10 ; i++ ){
            n.add(new Event("Title", date));
        }
        return n;
    }

    public ArrayList<Event> getEventListFromDb(String currentUser){
        ArrayList<Event>alEventRaw = new ArrayList<>();
        InterfaceDB iDb = new InterfaceDB(MonAgenda.this);
        alEventRaw = iDb.getEventList(currentUser);


        ArrayList<Event> alEvent = new ArrayList<>();
        for(int i = 0;i<alEventRaw.size();i++){
           Event eTemp =  alEventRaw.get(i);
           String title =  eTemp.getNom();
           String hDebut = eTemp.getDTStart();
           String hFin = eTemp.getDTEnd();
           String heure = ""+hDebut+" "+hFin;
           alEvent.add(new Event(title, heure));
        }
        return alEvent;
    }

}
