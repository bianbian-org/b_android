package com.techjumper.polyhomeb.manager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/10/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ShakeManager {

    private Context mContext;
    private static ShakeManager sShakeManager;
    private MySensor mySensor;
    private long mLastTime;
    private float lastX;
    private float lastY;
    private float lastZ;

    private ShakeManager(Context mContext) {
        this.mContext = mContext;
    }

    public static ShakeManager with(Context ctx) {
        if (sShakeManager == null || sShakeManager.mContext == null) {
            sShakeManager = new ShakeManager(ctx);
        }
        return sShakeManager;
    }

    public void startShake(final ISensor iSensor) {
        SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensorList.size() == 0) return;
        Sensor sensor = sensorList.get(0);
        mySensor = new MySensor(iSensor);
        sensorManager.registerListener(mySensor, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public interface ISensor {
        void onSensorChange(float force);
    }

    private class MySensor implements SensorEventListener {
        private ISensor iSensor;

        public MySensor(ISensor iSensor) {
            this.iSensor = iSensor;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (mLastTime == 0l) {
                mLastTime = event.timestamp;
                lastX = event.values[0];
                lastY = event.values[1];
                lastZ = event.values[2];
            } else {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float force = Math.abs(x + y + z - lastZ - lastY - lastX);
                if (iSensor != null) {
                    iSensor.onSensorChange(force);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    }

    public void cancel() {
        SensorManager sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(mySensor);
    }
}
