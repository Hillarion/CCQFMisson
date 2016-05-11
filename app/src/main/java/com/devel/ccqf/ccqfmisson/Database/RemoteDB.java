package com.devel.ccqf.ccqfmisson.Database;

import android.content.Context;

import com.devel.ccqf.ccqfmisson.ReseauSocial.MessagePacket;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyGroup;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyAnswer;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyMultiple;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyObject;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyText;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyYesNo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by thierry on 10/05/16.
 */
public class RemoteDB {
    private LocaleDB  lDb = null;
    public static final String strURL = "http://thierrystpierre.ddns.net:81/CCQFMission/script.php";
    private List<NameValuePair> paramRequete;
    private JSONParser parser;
    private String ligneResult = null;
    public volatile boolean parsingComplete = true;

    public RemoteDB(Context cntx){

    }

    void setLocalBackUp(LocaleDB dbaLite){
        lDb = dbaLite;
    }

    private void sendRequest(List<NameValuePair> pairs){
        paramRequete = pairs;
        ligneResult = null;
        parsingComplete = false;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(strURL);
                    httppost.setEntity(new UrlEncodedFormEntity(paramRequete));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity2 = response.getEntity();
                    ligneResult = EntityUtils.toString(entity2);
                    parsingComplete = true;
                } catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void sendRequestNoResponse(List<NameValuePair> pairs){
        paramRequete = pairs;
        ligneResult = null;
        parsingComplete = false;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(strURL);
                    httppost.setEntity(new UrlEncodedFormEntity(paramRequete));
                    HttpResponse response = httpclient.execute(httppost);
                    parsingComplete = true;
                } catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public int validateUser(String username, String password){
        int userID = -1;
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "validateLogin"));
        pairs.add(new BasicNameValuePair("userName", username));
        pairs.add(new BasicNameValuePair("userPass", password));
        sendRequest(pairs);
        while(parsingComplete == false);

        if(ligneResult != null) {
            parser = new JSONParser(ligneResult);
            String status = parser.getStatus();
            if (!status.isEmpty()) {
                if (status.equalsIgnoreCase("Success")) {
                    try {
                        JSONObject loginObject = parser.getJSONObject("login");
                        userID = loginObject.getInt("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return userID;
    }

    public interfaceDB.privilegeType getPrivilege(int UserId){
        interfaceDB.privilegeType privilege = interfaceDB.privilegeType.NO_PRIVILEGE;
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "getPrivilege"));
        pairs.add(new BasicNameValuePair("userID", "" + UserId));
        sendRequest(pairs);
        while(parsingComplete == false);

        if(ligneResult != null) {
            parser = new JSONParser(ligneResult);
            String status = parser.getStatus();
            if (!status.isEmpty()) {
                if (status.equalsIgnoreCase("Success")) {
                    String val = parser.getString("privilege");
                    if (val != null) {
                        if (val.equalsIgnoreCase("admin"))
                            privilege = interfaceDB.privilegeType.ADMIN;
                    }
                }
            }
        }
        return privilege;
    }

    public void sendMessage(MessagePacket msg){
        int msgId = -1;
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "sendMessage"));
        pairs.add(new BasicNameValuePair("messageContent", msg.getMessage()));
        pairs.add(new BasicNameValuePair("msgSource", "" + msg.getSource()));
        pairs.add(new BasicNameValuePair("destinataires", msg.getDestinataires()));
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        pairs.add(new BasicNameValuePair("timeStamp", sdf.format(msg.getTimestamp())));
        pairs.add(new BasicNameValuePair("attachement", msg.getAttachement()));
        sendRequest(pairs);
        while(parsingComplete == false);

        if(ligneResult != null) {
            parser = new JSONParser(ligneResult);
            String status = parser.getStatus();
            if (!status.isEmpty()) {
                if (status.equalsIgnoreCase("Success")) {
                    msgId = parser.getIndex();
                    msg.setId_msg(msgId);
                    if(lDb != null){
                        lDb.writeOutMessage(msg);
                    }
                }
            }
        }
    }

    public List<MessagePacket> readMessages(int userId, int lastMessage){
        ArrayList<MessagePacket> msgList = null;
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "readMessages"));
        pairs.add(new BasicNameValuePair("userID", "" + userId));
        pairs.add(new BasicNameValuePair("msgID", "" + lastMessage));
        sendRequest(pairs);
        while(parsingComplete == false);

        if(ligneResult != null) {
            parser = new JSONParser(ligneResult);
            String status = parser.getStatus();
            if (!status.isEmpty()) {
                if (status.equalsIgnoreCase("Success")) {
                    JSONArray jArray = parser.getList("msgQueue");
                    if (jArray != null) {
                        msgList = new ArrayList<MessagePacket>();
                        for (int idx = 0; idx < jArray.length(); idx++) {
                            try {
                                JSONObject jsonObject = jArray.getJSONObject(idx);
                                MessagePacket msg = new MessagePacket(
                                        jsonObject.getInt("msgId"),
                                        jsonObject.getInt("source"),
                                        jsonObject.getString("destinataires"),
                                        jsonObject.getString("message"),
                                        new Date(jsonObject.getString("timeStamp")),
                                        jsonObject.getString("attachement"));
                                msgList.add(msg);
                                if(lDb != null){
                                    lDb.writeInMessage(msg);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return  msgList;
    }

    public void sendSurvey(){
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "sendSurvey"));
        sendRequest(pairs);
        while(parsingComplete == false);

        if(ligneResult != null) {
            parser = new JSONParser(ligneResult);
            String status = parser.getStatus();
            if (!status.isEmpty()) {
                if (status.equalsIgnoreCase("Success")) {
                }
            }
        }
    }

    public SurveyGroup readSurvey(int surveyID){
        SurveyGroup surveyGrp = null;
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "readSurvey"));
        pairs.add(new BasicNameValuePair("surveyID", "" + surveyID));
        sendRequest(pairs);
        while(parsingComplete == false);


        if(ligneResult != null) {
            parser = new JSONParser(ligneResult);
            String status = parser.getStatus();
            if (!status.isEmpty()) {
                if (status.equalsIgnoreCase("Success")) {
                    JSONObject jsonObject = parser.getJSONObject("survey");
                    try {
                        surveyGrp = new SurveyGroup(
                                jsonObject.getInt("id"),
                                new Date(jsonObject.getString("dueTime"))
                                );
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(surveyGrp != null){
                        try{
                            SurveyObject qObj = null;
                            JSONArray jArray = jsonObject.getJSONArray("questions");
                            for (int idx = 0; idx < jArray.length(); idx++) {
                                JSONObject questionObject = jArray.getJSONObject(idx);
                                int type = questionObject.getInt("type");
                                switch(type) {
                                    case 0:
                                        qObj = new SurveyYesNo(
                                                questionObject.getInt("id"),
                                                questionObject.getString("question"));
                                        break;
                                    case 1:
                                        String choix = questionObject.getString("choix");
                                        String [] chList = choix.split(",");
                                        qObj = new SurveyMultiple(
                                                questionObject.getInt("id"),
                                                questionObject.getString("question"),
                                                chList.length,
                                                chList);
                                        break;
                                    case 2:
                                        qObj = new SurveyText(
                                                questionObject.getInt("id"),
                                                questionObject.getString("question"));
                                        break;
                                    default:
                                        qObj=null;
                                        break;
                                }
                                if(qObj != null)
                                    surveyGrp.addQuestion(qObj);
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return surveyGrp;
    }

    public int [] readSurveyList(int lastSurveyID){
        int[] surveylist = null;
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "readSurveyList"));
        pairs.add(new BasicNameValuePair("surveyID", "" + lastSurveyID));
        sendRequest(pairs);
        while(parsingComplete == false);

        if(ligneResult != null) {
            parser = new JSONParser(ligneResult);
            String status = parser.getStatus();
            if (!status.isEmpty()) {
                if (status.equalsIgnoreCase("Success")) {
                    JSONArray jArray = parser.getList("msgQueue");
                    if (jArray != null) {
                        surveylist = new int[jArray.length()];
                        for (int idx = 0; idx < jArray.length(); idx++) {
                            try {
                                surveylist[idx] = jArray.getInt(idx);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return surveylist;
    }

    public void answerSurveyQuestion(int UserID, SurveyAnswer sQuestion){
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("action", "answerSurveyQuestion"));
        pairs.add(new BasicNameValuePair("questionID", ""+sQuestion.getQuestionId()));
        pairs.add(new BasicNameValuePair("sourceID", ""+UserID));
        pairs.add(new BasicNameValuePair("reponseInt", ""+sQuestion.getReponseInt()));
        pairs.add(new BasicNameValuePair("reponseText", sQuestion.getReponseTexte()));
        sendRequestNoResponse(pairs);

    }




}
