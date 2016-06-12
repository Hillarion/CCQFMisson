package com.devel.ccqf.ccqfmisson.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.devel.ccqf.ccqfmisson.R;
import com.devel.ccqf.ccqfmisson.ReseauSocial.ConversationHead;

import java.util.List;

/**
 * Created by thierry on 16-06-07.
 */
public class TheadListAdapter extends BaseAdapter {

    private Context context;
    private List<ConversationHead> threadList;
    private LayoutInflater mInflater;

    public TheadListAdapter(Context context, List<ConversationHead> liste){
        this.context = context;
        threadList = liste;
        mInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        if(threadList != null) {
            return threadList.size();
        }
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        if(threadList != null)
           return threadList.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ConversationHead currentDoc = threadList.get(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.feed_list_adapter_layout, null);
        TextView txtConversationID = (TextView)convertView.findViewById(R.id.txtConversationID);
        TextView txtNameList = (TextView)convertView.findViewById(R.id.txtNameList);
        TextView txtLastMsgHdr = (TextView)convertView.findViewById(R.id.txtLastMsgHdr);
        TextView txtLastMsgTime = (TextView)convertView.findViewById(R.id.txtLastMsgTime);
        txtConversationID.setText(currentDoc.getConvID());
        txtNameList.setText(currentDoc.getFrom());
        txtLastMsgHdr.setText(currentDoc.getLastMsg());
        txtLastMsgTime.setText(currentDoc.getWhen());

        return convertView;
    }
}
