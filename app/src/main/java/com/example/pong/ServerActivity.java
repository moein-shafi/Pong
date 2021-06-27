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
import android.view.View;
import android.widget.Toast;

import com.ramimartin.multibluetooth.activity.BluetoothActivity;
import com.ramimartin.multibluetooth.bluetooth.manager.BluetoothManager;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class ServerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);


    }

    public void turnOn(View view){

    }

    public void turnOff(View view){

    }

    public void discover(View view){

    }

    public void showPaired(View view){

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