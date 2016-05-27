package com.devel.ccqf.ccqfmisson;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by thierry on 26/05/16.
 */
public class FeedListActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_list_listview_layout);
        Drawable logo = null;
        logo = getResources().getDrawable(R.mipmap.ccqf_logo);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setLogo(logo);

        FloatingActionButton fabNewFeed = (FloatingActionButton) findViewById(R.id.fabNewFeed);
        fabNewFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    /*Ici, on créé une  nouvelle conversation puis on va au FeedActivity*/
            }
        });


    }


}
