package com.example.roomapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;

import java.io.IOException;
import java.util.UUID;

public class RoomActivity extends AppCompatActivity implements Enable {

    private static final UUID STANDARD_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String EXCEPTION_MSG = "Exception";
    private static final int DEFAULT_COLOR = 0xFF6200EE;
    private BluetoothDevice device;
    private BluetoothCommunicationChannelThread btCommunicationChannel;
    public TextView deviceText;
    public Button lightButton;
    public Slider rollerBlindsSlider;


    @SuppressLint("MissingPermission")//cancellare
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        device = getIntent().getParcelableExtra(MainActivity.CONNECTED_DEVICE);

        deviceText = findViewById(R.id.device);
        deviceText.setText(device.getName());
        lightButton = findViewById(R.id.light);
        lightButton.getDrawingCacheBackgroundColor();
        lightButton.setBackgroundColor(Color.GRAY);
        lightButton.setEnabled(false);
        rollerBlindsSlider = findViewById(R.id.rollerBlinds);
        rollerBlindsSlider.setDrawingCacheBackgroundColor(Color.GRAY);
        rollerBlindsSlider.setEnabled(false);
        lightButton.setOnClickListener((v) -> btCommunicationChannel.turnLight());
        rollerBlindsSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener(){

            @SuppressLint("RestrictedApi")
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                btCommunicationChannel.setRollerBlinds((int) slider.getValue());
            }
        });


        try {
            getSystemService(BluetoothManager.class).getAdapter().cancelDiscovery();
            btCommunicationChannel = new BluetoothCommunicationChannelThread(device, STANDARD_UUID, this);
            btCommunicationChannel.start();
        } catch (Exception e) {
            Toast.makeText(this, EXCEPTION_MSG, Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(this, MainActivity.class);
            //startActivity(intent);
        }
    }

    @Override
    public void enable() {
        runOnUiThread(() -> {
            lightButton.setEnabled(true);
            lightButton.setBackgroundColor(DEFAULT_COLOR);
            rollerBlindsSlider.setEnabled(true);
        });
    }
}