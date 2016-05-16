package com.devel.ccqf.ccqfmisson;

import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.devel.ccqf.ccqfmisson.Adapters.CustomEventAdapter;
import com.devel.ccqf.ccqfmisson.AgendaObjects.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Program extends AppCompatActivity {
    private ListView listViewEvents;
    private ArrayList<Event> events;

    public static final String [] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,
            CalendarContract.Calendars.ACCOUNT_NAME,
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
            CalendarContract.Calendars.OWNER_ACCOUNT
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_agenda);


        listViewEvents = (ListView) findViewById(R.id.listViewEvents);
        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                writeToLocalDB();
            }
        });

        setListViewEvents();

    }

    private void writeToLocalDB() {
        Toast.makeText(Program.this, "In", Toast.LENGTH_SHORT).show();

    }

    public void setListViewEvents() {
        CustomEventAdapter adapter = new CustomEventAdapter(this, dummyList());
        listViewEvents.setAdapter(adapter);
    }
    public ArrayList<Event> dummyList(){
        ArrayList<Event> n = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date d = Calendar.getInstance().getTime();
        String date = df.format(d);

        for(int i = 0 ; i < 10 ; i++ ){
            n.add(new Event("MY PROGRAM CHOICE", date));
        }
        return n;
    }

}
