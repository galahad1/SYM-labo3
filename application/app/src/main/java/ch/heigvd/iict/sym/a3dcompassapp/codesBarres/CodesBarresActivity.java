package ch.heigvd.iict.sym.a3dcompassapp.codesBarres;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ch.heigvd.iict.sym.a3dcompassapp.R;

public class CodesBarresActivity extends AppCompatActivity {


    private EditText scannerResult;
    private Button scannerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codesbarres);

        //Recuperate Ui things
        this.scannerButton = (Button) findViewById(R.id.scannerButton);
        this.scannerResult = (EditText) findViewById(R.id.scannerResult);

        // Listener associated to "scanner" button
        scannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo
            }
        });


    }
}
