package com.imie.bot.imibot;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ros.address.InetAddressFactory;
import org.ros.android.NodeMainExecutorService;
import org.ros.android.view.VirtualJoystickView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.stream.IntStream;

import io.github.controlwear.virtual.joystick.android.JoystickView;
import sensor_msgs.Joy;

public class JoystickActivity extends Activity {
    private TextView mTextViewAngle;
    private TextView mTextViewStrength;
    private Button quitButton;
    String masterUri;
    private VirtualJoystickView virtualJoystickView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);
        Intent i = getIntent();
        masterUri = i.getStringExtra(FirstConnectionActivity.ROS_MASTER_URI);
        mTextViewAngle = findViewById(R.id.textView_angle);
        mTextViewStrength = findViewById(R.id.textView_strength);
        quitButton = findViewById(R.id.button_quit);

        virtualJoystickView = (VirtualJoystickView) findViewById(R.id.virtual_joystick);
        

        // Set up ros master & JoysStick node
        NodeMain nodePublisher = new JoystickPublisherNode(JoystickActivity.this);
        NodeMainExecutorService nodeMainExe = new NodeMainExecutorService();
        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress(), URI.create(masterUri));
        nodeConfiguration.setNodeName(nodePublisher.getDefaultNodeName());
        nodeMainExe.execute(virtualJoystickView, nodeConfiguration);

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(JoystickActivity.this, "Connexion perdue !",  Toast.LENGTH_LONG).show();
                nodeMainExe.shutdownNodeMain(nodePublisher);
                Intent intent = new Intent(JoystickActivity.this, FirstConnectionActivity.class);
                startActivity(intent);
            }
        });
    }
}
