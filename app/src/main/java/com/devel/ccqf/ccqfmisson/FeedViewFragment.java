package com.devel.ccqf.ccqfmisson;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.devel.ccqf.ccqfmisson.ReseauSocial.MessagePacket;

import java.util.List;

/**
 * Created by thierry on 26/05/16.
 */
public class FeedViewFragment extends Fragment {
    private Button btnSend;
    private EditText inputMsg;
    private ListView listViewMessages;
    private List<MessagePacket> listMessages;

    public FeedViewFragment(){
        super();
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_feed_list, container, false);

        btnSend = (Button)v.findViewById(R.id.btnSend);
        inputMsg = (EditText) v.findViewById(R.id.inputMsg);
        listViewMessages = (ListView) v.findViewById(R.id.list_view_messages);

        return v;
    }

}
