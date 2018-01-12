package com.imie.bot.imibot;

import android.app.Application;
import android.bluetooth.BluetoothDevice;

/**
 * Created by Blackwaxx on 12/01/2018.
 */

public class IMIBot extends Application {
    private String BTAddress;
    private String BTname;
    private BluetoothDevice device;

    public String getBTAddress() {
        return BTAddress;
    }

    public void setBTAddress(String BTAddress) {
        this.BTAddress = BTAddress;
    }

    public String getBTname() {
        return BTname;
    }

    public void setBTname(String BTname) {
        this.BTname = BTname;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }
}
