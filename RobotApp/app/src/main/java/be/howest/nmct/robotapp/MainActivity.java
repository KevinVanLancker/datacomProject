package be.howest.nmct.robotapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    RadioButton rbV1, rbV2;
    Button btnUp, btnDown, btnLeft, btnRight;
    Boolean bedien = false;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket btSocket = null;
    private BluetoothDevice mmDevice;
    private OutputStream outStream = null;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float last_x, last_z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDown = (Button) findViewById(R.id.btnDown);
        btnUp = (Button) findViewById(R.id.btnUp);
        btnRight = (Button) findViewById(R.id.btnRight);
        btnLeft = (Button) findViewById(R.id.btnLeft);

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Forward();
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Left();
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Backward();
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Right();
            }
        });


        rbV1 = (RadioButton) findViewById(R.id.rbVersie1);
        rbV2 = (RadioButton) findViewById(R.id.rbVersie2);

        rbV1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EnableRbV1();
            }
        });

        rbV2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnableRbV2();
            }
        });


        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        CheckBt();
        findArBt();
        connect();


    }

    private void EnableRbV1() {
        if(rbV1.isChecked()){
            if(rbV2.isChecked()){
                rbV2.setChecked(false);
            }
            DisableButtons();
            bedien = false;
            AutonoomRijden();
        }
    }

    private void EnableRbV2() {
        if(rbV2.isChecked()){
            if(rbV1.isChecked()){
                rbV1.setChecked(false);
            }
            EnableButtons();
            bedien = true;
            BedienRobot();
        }
    }

    private void DisableButtons(){
        btnRight.setEnabled(false);
        btnLeft.setEnabled(false);
        btnDown.setEnabled(false);
        btnUp.setEnabled(false);
    }

    private void EnableButtons(){
        btnRight.setEnabled(true);
        btnLeft.setEnabled(true);
        btnDown.setEnabled(true);
        btnUp.setEnabled(true);
    }

    private void AutonoomRijden(){
        //laat robot autonoom rijden
        Log.d("autonoom","autonoom");
        writeData("auto");
    }

    private void BedienRobot(){
        //laat robot bedienen
        //zorg dat robot stilstaat als er geen input is
        Log.d("bedien", "bedien");
        writeData("bedien");


    }

    private void Forward(){
        //ga vooruit
        if(Collision())
            return;
        Log.d("vooruit", "vooruit");
        writeData("vooruit");
    }

    private void Backward(){
        //ga achteruit
        Log.d("","achteruit");
        writeData("achteruit");
    }

    private void Left(){
        //ga links
        Log.d("","links");
        writeData("links");
    }

    private void Right(){
        //ga rechts
        Log.d("","rechts");
        writeData("rechts");
    }

    private boolean Collision(){
        //collision detectie

        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(bedien){
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
           // float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                lastUpdate = curTime;
                // if(Math.round(z)/1000>8.0000)
                if (Math.round(z) > 8) {
                    //achteruit me dunkt
                        Backward();
                } else if (Math.round(z) < -8) {
                    //vooruit me dunkt
                        Forward();
                } else if (Math.round(x) > 8) {
                    //rechts me dunkt
                        Right();
                } else if (Math.round(x) < -8) {
                    //links me dunkt
                        Left();
                }
                last_x = x;
               // last_y = y;
                last_z = z;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void writeData(String data) {
        try {
            outStream.write(data.getBytes());
        } catch (IOException e) {
            Log.d("error", "Bug while sending stuff", e);
        }
    }


    private void CheckBt() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }
    }

    public void findArBt() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for (BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("HC-06")) //connecteer met het juiste device
                {
                    mmDevice = device;
                    break;
                }
            }
        }
    }

    public void connect(){
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        try{
            btSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            btSocket.connect();
            outStream = btSocket.getOutputStream();
        }
        catch(IOException e){
            Log.d("con", "foutje bij connectie");
        }

    }



        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            btSocket.close();
        }
        catch (IOException e) {
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
        try {
            btSocket.close();
        }
        catch (IOException e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        try {
            btSocket.close();
        }
        catch (IOException e) {
        }
    }
}
