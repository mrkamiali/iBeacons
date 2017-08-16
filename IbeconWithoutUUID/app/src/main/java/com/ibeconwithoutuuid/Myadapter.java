package com.ibeconwithoutuuid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class Myadapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private Context context;
    private ArrayList<MainActivity.ModelClass> arrayList;

    public Myadapter(Context context, ArrayList<MainActivity.ModelClass> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public MainActivity.ModelClass getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView;
        if (convertView == null) {
            rootView = inflater.inflate(R.layout.beacon_model_layout, parent, false);
        } else {
            rootView = convertView;
        }
        TextView uuidView = (TextView) rootView.findViewById(R.id.uuidView);
        TextView majorView = (TextView) rootView.findViewById(R.id.major);
        TextView minorView = (TextView) rootView.findViewById(R.id.minor);

        MainActivity.ModelClass modelClass = arrayList.get(position);

        if (modelClass != null) {
            uuidView.setText(modelClass.getUUID());
            majorView.setText("Major: " + modelClass.getMajor());
            minorView.setText("Minor: " + modelClass.getMinor());
        }

        return rootView;

    }
}