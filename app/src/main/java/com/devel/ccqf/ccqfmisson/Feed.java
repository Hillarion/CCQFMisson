package com.devel.ccqf.ccqfmisson;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.devel.ccqf.ccqfmisson.Adapters.CustomFeedAdapter;
import com.devel.ccqf.ccqfmisson.Database.InterfaceDB;
import com.devel.ccqf.ccqfmisson.ReseauSocial.MessagePacket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Feed extends CCQFBaseActivity {

    private Button btnSend;
    private EditText inputMsg;
    private ListView listViewMessages;
    private List<MessagePacket> listMessages;
    private String userList;
    private CustomFeedAdapter feedAdapter;
    private InterfaceDB iDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        btnSend = (Button)findViewById(R.id.btnSend);
        inputMsg = (EditText)findViewById(R.id.inputMsg);
        feedAdapter = new CustomFeedAdapter(this, new ArrayList<MessagePacket>());
        listViewMessages = (ListView) findViewById(R.id.list_view_messages);
        listViewMessages.setAdapter(feedAdapter);

        iDb = new InterfaceDB(this);

        String strConv = bundle.getString(FeedListActivity.USER_KEY_CONV);
        if(strConv.matches("[0-9]*")) {
            System.out.print("CCQF Feed onCreate strConv = " + strConv + "\n\n");
            System.out.flush();
            int convID = Integer.parseInt(strConv);
            System.out.print("CCQF Feed onCreate convID = " + convID + "\n\n");
            System.out.flush();
            listMessages = iDb.getMessages(convID);
            System.out.print("CCQF Feed onCreate listMessages = " + listMessages + "\n\n");
            System.out.flush();
            Iterator<MessagePacket> iter = listMessages.iterator();
            while(iter.hasNext())
                feedAdapter.add(iter.next());
            feedAdapter.notifyDataSetChanged();
//            userList =
        }
        else
            userList = bundle.getString(FeedListActivity.USER_KEY_USERS);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String msgText = inputMsg.getText().toString();

            if(!msgText.isEmpty()){
                new SendMessageAsyncTask().execute(new String[]{userList, msgText});
            }
            }
        });
    }

    private class SendMessageAsyncTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... msgInfo){
            int msgId = -1;
            if(iDb != null){
                msgId = new Integer(iDb.sendMessage(msgInfo[0], msgInfo[1]));
                feedAdapter.add(iDb.getMessage(msgId));
            }
            return msgId;
        }
        @Override
        protected void onPostExecute(Integer unused) {

        }

        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }

        @Override
        protected void onProgressUpdate(Void... text) {
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }
    }
}
