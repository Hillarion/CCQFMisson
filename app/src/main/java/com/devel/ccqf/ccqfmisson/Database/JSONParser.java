package com.devel.ccqf.ccqfmisson.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thierry on 09/04/16.
 */
public class JSONParser {
    JSONObject reader =null ;

    public JSONParser(String buf){
        try {
            reader = new JSONObject(buf);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getStatus(){
        if(reader != null)
            try{
                return reader.getString("Status");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        else
            return null;
    }

    public JSONArray getList(String str){
        if(reader != null)
            return reader.optJSONArray(str);
        else
            return null;
    }

    public JSONObject getJSONObject(String str){
        if(reader != null)
            return reader.optJSONObject(str);
        else
            return null;
    }

    public int getIndex(){
        int value = -1;
        if(reader != null) {
            try{
                value =  reader.getInt("Id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public int getInt(String str) {
        int value = -1;
        if (reader != null) {
            try {
                value = reader.getInt(str);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public String getString(String str) {
        String value = null;

        if (reader != null) {
            try {
                value =  reader.getString(str);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return value;
    }
}
