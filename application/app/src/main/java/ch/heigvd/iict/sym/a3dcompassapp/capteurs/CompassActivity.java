package ch.heigvd.iict.sym.a3dcompassapp.capteurs;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import ch.heigvd.iict.sym.a3dcompassapp.R;

/**
 * This class implements a 3D compass that display magnetic north direction
 * Modified by Tano Iannetta, Lara Chauffoureaux, Wojciech Myszkorowski
 */
public class CompassActivity extends AppCompatActivity implements SensorEventListener {

    //opengl
    private OpenGLRenderer opglr           = null;
    private GLSurfaceView  m3DView         = null;

    //sensors
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnometer;


    // sensors values
    private float[] accelerometerReading = null;
    private float[] magnetometerReading = null;

    // matrix
    private float[] rotationMatrix = new float[16];


    public CompassActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // we need fullscreen
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // we initiate the view
        setContentView(R.layout.activity_compass);

        // link to GUI
        this.m3DView = findViewById(R.id.compass_opengl);

        //we create the 3D renderer
        this.opglr = new OpenGLRenderer(getApplicationContext());

        //init opengl surface view
        this.m3DView.setRenderer(this.opglr);

        // sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.magnometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, magnometer, SensorManager.SENSOR_DELAY_GAME );
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);

    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }



    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // new values
       if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerReading = sensorEvent.values;
        }
        else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
           magnetometerReading = sensorEvent.values;
        }

        if (accelerometerReading != null && magnetometerReading != null)
        {
            // calculus and display
            SensorManager.getRotationMatrix(rotationMatrix, null,accelerometerReading,magnetometerReading);
            this.opglr.swapRotMatrix(rotationMatrix);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
