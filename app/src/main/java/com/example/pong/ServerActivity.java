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

import java.nio.charset.StandardCharsets;
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
    private int numOfConnected = 0;


    BluetoothAdapter mBluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        Log.d("bluetooth-debug","request for service");
        bindService(new Intent(this,BluetoothService.class),connection,BIND_AUTO_CREATE);
        Log.d("bluetooth-debug","after request for service");
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
            Log.d("bluetooth-debug","init service");
            btService = ((BluetoothService.BtBinder) service).getService();
            //GameData.getInstance().setBtService(btService);
            GameActivity.setBluetoothService(btService);

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
                            if (numOfConnected == 1) {
                                shouldStop = false;
                                GameActivity.setIsServer(true);
                                startActivity(new Intent(ServerActivity.this,
                                        GameActivity.class));
                            }else{
                                numOfConnected += 1;
                            }
                        }
                    });
                }
            });
        }
    };
    private void initBt() {
        btService.startAcceptThread();
        Log.d("bluetooth-debug","accept thread started");
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
                    Bundle extras = data.getExtras();
                    if (extras == null) {
                        return;
                    }
                    String clientAddress = extras.getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    System.out.println("client address: "+clientAddress);
                    Log.d("bluetooth-debug","request for connection thread");
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
        super.onDestroy();
    }
}
