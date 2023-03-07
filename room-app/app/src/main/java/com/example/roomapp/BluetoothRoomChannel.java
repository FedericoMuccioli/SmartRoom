package com.example.roomapp;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class BluetoothRoomChannel extends Thread {

    private static final String LOG_TAG = "BluetoothRoomChannel";

    private final BluetoothSocket socket;
    private OutputStream outputStream;
    private Handler handler;


    @SuppressLint("MissingPermission")
    public BluetoothRoomChannel(final BluetoothDevice device, final UUID uuid, final Handler handler) throws IOException {
        socket = device.createRfcommSocketToServiceRecord(uuid);
        this.handler = handler;
    }

    @SuppressLint("MissingPermission")
    public void run() {
        try {
            socket.connect();
            outputStream = socket.getOutputStream();
            handler.enable();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting socket", e);
            throw new RuntimeException(e);
        }
    }

    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing socket", e);
        }
    }

    public void turnLight() {
        new Thread(() -> {
            try {
                String message = "l2\n";
                outputStream.write(message.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error turning on the light", e);
            }
        }).start();
    }

    public void setRollerBlinds(final int percentage) {
        new Thread(() -> {
            try {
                String message = "r" + String.valueOf(percentage) + "\n";
                outputStream.write(message.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error setting the roller blinds", e);
            }
        }).start();
    }
}
