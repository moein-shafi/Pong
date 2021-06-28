package com.example.pong;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.*;


public class ServerActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;

    ArrayList<String> clientAddresses = new ArrayList<>();

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

    public void discoverable(View view){
        if (!mBluetoothAdapter.isDiscovering()){
            Toast.makeText(this,"Making your device Discoverable",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivityForResult(intent,REQUEST_DISCOVER_BT);
        }

    }

    public void connect(View view) {
        if (mBluetoothAdapter.isEnabled()) {
            Intent serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
        } else {
            Toast.makeText(this, "Turn on bluetooth to get paired devices", Toast.LENGTH_SHORT).show();
        }
    }

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
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        Bundle extras = data.getExtras();
        if (extras == null) {
            return;
        }
        String clientAddress = extras.getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(clientAddress);


        mChatService.connect(device, secure);
    }
}
