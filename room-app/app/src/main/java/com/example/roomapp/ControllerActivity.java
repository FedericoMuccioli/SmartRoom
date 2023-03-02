package com.example.roomapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.slider.Slider;

import java.io.IOException;
import java.util.UUID;

public class ControllerActivity extends AppCompatActivity implements Handler {

    private static final UUID STANDARD_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String EXCEPTION_MSG = "Exception";
    private static final int DEFAULT_COLOR = 0xFF6200EE;
    public TextView deviceText;
    public Button lightButton;
    public Slider rollerBlindsSlider;
    private BluetoothDevice device;
    private BluetoothRoomChannel btRoomChannel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        device = getIntent().getParcelableExtra(DiscoveryActivity.CONNECTED_DEVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        createUI();
        try {
            btRoomChannel = new BluetoothRoomChannel(device, STANDARD_UUID, this);
            btRoomChannel.start();
        } catch (IOException e) {
            btRoomChannel.cancel();
            startActivity(new Intent(this, DiscoveryActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btRoomChannel.cancel();
    }

    @SuppressLint("MissingPermission")
    private void createUI() {
        deviceText = findViewById(R.id.device);
        deviceText.setText(device.getName());
        lightButton = findViewById(R.id.light);
        lightButton.setBackgroundColor(Color.GRAY);
        lightButton.setEnabled(false);
        rollerBlindsSlider = findViewById(R.id.rollerBlinds);
        rollerBlindsSlider.setDrawingCacheBackgroundColor(Color.GRAY);
        rollerBlindsSlider.setEnabled(false);
        lightButton.setOnClickListener((v) -> btRoomChannel.turnLight());
        rollerBlindsSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener(){

            @SuppressLint("RestrictedApi")
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                btRoomChannel.setRollerBlinds((int) slider.getValue());
            }
        });
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