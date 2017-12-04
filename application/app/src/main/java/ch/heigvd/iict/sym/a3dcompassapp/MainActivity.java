package ch.heigvd.iict.sym.a3dcompassapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ch.heigvd.iict.sym.a3dcompassapp.beacon.BeaconActivity;
import ch.heigvd.iict.sym.a3dcompassapp.capteurs.CompassActivity;
import ch.heigvd.iict.sym.a3dcompassapp.barCodes.BarCodesActivity;

/**
 * Main activity of the application, offer 4 buttons to the user to allow him to choose the
 * activity to open.
 *
 * @author Tano Iannetta, Lara Chauffoureaux, Wojciech Myszkorowski
 */
public class MainActivity extends AppCompatActivity {

    // For logging purposes
    private static final String TAG = MainActivity.class.getSimpleName();

    private Button buttonNFC;
    private Button buttonCodesBarres;
    private Button buttonBeacon;
    private Button buttonCapteurs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recuperate UI things
        this.buttonNFC = (Button) findViewById(R.id.buttonNFC);
        this.buttonCodesBarres = (Button) findViewById(R.id.buttonCodesBarres);
        this.buttonBeacon = (Button) findViewById(R.id.buttonBeacon);
        this.buttonCapteurs = (Button) findViewById(R.id.buttonCapteurs);

        // Listener associated to "NFC" button
        buttonNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Listener associated to "Codes Barres" button
        buttonCodesBarres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BarCodesActivity.class);
                startActivity(intent);
            }
        });

        // Listener associated to "Beacon" button
        buttonBeacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BeaconActivity.class);
                startActivity(intent);
            }
        });

        // Listener associated to "Capteurs" button
        buttonCapteurs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CompassActivity.class);
                startActivity(intent);
            }
        });
    }
}

