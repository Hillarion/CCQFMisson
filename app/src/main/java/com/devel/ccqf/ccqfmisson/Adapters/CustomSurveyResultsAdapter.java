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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jo on 5/4/16.
 */
public class CustomSurveyResultsAdapter extends BaseAdapter {
    private static ArrayList<SurveyObject> arrayListSurvey;

    private LayoutInflater mInflater;

    public CustomSurveyResultsAdapter(Context context, ArrayList<SurveyObject>results){
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
            convertView = mInflater.inflate(R.layout.survey_results_row_layout, null);
            holder = new ViewHolder();
            holder.txtQuestion = (TextView)convertView.findViewById(R.id.txtSurveyResultsQuestion);
            holder.txtStatus = (TextView)convertView.findViewById(R.id.txtResultStatus);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.txtQuestion.setText(arrayListSurvey.get(position).getQuestionTexte());
        holder.txtStatus.setText(String.valueOf(arrayListSurvey.get(position).getQuestionId()));

        return convertView;
    }
    static class ViewHolder{
        TextView txtQuestion;
        TextView txtStatus;
    }
}
