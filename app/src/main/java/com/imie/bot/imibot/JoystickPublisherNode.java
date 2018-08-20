package com.imie.bot.imibot;

import android.util.Log;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;

import geometry_msgs.Twist;
import imibot.StickControl;
import io.github.controlwear.virtual.joystick.android.JoystickView;

public class JoystickPublisherNode extends AbstractNodeMain implements NodeMain {
    private static final String TAG = JoystickPublisherNode.class.getSimpleName();

    public JoystickPublisherNode() {
    }

    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        Log.d(TAG,"Start ..");
        Publisher<Twist> publisher = connectedNode.newPublisher(GraphName.of("cmd_vel/"), Twist._TYPE);
    }

    @Override
    public void onShutdown(Node node) {
        Log.d(TAG,"ShutDown node in progress");
    }

    @Override
    public void onShutdownComplete(Node node) {
        Log.d(TAG,"ShutDown node complete");
    }

    @Override
    public void onError(Node node, Throwable throwable) {
        Log.d(TAG,"An Error occured with JoystickPublisherNode");
    }
}
