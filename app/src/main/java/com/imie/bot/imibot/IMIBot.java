package com.imie.bot.imibot;

import android.app.Application;
import android.bluetooth.BluetoothDevice;

/**
 * Created by Blackwaxx on 12/01/2018.
 */

public class IMIBot extends Application {
    private String ip;
    private String port;

    public IMIBot() {
        this.ip = "localhost";
        this.port = "11311";
    }

    public IMIBot(String ip) {
        this.ip = ip;
        this.port = "11311";
    }

    public IMIBot(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
    public String getStringAddress(){
        return "http://" + this.ip + ":" + port;
    }
}
