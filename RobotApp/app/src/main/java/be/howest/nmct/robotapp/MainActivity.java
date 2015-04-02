package be.howest.nmct.robotapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import java.io.Console;


public class MainActivity extends ActionBarActivity {

    RadioButton rbV1, rbV2;
    Button btnUp, btnDown, btnLeft, btnRight;
    boolean isStartUp = true;

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



    }

    private void EnableRbV1() {
        if(rbV1.isChecked()){
            if(rbV2.isChecked()){
                rbV2.setChecked(false);
            }
            DisableButtons();
            AutonoomRijden();
        }
    }

    private void EnableRbV2() {
        if(rbV2.isChecked()){
            if(rbV1.isChecked()){
                rbV1.setChecked(false);
            }
                EnableButtons();


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
    }

    private void BedienRobot(){
        //laat robot bedienen
        //zorg dat robot stilstaat als er geen input is
        Log.d("bedien", "bedien");


    }

    private void Forward(){
        //ga vooruit
        Log.d("vooruit", "vooruit");
    }

    private void Backward(){
        //ga achteruit
        Log.d("","achteruit");
    }

    private void Left(){
        //ga links
        Log.d("","links");
    }

    private void Right(){
        //ga rechts
        Log.d("","rechts");
    }

    private boolean Collision(){
        //collision detectie

        return true;
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
}
