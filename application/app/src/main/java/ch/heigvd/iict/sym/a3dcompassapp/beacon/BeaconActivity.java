package ch.heigvd.iict.sym.a3dcompassapp.beacon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ch.heigvd.iict.sym.a3dcompassapp.R;

public class BeaconActivity extends AppCompatActivity {

    // For logging purposes
    private static final String TAG = BeaconActivity.class.getSimpleName();

    private ListView listView;
    private List<String> beacons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);

        //TODO Voir s'il y a besoin de faire mieux (recycledView)
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(BeaconActivity.this,
            android.R.layout.simple_list_item_1, beacons);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}
