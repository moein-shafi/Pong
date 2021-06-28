/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.pong;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * This class does all the work for setting up and managing Bluetooth
 * connections with other devices. It has a thread that listens for
 * incoming connections, a thread for connecting with a device, and a
 * thread for performing data transmissions when connected.
 */
//public class BluetoothService {

//    // Debugging
//    private static final String TAG = "BluetoothService";
//
//    // Name for the SDP record when creating server socket
//    private static final String NAME_SECURE = "BluetoothChatSecure";
//    private static final String NAME_INSECURE = "BluetoothChatInsecure";
//
//    // Unique UUID for this application
//    private static final UUID MY_UUID_SECURE =
//            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
//    private static final UUID MY_UUID_INSECURE =
//            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
//
//    // Member fields
//    private final BluetoothAdapter mAdapter;
//    private final Handler mHandler;
//    private AcceptThread mSecureAcceptThread;
//    private AcceptThread mInsecureAcceptThread;
//    private ConnectThread mConnectThread;
//    private ConnectedThread mConnectedThread;
//    private int mState;
//    private int mNewState;
//
//    // Constants that indicate the current connection state
//    public static final int STATE_NONE = 0;       // we're doing nothing
//    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
//    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
//    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
//
//    /**
//     * Constructor. Prepares a new BluetoothChat session.
//     *
//     * @param context The UI Activity Context
//     * @param handler A Handler to send messages back to the UI Activity
//     */
//    public BluetoothService(Context context, Handler handler) {
//        mAdapter = BluetoothAdapter.getDefaultAdapter();
//        mState = STATE_NONE;
//        mNewState = mState;
//        mHandler = handler;
//    }
//
//    /**
//     * Update UI title according to the current state of the chat connection
//     */
//    private synchronized void updateUserInterfaceTitle() {
//        mState = getState();
//        Log.d(TAG, "updateUserInterfaceTitle() " + mNewState + " -> " + mState);
//        mNewState = mState;
//
//        // Give the new state to the Handler so the UI Activity can update
//        mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE, mNewState, -1).sendToTarget();
//    }
//
//    /**
//     * Return the current connection state.
//     */
//    public synchronized int getState() {
//        return mState;
//    }
//
//    /**
//     * Start the chat service. Specifically start AcceptThread to begin a
//     * session in listening (server) mode. Called by the Activity onResume()
//     */
//    public synchronized void start() {
//        Log.d(TAG, "start");
//
//        // Cancel any thread attempting to make a connection
//        if (mConnectThread != null) {
//            mConnectThread.cancel();
//            mConnectThread = null;
//        }
//
//        // Cancel any thread currently running a connection
//        if (mConnectedThread != null) {
//            mConnectedThread.cancel();
//            mConnectedThread = null;
//        }
//
//        // Start the thread to listen on a BluetoothServerSocket
//        if (mSecureAcceptThread == null) {
//            mSecureAcceptThread = new AcceptThread(true);
//            mSecureAcceptThread.start();
//        }
//        if (mInsecureAcceptThread == null) {
//            mInsecureAcceptThread = new AcceptThread(false);
//            mInsecureAcceptThread.start();
//        }
//        // Update UI title
//        updateUserInterfaceTitle();
//    }
//
//    /**
//     * Start the ConnectThread to initiate a connection to a remote device.
//     *
//     * @param device The BluetoothDevice to connect
//     * @param secure Socket Security type - Secure (true) , Insecure (false)
//     */
//    public synchronized void connect(BluetoothDevice device, boolean secure) {
//        Log.d(TAG, "connect to: " + device);
//
//        // Cancel any thread attempting to make a connection
//        if (mState == STATE_CONNECTING) {
//            if (mConnectThread != null) {
//                mConnectThread.cancel();
//                mConnectThread = null;
//            }
//        }
//
//        // Cancel any thread currently running a connection
//        if (mConnectedThread != null) {
//            mConnectedThread.cancel();
//            mConnectedThread = null;
//        }
//
//        // Start the thread to connect with the given device
//        mConnectThread = new ConnectThread(device, secure);
//        mConnectThread.start();
//        // Update UI title
//        updateUserInterfaceTitle();
//    }
//
//    /**
//     * Start the ConnectedThread to begin managing a Bluetooth connection
//     *
//     * @param socket The BluetoothSocket on which the connection was made
//     * @param device The BluetoothDevice that has been connected
//     */
//    public synchronized void connected(BluetoothSocket socket, BluetoothDevice
//            device, final String socketType) {
//        Log.d(TAG, "connected, Socket Type:" + socketType);
//
//        // Cancel the thread that completed the connection
//        if (mConnectThread != null) {
//            mConnectThread.cancel();
//            mConnectThread = null;
//        }
//
//        // Cancel any thread currently running a connection
//        if (mConnectedThread != null) {
//            mConnectedThread.cancel();
//            mConnectedThread = null;
//        }
//
//        // Cancel the accept thread because we only want to connect to one device
//        if (mSecureAcceptThread != null) {
//            mSecureAcceptThread.cancel();
//            mSecureAcceptThread = null;
//        }
//        if (mInsecureAcceptThread != null) {
//            mInsecureAcceptThread.cancel();
//            mInsecureAcceptThread = null;
//        }
//
//        // Start the thread to manage the connection and perform transmissions
//        mConnectedThread = new ConnectedThread(socket, socketType);
//        mConnectedThread.start();
//
//        // Send the name of the connected device back to the UI Activity
//        Message msg = mHandler.obtainMessage(Constants.MESSAGE_DEVICE_NAME);
//        Bundle bundle = new Bundle();
//        bundle.putString(Constants.DEVICE_NAME, device.getName());
//        msg.setData(bundle);
//        mHandler.sendMessage(msg);
//        // Update UI title
//        updateUserInterfaceTitle();
//    }
//
//    /**
//     * Stop all threads
//     */
//    public synchronized void stop() {
//        Log.d(TAG, "stop");
//
//        if (mConnectThread != null) {
//            mConnectThread.cancel();
//            mConnectThread = null;
//        }
//
//        if (mConnectedThread != null) {
//            mConnectedThread.cancel();
//            mConnectedThread = null;
//        }
//
//        if (mSecureAcceptThread != null) {
//            mSecureAcceptThread.cancel();
//            mSecureAcceptThread = null;
//        }
//
//        if (mInsecureAcceptThread != null) {
//            mInsecureAcceptThread.cancel();
//            mInsecureAcceptThread = null;
//        }
//        mState = STATE_NONE;
//        // Update UI title
//        updateUserInterfaceTitle();
//    }
//
//    /**
//     * Write to the ConnectedThread in an unsynchronized manner
//     *
//     * @param out The bytes to write
//     * @see ConnectedThread#write(byte[])
//     */
//    public void write(byte[] out) {
//        // Create temporary object
//        ConnectedThread r;
//        // Synchronize a copy of the ConnectedThread
//        synchronized (this) {
//            if (mState != STATE_CONNECTED) return;
//            r = mConnectedThread;
//        }
//        // Perform the write unsynchronized
//        r.write(out);
//    }
//
//    /**
//     * Indicate that the connection attempt failed and notify the UI Activity.
//     */
//    private void connectionFailed() {
//        // Send a failure message back to the Activity
//        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
//        Bundle bundle = new Bundle();
//        bundle.putString(Constants.TOAST, "Unable to connect device");
//        msg.setData(bundle);
//        mHandler.sendMessage(msg);
//
//        mState = STATE_NONE;
//        // Update UI title
//        updateUserInterfaceTitle();
//
//        // Start the service over to restart listening mode
//        BluetoothService.this.start();
//    }
//
//    /**
//     * Indicate that the connection was lost and notify the UI Activity.
//     */
//    private void connectionLost() {
//        // Send a failure message back to the Activity
//        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
//        Bundle bundle = new Bundle();
//        bundle.putString(Constants.TOAST, "Device connection was lost");
//        msg.setData(bundle);
//        mHandler.sendMessage(msg);
//
//        mState = STATE_NONE;
//        // Update UI title
//        updateUserInterfaceTitle();
//
//        // Start the service over to restart listening mode
//        BluetoothService.this.start();
//    }
//
//    /**
//     * This thread runs while listening for incoming connections. It behaves
//     * like a server-side client. It runs until a connection is accepted
//     * (or until cancelled).
//     */
//    private class AcceptThread extends Thread {
//        // The local server socket
//        private final BluetoothServerSocket mmServerSocket;
//        private String mSocketType;
//
//        public AcceptThread(boolean secure) {
//            BluetoothServerSocket tmp = null;
//            mSocketType = secure ? "Secure" : "Insecure";
//
//            // Create a new listening server socket
//            try {
//                if (secure) {
//                    tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE,
//                            MY_UUID_SECURE);
//                } else {
//                    tmp = mAdapter.listenUsingInsecureRfcommWithServiceRecord(
//                            NAME_INSECURE, MY_UUID_INSECURE);
//                }
//            } catch (IOException e) {
//                Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e);
//            }
//            mmServerSocket = tmp;
//            mState = STATE_LISTEN;
//        }
//
//        public void run() {
//            Log.d(TAG, "Socket Type: " + mSocketType +
//                    "BEGIN mAcceptThread" + this);
//            setName("AcceptThread" + mSocketType);
//
//            BluetoothSocket socket;
//
//            // Listen to the server socket if we're not connected
//            while (mState != STATE_CONNECTED) {
//                try {
//                    // This is a blocking call and will only return on a
//                    // successful connection or an exception
//                    socket = mmServerSocket.accept();
//                } catch (IOException e) {
//                    Log.e(TAG, "Socket Type: " + mSocketType + "accept() failed", e);
//                    break;
//                }
//
//                // If a connection was accepted
//                if (socket != null) {
//                    synchronized (BluetoothService.this) {
//                        switch (mState) {
//                            case STATE_LISTEN:
//                            case STATE_CONNECTING:
//                                // Situation normal. Start the connected thread.
//                                connected(socket, socket.getRemoteDevice(),
//                                        mSocketType);
//                                break;
//                            case STATE_NONE:
//                            case STATE_CONNECTED:
//                                // Either not ready or already connected. Terminate new socket.
//                                try {
//                                    socket.close();
//                                } catch (IOException e) {
//                                    Log.e(TAG, "Could not close unwanted socket", e);
//                                }
//                                break;
//                        }
//                    }
//                }
//            }
//            Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType);
//
//        }
//
//        public void cancel() {
//            Log.d(TAG, "Socket Type" + mSocketType + "cancel " + this);
//            try {
//                mmServerSocket.close();
//            } catch (IOException e) {
//                Log.e(TAG, "Socket Type" + mSocketType + "close() of server failed", e);
//            }
//        }
//    }
//
//
//    /**
//     * This thread runs while attempting to make an outgoing connection
//     * with a device. It runs straight through; the connection either
//     * succeeds or fails.
//     */
//    private class ConnectThread extends Thread {
//        private final BluetoothSocket mmSocket;
//        private final BluetoothDevice mmDevice;
//        private String mSocketType;
//
//        public ConnectThread(BluetoothDevice device, boolean secure) {
//            mmDevice = device;
//            BluetoothSocket tmp = null;
//            mSocketType = secure ? "Secure" : "Insecure";
//
//            // Get a BluetoothSocket for a connection with the
//            // given BluetoothDevice
//            try {
//                if (secure) {
//                    tmp = device.createRfcommSocketToServiceRecord(
//                            MY_UUID_SECURE);
//                } else {
//                    tmp = device.createInsecureRfcommSocketToServiceRecord(
//                            MY_UUID_INSECURE);
//                }
//            } catch (IOException e) {
//                Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e);
//            }
//            mmSocket = tmp;
//            mState = STATE_CONNECTING;
//        }
//
//        public void run() {
//            Log.i(TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
//            setName("ConnectThread" + mSocketType);
//
//            // Always cancel discovery because it will slow down a connection
//            mAdapter.cancelDiscovery();
//
//            // Make a connection to the BluetoothSocket
//            try {
//                // This is a blocking call and will only return on a
//                // successful connection or an exception
//                mmSocket.connect();
//            } catch (IOException e) {
//                // Close the socket
//                try {
//                    mmSocket.close();
//                } catch (IOException e2) {
//                    Log.e(TAG, "unable to close() " + mSocketType +
//                            " socket during connection failure", e2);
//                }
//                connectionFailed();
//                return;
//            }
//
//            // Reset the ConnectThread because we're done
//            synchronized (BluetoothService.this) {
//                mConnectThread = null;
//            }
//
//            // Start the connected thread
//            connected(mmSocket, mmDevice, mSocketType);
//        }
//
//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) {
//                Log.e(TAG, "close() of connect " + mSocketType + " socket failed", e);
//            }
//        }
//    }
//
//    /**
//     * This thread runs during a connection with a remote device.
//     * It handles all incoming and outgoing transmissions.
//     */
//    private class ConnectedThread extends Thread {
//        private final BluetoothSocket mmSocket;
//        private final InputStream mmInStream;
//        private final OutputStream mmOutStream;
//
//        public ConnectedThread(BluetoothSocket socket, String socketType) {
//            Log.d(TAG, "create ConnectedThread: " + socketType);
//            mmSocket = socket;
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//
//            // Get the BluetoothSocket input and output streams
//            try {
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
//            } catch (IOException e) {
//                Log.e(TAG, "temp sockets not created", e);
//            }
//
//            mmInStream = tmpIn;
//            mmOutStream = tmpOut;
//            mState = STATE_CONNECTED;
//        }
//
//        public void run() {
//            Log.i(TAG, "BEGIN mConnectedThread");
//            byte[] buffer = new byte[1024];
//            int bytes;
//
//            // Keep listening to the InputStream while connected
//            while (mState == STATE_CONNECTED) {
//                try {
//                    // Read from the InputStream
//                    bytes = mmInStream.read(buffer);
//
//                    // Send the obtained bytes to the UI Activity
//                    mHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer)
//                            .sendToTarget();
//                } catch (IOException e) {
//                    Log.e(TAG, "disconnected", e);
//                    connectionLost();
//                    break;
//                }
//            }
//        }
//
//        /**
//         * Write to the connected OutStream.
//         *
//         * @param buffer The bytes to write
//         */
//        public void write(byte[] buffer) {
//            try {
//                mmOutStream.write(buffer);
//
//                // Share the sent message back to the UI Activity
//                mHandler.obtainMessage(Constants.MESSAGE_WRITE, -1, -1, buffer)
//                        .sendToTarget();
//            } catch (IOException e) {
//                Log.e(TAG, "Exception during write", e);
//            }
//        }
//
//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) {
//                Log.e(TAG, "close() of connect socket failed", e);
//            }
//        }
//    }
//}
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
//import android.support.annotation.Nullable;
import android.util.Log;

