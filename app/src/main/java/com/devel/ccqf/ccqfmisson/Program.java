package com.devel.ccqf.ccqfmisson;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_agenda);

        listViewEvents = (ListView) findViewById(R.id.listViewEvents);
        setListViewEvents();
        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  Intent i = new Intent(Program.this )


            }
        });
    }
    public void setListViewEvents() {
        CustomEventAdapter adapter = new CustomEventAdapter(this, dummyListDays());
        listViewEvents.setAdapter(adapter);
    }
    public ArrayList<Event> dummyListDays(){
        ArrayList<Event> n = new ArrayList<>();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy ");
        Date d = Calendar.getInstance().getTime();
        String date = df.format(d);

        for(int i = 0 ; i < 20 ; i++ ){
            n.add(new Event(date, "TITLE"));
        }
        return n;
    }

}
