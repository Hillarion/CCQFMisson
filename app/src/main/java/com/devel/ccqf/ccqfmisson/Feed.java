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

import com.devel.ccqf.ccqfmisson.Database.InterfaceDB;
import com.devel.ccqf.ccqfmisson.ReseauSocial.MessagePacket;

import java.util.List;

public class Feed extends CCQFBaseActivity/*AppCompatActivity*/ {

    private Button btnSend;
    private EditText inputMsg;
    private ListView listViewMessages;
    private List<MessagePacket> listMessages;
    private String userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Intent i = getIntent();

        Bundle bundle = i.getExtras();

        userList = bundle.getString(FeedListActivity.USER_KEY);
        System.out.print("CCQF Feed intent userList = "+userList+"\n\n");
        System.out.flush();

        btnSend = (Button)findViewById(R.id.btnSend);
        inputMsg = (EditText)findViewById(R.id.inputMsg);
        listViewMessages = (ListView) findViewById(R.id.list_view_messages);
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
            InterfaceDB iDb = new InterfaceDB(Feed.this);
            if(iDb != null){
                msgId = new Integer(iDb.sendMessage(msgInfo[0], msgInfo[1]));

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
