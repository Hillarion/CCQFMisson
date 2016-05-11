package com.devel.ccqf.ccqfmisson.Database;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thierry on 10/05/16.
 */
public class RemoteDB {
    public static final String strURL = "http://thierrystpierre.ddns.net:81/CCQFMission/script.php";
    private List<NameValuePair> paramRequete;
    private JSONParser parser;
    private String ligneResult = null;
    public volatile boolean parsingComplete = true;


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

/*

        pairs.add(new BasicNameValuePair("action", "getPrivilege"));
        pairs.add(new BasicNameValuePair("action", "sendMessage"));
        pairs.add(new BasicNameValuePair("action", "readMessages"));
        pairs.add(new BasicNameValuePair("action", "sendSurvey"));
        pairs.add(new BasicNameValuePair("action", "readSurvey"));
        pairs.add(new BasicNameValuePair("action", "answerSurveyQuestion"));

 */

}
