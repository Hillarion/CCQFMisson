package com.devel.ccqf.ccqfmisson.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.devel.ccqf.ccqfmisson.R;
import com.devel.ccqf.ccqfmisson.ReseauSocial.ConversationHead;

import java.util.ArrayList;

/**
 * Created by thierry on 26/05/16.
 */
public class CustomFeedListAdapter extends BaseAdapter {
    private Context context;
    ArrayList<ConversationHead> convers;

    public CustomFeedListAdapter(Context context, ArrayList<ConversationHead> convItems) {
        this.context = context;
        convers = convItems;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return convers.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ConversationHead conv = convers.get(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.feed_list_adapter_layout, null);
        TextView txtLastMsgHdr =  (TextView) convertView.findViewById(R.id.lblMsgFrom);
        TextView txtLastMsgTime =  (TextView) convertView.findViewById(R.id.txtLastMsgTime);
        TextView txtNameList =  (TextView) convertView.findViewById(R.id.txtNameList);
        txtLastMsgHdr.setText(conv.getLastMsg());
        txtNameList.setText(conv.getFrom());
        txtLastMsgTime.setText(conv.getWhen().toString());

        return convertView;
    }
}
