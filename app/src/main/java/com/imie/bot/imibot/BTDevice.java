package com.imie.bot.imibot;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Blackwaxx on 18/12/2017.
 */

class BTDevice {
    private static final BTDevice instance = new BTDevice();
    private BluetoothDevice device;
    private BluetoothSocket mmSocket;
    final UUID uuid = UUID.fromString("815425a5-bfac-47bf-9321-c5ff980b5e11");

    static BTDevice getInstance() {
        return instance;
    }

    private BTDevice() {
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public UUID getUuid() {
        return uuid;
    }

    public BluetoothSocket getMmSocket() {
        return mmSocket;
    }

    public void setMmSocket(BluetoothSocket mmSocket) {
        this.mmSocket = mmSocket;
    }

    public void closeSocket(){
        try {
            this.mmSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
