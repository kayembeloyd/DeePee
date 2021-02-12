package com.loycompany.deepee.services;

import android.app.Service;
import android.content.Intent;
import android.net.VpnService;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.loycompany.deepee.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class MyVpnService extends VpnService implements Handler.Callback, Runnable {
    private static final String TAG = "MyVpnService";

    private Handler mHandler;
    private Thread mThread;

    private ParcelFileDescriptor mInterface;

    public MyVpnService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // The handler is only used to show messages
        if (mHandler == null){
            mHandler = new Handler(this);
        }

        // Stopping the previous thread
        if (mThread != null){
            mThread.interrupt();
        }

        // Start a new session
        mThread = new Thread(this, "MyVpnServiceMThread");
        mThread.start();

        return START_STICKY;
        // return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mThread != null){
            mThread.interrupt();
        }
        super.onDestroy();
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        Toast.makeText(this, msg.what, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public synchronized void run() {
        Log.i(TAG, "running vpn service");
        try {
            runVpnConnection();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try{
                mInterface.close();
            } catch (Exception e){
                // ignore
            }

            mInterface = null;

            mHandler.sendEmptyMessage(R.string.disconnected);
            Log.i(TAG, "Exiting");
        }
    }

    private void configure() throws Exception{
        if (mInterface != null){
            Log.i(TAG, "Using previous interface");
            return;
        }

        Builder builder = new Builder();
        builder.setSession(TAG);
        builder.addAddress("10.0.0.2", 32);

        try{
            mInterface.close();
        } catch (Exception e) {
            // ignore
        }

        mInterface = builder.establish();
    }

    private boolean runVpnConnection() throws Exception{

        configure();

        FileInputStream in = new FileInputStream(mInterface.getFileDescriptor());
        FileOutputStream out = new FileOutputStream(mInterface.getFileDescriptor());

        DatagramChannel tunnel = DatagramChannel.open();
        tunnel.configureBlocking(false);

        ByteBuffer packet = ByteBuffer.allocate(32767);
        tunnel.connect(new InetSocketAddress("127.0.0.1", 8087));

        protect(tunnel.socket());

        int timer = 0;

        do {
            // Assume that we did not make any progress in this iteration.
            boolean idle = true;

            // Read the outgoing packet from the input stream.
            int length = in.read(packet.array());

            if (length > 0) {
                Log.i(TAG, "************new packet");

                // Write the outgoing packet to the tunnel.
                packet.limit(length);
                tunnel.write(packet);
                packet.clear();

                // There might be more outgoing packets.
                idle = false;

                // If we were receiving, switch to sending.
                if (timer < 1) {
                    timer = 1;
                }
            }

            length = tunnel.read(packet);

            if (length > 0) {
                // Ignore control messages, which start with zero.

                if (((int) packet.get(0)) != 0) {
                    // Write the incoming packet to the output stream.
                    out.write(packet.array(), 0, length);
                }
                packet.clear();
                // There might be more incoming packets.
                idle = false;
                // If we were sending, switch to receiving.
                if (timer > 0) {
                    timer = 0;
                }
            }

            // If we are idle or waiting for the network, sleep for a
            // fraction of time to avoid busy looping.
            if (idle) {
                Thread.sleep(100);
                // Increase the timer. This is inaccurate but good enough,
                // since everything is operated in non-blocking mode.

                if (timer > 0) timer += 100;
                else timer += -100;
                // timer += if (timer > 0) 100 else -100

                // We are receiving for a long time but not sending.
                if (timer < -15000) {
                    // Send empty control messages.
                    packet.put((byte) 0).limit(1);

                    for (int i = 0; i <= 2; i++) {
                        packet.position(0);
                        tunnel.write(packet);
                    }

                    /*for (i in 0..2) {
                        packet.position(0)
                        tunnel.write(packet)
                    }*/

                    packet.clear();
                    // Switch to sending.
                    timer = 1;
                }

                // We are sending for a long time but not receiving.
                if (timer > 20000) {
                    throw new IllegalStateException("Timed out");
                }
            }

            Thread.sleep(50);
        } while (true);
    }

    /*
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
     */

}