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
        System.out.print("CCQF FeedService() \n\n");
        System.out.flush();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InterfaceDB iDb = new InterfaceDB(FeedService.this);
        System.out.print("CCQF FeedService onHandleIntent\n\n");
        System.out.flush();
        if(iDb != null){
            if(iDb.isNetAccessible()){
                System.out.print("CCQF FeedService readMessages\n\n");
                System.out.flush();
                iDb.readMessages(iDb.getCurrentUserID());
            }
        }
    }
}
