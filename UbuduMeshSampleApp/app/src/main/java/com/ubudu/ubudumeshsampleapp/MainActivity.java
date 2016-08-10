package com.ubudu.ubudumeshsampleapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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

    private static final int ASK_GEOLOCATION_PERMISSION_REQUEST = 0;

    //UbuduManager
    private UbuduManager mUbuduManager;

    private Menu menu;

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
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ASK_GEOLOCATION_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onOptionsItemSelected(menu.findItem(R.id.action_start_mesh));
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_start_mesh) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ASK_GEOLOCATION_PERMISSION_REQUEST);
                return false;
            }

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
