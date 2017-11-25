package ch.heigvd.iict.sym.a3dcompassapp.codesBarres;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import ch.heigvd.iict.sym.a3dcompassapp.R;

/**
 * This activity will scan bare codes and print their results
 * Note that it will need an application (BarCode Scanner) for the scan itself
 * If the application is not installed on the device, it will ask the user to install it
 * when he click on the scan button
 * @author Tano Iannetta, Wojciech Myszkorowski and Lara Chauffoureaux
 */
public class CodesBarresActivity extends AppCompatActivity {

    private EditText result;
    private Button scannerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codesbarres);

        //Recuperate Ui things
        this.scannerButton = (Button) findViewById(R.id.scannerButton);
        this.result = (EditText) findViewById(R.id.result);

        // Listener associated to "scanner" button
        scannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch scan with the Barcode Scanner application
                IntentIntegrator integrator = new IntentIntegrator(CodesBarresActivity.this);
                integrator.initiateScan();
            }
        });


    }
    // get result of the scan
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(scanResult != null)
        {
            result.setText(scanResult.getContents()); // print result
        }
    }

}