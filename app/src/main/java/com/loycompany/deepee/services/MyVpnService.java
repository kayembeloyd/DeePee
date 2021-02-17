package com.loycompany.deepee.services;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.VpnService;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.loycompany.deepee.classes.CustomApp;
import com.loycompany.deepee.classes.DataPlan;
import com.loycompany.deepee.database.DeePeeDatabase;

import net.sourceforge.jpcap.net.TCPPacket;
import net.sourceforge.jpcap.net.UDPPacket;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.List;

public class MyVpnService extends VpnService implements Handler.Callback, Runnable {
    private static final String TAG = "MyVpnService";

    private Handler mHandler;
    private Thread mThread;
    private ParcelFileDescriptor mInterface;

    //a. Configure a builder for the interface.
    Builder builder = new Builder();

    List<CustomApp> customApps = null;

    public MyVpnService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DeePeeDatabase deePeeDatabase = new DeePeeDatabase(this);
        try { deePeeDatabase.updateDataBase(); } catch (IOException mIOException) { throw new Error("UnableToUpdateDatabase"); }
        SQLiteDatabase mDb = deePeeDatabase.getWritableDatabase();

        // Get id of DataPlan.custom apps
        DataPlan dp = deePeeDatabase.activeDataPlan();
        if (dp == null){
            // basitu
        } else {
            customApps = deePeeDatabase.getCustomApps(dp.id);
        }


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
        // Running here
        try {
            if (mInterface != null){
                // Using prev
            } else {
                try{
                    mInterface.close();
                } catch (Exception e){
                    // ignore
                }

                if (customApps != null){
                    for (int i = 0; i < customApps.size(); i++){
                        // Addition of disallowed enables internet connection
                        if (customApps.get(i).isEnabled){
                            builder.addDisallowedApplication(customApps.get(i).mPackageName);
                        }

                        Log.i(TAG, "I-Allowed application = " + customApps.get(i).mPackageName);
                    }
                }

                //a. Configure the TUN and get the interface.
                mInterface = builder.setSession("MyVPNService")
                        .addAddress("192.168.0.1", 24)
                        .addDnsServer("8.8.8.8")
                        .addRoute("0.0.0.0", 0).establish();
            }

            //b. Packets to be sent are queued in this input stream.
            FileInputStream in = new FileInputStream(
                    mInterface.getFileDescriptor());

            //b. Packets received need to be written to this output stream.
            FileOutputStream out = new FileOutputStream(
                    mInterface.getFileDescriptor());

            //c. The UDP channel can be used to pass/get ip package to/from server
            DatagramChannel tunnel = DatagramChannel.open();

            // Connect to the server, localhost is used for demonstration only.
            // InetAddress.getLocalHost() could be used instead of 127.0.0.1
            tunnel.connect(new InetSocketAddress("127.0.0.1", 8087));
            Log.i("Localhost: ", InetAddress.getLocalHost().toString());

            //d. Protect this socket, so package send by it will not be feedback to the vpn service.
            protect(tunnel.socket());

            // Allocate the buffer for a single packet
            ByteBuffer packet = ByteBuffer.allocate(32767);
            // We use a timer to determine the status of the tunnel. It
            // works on both sides. A positive value means sending, and
            // any other means receiving. We start with receiving.
            int timer = 0;
            // int myTimer = 0;
            Thread.sleep(1000);
            // Keep forwarding till something goes wrong

            while(true) {
                // Assume that we did not make any progress in this iteration.
                boolean idle = true;

                // Read the outgoing packet from the input stream.
                int length = in.read(packet.array());

                if (length > 0) {
                    // Write the outgoing packet to the tunnel.
                    packet.limit(length);
                    //DatagramPacket datagramPacket = new DatagramPacket(packet.array(),packet.array().length);
                    DatagramPacket datagramPacket = new DatagramPacket(packet.array(),packet.array().length);
                    byte[] dataInThePacket = datagramPacket.getData();
                    // NOTE // System.out.println(DatagramSocketService.stringFromPacket(datagramPacket));

                    TCPPacket tcpPacket = new TCPPacket(0, packet.array());
                    int sourcePort = tcpPacket.getSourcePort();
                    String destAddress = tcpPacket.getDestinationAddress();
                    int destPort = tcpPacket.getDestinationPort();

                    //DatagramSocket datagramSocket = new DatagramSocket(sourcePort, InetAddress.getLocalHost());
                    //datagramPacket.setData(new String("Hello").getBytes());

                            /* Extract destionation address and port from the packet and set destination of the packet
                            datagramPacket.setAddress(InetAddress.getByAddress(tcpPacket.getDestinationAddressBytes()));
                            datagramPacket.setPort(destPort);
                            */

                    /* Use localhost and port 8087 as the destination which was the endpoint of the tunnel earlier.
                     */
                    datagramPacket.setSocketAddress(new InetSocketAddress("127.0.0.1", 8087));

                    UDPPacket udpPacket = new UDPPacket(0, packet.array());

                    // Proxy proxy = new Proxy(Proxy.Type.SOCKS,datagramSocket.getLocalSocketAddress());
                    //dataInThePacket= udpPacket.getData();
                    //datagramPacket.setData(dataInThePacket);

                    Log.i(TAG, "I-TCP Source port: " + tcpPacket.getSourcePort());
                    // System.out.println("TCP Source port: " + tcpPacket.getSourcePort());

                    Log.i(TAG, "I-TCP Destination port: " + tcpPacket.getDestinationPort());
                    // System.out.println("TCP Destination port: " + tcpPacket.getDestinationPort());

                    Log.i(TAG, "I-TCP Destination address: " + tcpPacket.getDestinationAddressAsLong());
                    // System.out.println("TCP Destination address: " + tcpPacket.getDestinationAddressAsLong());

                    Log.i(TAG, "I-Length of data: " + tcpPacket.getData().length);
                    // System.out.println("Length of data: " + tcpPacket.getData().length);

                    Log.i(TAG, "I-About to connect to Destination");
                    // System.out.println("About to connect to Destination");

                    Log.i(TAG, "I-UDPPacket destination address: " + udpPacket.getDestinationAddressAsLong());
                    // System.out.println("UDPPacket destination address: " + udpPacket.getDestinationAddressAsLong());

                    Log.i(TAG, "I-UDPPacket destination port: " + udpPacket.getDestinationPort());
                    // System.out.println("UDPPacket destination port: " + udpPacket.getDestinationPort());

                    Log.i(TAG, "I-IP Protocol: " + tcpPacket.getIPProtocol());
                    // System.out.println("IP Protocol: " + tcpPacket.getIPProtocol());

                    Log.i(TAG, "I-Length of data: " + udpPacket.getData().length);
                    // System.out.println("Length of data: " + udpPacket.getData().length);

                    // Failing to send!
                    //datagramSocket.send(datagramPacket);

                    //System.out.println(new String(packet.array(), 0, packet.array().length));
                    tunnel.write(packet);
                    packet.clear();
                    // There might be more outgoing packets.
                    idle = false;

                    // If we were receiving, switch to sending.
                    if (timer < 1) {
                        timer = 1;
                    }
                }

                // Read the incoming packet from the tunnel.
                length = tunnel.read(packet);
                if (length > 0) {
                    // Ignore control messages, which start with zero.
                    if (packet.get(0) != 0) {
                        // Write the incoming packet to the output stream.
                        out.write(packet.array(), 0, length);
                    } else {
                        Log.i(TAG, "I-Control message received");
                    }
                    packet.clear();
                    // There might be more incoming packets.
                    idle = false;
                    // If we were sending, switch to receiving.
                    if (timer > 0) {
                        timer = 0;
                    }
                }

                //put packet to tunnel
                //get packet from tunnel
                //return packet with out
                //sleep is a must
                Thread.sleep(100);
                // myTimer += 100;

                /*if (myTimer > 10000){
                    packet.clear();

                    // Send a control message
                    packet.put((byte) 0).limit(1);

                    packet.position(0);
                    tunnel.write(packet);

                    packet.clear();

                    // Reset the timer
                    myTimer = 0;
                } */
            }

        } catch (Exception e){
            // ignore
        }
    }

    /*
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
     */

}