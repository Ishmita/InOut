package com.clarifai.android.starter.api.v2.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

import com.clarifai.android.starter.api.v2.activity.ShakeEventListener;


/**
 * Created by ishmitaloona on 07/10/2017.
 */

public class OnBootReceiver extends BroadcastReceiver {

    private SensorManager mSensorManager;

    private ShakeEventListener mSensorListener;

    @Override
    public void onReceive(Context context, Intent intent){

        Log.d("Smartphone Receiver", "onReceive...............");
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                Log.d("Receiver", "shake!!");
            }
        });

        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        //sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //sManager.registerListener(new ShakeEventListener(), sensor, SensorManager.SENSOR_DELAY_NORMAL); // or other delay
    }
}
