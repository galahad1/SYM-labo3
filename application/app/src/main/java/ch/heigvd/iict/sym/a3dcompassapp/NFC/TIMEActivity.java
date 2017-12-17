package ch.heigvd.iict.sym.a3dcompassapp.NFC;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

import ch.heigvd.iict.sym.a3dcompassapp.R;

/**
 * This class is a class that shows your level of access during a period of time
 * if you wait your level of authentication decreases
 * Modified by Tano Iannetta, Lara Chauffoureaux, Wojciech Myszkorowski
 */

public class TIMEActivity extends NFCActivity{


    private TextView view = null;
    private TextView textAuthenticate = null;
    private int AUTHENTICATE_MAX = 10;
    private int AUTHENTICATE_MEDIUM = 6;
    private int AUTHENTICATE_LOW = 2;
    public  int level = AUTHENTICATE_MAX;
    private Timer timer;            // Timer regulating the list send
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        this.view = (TextView) findViewById(R.id.textNFC);
        this.textAuthenticate = (TextView) findViewById(R.id.textLevel);
        level = AUTHENTICATE_MAX;
        TimerTask timerTask = new MyTimerTask();
        Timer timer = new Timer(true);
        //start after 5s with a period before a decrease of 3s
        timer.schedule(timerTask, 0, 2000);


    }
    //mehtod inheried from mother class to be able to read nfc and right text field
    protected void setResult(String text) {
        if (text.equals("test")) {
            this.view.setText(text);
            level = AUTHENTICATE_MAX;
        }
    }
    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            // needed because code is setting view
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    level--;
                    //if you come under this level you come back to first login page
                    if (level < AUTHENTICATE_LOW) {
                        finish();
                        // need to authenticate with nfc
                    } else if (level < AUTHENTICATE_MEDIUM) {
                        textAuthenticate.setText("Level of Authentification medium \n you will lose access");
                    } else {
                        textAuthenticate.setText("Level of Authentification maximum");
                    }
                }
            });
        }

    }

}
