package com.devel.ccqf.ccqfmisson.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.devel.ccqf.ccqfmisson.Users;

import java.util.ArrayList;

/**
 * Created by thierry on 16-05-27.
 */
public class CustomContactListAdapter extends BaseAdapter {
    private ArrayList<Users> uList;
    private LayoutInflater mInflater;

    public CustomContactListAdapter(Context context, ArrayList<Users>results){
        uList = results;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return uList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
