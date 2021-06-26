package com.example.pong;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.ramimartin.multibluetooth.activity.BluetoothActivity;
import com.ramimartin.multibluetooth.bluetooth.manager.BluetoothManager;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class ServerActivity extends BluetoothActivity {
//    private static String UUID_String = "PONG SERVER";
//    private static final UUID MY_UUID = UUID.nameUUIDFromBytes(UUID_String.getBytes());
//    private static final String NAME = "pong";
//    private static final String TAG = "pongServer";
//    private static final int REQUEST_ENABLE_BT = 1;
//    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//    private class AcceptThread extends Thread {
//        private final BluetoothServerSocket mmServerSocket;
//        public AcceptThread() {
//
//            // Use a temporary object that is later assigned to mmServerSocket
//            // because mmServerSocket is final.
//            BluetoothServerSocket tmp = null;
//            try {
//                // MY_UUID is the app's UUID string, also used by the client code.
//                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
//            } catch (IOException e) {
//                Log.e(TAG, "Socket's listen() method failed", e);
//            }
//            mmServerSocket = tmp;
//        }
//
//        public void run() {
//            BluetoothSocket socket = null;
//            // Keep listening until exception occurs or a socket is returned.
//            while (true) {
//                try {
//                    socket = mmServerSocket.accept();
//                } catch (IOException e) {
//                    Log.e(TAG, "Socket's accept() method failed", e);
//                    break;
//                }
//
//                if (socket != null) {
//                    // A connection was accepted. Perform work associated with
//                    // the connection in a separate thread.
//                    Toast.makeText(getApplicationContext(), "One device connected", Toast.LENGTH_LONG).show();
//                    try {
//                        mmServerSocket.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                }
//            }
//        }
//
//        // Closes the connect socket and causes the thread to finish.
//        public void cancel() {
//            try {
//                mmServerSocket.close();
//            } catch (IOException e) {
//                Log.e(TAG, "Could not close the connect socket", e);
//            }
//        }
//    }
//
//    private static final int DISCOVER_DURATION = 300;
//    private static final int REQUEST_BLU = 1;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
        setTimeDiscoverable(BluetoothManager.BLUETOOTH_TIME_DICOVERY_3600_SEC);
        selectServerMode();

        scanAllBluetoothDevice();

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
//                ball.move(deltaT);
//                show();
                Toast.makeText(getApplicationContext(),"Hello Server",Toast.LENGTH_LONG).show();

            }
        }, 0, 30000);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                // TODO stuff if you need
            }
        }
    }

    @Override
    public String setUUIDappIdentifier() {
        return "e0912847-d749-81e9-8841";
    }

    @Override
    public int myNbrClientMax() {
        return 3;
    }

    @Override
    public void onBluetoothDeviceFound(BluetoothDevice bluetoothDevice) {
        if(getTypeBluetooth() == BluetoothManager.TypeBluetooth.Server) {
            Toast.makeText(getApplicationContext(),"==> Device detected and Thread Server created" + bluetoothDevice.getAddress(),Toast.LENGTH_LONG).show();
            //            setLogText("===> Device detected and Thread Server created for this address : " + device.getAddress());
        }else{
            Toast.makeText(getApplicationContext(),"Device detected" + bluetoothDevice.getAddress(),Toast.LENGTH_LONG).show();
//            setLogText("===> Device detected : "+ device.getAddress());
        }
    }

    @Override
    public void onClientConnectionSuccess() {

        Toast.makeText(getApplicationContext(),"Client Connection Success",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onClientConnectionFail() {
        Toast.makeText(getApplicationContext(),"Client Connection Failed",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onServeurConnectionSuccess() {
        Toast.makeText(getApplicationContext(),"Server Connection Success",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onServeurConnectionFail() {
        Toast.makeText(getApplicationContext(),"Server Connection Failed",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBluetoothStartDiscovery() {
        Toast.makeText(getApplicationContext(),"Server Started Bluetooth Discovery!",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBluetoothMsgStringReceived(String s) {

    }

    @Override
    public void onBluetoothMsgObjectReceived(Object o) {

    }

    @Override
    public void onBluetoothMsgBytesReceived(byte[] bytes) {

    }

    @Override
    public void onBluetoothNotAviable() {

    }
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
//        AcceptThread acceptThread = new AcceptThread();
//        acceptThread.run();
//
//
////        enableBluetooth();
//
//    }
////
////    public void enableBluetooth() {
////        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
////
////        startActivityForResult(discoveryIntent, REQUEST_BLU);
////    }
//
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////
////        if(resultCode == DISCOVER_DURATION && requestCode == REQUEST_BLU) {
////            Intent intent = new Intent();
////            intent.setAction(Intent.ACTION_SEND);
////            intent.setType("text/plain");
////            intent.putExtra(Intent.EXTRA_STREAM, "YO BROOOOO");
////
////            PackageManager pm = getPackageManager();
////            List<ResolveInfo> appsList = pm.queryIntentActivities(intent, 0);
////
////            if(appsList.size() > 0) {
////                String packageName = null;
////                String className = null;
////                boolean found = false;
////
////                for(ResolveInfo info : appsList) {
////                    packageName = info.activityInfo.packageName;
////                    if(packageName.equals("com.android.bluetooth")) {
////                        className = info.activityInfo.name;
////                        found = true;
////                        break;
////                    }
////                }
////
////                if(!found) {
////                    Toast.makeText(this, "Bluetooth not found",
////                            Toast.LENGTH_LONG).show();
////                } else {
////                    intent.setClassName(packageName, className);
////                    startActivity(intent);
////                }
////            }
////        } else {
////            Toast.makeText(this, "Bluetooth is cancelled", Toast.LENGTH_LONG).show();
////        }
////    }
}