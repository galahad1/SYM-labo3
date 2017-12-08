package ch.heigvd.iict.sym.a3dcompassapp.NFC;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ch.heigvd.iict.sym.a3dcompassapp.R;

/**
 * Created by wojtek on 12/8/17.
 */

public class TIMEActivity extends AppCompatActivity{


    private TextView view = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.view = (TextView) findViewById(R.id.textTime);
    }


}
