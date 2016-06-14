package com.devel.ccqf.ccqfmisson;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.AdapterView;
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
    public final static String USER_KEY_USERS = "com.devel.ccqf.ccqfmisson.FeedListActivity.USER_KEY_USERS";
    public final static String USER_KEY_CONV = "com.devel.ccqf.ccqfmisson.FeedListActivity.USER_KEY_CONV";
    public final static String USER_KEY_USERLIST = "com.devel.ccqf.ccqfmisson.FeedListActivity.USER_KEY_USERLIST";
    private ListView lstFeedList;
    private ListView lstUserList;
    private ArrayList<ConversationHead> cHeadList;
    private ArrayList<Users> globalUserList = null;

    private InterfaceDB iDb = null;
    private TheadListAdapter tlAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cHeadList = null;
        iDb = new InterfaceDB(this);

        setContentView(R.layout.feed_list_listview_layout);
        lstFeedList = (ListView) findViewById(R.id.lstFeedList);
        InterfaceDB iDb = new InterfaceDB(this);
        if (iDb.isOnline()) {
            new GetUserListAsyncTask().execute();

            new GetMessageAsyncTask().execute();

            lstFeedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ConversationHead ch = cHeadList.get(position);
                    Intent i = new Intent(FeedListActivity.this, Feed.class);
                    Bundle donnees = new Bundle();
                    donnees.putString(USER_KEY_CONV, ch.getConvID());
                    donnees.putParcelableArrayList(USER_KEY_USERLIST, globalUserList);
                    i.putExtras(donnees);
                    startActivity(i);
                }
            });

            FloatingActionButton fabNewFeed = (FloatingActionButton) findViewById(R.id.fabNewFeed);
            fabNewFeed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogSelectUsers();
                }
            });
        } else {
            dialogAlerteReseau();
        }
    }

    public void dialogSelectUsers() {

        final Dialog d = new Dialog(FeedListActivity.this);
        d.setContentView(R.layout.dialog_destinataires);
        lstUserList = (ListView) d.findViewById(R.id.lstUserList);
        ArrayList<Users> localUserList = new ArrayList<>();
        d.setTitle("Send to:");
        int currentUser = iDb.getCurrentUserID();
        Button btnOK = (Button) d.findViewById(R.id.btnUserListOk);
        Iterator<Users> uIter = globalUserList.iterator();
        while(uIter.hasNext()){
            Users user = uIter.next();
            if(user.getUserID() != currentUser)
                localUserList.add(user);
        }
        lstUserList.setAdapter(new CustomContactListAdapter(FeedListActivity.this, localUserList));

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
                if (selectedIndex > 0) {
                    Intent i = new Intent(FeedListActivity.this, Feed.class);
                    Bundle donnees = new Bundle();
                    donnees.putParcelableArrayList(USER_KEY_USERLIST, globalUserList);
                    donnees.putString(USER_KEY_USERS, userList);
                    i.putExtras(donnees);
                    startActivity(i);
                }
            }
        });
        d.show();
    }

    // retrouve tout les messages adressés à l'usager.
    private class GetMessageAsyncTask extends AsyncTask<Void, Void, ArrayList<ConversationHead>> {

        @Override
        protected ArrayList<ConversationHead> doInBackground(Void... unused) {
            ArrayList<ConversationHead> cList = null;
            if (iDb != null) {
                int userID = iDb.getCurrentUserID();
                List<MessagePacket> lMsg = iDb.readMessages(userID);

                List<Integer> tList = iDb.getMessageThreadList();
                if (tList != null) {
                    cList = new ArrayList<ConversationHead>();
                    Iterator<Integer> iter = tList.iterator();
                    while (iter.hasNext()) {
                        Integer i = iter.next();
                        ConversationHead ch = iDb.getMessageHead(i);
                        if (ch != null)
                            cList.add(ch);
                    }
                }

            }
            return cList;
        }

        @Override
        protected void onPostExecute(ArrayList<ConversationHead> cList) {
            cHeadList = cList;
            tlAdapter = new TheadListAdapter(FeedListActivity.this, cList);
            lstFeedList.setAdapter(tlAdapter);
        }
    }

    // retrouve la liste des usager sur le réseau de la Mission
    private class GetUserListAsyncTask extends AsyncTask<Void, Void, ArrayList<Users>> {
        @Override
        protected ArrayList<Users> doInBackground(Void... userId) {
            ArrayList<Users> uList = null;
            if (iDb != null) {
                uList = iDb.getUserList();
            }
            return uList;
        }

        @Override
        protected void onPostExecute(ArrayList<Users> uList) {
            globalUserList = uList;
//            lstUserList.setAdapter(new CustomContactListAdapter(FeedListActivity.this, uList));
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

    private void dialogAlerteReseau() {
        final Dialog d = new Dialog(FeedListActivity.this);
        d.setContentView(R.layout.dialog_message);
        d.setTitle("Alerte!");
        Button btnOK = (Button) d.findViewById(R.id.btnNoNetOk);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedListActivity.this.finish();
            }
        });
        d.show();
    }

}
