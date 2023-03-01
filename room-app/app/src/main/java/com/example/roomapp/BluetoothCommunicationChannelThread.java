package com.example.roomapp;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Consumer;

public class BluetoothCommunicationChannelThread extends Thread {

    private final BluetoothSocket socket;
    private OutputStream outputStream;
    private Enable handler;


    @SuppressLint("MissingPermission")
    public BluetoothCommunicationChannelThread(final BluetoothDevice device, final UUID uuid, final Enable handler) throws IOException {
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
            throw new RuntimeException(e);
        }
    }

    public void cancel() throws IOException {
        socket.close();
    }

    public void turnLight() {
        new Thread(() -> {
            try {
                String message = "l2\n";
                outputStream.write(message.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void setRollerBlinds(final int percentage) {
        new Thread(() -> {
            try {
                String message = "d" + String.valueOf(percentage) + "\n";
                outputStream.write(message.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
