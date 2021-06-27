package com.example.pong;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ramimartin.multibluetooth.activity.BluetoothActivity;
import com.ramimartin.multibluetooth.bluetooth.manager.BluetoothManager;

import java.io.IOException;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class ServerActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    BluetoothAdapter mBluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null){
            Toast.makeText(this,"Bluetooth is unavailable",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"Bluetooth is available",Toast.LENGTH_SHORT).show();
        }

        if (mBluetoothAdapter.isEnabled()){
            Toast.makeText(this,"Bluetooth is enable",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"Bluetooth is disable",Toast.LENGTH_SHORT).show();
        }


    }

    public void turnOn(View view){
        if (!mBluetoothAdapter.isEnabled()){
            Toast.makeText(this,"turning on bluetooth...",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent,REQUEST_ENABLE_BT);
        }
        else{
            Toast.makeText(this,"Bluetooth is already on",Toast.LENGTH_SHORT).show();
        }
    }

    public void turnOff(View view){
        if (mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.disable();
            Toast.makeText(this,"Turning Bluetooth off",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"Bluetooth is already off",Toast.LENGTH_SHORT).show();
        }
    }

    public void discover(View view){
        if (!mBluetoothAdapter.isDiscovering()){
            Toast.makeText(this,"Making your device Discoverable",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivityForResult(intent,REQUEST_DISCOVER_BT);
        }

    }

    public void showPaired(View view){
//        if (mBluetoothAdapter.isEnabled()){
//            Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
//            String devices_info = "";
//            for (BluetoothDevice device: devices){
//                devices_info += "\nDevice: " + device.getName() + "," + device.getAddress();
//            }
//            Toast.makeText(this,devices_info,Toast.LENGTH_SHORT).show();
//        }
//        else{
//            Toast.makeText(this,"Turn on bluetooth to get paired devices",Toast.LENGTH_SHORT).show();
//        }
        mBluetoothAdapter.startDiscovery();
        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                //Finding devices
                if (BluetoothDevice.ACTION_FOUND.equals(action))
                {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    Toast.makeText(context,device.getName() + "\n" + device.getAddress(),Toast.LENGTH_SHORT).show();
                }
            }
        };
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);


//        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        this.registerReceiver(mReceiver, filter);
//        mBluetoothAdapter.startDiscovery();
    }

//    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                // Discovery has found a device. Get the BluetoothDevice
//                // object and its info from the Intent.
//                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                String deviceName = device.getName();
//                String deviceHardwareAddress = device.getAddress(); // MAC address
//                Toast.makeText(context,"device " + deviceName,Toast.LENGTH_SHORT).show();
////                Log.i("Device Name: " , "device " + deviceName);
//                Toast.makeText(context,"hard"  + deviceHardwareAddress,Toast.LENGTH_SHORT).show();
////                Log.i("deviceHardwareAddress " , "hard"  + deviceHardwareAddress);
//            }
//        }
//    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK){
                    Toast.makeText(this,"Bluetooth is on",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this,"couldn't on Bluetooth",Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
//    BluetoothAdapter b_adapter;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_server);
//        b_adapter = BluetoothAdapter.getDefaultAdapter();
//
//
//
//            if (Build.VERSION.SDK_INT >= 23) {
//                int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
//                permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
//                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
//                }
//            }
//            setTimeDiscoverable(BluetoothManager.BLUETOOTH_TIME_DICOVERY_3600_SEC);
//            selectServerMode();
//
//            scanAllBluetoothDevice();
//
//            Timer t = new Timer();
//            t.schedule(new TimerTask() {
//                @Override
//                public void run() {
////                ball.move(deltaT);
////                show();
//                    Toast.makeText(getApplicationContext(), "Hello Server", Toast.LENGTH_LONG).show();
//
//                }
//            }, 0, 30000);
//
//        }
//
//
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
//
//    @Override
//    public int myNbrClientMax() {
//        return 3;
//    }
//
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
//
//    @Override
//    public void onClientConnectionSuccess() {
//
//        Toast.makeText(getApplicationContext(),"Client Connection Success",Toast.LENGTH_LONG).show();
//
//    }
//
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
//        Toast.makeText(getApplicationContext(),"Server Started Bluetooth Discovery!",Toast.LENGTH_LONG).show();
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
//
//    }

}