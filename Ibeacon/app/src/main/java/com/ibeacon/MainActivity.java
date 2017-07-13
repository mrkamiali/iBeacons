package com.ibeacon;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

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

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }
    @Override
    public void onBeaconServiceConnect() {

        //input your beacons UUID
        final Region region = new Region("mybeacon1", Identifier.parse("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"), null, null);
       //if you want multiple beacons to hear uncommit this line
//        final Region region1 = new Region("mybeacon2", Identifier.parse("FDA50693-A4E2-4FB1-AFCF-C6EB07647825"), null, null);
        try {
            beaconManager.startRangingBeaconsInRegion(region);
            //for multiple beacons
//            beaconManager.startRangingBeaconsInRegion(region1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            beaconManager.startMonitoringBeaconsInRegion(region);
            //for multiple beacons
//            beaconManager.startMonitoringBeaconsInRegion(region1);
        } catch (RemoteException e) {
        }

        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i("", "I just saw an beacon for the first time!"+region.getId1());

            }

            @Override
            public void didExitRegion(Region region) {
                Log.i("", "I no longer see an beacon"+region.getId1());

                try {
                    beaconManager.startMonitoringBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i("", "I have just switched from seeing/not seeing beacons: " + state);
            }
        });

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for (final Beacon beacon : beacons) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!beaconsModelsList.contains(beacon)){
                                beaconsModelsList.add(beacon);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                    Log.d(TAG, "didRangeBeaconsInRegion: UUID: " + beacon.getId1()
                            + "\nMAJOR: " + beacon.getId2()
                            + "\nMINOR: " + beacon.getId3()
                            + "\nRSSI: " + beacon.getRssi()
                            + "\nTX: " + beacon.getTxPower()
                            + "\nDISTANCE: " + beacon.getDistance());
                }
            }
        });
    }

}
