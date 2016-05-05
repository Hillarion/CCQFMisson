package com.devel.ccqf.ccqfmisson.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.devel.ccqf.ccqfmisson.R;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyObject;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by jo on 5/4/16.
 */
public class CustomSurveyAdapter extends BaseAdapter {
    private static List<SurveyObject> arrayListSurvey;

    private LayoutInflater mInflater;

    public CustomSurveyAdapter(Context context, List<SurveyObject>results){
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
            convertView = mInflater.inflate(R.layout.survey_row_layout, null);
            holder = new ViewHolder();
            holder.txtId = (TextView)convertView.findViewById(R.id.txtIDSurvey);
            holder.txtNom = (TextView)convertView.findViewById(R.id.txtAnswerSurvey);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.txtId.setText(Integer.toString(arrayListSurvey.get(position).getSurveyId()));
        holder.txtNom.setText(arrayListSurvey.get(position).getQuestionTexte());

        return convertView;
    }
    static class ViewHolder{
        TextView txtId;
        TextView txtNom;
    }
}
