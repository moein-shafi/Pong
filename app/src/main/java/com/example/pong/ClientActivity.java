package com.example.pong;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

//import com.ramimartin.multibluetooth.activity.BluetoothActivity;
//import com.ramimartin.multibluetooth.bluetooth.manager.BluetoothManager;

import java.io.IOException;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ClientActivity extends AppCompatActivity {
//    private static final int REQUEST_ENABLE_BT = 1;
//    private static String UUID_String = "PONG SERVER";
//    private static final UUID MY_UUID = UUID.nameUUIDFromBytes(UUID_String.getBytes());
//    private static final String TAG = "pongClient";
//
//    private class ConnectThread extends Thread {
//        private BluetoothAdapter bluetoothAdapter;
//        private final BluetoothSocket mmSocket;
//        private final BluetoothDevice mmDevice;
//
//        public ConnectThread(BluetoothDevice device) {
//            // Use a temporary object that is later assigned to mmSocket
//            // because mmSocket is final.
//            BluetoothSocket tmp = null;
//            mmDevice = device;
//
//            try {
//                // Get a BluetoothSocket to connect with the given BluetoothDevice.
//                // MY_UUID is the app's UUID string, also used in the server code.
//                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
//            } catch (IOException e) {
//                Log.e(TAG, "Socket's create() method failed", e);
//            }
//            mmSocket = tmp;
//        }
//
//        public void run() {
//            // Cancel discovery because it otherwise slows down the connection.
//            bluetoothAdapter.cancelDiscovery();
//
//            try {
//                // Connect to the remote device through the socket. This call blocks
//                // until it succeeds or throws an exception.
//                mmSocket.connect();
//            } catch (IOException connectException) {
//                // Unable to connect; close the socket and return.
//                try {
//                    mmSocket.close();
//                } catch (IOException closeException) {
//                    Log.e(TAG, "Could not close the client socket", closeException);
//                }
//                return;
//            }
//
//            // The connection attempt succeeded. Perform work associated with
//            // the connection in a separate thread.
//            Toast.makeText(getApplicationContext(), "Connected to server", Toast.LENGTH_LONG).show();
//        }
//
//        // Closes the client socket and causes the thread to finish.
//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) {
//                Log.e(TAG, "Could not close the client socket", e);
//            }
//        }
//    }
//
//    private static final int DISCOVER_DURATION = 300;
//    private static final int REQUEST_BLU = 1;
//
//

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

//        if (Build.VERSION.SDK_INT >= 23) {
//            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
//            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
//            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
//            }
//        }

//        setMessageMode(BluetoothManager.MessageMode.String);
//        setTimeDiscoverable(BluetoothManager.BLUETOOTH_TIME_DICOVERY_120_SEC);
//        selectClientMode();

//        Timer t = new Timer();
//        t.schedule(new TimerTask() {
//            @Override
//            public void run() {
////                ball.move(deltaT);
////                show();
//                Toast.makeText(getApplicationContext(),"Hello Client",Toast.LENGTH_LONG).show();
//
//            }
//        }, 0, 30000);

//        createClient(mServerAddressMac);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSION_REQUEST_COARSE_LOCATION: {
//                // TODO stuff if you need
//            }
//        }
//    }
//
//    @Override
//    public String setUUIDappIdentifier() {
//        return "e0912847-d749-81e9-8841";
//    }

//    @Override
//    public int myNbrClientMax() {
//        return 1;
//    }

//    @Override
//    public void onBluetoothDeviceFound(BluetoothDevice bluetoothDevice) {
//        if(getTypeBluetooth() == BluetoothManager.TypeBluetooth.Server) {
//            Toast.makeText(getApplicationContext(),"==> Device detected and Thread Server created" + bluetoothDevice.getAddress(),Toast.LENGTH_LONG).show();
//            //            setLogText("===> Device detected and Thread Server created for this address : " + device.getAddress());
//        }else{
//            Toast.makeText(getApplicationContext(),"Device detected" + bluetoothDevice.getAddress(),Toast.LENGTH_LONG).show();
////            setLogText("===> Device detected : "+ device.getAddress());
//        }
//    }

//    @Override
//    public void onClientConnectionSuccess() {
//
//        Toast.makeText(getApplicationContext(),"Client Connection Success",Toast.LENGTH_LONG).show();
//
//    }

//    @Override
//    public void onClientConnectionFail() {
//        Toast.makeText(getApplicationContext(),"Client Connection Failed",Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onServeurConnectionSuccess() {
//        Toast.makeText(getApplicationContext(),"Server Connection Success",Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onServeurConnectionFail() {
//        Toast.makeText(getApplicationContext(),"Server Connection Failed",Toast.LENGTH_LONG).show();
//    }
//
//    @Override
//    public void onBluetoothStartDiscovery() {
//
//    }
//
//    @Override
//    public void onBluetoothMsgStringReceived(String s) {
//
//    }
//
//    @Override
//    public void onBluetoothMsgObjectReceived(Object o) {
//
//    }
//
//    @Override
//    public void onBluetoothMsgBytesReceived(byte[] bytes) {
//
//    }
//
//    @Override
//    public void onBluetoothNotAviable() {
//        Toast.makeText(getApplicationContext(),"Bluetooth not available on this device.",Toast.LENGTH_LONG).show();
//
//    }


    //
//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        enableBluetooth();
//
//        if (bluetoothAdapter == null) {
//            Log.e(TAG, "Device doesn't support bluetooth");
//        }
//
//        if (!bluetoothAdapter.isEnabled()) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//        }
//
//
//        // Register for broadcasts when a device is discovered.
//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
////        registerReceiver(receiver, filter);
//
//        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
//
//        // Register for broadcasts when discovery has finished
//        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//        registerReceiver(discoveryFinishReceiver, filter);
//
////        if (pairedDevices.size() > 0) {
////            // There are paired devices. Get the name and address of each paired device.
////            for (BluetoothDevice device : pairedDevices) {
////                String deviceName = device.getName();
////                String deviceHardwareAddress = device.getAddress(); // MAC address
////            }
////        }
//
//        new ConnectThread(pairedDevices.iterator().next()).run();//start? //TODO CHECK ITERATOR.NEXT
//    }
//
//    private final BroadcastReceiver discoveryFinishReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
////                    discoveredDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
//                }
//            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
////                if (discoveredDevicesAdapter.getCount() == 0) {
////                    discoveredDevicesAdapter.add(getString(R.string.none_found));
////                }
//            }
//        }
//    };
//
//    public void enableBluetooth(){
//        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//
//        Intent intent = discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVER_DURATION);
//
//        startActivityForResult(discoveryIntent, REQUEST_BLU);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // Don't forget to unregister the ACTION_FOUND receiver.
////        unregisterReceiver(receiver);
//    }
}
