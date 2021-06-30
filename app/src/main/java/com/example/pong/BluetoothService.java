package com.example.pong;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import java.util.Arrays;

public class BluetoothService extends Service {

    private static final String TAG = "BluetoothService";
    private static final UUID MY_UUID = UUID.fromString("31a85a63-b17c-213e-1b40-af1d4d7ed4b9");

    private BluetoothAdapter btAdapter;
    private BluetoothSocket btSocket;

    private AcceptThread acceptThread;
    private ConnectThread connectThread;

    private ConnectedThread connectedThread1;
    private ConnectedThread connectedThread2;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("bluetooth-debug","destroyed");

        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }

        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (connectedThread1 != null) {
            connectedThread1.cancel();
            connectedThread1 = null;
        }

        if (connectedThread2 != null) {
            connectedThread2.cancel();
            connectedThread2 = null;
        }

        if (btSocket != null) {
            try {
                btSocket.close();
            } catch (IOException ignored) {
            }
            btSocket = null;
        }
    }

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
        return connectedThread1 != null;
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

        public void sendString(String s) {
            this.send(s.getBytes(StandardCharsets.UTF_8));
        }

        public void send(byte[] bytes) {

            if (connectedThread1 != null) {
                synchronized (connectedThread1) {
                    byte[] buffer = new byte[bytes.length];
                    System.arraycopy(bytes, 0, buffer, 0, bytes.length);
                    connectedThread1.write(buffer);
                }
            }
            if(connectedThread2 != null) {
                synchronized (connectedThread2) {
                    byte[] buffer = new byte[bytes.length];
                    System.arraycopy(bytes, 0, buffer, 0, bytes.length);
                    connectedThread2.write(buffer);
                }
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

        if (connectedThread1 == null) {
            Log.d("bluetooth-debug","thread 111 is null");
            connectedThread1 = new ConnectedThread();
            connectedThread1.start();
        } else {
            connectedThread2 = new ConnectedThread();
            connectedThread2.start();
        }


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
            Log.d("bluetooth-debug","before while in connection thread");
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
//                Log.d("bluetooth-debug","bluetooth write");
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

