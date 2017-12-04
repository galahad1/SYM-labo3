package ch.heigvd.iict.sym.a3dcompassapp.beacon;

import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.List;

import ch.heigvd.iict.sym.a3dcompassapp.R;

public class BeaconActivity extends AppCompatActivity implements BeaconConsumer {

    // For logging purposes
    private static final String TAG = BeaconActivity.class.getSimpleName();

    private ListView listView;
    private List<String> beacons = new ArrayList<>();
    private BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);
        beaconManager.getBeaconParsers().add(new BeaconParser()
            .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));

        //TODO Voir s'il y a besoin de faire mieux (recycledView)
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(BeaconActivity.this,
            android.R.layout.simple_list_item_1, beacons);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.i(TAG, "I just saw a beacon for the first time");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see a beacon");
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + i);
            }
        });

        try {
            beaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
        } catch (RemoteException e) {
            Log.i(TAG, "An error occured");
        }
    }
}
