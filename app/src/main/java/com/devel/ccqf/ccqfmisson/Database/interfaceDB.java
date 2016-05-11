package com.devel.ccqf.ccqfmisson.Database;

import android.content.Context;
import android.net.ConnectivityManager;

import com.devel.ccqf.ccqfmisson.ReseauSocial.MessagePacket;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyAnswer;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyGroup;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by thierry on 10/05/16.
 */
public class interfaceDB {
    public enum privilegeType {NO_PRIVILEGE, ADMIN};

    private LocaleDB lDb = null;
    private RemoteDB rDb = null;

    private Context parentContext = null;
    private boolean workLocaly = true;
    private boolean parsingComplete = false;
    private String ligneResult = null;

    private int currentUser;

    public interfaceDB(Context cntx) {
        currentUser = -1;
        parentContext = cntx;
        if (isOnline()) {
            if (testConnexionToServer())
                workLocaly = false;
        }
        LocaleDB lDb = new LocaleDB(cntx);
        if (!workLocaly) {
            RemoteDB rDb = new RemoteDB(cntx);
            rDb.setLocalBackUp(lDb);
        }
    }

    private boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) parentContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            return false;
        }
    }

    private boolean testConnexionToServer() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://thierrystpierre.ddns.net:81/CCQFMission/script.php");
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity2 = response.getEntity();
                    ligneResult = EntityUtils.toString(entity2);
                    parsingComplete = true;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        thread.start();
        while (parsingComplete == false) ;
        return (ligneResult != null);
    }


    public int validateUser(String userName, String password) {
        if(rDb != null)
            currentUser = rDb.validateUser(userName, password);
        return currentUser;
    }

    public privilegeType getPrivilege(int UserId) {
        privilegeType privilege = null;

        int priv = -1;
        if ((priv = lDb.getPrivilege(UserId)) != -1) {
            switch (priv) {
                case 0:
                    privilege = (privilegeType.NO_PRIVILEGE);
                    break;
                case 1:
                    privilege = (privilegeType.ADMIN);
                    break;
            }
        } else if (!workLocaly) {
            privilege = rDb.getPrivilege(UserId);
            lDb.setPrivilege(UserId, privilege);
        }
        return privilege;
    }


    public void answerSurveyQuestion(int UserID, SurveyAnswer sQuestion) {
        if (rDb != null)
            rDb.answerSurveyQuestion(UserID, sQuestion);
    }

    public int[] readSurveyList() {
        int[] tbl = null;
        int lastSurveyID = lDb.getLastSurveyIndex(currentUser);

        if (rDb != null)
            tbl = rDb.readSurveyList(lastSurveyID);
        return tbl;
    }

    public SurveyGroup readSurvey(int surveyID) {
        SurveyGroup sGrp = null;
        if (rDb != null) {
            sGrp = rDb.readSurvey(surveyID);
            lDb.setLastSurveyIndex(currentUser, sGrp.getId());
        }
        return sGrp;
    }

    public void sendSurvey(SurveyGroup sGrp) {
        if (rDb != null)
            rDb.sendSurvey(sGrp);
    }

    public List<MessagePacket> readMessages(int userId) {
        List<MessagePacket> msgList = null;
        int lastMessageID = lDb.getLastMsgIndex(currentUser);
        if (rDb != null) {
            msgList = rDb.readMessages(userId, lastMessageID);
            lDb.setLastMsgIndex(currentUser, msgList.get(msgList.size() - 1).getId_msg());
        }
        return msgList;
    }

    public void sendMessage(MessagePacket msg){
        if(rDb != null)
            rDb.sendMessage( msg);
    }




}