package com.devel.ccqf.ccqfmisson;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.devel.ccqf.ccqfmisson.Database.InterfaceDB;
import com.devel.ccqf.ccqfmisson.ReseauSocial.ConversationHead;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by thierry on 26/05/16.
 */
public class FeedListActivity extends AppCompatActivity {
    ListView lstFeedList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_list_listview_layout);
        Drawable logo = null;
        logo = getResources().getDrawable(R.mipmap.ccqf_logo);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setLogo(logo);

        lstFeedList = (ListView)findViewById(R.id.lstFeedList);

        InterfaceDB iDb = new InterfaceDB(this);
        if(iDb != null) {
            List<Integer>  convList = iDb.getMessageThreadList();
            if(convList != null){
                ArrayList<ConversationHead> cHeadList = new ArrayList<ConversationHead>();
                Iterator<Integer> iter = convList.iterator();
                while(iter.hasNext()){
                    ConversationHead tmpHead = iDb.getmessageHead(iter.next().intValue());
                    if(tmpHead != null)
                        cHeadList.add(tmpHead);
                }
            }
        }

        FloatingActionButton fabNewFeed = (FloatingActionButton) findViewById(R.id.fabNewFeed);
        fabNewFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    /*Ici, on créé une  nouvelle conversation puis on va au FeedActivity*/
            }
        });


    }

    public void dialogSelectUsers() {
    }

}
