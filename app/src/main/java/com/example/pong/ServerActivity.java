package com.example.pong;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.*;


public class ServerActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_DISCOVERABLE = 1;
    private static final int REQUEST_ENABLE_BLUETOOTH = 2;
    ArrayList<String> clientAddresses = new ArrayList<>();
    BluetoothService btService;
    private boolean shouldStop = true;


    BluetoothAdapter mBluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        bindService(new Intent(this,BluetoothService.class),connection,BIND_AUTO_CREATE);
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

    private final ServiceConnection connection = new ServiceConnection() {


        @Override
        public void onServiceDisconnected(ComponentName name) {
            btService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            btService = ((BluetoothService.BtBinder) service).getService();
            //GameData.getInstance().setBtService(btService);
            btService.registerActivity(ServerActivity.class);

            try {
                btService.initBtAdapter();
            } catch (BluetoothService.BtUnavailableException e) {
                Toast.makeText(ServerActivity.this, "bluetooth is absent", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

            if (!btService.getBluetoothAdapter().isEnabled()) {
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BLUETOOTH);
            } else {
                initBt();
            }


            btService.setOnConnected(new BluetoothService.OnConnected() {
                @Override
                public void success() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            shouldStop = false;
                            if (btService.isServer()) {
//                                GameData.getInstance().setServer(true);
//                                startActivity(new Intent(DeviceChooserActivity.this,
//                                        BattleActivity.class));
                            } else {
//                                GameData.getInstance().setServer(false);
//                                startActivity(new Intent(DeviceChooserActivity.this,
//                                        BattleActivity.class));
                            }
                            finish();
                        }
                    });
                }
            });
        }
    };
    private void initBt() {
//        Set<BluetoothDevice> paired = btService.getBluetoothAdapter().getBondedDevices();
//        for (BluetoothDevice device : paired) {
//            arrayAdapter.add(device.getName() + "\n" + device.getAddress());
//        }
        btService.startAcceptThread();
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
//                    connectDevice(data, true);
                    Bundle extras = data.getExtras();
                    if (extras == null) {
                        return;
                    }
                    String clientAddress = extras.getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    System.out.println("client address: "+clientAddress);
                    btService.startConnectThread(clientAddress);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        if (btService != null) {
            btService.unregisterActivity();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (btService.getBluetoothAdapter() != null) {
            btService.getBluetoothAdapter().cancelDiscovery();
        }
//        unregisterReceiver(broadcastReceiver);
        if (btService != null && shouldStop) {
//            Log.d("DeviceChooser", "onDestroy: BtService is STOPPING!");
//            btService.stopSelf();
//            btService = null;
        }
        unbindService(connection);
        super.onDestroy();
    }
//    private void connectDevice(Intent data, boolean secure) {
//        // Get the device MAC address
//        Bundle extras = data.getExtras();
//        if (extras == null) {
//            return;
//        }
//        String clientAddress = extras.getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//        // Get the BluetoothDevice object
//        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(clientAddress);
//
//
//        mChatService.connect(device, secure);
//    }
}
