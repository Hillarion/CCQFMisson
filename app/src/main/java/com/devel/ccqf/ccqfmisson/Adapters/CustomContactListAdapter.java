package com.devel.ccqf.ccqfmisson.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.devel.ccqf.ccqfmisson.R;
import com.devel.ccqf.ccqfmisson.Users;

import java.util.ArrayList;

/**
 * Created by thierry on 16-05-27.
 */
public class CustomContactListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Users> uList;
    private LayoutInflater mInflater;

    public CustomContactListAdapter(Context context, ArrayList<Users>results){
        this.context = context;
        uList = results;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return uList.size();
    }

    @Override
    public Object getItem(int position) {
        return uList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return uList.get(position).getUserID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Users currentUser = uList.get(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.contact_list_adapter_layout, null);
        TextView userID = (TextView)convertView.findViewById(R.id.userID);
        TextView userName = (TextView)convertView.findViewById(R.id.userName);
        userID.setText(currentUser.getUID());
        userName.setText(currentUser.getUserName());

        return convertView;
    }
}
