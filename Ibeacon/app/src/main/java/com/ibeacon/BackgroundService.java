package com.ibeacon;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

import static org.altbeacon.beacon.service.BeaconService.TAG;

/**
 * Created by kamranali on 20/07/2017.
 */

public class BackgroundService extends Service implements BeaconConsumer {
    private BeaconManager beaconManager;

    public BackgroundService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setBackgroundScanPeriod(1100l);
        beaconManager.setBackgroundBetweenScanPeriod(3000l);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(TAG, "onStart: ");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onStartCommand: ");
        beaconManager
                .getBeaconParsers()
                .add(new BeaconParser().
                        setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
        return START_STICKY;
    }

    public void showNotification(Region region) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Entered beacon " + region.getUniqueId() + " with UUID " + region.getId1())
                .setContentText((CharSequence) "BeaconEntered")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    public void showNotificationforExit(Region region) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Exit beacon " + region.getUniqueId() + " with UUID " + region.getId1())
                .setContentText((CharSequence) "BeaconEntered")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    @Override
    public void onBeaconServiceConnect() {

        //input your beacons UUID
        final Region region = new Region("mybeacon1", Identifier.parse("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"), null, null);
        //if you want multiple beacons to hear uncommit this line
//        final Region region1 = new Region("mybeacon2", Identifier.parse("FDA50693-A4E2-4FB1-AFCF-C6EB07647825"), null, null);


        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i("", "I just saw an beacon for the first time!" + region.getId1());
                showNotification(region);
                try {
                    beaconManager.startRangingBeaconsInRegion(region);
                    //for multiple beacons
//                    beaconManager.startRangingBeaconsInRegion(region1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(getApplicationContext(), "Entered Beacon " + region.getId1(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void didExitRegion(Region region) {
                Log.i("", "I no longer see an beacon" + region.getId1());
                showNotificationforExit(region);
                try {
                    beaconManager.stopRangingBeaconsInRegion(region);
                    //for multiple beacons
//                    beaconManager.startRangingBeaconsInRegion(region1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                try {
                    beaconManager.startMonitoringBeaconsInRegion(region);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i("", "I have just switched from seeing/not seeing beacons: " + state + " " + region.getId1());
                try {
                    beaconManager.startRangingBeaconsInRegion(region);
                    //for multiple beacons
//                    beaconManager.startRangingBeaconsInRegion(region1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for (final Beacon beacon : beacons) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (!beaconsModelsList.contains(beacon)){
//                                beaconsModelsList.add(beacon);
//                                adapter.notifyDataSetChanged();
//                            }
//                        }
//                    });

                    Log.d(TAG, "didRangeBeaconsInRegion: UUID: " + beacon.getId1()
                            + "\nMAJOR: " + beacon.getId2()
                            + "\nMINOR: " + beacon.getId3()
                            + "\nRSSI: " + beacon.getRssi()
                            + "\nTX: " + beacon.getTxPower()
                            + "\nDISTANCE: " + beacon.getDistance());
                }
//                try {
//                    beaconManager.startRangingBeaconsInRegion(region);
//                    //for multiple beacons
////                    beaconManager.startRangingBeaconsInRegion(region1);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }


            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(region);
            //for multiple beacons
//            beaconManager.startMonitoringBeaconsInRegion(region1);
        } catch (RemoteException e) {
        }
    }
}
