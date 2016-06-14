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
import com.devel.ccqf.ccqfmisson.Users;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by thierry on 26/05/16.
 */
public class CustomFeedAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MessagePacket> messagesItems;
    private ArrayList<Users> userNameList;

    public CustomFeedAdapter(Context context, ArrayList<MessagePacket> navDrawerItems) {
        this.context = context;
        this.messagesItems = navDrawerItems;
    }

    public void add(MessagePacket msg){
        messagesItems.add(msg);
    }

    public void setUserNameList(ArrayList<Users> liste){
        userNameList = liste;
    }

    public void setUserMessageList(ArrayList<MessagePacket> liste){
        messagesItems = liste;
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

        MessagePacket m = messagesItems.get(position);

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

        txtMsg.setText(m.getMessage());
        Iterator<Users> uIter = userNameList.iterator();
        while(uIter.hasNext()){
            Users lUser = uIter.next();
            if(lUser.getUserID() == m.getSource())
                lblFrom.setText("" + lUser.getUserName());
        }

        return convertView;
    }


}
