package com.example.newsreader;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<detailsOfNews>{
    ArrayList<detailsOfNews> newsl;
    Context context;
    public ListViewAdapter(Context context, ArrayList<detailsOfNews> newsl){
        super(context,R.layout.list_item,newsl);
        this.newsl = newsl;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item,null);
            TextView headl = (TextView) convertView.findViewById(R.id.headline);
            headl.setText(newsl.get(position).getNews());
        }
        return convertView;
    }
}
