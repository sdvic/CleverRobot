package org.jointheleague.erik.cleverrobot;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.widget.ScrollView;
import android.widget.TextView;

import org.jointheleague.erik.irobot.IRobotInterface;
import org.jointheleague.erik.irobot.SimpleIRobot;

import java.util.Locale;

import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;

/***********************************************************************
 * This is the main activity for the iRobot Create 2 Roomba
 * modified with the League IOIO interface board #4700-0003
 * There should be no need to modify this class. Modify Pilot instead.
 * rev 160506A by Stanley Kurdziel...removed TTS Android code
 * rev 160606A by Vic...added blinking light
 * @author Erik Colban
 ************************************************************************/
public class Dashboard extends IOIOActivity
        implements TextToSpeech.OnInitListener, SensorEventListener {
    /**
     * Text view that contains all logged messages
     */
    private TextView mText;
    private ScrollView scroller;
    /**
     * A Pilot instance
     */
    private Pilot kalina;
    protected static final int MY_DATA_CHECK_CODE = 33;
    /**
     * Compass stuff
     */
    SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private Sensor sensorMagneticField;
    private float[] valuesAccelerometer;
    private float[] valuesMagneticField;
    private float[] matrixR;
    private float[] matrixI;
    private float[] matrixValues;
    private double azimuth;
    private double pitch;
    private double roll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * Since the android device is carried by the iRobot, we want to
		 * prevent a change of orientation, which would cause the activity to
		 * pause.
		 */
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.main);
        // Compass stuff
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorAccelerometer = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagneticField = sensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        valuesAccelerometer = new float[3];
        valuesMagneticField = new float[3];

        matrixR = new float[9];
        matrixI = new float[9];
        matrixValues = new float[3];
        mText = (TextView) findViewById(R.id.text);
        scroller = (ScrollView) findViewById(R.id.scroller);
        log(getString(R.string.wait_ioio));
    }

    @Override
    public void onPause() {
        if (kalina != null) {
            log(getString(R.string.pausing));
        }
        sensorManager.unregisterListener(this, sensorAccelerometer);
        sensorManager.unregisterListener(this, sensorMagneticField);
        super.onPause();
    }

    @Override
    protected void onResume() {
        sensorManager.registerListener(this, sensorAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorMagneticField,
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
        }
    }

    public void onInit(int arg0) {
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                System.arraycopy(event.values, 0, valuesAccelerometer, 0, 3);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                System.arraycopy(event.values, 0, valuesMagneticField, 0, 3);
                break;
        }

        boolean success = SensorManager.getRotationMatrix(matrixR, matrixI,
                valuesAccelerometer, valuesMagneticField);

        if (success) {
            SensorManager.getOrientation(matrixR, matrixValues);
            synchronized (this) {
                azimuth = Math.toDegrees(matrixValues[0]);
                pitch = Math.toDegrees(matrixValues[1]);
                roll = Math.toDegrees(matrixValues[2]);
            }
        }
    }

    /**
     * Gets the azimuth
     *
     * @return the azimuth
     */
    public synchronized double getAzimuth() {
        return azimuth;
    }

    /**
     * Gets the pitch
     * @return the pitch
     */
    public synchronized double getPitch() {
        return pitch;
    }

    /**
     * Gets the roll
     * @return the roll
     */
    public synchronized double getRoll() {
        return roll;
    }

    @Override
    public IOIOLooper createIOIOLooper() {
        return new IOIOLooper() {

            public void setup(IOIO ioio) throws ConnectionLostException,
                    InterruptedException {
                // When the setup() method is called the IOIO is connected.
                log(getString(R.string.ioio_connected));

                // Establish communication between the android and the iRobot
                // through the IOIO board.
                log(getString(R.string.wait_irobot));
                IRobotInterface basicRobot = new SimpleIRobot(ioio);
                log(getString(R.string.irobot_connected));

                // Get a Pilot and let it go...
                // The ioio instance is passed to the constructor in case it is
                // needed for establishing connections to other peripherals, such as
                // sensors that are not part of the iRobot.
                kalina = new Pilot(basicRobot, Dashboard.this, ioio);
                kalina.initialize();
            }

            public void loop() throws ConnectionLostException, InterruptedException {
                // This thread hogs the CPU and prevents other threads from running (bad scheduler?)
                // The following sleep is needed to allow access to other threads.
                SystemClock.sleep(10L);
                kalina.loop();
            }

            public void disconnected() {
                log(getString(R.string.ioio_disconnected));
            }

            public void incompatible() {
            }
        };
    }

    /**
     * Writes a message to the Dashboard instance.
     *
     * @param msg the message to write
     */
    public void log(final String msg) {
        runOnUiThread(new Runnable() {

            public void run() {
                mText.append(msg);
                mText.append("\n");
                scroller.smoothScrollTo(0, mText.getBottom());
            }
        });
    }
}
