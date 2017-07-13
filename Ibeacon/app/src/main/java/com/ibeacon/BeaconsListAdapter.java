package com.ibeacon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

/**
 * Created by kamranali on 13/07/2017.
 */

class BeaconsListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Beacon> beaconsList;

    public BeaconsListAdapter() {
    }

    public BeaconsListAdapter(Context context, ArrayList<Beacon> beaconsList) {
        this.context = context;
        this.beaconsList = beaconsList;
    }

    @Override
    public int getCount() {
        return beaconsList.size();
    }

    @Override
    public Beacon getItem(int position) {
        return beaconsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.main_beacon_listview, null);

        Beacon beacon = beaconsList.get(position);

        TextView uuidView = (TextView) view.findViewById(R.id.main_uuid);
        TextView minorView = (TextView) view.findViewById(R.id.main_minor);
        TextView majorView = (TextView) view.findViewById(R.id.main_major);

        uuidView.setText("UUID: "+beacon.getId1());
        minorView.setText("Minor: "+beacon.getId3());
        majorView.setText("Major: "+beacon.getId2());


        return view;
    }
}
