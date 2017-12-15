package ch.heigvd.iict.sym.a3dcompassapp.beacon;

import android.Manifest;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.heigvd.iict.sym.a3dcompassapp.R;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * This activity will detect beacon in the region and display their ID and the distance.
 * Note that this activity also implements runtime permissions to prompt the user
 * to authorize geolocation.
 *
 * @author Tano Iannetta, Wojciech Myszkorowski and Lara Chauffoureaux
 */
@RuntimePermissions
public class BeaconActivity extends AppCompatActivity implements BeaconConsumer {

    // For logging purposes
    private static final String TAG = BeaconActivity.class.getSimpleName();

    private ListView listView;                          // GUI element
    private List<String> beacons = new ArrayList<>();   // List of beacons information
    private ArrayAdapter<String> adapter;               // Adapter for list view
    private BeaconManager beaconManager;                // Beacon manager entity

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);

        // Recuperate UI things
        listView = (ListView) findViewById(R.id.listView);

        // Adapter preparation for the list
        adapter = new ArrayAdapter<>(BeaconActivity.this,
            android.R.layout.simple_list_item_1, beacons);
        listView.setAdapter(adapter);

        // Beacon initialization
        initializeBeacon();
        BeaconActivityPermissionsDispatcher.initializeBeaconWithPermissionCheck(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {

                // Cleaning the list before updating it
                beacons.clear();

                // For each beacon, add it in the list
                for(Beacon beacon : collection) {
                    beacons.add("Beacon's UUID: " + beacon.getId1() + "\n" +
                        "Distance: " + beacon.getDistance());
                }

                // Update of the view by UIThread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myMonitoringUniqueId",
                null, null, null));
        } catch (RemoteException e) {
            Log.i(TAG, "An error occured");
        }
    }

    /**
     * Small method initializing the beacon manager, it requires the "access_coarse_location"
     * which is displayed at runtime.
     */
    @NeedsPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    protected void initializeBeacon() {
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser()
            .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
    }
}
