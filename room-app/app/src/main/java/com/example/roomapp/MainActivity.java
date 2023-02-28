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
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String BLUETOOTH_NO_SUPPORT_MSG = "Bluetooth not supported!";
    private static final String BLUETOOTH_ENABLE_MSG = "Enable bluetooth to use app!";
    private static final String BLUETOOTH_PERMISSION_MSG = "Allow permission to use app!";
    private static final String UNKNOWN_RESULT_MSG = "Unknown result";
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_ENABLE_BT_1 = 2;
    private static final int REQUEST_ENABLE_BT_2= 3;
    private static final int REQUEST_SCAN_BT = 4;

    private ListView pairedListView;
    private ListView unknownListView;
    private Button discoveryButton;
    private BluetoothAdapter bluetoothAdapter;
    private List<BluetoothDevice> pairedList;
    private List<BluetoothDevice> unknownList;
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
        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        pairedList = new ArrayList<>();
        pairedListName = new ArrayList<>();
        pairedListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pairedListName);
        pairedListView.setAdapter(pairedListAdapter);
        unknownList = new ArrayList<>();
        unknownListName = new ArrayList<>();
        unknownListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, unknownListName);
        unknownListView.setAdapter(unknownListAdapter);
        unknownListView.setOnItemClickListener((adapterView, view, i, l) -> {
            onDeviceClicked(unknownList.get(i));
        });

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

    private void enableBluetooth() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S){
            if (checkSelfPermission(Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ENABLE_BT_1);
            } else {
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
            }
        } else {
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_ENABLE_BT_2);
            } else {
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
            }
        }
    }


    private void scanBluetooth() {
        String permission = Build.VERSION.SDK_INT < Build.VERSION_CODES.S ? Manifest.permission.BLUETOOTH_ADMIN : Manifest.permission.BLUETOOTH_SCAN;
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{permission}, REQUEST_SCAN_BT);
            return;
        }
        enableBluetooth();
        unknownList.clear();
        unknownListName.clear();
        bluetoothAdapter.startDiscovery();
        pairedList.clear();
        pairedList.addAll(bluetoothAdapter.getBondedDevices());
        pairedListName.clear();
        for (BluetoothDevice device : pairedList) {
            pairedListName.add(device.getName());
        }
        pairedListAdapter.notifyDataSetChanged();
    }

    @SuppressLint("MissingPermission")
    private void foundUnknownDevice(BluetoothDevice device){
        unknownList.add(device);
        unknownListName.add(device.getName());
        unknownListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ENABLE_BT_1:
                if(grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    enableBluetooth();
                } else {
                    Toast.makeText(this, BLUETOOTH_PERMISSION_MSG, Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_ENABLE_BT_2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableBluetooth();
                } else {
                    Toast.makeText(this, BLUETOOTH_PERMISSION_MSG, Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_SCAN_BT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    scanBluetooth();
                } else {
                    Toast.makeText(this, BLUETOOTH_PERMISSION_MSG, Toast.LENGTH_SHORT).show();
                }
            default:
                Toast.makeText(this, UNKNOWN_RESULT_MSG, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, BLUETOOTH_ENABLE_MSG, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Toast.makeText(this, UNKNOWN_RESULT_MSG, Toast.LENGTH_SHORT).show();
        }
    }


}