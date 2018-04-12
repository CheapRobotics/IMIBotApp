package com.imie.bot.imibot;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.ros.address.InetAddressFactory;
import org.ros.android.NodeMainExecutorService;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;

import java.net.URI;

public class FirstConnectionActivity extends Activity {
    private Spinner devicesSpinner;
    private Button startButton;
    private TextView messageTextView;
    private EditText rosMasterUri;
    private DeviceAdapter adapter_devices;

    final byte delimiter = 33;
    int readBufferPosition = 0;

    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String ROS_MASTER_URI = "ROS_MASTER_URI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_connection);
        messageTextView = findViewById(R.id.messages_text);
        messageTextView.setMovementMethod(new ScrollingMovementMethod());
        devicesSpinner = findViewById(R.id.devices_spinner);
        startButton = findViewById(R.id.start_button);
        rosMasterUri = findViewById(R.id.rosMasterUri);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String address = settings.getString("BTaddress", "");


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstConnectionActivity.this, JoystickActivity.class);
                intent.putExtra(ROS_MASTER_URI, rosMasterUri.getText().toString());
                startActivity(intent);
            }
        });

    }
}
