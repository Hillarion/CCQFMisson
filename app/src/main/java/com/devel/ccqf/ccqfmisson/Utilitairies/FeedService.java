package com.devel.ccqf.ccqfmisson.Utilitairies;

import android.app.IntentService;
import android.content.Intent;

import com.devel.ccqf.ccqfmisson.Database.InterfaceDB;

/**
 * Created by thierry on 13/06/16.
 */
public class FeedService extends IntentService {
    public FeedService(){
        super("Feed-service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InterfaceDB iDb = new InterfaceDB(FeedService.this);
        if(iDb != null){
            if(iDb.isNetAccessible()){
                iDb.readMessages(iDb.getCurrentUserID());
            }
        }
    }
}
