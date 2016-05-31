package com.devel.ccqf.ccqfmisson;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.devel.ccqf.ccqfmisson.Adapters.CustomContactListAdapter;
import com.devel.ccqf.ccqfmisson.Database.InterfaceDB;
import com.devel.ccqf.ccqfmisson.ReseauSocial.ConversationHead;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by thierry on 26/05/16.
 */
public class FeedListActivity extends CCQFBaseActivity/*AppCompatActivity*/ {
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
                final Dialog d = new Dialog(FeedListActivity.this);
                d.setContentView(R.layout.dialog_destinataires);
                d.setTitle("qwerjhgfdsdfgh");
                Button btnOK = (Button)d.findViewById(R.id.btnUserListOk);
                Button btnRefreshUserList = (Button)d.findViewById(R.id.btnRefreshUserList);
                final ListView lstUserList = (ListView)d.findViewById(R.id.lstUserList);
                btnRefreshUserList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InterfaceDB iDb = new InterfaceDB(FeedListActivity.this);
                        ArrayList<Users> uList = null;
                        if(iDb != null){
                            uList = iDb.getUserList();
                            lstUserList.setAdapter(new CustomContactListAdapter(FeedListActivity.this, uList));
                        }
                    }
                });
                d.show();
                    /*Ici, on créé une  nouvelle conversation puis on va au FeedActivity*/
            }
        });


    }

    public void dialogSelectUsers() {
    }

}
