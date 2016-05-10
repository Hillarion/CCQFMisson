package com.devel.ccqf.ccqfmisson.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.devel.ccqf.ccqfmisson.R;

import java.util.ArrayList;

/**
 * Created by jo on 5/4/16.
 */
public class CustomSurveyAnswerAdapter extends BaseAdapter {
    private static ArrayList<String> arrayListSurvey;

    private LayoutInflater mInflater;

    public CustomSurveyAnswerAdapter(Context context, ArrayList<String>results){
        arrayListSurvey = results;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayListSurvey.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayListSurvey.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.answer_row_layout, null);
            holder = new ViewHolder();
            holder.txtNom = (TextView)convertView.findViewById(R.id.txtNewAnswerSurvey);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.txtNom.setText(arrayListSurvey.get(position));

        return convertView;
    }
    static class ViewHolder{
        TextView txtNom;
    }
}
