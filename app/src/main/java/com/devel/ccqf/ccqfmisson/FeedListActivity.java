package com.devel.ccqf.ccqfmisson;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.devel.ccqf.ccqfmisson.Adapters.CustomContactListAdapter;
import com.devel.ccqf.ccqfmisson.Adapters.TheadListAdapter;
import com.devel.ccqf.ccqfmisson.Database.InterfaceDB;
import com.devel.ccqf.ccqfmisson.ReseauSocial.ConversationHead;
import com.devel.ccqf.ccqfmisson.ReseauSocial.MessagePacket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by thierry on 26/05/16.
 */
public class FeedListActivity extends CCQFBaseActivity {
    public final static String USER_KEY = "com.devel.ccqf.ccqfmisson.FeedListActivity.USER_KEY";
    private ListView lstFeedList;
    private ListView lstUserList;
    private ArrayList<ConversationHead> cHeadList;
    private InterfaceDB iDb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cHeadList = null;
        iDb = new InterfaceDB(this);

        setContentView(R.layout.feed_list_listview_layout);
        ArrayList<ConversationHead> cHeadList = null;
        lstFeedList = (ListView)findViewById(R.id.lstFeedList);

        new GetMessageAsyncTask().execute();
        InterfaceDB iDb = new InterfaceDB(this);
        if(iDb != null) {
            List<Integer>  convList = iDb.getMessageThreadList();
            if(convList != null){
                cHeadList = new ArrayList<ConversationHead>();
                Iterator<Integer> iter = convList.iterator();
                while(iter.hasNext()){
                    ConversationHead tmpHead = iDb.getMessageHead(iter.next().intValue());
                    if(tmpHead != null)
                        cHeadList.add(tmpHead);
                }
            }
        }
/*        TheadListAdapter tlAdapter = new TheadListAdapter(this, cHeadList);
        lstFeedList.setAdapter(tlAdapter);
        lstFeedList.setVisibility(View.VISIBLE);*/

        FloatingActionButton fabNewFeed = (FloatingActionButton) findViewById(R.id.fabNewFeed);
        fabNewFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSelectUsers();
            }
        });


    }

    public void dialogSelectUsers() {

        final Dialog d = new Dialog(FeedListActivity.this);
        d.setContentView(R.layout.dialog_destinataires);
        lstUserList = (ListView)d.findViewById(R.id.lstUserList);
        d.setTitle("Send to:");
        Button btnOK = (Button)d.findViewById(R.id.btnUserListOk);
//        Button btnRefreshUserList = (Button)d.findViewById(R.id.btnRefreshUserList);
        new GetUserListAsyncTask().execute();

/*        btnRefreshUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetUserListAsyncTask().execute();
            }
        });*/
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View c) {
                String userList = "";
                int selectedIndex = 0;
                for (int cIdx = 0; cIdx < lstUserList.getChildCount(); cIdx++) {
                    LinearLayout itemLayout = (LinearLayout) lstUserList.getChildAt(cIdx);
                    AppCompatCheckBox chBox = (AppCompatCheckBox) itemLayout.getChildAt(2);
                    TextView uIDText = (TextView) itemLayout.getChildAt(0);
                    if (chBox.isChecked()) {
                        if (userList != "")
                            userList += ",";
                        userList += uIDText.getText().toString();
                        selectedIndex++;
                    }
                }
                d.hide();
                if(selectedIndex>0) {
                    Intent i = new Intent(FeedListActivity.this, Feed.class);
                    Bundle donnees = new Bundle();
                    donnees.putString(USER_KEY, userList);
                    i.putExtras(donnees);
                    startActivity(i);
                }
            }
        });
        d.show();
    }

    // retrouve tout les messages adressés à l'usager.
    private class GetMessageAsyncTask extends AsyncTask<Void, Void, ArrayList<ConversationHead>>{

        @Override
        protected ArrayList<ConversationHead> doInBackground(Void... unused) {
            ArrayList<ConversationHead> cList = null;
            if(iDb != null){
                int userID = iDb.getCurrentUserID();
                List<MessagePacket> lMsg =   iDb.readMessages(userID);

                System.out.flush();
                List<Integer> tList = iDb.getMessageThreadList();
                if(tList != null) {
                    cList = new ArrayList<ConversationHead>();
                    Iterator<Integer> iter = tList.iterator();
                    while (iter.hasNext()) {
                        Integer i = iter.next();
                        ConversationHead ch = iDb.getMessageHead(i);
                        if(ch != null)
                            cList.add(ch);
                    }
                }

            }
            return cList;
        }

        @Override
        protected void onPostExecute(ArrayList<ConversationHead> cList) {
            cHeadList = cList;
            TheadListAdapter tlAdapter = new TheadListAdapter(FeedListActivity.this, cList);
            lstFeedList.setAdapter(tlAdapter);
        }
    }

    // retrouve la liste des usager sur le réseau de la Mission
    private class GetUserListAsyncTask extends AsyncTask<Void, Void, ArrayList<Users>>{
        @Override
        protected ArrayList<Users> doInBackground(Void... userId){
            ArrayList<Users> uList = null;
            if(iDb != null){
                uList = iDb.getUserList();
            }
            return uList;
        }
        @Override
        protected void onPostExecute(ArrayList<Users> uList) {
            lstUserList.setAdapter(new CustomContactListAdapter(FeedListActivity.this, uList));
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
