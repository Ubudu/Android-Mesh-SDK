package com.ubudu.ubudumeshsampleapp;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.ubudu.ubudumeshsampleapp.ubudu.UbuduManager;
import com.ubudu.ubudumeshsampleapp.ubudu.UbuduManagerListener;


public class MainActivity extends AppCompatActivity implements UbuduManagerListener {

    private static final String TAG = "meshapp.MainActivity";

    //UbuduManager
    private UbuduManager mUbuduManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init UbuduSDK
        initUbuduMeshSDK();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_start_mesh) {
            if (mUbuduManager.isMeshManagerStarted()) {
                stopUbuduMesh();
                item.setTitle("Start mesh");
            } else {
                startUbuduMesh();
                item.setTitle("Stop mesh");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initUbuduMeshSDK() {
        mUbuduManager = UbuduManager.getInstance(getApplicationContext(), this);
    }

    private void startUbuduMesh() {
        activateBluetooth();
        mUbuduManager.startMeshManager();
    }

    private void stopUbuduMesh() {
        mUbuduManager.stopMeshManager();
    }

    private boolean activateBluetooth() {
        BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
        // Check Availability of bluetooth
        if (bt == null) {
            return false;
        } else {
            if (!bt.isEnabled()) {
                Intent intentBtEnabled = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                int REQUEST_ENABLE_BT = 1;
                startActivityForResult(intentBtEnabled, REQUEST_ENABLE_BT);
            }
            return true;
        }
    }

    public void onSendButton(View view){
        EditText meshDeviceIdText = (EditText)this.findViewById(R.id.meshDeviceIdText);
        EditText meshMessageText = (EditText)this.findViewById(R.id.meshMessageText);
        if(!meshMessageText.getText().toString().equals("")) {
            try {
                mUbuduManager.sendMeshMessage(meshMessageText.getText().toString(), Integer.parseInt(meshDeviceIdText.getText().toString()), "f80ece82-9749-a840-17b1-1500edabf6ff", true, true);
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "Device id cannot be empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void message(String formatControl, Object... arguments) {

    }
}