//import com.afaa.tanktrouble.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;


public class BluetoothService extends Service {

    private static final String TAG = "BluetoothService";
    private static final UUID MY_UUID = UUID.fromString("27e86a38-a29c-421e-9d17-fe9c0c3bf2e6");

    private BluetoothAdapter btAdapter;
    private BluetoothSocket btSocket;

    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }

        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        if (btSocket != null) {
            try {
                btSocket.close();
            } catch (IOException ignored) {
            }
            btSocket = null;
        }
    }

//    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class BtBinder extends Binder {
        public BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    private final IBinder binder = new BtBinder();

    public static class BtUnavailableException extends Exception {
        public BtUnavailableException() {
            super("bluetooth is not supported");
        }
    }

    public void initBtAdapter() throws BtUnavailableException {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            throw new BtUnavailableException();
        }
    }

    public boolean isConnected() {
        return connectedThread != null;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return btAdapter;
    }

    public interface OnConnected {
        void success();
    }

    private OnConnected onConnected;

    public void setOnConnected(OnConnected onConnected) {
        this.onConnected = onConnected;
    }

    public void startAcceptThread() {
        acceptThread = new AcceptThread();
        acceptThread.start();
    }

    public void startConnectThread(String address) {
        connectThread = new ConnectThread(address);
        connectThread.start();
    }

    public BluetoothSocket getBluetoothSocket() {
        return btSocket;
    }


    public void registerActivity(Class<?> activityClass) {
        lastClass = activityClass;
        changeRegCount(1);
    }


    public void unregisterActivity() {
        changeRegCount(-1);
    }

    private int regCount = 0;
    private Class<?> lastClass;

    private synchronized void changeRegCount(int d) {
        int newRegCount = regCount + d;

        if (newRegCount != 0 && regCount == 0) {
            hideNotification();
        }

        regCount = newRegCount;
    }

    public boolean isServer() {
        return isServer;
    }

    public interface OnMessageReceivedListener {
        void process(byte[] buffer);
    }

    public class MessageChannel {
        private OnMessageReceivedListener onMessageReceivedListener;

        public void setOnMessageReceivedListener(OnMessageReceivedListener onMessageReceivedListener) {
            this.onMessageReceivedListener = onMessageReceivedListener;
        }

        public void send(byte[] bytes) {

            if(connectedThread == null)
                return;
            synchronized (connectedThread) {
                byte[] buffer = new byte[bytes.length + 1];
                System.arraycopy(bytes, 0, buffer, 1, bytes.length);
                connectedThread.write(buffer);
            }
        }
    }

    MessageChannel channel = new MessageChannel();

    public MessageChannel getChannel() {
        if(channel == null) {
            channel = new MessageChannel();
        }
        return channel;
    }

    public void unregisterChannel() {
        channel = null;
    }

    private void process(byte[] message) {
        if(channel == null || channel.onMessageReceivedListener == null) {
            return;
        }
        channel.onMessageReceivedListener.process(message);
    }

    private void hideNotification() {
        stopForeground(true);
    }

    private boolean isServer;

    private synchronized void connected(BluetoothSocket socket, boolean isServer) {
        this.btSocket = socket;

        this.isServer = isServer;

        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }

        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        onConnected.success();

        connectedThread = new ConnectedThread();
        connectedThread.start();
    }

    private class AcceptThread extends Thread {
        private BluetoothServerSocket serverSocket;

        public AcceptThread() {
            try {
                serverSocket = btAdapter.listenUsingRfcommWithServiceRecord(TAG, MY_UUID);
            } catch (IOException ignored) {
            }
        }

        public void run() {
            BluetoothSocket socket;
            while (true) {
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    break;
                }
                if (socket != null) {
                    connected(socket, true);
                    try {
                        serverSocket.close();
                    } catch (IOException ignored) {
                    }
                    break;
                }
            }
        }

        public void cancel() {
            try {
                serverSocket.close();
            } catch (IOException ignored) {
            }
        }
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket socket;
        public static final String TAG = "ConnectThread";

        public ConnectThread(String address) {
            try {
                socket = btAdapter.getRemoteDevice(address).createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException ignored) {
            }
        }

        public void run() {
            btAdapter.cancelDiscovery();

            try {
                socket.connect();
            } catch (IOException connectException) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
                return;
            }

            BluetoothSocket tmp = socket;
            socket = null;
            connected(tmp, false);
        }

        public void cancel() {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public ConnectedThread() {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = btSocket.getInputStream();
                tmpOut = btSocket.getOutputStream();
            } catch (IOException ignored) {
                Log.d(TAG, "error");
            }

            inputStream = tmpIn;
            outputStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];

            while (true) {
                try {
                    int countReadBytes = inputStream.read(buffer);
                    process(Arrays.copyOfRange(buffer, 0, countReadBytes));
                    buffer = new byte[1024];
                } catch (IOException e) {
                    break;
                }
            }
        }

        private void write(byte[] bytes) {
            try {
                outputStream.write(bytes);
            } catch (IOException ignored) {
            }
        }

        public void cancel() {
            try {
                btSocket.close();
            } catch (IOException ignored) {
            }
        }
    }
}

