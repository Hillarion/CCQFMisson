package com.devel.ccqf.ccqfmisson.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.devel.ccqf.ccqfmisson.R;
import com.devel.ccqf.ccqfmisson.SurveyStruct.SurveyGroup;

import java.util.ArrayList;

/**
 * Created by thierry on 16-07-22.
 */
public class CustomSurveyListAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<SurveyGroup> sgList = null;
    private LayoutInflater mInflater;

    public CustomSurveyListAdapter(Context context, ArrayList<SurveyGroup> list){
        sgList = list;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(sgList!= null)
            return sgList.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        if(sgList != null)
            return sgList.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        if(sgList != null)
            return sgList.get(position).getId();
        else
            return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SurveyGroup sg = sgList.get(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.survey_results_row_layout, null);
        TextView groupID = (TextView)convertView.findViewById(R.id.txtSurveyResultsQuestion);
        groupID.setTextSize(20.0f);
        TextView grpDate = (TextView)convertView.findViewById(R.id.txtResultStatus);
        grpDate.setTextSize(20.0f);
        groupID.setText("" + sg.getId());
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        grpDate.setText(sdf.format(sg.getDateLimite()));

        return convertView;
    }
}
