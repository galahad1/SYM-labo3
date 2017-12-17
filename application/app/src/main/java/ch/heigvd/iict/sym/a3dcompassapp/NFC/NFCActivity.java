package ch.heigvd.iict.sym.a3dcompassapp.NFC;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import ch.heigvd.iict.sym.a3dcompassapp.R;

import static android.R.attr.mode;


/**
 * This class is the mother class that allows you to read and process
 * NFC technology. It allows you to read the tag inside the NFC.
 * Modified by Tano Iannetta, Lara Chauffoureaux, Wojciech Myszkorowski
 */
public class NFCActivity extends AppCompatActivity {

    private static final String validPassword = "tata";
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NFC_TASK";
    public static final String NFC = "test";
    private Switch switch1 = null;
    private EditText password = null;
    private TextView viewNFC = null;
    private Button Signin = null;
    private NfcAdapter mNfcAdapter;
    private boolean versionAuth = true;

    protected void setResult(String text) {
        viewNFC.setText(text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        switch1 = (Switch) findViewById(R.id.switch1);
        password = (EditText) findViewById(R.id.passwdtext);
        Signin = (Button) findViewById(R.id.buttonLog);
        viewNFC = (TextView) findViewById(R.id.textnfc);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        // if we click on the swtich then we change mode so we put a negation to the variable
        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                versionAuth = !versionAuth;
                if (versionAuth) {
                    Toast.makeText(getApplicationContext(), "mode OR", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "mode AND", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (!mNfcAdapter.isEnabled()) {
            viewNFC.setText("NFC is disabled.");
        } else {
            viewNFC.setText("Waiting on NFC tag");
        }

        handleIntent(getIntent());
        //login action and test authorization
        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwd = password.getText().toString();
                String textNFC = viewNFC.getText().toString();

                //authentification and
                if ( !versionAuth &&( passwd.equals(validPassword) && textNFC.equals(NFC) )) {
                    boolean val = (textNFC.equals(NFC) && passwd.equals(validPassword));
                    Intent intent = new Intent(NFCActivity.this, TIMEActivity.class);
                    NFCActivity.this.startActivity(intent);
                    //authentification or
                } else if (versionAuth && (passwd.equals(validPassword) || textNFC.equals(NFC)) ) {
                    Intent intent = new Intent(NFCActivity.this, TIMEActivity.class);
                    NFCActivity.this.startActivity(intent);
                } else {
                    //set a message that password is wrong or wrong nfc
                    Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    // called in onPause()
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
         adapter.disableForegroundDispatch(activity);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        stopForegroundDispatch(this, mNfcAdapter);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }
    /**
     * Background task for reading the data. Do not block the UI thread while reading.
     *
     * @author Ralf Wondratschek
     */
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                //display the nfc tag
                setResult(result);
            }
        }
    }

}
