package com.devel.ccqf.ccqfmisson.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.devel.ccqf.ccqfmisson.CCQFCategorie;
import com.devel.ccqf.ccqfmisson.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thierry on 16-06-06.
 */
public class CCQFCategorieAdapter extends BaseAdapter {
    private Context context;
    private List<CCQFCategorie> docList;
    private LayoutInflater mInflater;

    public CCQFCategorieAdapter(Context context, List<CCQFCategorie> liste){
        this.context = context;
        docList = liste;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return docList.size();
    }

    @Override
    public Object getItem(int position) {
        return docList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CCQFCategorie currentDoc = docList.get(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.document_list_adapter_layout, null);
        TextView docName = (TextView)convertView.findViewById(R.id.docName);
        docName.setText(currentDoc.getNom());
        return convertView;
    }
}
