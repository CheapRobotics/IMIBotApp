package com.imie.bot.imibot;

import android.util.Log;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.Node;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;

import imibot.StickControl;
import io.github.controlwear.virtual.joystick.android.JoystickView;

public class JoystickPublisherNode extends AbstractNodeMain implements NodeMain {
    private static final String TAG = JoystickPublisherNode.class.getSimpleName();
    private MoveListener moveListener;
    private final JoystickView joystick;

    public JoystickPublisherNode(JoystickView joystick) {
        this.joystick = joystick;
    }


    private final class MoveListener implements JoystickView.OnMoveListener {
        private final Publisher<imibot.StickControl> publisher;

        private MoveListener(Publisher<StickControl> publisher) {
            this.publisher = publisher;
        }

        @Override
        public void onMove(int angle, int strength) {
            Log.d(TAG, " Move => angle : "+angle+" strength : "+strength);
            imibot.StickControl str = publisher.newMessage();
            str.setAngle(angle);
            str.setStrength(strength);
            publisher.publish(str);
        }
    }


        @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("imibot/move_android_sender");
    }

    @Override
    public void onStart(ConnectedNode connectedNode) {
        Log.d(TAG,"Start ..");
        Publisher<imibot.StickControl> publisher = connectedNode.newPublisher(GraphName.of("imibot/stick_control"), StickControl._TYPE);
        moveListener = new MoveListener(publisher);
        joystick.setOnMoveListener(moveListener);

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
