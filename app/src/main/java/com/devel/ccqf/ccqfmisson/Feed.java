package com.devel.ccqf.ccqfmisson;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.devel.ccqf.ccqfmisson.Adapters.CustomFeedAdapter;
import com.devel.ccqf.ccqfmisson.Database.InterfaceDB;
import com.devel.ccqf.ccqfmisson.ReseauSocial.MessagePacket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Feed extends CCQFBaseActivity {

    private Button btnSend;
    private EditText inputMsg;
    private TextView txtChatRoomTitle;
    private ListView listViewMessages;
    private List<MessagePacket> listMessages;
    private String userList;
    private CustomFeedAdapter feedAdapter;
    private InterfaceDB iDb;
    private int currentUser;
    private int convID;
    private ArrayList<Users> globalUserList;

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
        txtChatRoomTitle = (TextView) findViewById(R.id.txtChatRoomTitle);
        convID = -1;

        iDb = new InterfaceDB(this);
        if(iDb != null)
            currentUser = iDb.getCurrentUserID();
        globalUserList = bundle.getParcelableArrayList(FeedListActivity.USER_KEY_USERLIST);
        feedAdapter.setUserNameList(globalUserList);

        final String strConv = bundle.getString(FeedListActivity.USER_KEY_CONV);
        /* Si un numéro de conversation est fourni, s'en servir pour aller chercher la liste
         de tout les messages en faisant partie.  Extraire, aussi la liste des usager en faisant partie.
          */
        if(strConv != null) {
            if (strConv.matches("[0-9]*")) { // vérifier que strConv est un numérique.
                convID = Integer.parseInt(strConv);
                listMessages = iDb.getMessages(convID); // trouver tout les messages ayant le même convID.
                Iterator<MessagePacket> iter = listMessages.iterator();
                int msgCount = 0;
                String tmpUserList = "";
                while (iter.hasNext()) {
                    MessagePacket msg = iter.next();
                    if (msg.getSource() == currentUser) // marquer les message qui vienne de l'usager courant
                        msg.setSelf(true);
                    if (msgCount++ == 0)  // retrouver la liste des destinataires.
                        tmpUserList = msg.getDestinataires()/* +","+ msg.getSource() */;
                    feedAdapter.add(msg);

                    // la boucle qui suit, retrouve la liste des usagers de la conversation et en enlève l'usager courant.
                    String[] userTbl = tmpUserList.split(",");
                    userList = "";
                    for (int idx = 0; idx < userTbl.length; idx++) {
                        String tmpId = userTbl[idx];
                        if (!userTbl[idx].equals("" + currentUser)) {
                            userList += userTbl[idx];
                            if (idx < userTbl.length - 1)
                                userList += ",";
                        }
                    }

                }
                feedAdapter.notifyDataSetChanged();
            }
        }
        else
            userList = bundle.getString(FeedListActivity.USER_KEY_USERS);

        // préparer la liste des usager pour afficher leurs noms dans le titre de la page.
        String textViewList = "";
        String [] splitedtUserList = userList.split(",");
        for(int uIdx=0; uIdx < splitedtUserList.length; uIdx++){
//            int value = Integer.parseInt(splitedtUserList[uIdx]);
            String uid = splitedtUserList[uIdx];
            Iterator<Users>  uIter = globalUserList.iterator();
            while(uIter.hasNext()) {
                Users user = uIter.next();
                if (user.getUID().equals(splitedtUserList[uIdx])) {
                    textViewList += user.getUserName();
                    if (uIdx < splitedtUserList.length - 1)
                        textViewList += "\n";
                }
            }
        }
        txtChatRoomTitle.setText("conversation avec :\n" + textViewList);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msgText = inputMsg.getText().toString();

                if (!msgText.isEmpty()) {
                    new SendMessageAsyncTask().execute(new String[]{userList, msgText, ""+strConv});
                }
            }
        });
    }

    private void getMsg(){
        new getMessageAsyncTask().execute();

    }

    private class SendMessageAsyncTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... msgInfo){
            int msgId = -1;
            if(iDb != null){
                // convID est commun à tout les messages entrant et sortant pour l'instance courante de Feed.
                convID = iDb.sendMessage(convID, msgInfo[0], msgInfo[1]);
                MessagePacket msg = iDb.getMessage(msgId);
                feedAdapter.add(msg);
            }
            return convID;
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

    private class getMessageAsyncTask extends AsyncTask<Void, Void, ArrayList<MessagePacket>> {

        @Override
        protected ArrayList<MessagePacket> doInBackground(Void... params) {
            return  iDb.getMessages(convID);
        }
        @Override
        protected void onPostExecute(ArrayList<MessagePacket> msgList) {
            feedAdapter.setUserMessageList(msgList);
        }

    }

}
