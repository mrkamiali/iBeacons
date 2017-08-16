package com.ibeacon;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BeaconManager beaconManager;
    private String TAG = MainActivity.class.getSimpleName();
    private ListView beacons_list;
    private BeaconsListAdapter adapter;
    private ArrayList<Beacon> beaconsModelsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        beaconsModelsList = new ArrayList<>();

        beacons_list = (ListView) findViewById(R.id.beacons_list);
        adapter = new BeaconsListAdapter(this, beaconsModelsList);
        beacons_list.setAdapter(adapter);

        Intent i = new Intent(this,BackgroundService.class) ;
        startService(i);

    }


}
