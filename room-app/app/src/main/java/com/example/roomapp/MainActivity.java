package com.example.roomapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String BLUETOOTH_NO_SUPPORT_MSG = "Bluetooth not supported!";
    private static final String BLUETOOTH_CONNECT_MSG = "Connect to SmartRoom!";

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice device;
    private TextView deviceLabel;
    private TextView deviceText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = getSystemService(BluetoothManager.class).getAdapter();
        //Bluetooth non supportato
        if (bluetoothAdapter == null) {
            Toast.makeText(this, BLUETOOTH_NO_SUPPORT_MSG, Toast.LENGTH_SHORT).show();
            finish();
        }
        bluetoothConnectRequest();

        deviceLabel = findViewById(R.id.deviceLabel);
        deviceText = findViewById(R.id.device);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        //Setto UI
        deviceLabel.setText("Device connected:");
        device = getConnectedBluetooth();
        if (device != null) {
            deviceText.setText(device.getName());
        } else {
            deviceText.setText("null");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void bluetoothConnectRequest(){
        if (!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, BLUETOOTH_CONNECT_MSG, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private BluetoothDevice getConnectedBluetooth() {
        if (bluetoothAdapter.isEnabled()) {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device : pairedDevices) {
                if (bluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP) == BluetoothAdapter.STATE_CONNECTED) {
                    return device;
                }
            }
        }
        return null;
    }

}