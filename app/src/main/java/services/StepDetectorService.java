package services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import general.Day;
import stepdetector.StepDetector;
import stepdetector.StepListener;
import com.google.firebase.auth.FirebaseAuth;

import storage.SharedPrefData;


/**
 * Created by tanvir on 8/28/17.
 */

public class StepDetectorService extends Service implements SensorEventListener, StepListener {
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    FirebaseAuth mAuth;
    private static final String TAG = "StepDetectorService: ";
    Day day;

    SharedPreferences stepCountMapPref;
    SharedPreferences.Editor stepCountMapeditor ;

    SharedPreferences calorieMapPref;
    SharedPreferences.Editor calorieMapeditor ;

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        mAuth= FirebaseAuth.getInstance();
        day=new Day();
        stepCountMapPref = StepDetectorService.this.getSharedPreferences(SharedPrefData.STEPCOUNTMAP, MODE_PRIVATE);
        stepCountMapeditor =  stepCountMapPref.edit();
        calorieMapPref = StepDetectorService.this.getSharedPreferences(SharedPrefData.CALORIEMAP, MODE_PRIVATE);
        calorieMapeditor =  calorieMapPref.edit();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sensorManager.registerListener(StepDetectorService.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public synchronized void step(long timeNs) {
        if(mAuth.getCurrentUser()!=null){
            stepCountMapeditor.putInt(new Day().toString(),stepCountMapPref.getInt(new Day().toString(),0)+1);
            calorieMapeditor.putInt(new Day().toString(),calorieMapPref.getInt(new Day().toString(),0)+1);
            stepCountMapeditor.commit();
            calorieMapeditor.commit();
            Log.d(TAG, "detected");
        }
    }


}
