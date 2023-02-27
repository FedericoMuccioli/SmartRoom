package com.example.roomapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String BLUETOOTH_NO_SUPPORT_MSG = "Bluetooth not supported!";
    private static final String BLUETOOTH_ENABLE_MSG = "Enable bluetooth to use app!";
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_SCAN_BT = 2;

    private ListView pairedListView;
    private ListView unknownListView;
    private Button discoveryButton;
    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedList;
    private Set<BluetoothDevice> unknownList;
    private List<String> pairedListName;
    private List<String> unknownListName;
    private ArrayAdapter<String> pairedListAdapter;
    private ArrayAdapter<String> unknownListAdapter;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                foundUnknownDevice(device);
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pairedListView = findViewById(R.id.pairedList);
        unknownListView = findViewById(R.id.unknownList);
        discoveryButton = findViewById(R.id.discoveryButton);
        discoveryButton.setOnClickListener((v) -> scanBluetooth());
        bluetoothAdapter = getSystemService(BluetoothManager.class).getAdapter();
        pairedListName = new ArrayList<>();
        pairedListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pairedListName);
        pairedListView.setAdapter(pairedListAdapter);
        unknownListName = new ArrayList<>();
        unknownListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, unknownListName);
        unknownListView.setAdapter(unknownListAdapter);

        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));


        checkSupportedBluetooth();
        scanBluetooth();


    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onStop() {
        super.onStop();
        bluetoothAdapter.cancelDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void checkSupportedBluetooth() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, BLUETOOTH_NO_SUPPORT_MSG, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @SuppressLint("MissingPermission")
    private void enableBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }


    private void scanBluetooth() {
        enableBluetooth();
        Toast.makeText(this, "INIZIO SCAN", Toast.LENGTH_SHORT).show();
        unknownList = new HashSet<>();
        unknownListName.clear();
        if (checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.BLUETOOTH_ADMIN}, REQUEST_SCAN_BT);
            Toast.makeText(this, "non ho i permessi", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "ho i permessi", Toast.LENGTH_SHORT).show();
        bluetoothAdapter.startDiscovery();

        pairedList = bluetoothAdapter.getBondedDevices();
        pairedListName.clear();
        for (BluetoothDevice device : pairedList) {
            pairedListName.add(device.getName());
        }
        pairedListAdapter.notifyDataSetChanged();

    }

    private void foundUnknownDevice(BluetoothDevice device){
        Toast.makeText(this, device.getAddress(), Toast.LENGTH_SHORT).show();
        unknownList.add(device);
        unknownListName.add(device.getAddress());
        unknownListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_SCAN_BT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permesso concesso.", Toast.LENGTH_SHORT).show();
                    scanBluetooth();
                } else {
                    Toast.makeText(this, "Permesso negato.", Toast.LENGTH_SHORT).show();
                }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {

                } else {
                    Toast.makeText(this, BLUETOOTH_ENABLE_MSG, Toast.LENGTH_SHORT).show();

                    //displayError("You need to enable bluetooth to use the app");
                }
                break;
            case REQUEST_SCAN_BT:
        }
    }


}