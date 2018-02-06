package com.imie.bot.imibot;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class FirstConnectionActivity extends Activity {
    private BTDevice btDevice;
    private Spinner devicesSpinner;
    private Button refreshDevicesButton;
    private Button startButton;
    private TextView messageTextView;
    private BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

    private DeviceAdapter adapter_devices;

    final byte delimiter = 33;
    int readBufferPosition = 0;

    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_connection);
        messageTextView = findViewById(R.id.messages_text);
        messageTextView.setMovementMethod(new ScrollingMovementMethod());
        devicesSpinner = findViewById(R.id.devices_spinner);
        refreshDevicesButton = findViewById(R.id.refresh_devices_button);
        startButton = findViewById(R.id.start_button);
        btDevice = BTDevice.getInstance();

        Intent intent = new Intent(this, WifiConfiguration.class);
        startActivity(intent);


        if (btDevice.getMmSocket() != null && btDevice.getMmSocket().isConnected()) {
            (new Thread(new workerThread(btDevice.getDevice()))).start();
        }

        // refreshDevices();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String address = settings.getString("BTaddress", "");

        if (!address.equals("")) {
            BluetoothDevice memorizedDevice = btAdapter.getRemoteDevice(address);
            (new Thread(new workerThread(memorizedDevice))).start();
        }

        refreshDevicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshDevices();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BluetoothDevice device = (BluetoothDevice) devicesSpinner.getSelectedItem();
                (new Thread(new workerThread(device))).start();
            }
        });
    }

    private void refreshDevices() {
        adapter_devices = new DeviceAdapter(this, R.layout.spinner_devices, new ArrayList<BluetoothDevice>());
        devicesSpinner.setAdapter(adapter_devices);

        if (!btAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                adapter_devices.add(device);
            }
        } else {
            btAdapter.startDiscovery();
        }
    }

    final class workerThread implements Runnable {

        public workerThread(BluetoothDevice device) {
            btDevice.setDevice(device);
            btAdapter.cancelDiscovery();
        }

        public void run() {
            clearOutput();
            writeOutput("Device: " + btDevice.getDevice().getName() + " - " + btDevice.getDevice().getAddress());

            try {
                btDevice.createSocket();
                if (!btDevice.getMmSocket().isConnected()) {
                    btDevice.getMmSocket().connect();
                    Thread.sleep(1000);
                }
                OutputStream mmOutputStream = btDevice.getMmSocket().getOutputStream();
                final InputStream mmInputStream = btDevice.getMmSocket().getInputStream();

                waitForResponse(mmInputStream, -1);

                String msg = "Android says yo !";
                mmOutputStream.write(msg.getBytes());
                mmOutputStream.flush();
                writeOutput("Connecté.");

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("BTaddress", btDevice.getDevice().getAddress());
                editor.apply();

                Intent intent = new Intent(FirstConnectionActivity.this, JoystickActivity.class);
                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
                writeOutput(e.getMessage());
                writeOutput("Failed.");
            }
        }
    }

    private void writeOutput(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String currentText = messageTextView.getText().toString();
                messageTextView.setText(currentText + "\n" + text);
            }
        });
    }

    private void clearOutput() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageTextView.setText("");
            }
        });
    }

    private void waitForResponse(InputStream mmInputStream, long timeout) throws IOException {
        int bytesAvailable;

        while (true) {
            bytesAvailable = mmInputStream.available();
            if (bytesAvailable > 0) {
                byte[] packetBytes = new byte[bytesAvailable];
                byte[] readBuffer = new byte[1024];
                mmInputStream.read(packetBytes);

                for (int i = 0; i < bytesAvailable; i++) {
                    byte b = packetBytes[i];

                    if (b == delimiter) {
                        byte[] encodedBytes = new byte[readBufferPosition];
                        System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                        final String data = new String(encodedBytes, "US-ASCII");
                        return;
                    } else {
                        readBuffer[readBufferPosition++] = b;
                    }
                }
            }
        }
    }
}
