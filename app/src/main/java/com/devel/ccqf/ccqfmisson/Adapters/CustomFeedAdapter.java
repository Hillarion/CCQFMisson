package com.devel.ccqf.ccqfmisson.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.devel.ccqf.ccqfmisson.R;
import com.devel.ccqf.ccqfmisson.ReseauSocial.MessagePacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thierry on 26/05/16.
 */
public class CustomFeedAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MessagePacket> messagesItems;

    public CustomFeedAdapter(Context context, ArrayList<MessagePacket> navDrawerItems) {
        this.context = context;
        this.messagesItems = navDrawerItems;
    }

    public void add(MessagePacket msg){
        messagesItems.add(msg);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messagesItems.size();
    }

    @Override
    public Object getItem(int position) {
        return messagesItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position/*messagesItems.get(position).getConvID()*/;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /**
         * The following list not implemented reusable list items as list items
         * are showing incorrect data Add the solution if you have one
         * */
        System.out.print("CCQF CustomFeedAdapter getView() messagesItems = " +messagesItems + "\n\n");
        System.out.flush();

        MessagePacket m = messagesItems.get(position);
        System.out.print("CCQF CustomFeedAdapter getView() m = " + m + "\n\n");
        System.out.flush();

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // Identifying the message owner
        if (messagesItems.get(position).isSelf()) {
            // message belongs to you, so load the right aligned layout
            convertView = mInflater.inflate(R.layout.list_item_message_right,
                    null);
        } else {
            // message belongs to other person, load the left aligned layout
            convertView = mInflater.inflate(R.layout.list_item_message_left,
                    null);
        }

        TextView lblFrom = (TextView) convertView.findViewById(R.id.lblMsgFrom);
        TextView txtMsg = (TextView) convertView.findViewById(R.id.txtMsg);

        System.out.print("CCQF CustomFeedAdapter getView() m.getMessage() =  " + m.getMessage() + "\n\n");
        System.out.flush();
        System.out.print("CCQF CustomFeedAdapter getView() m.getSource() = " + m.getSource() + "\n\n");
        System.out.flush();

        txtMsg.setText(m.getMessage());
        lblFrom.setText(m.getSource());

        return convertView;
    }
}
