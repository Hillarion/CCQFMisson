package com.devel.ccqf.ccqfmisson;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by thierry on 26/05/16.
 */
public class FeedListFragment  extends Fragment {

    public FeedListFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_feed_list, container, false);

        return v;
    }
}
