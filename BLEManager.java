package com.excel.unityplugin;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class BLEManager {

    public static final int PERMISSION_REQUEST_CODE = 1001;

    private final Activity activity;
    private final BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private ScanCallback scanCallback;

    public BLEManager(Activity activity) {
        this.activity = activity;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        }
    }

    // Check if all permissions are granted
    public boolean hasPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+
            boolean scan = ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED;
            boolean connect = ContextCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED;
            boolean location = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

            Log.d("BLEManager", "Permissions - SCAN: " + scan + ", CONNECT: " + connect + ", LOCATION: " + location);
            return scan && connect && location;
        } else {
            boolean location = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            Log.d("BLEManager", "Permission LOCATION: " + location);
            return location;
        }
    }

    // Request all needed permissions
    public void requestPermissions() {
        if (!hasPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{
                                Manifest.permission.BLUETOOTH_SCAN,
                                Manifest.permission.BLUETOOTH_CONNECT,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        PERMISSION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        PERMISSION_REQUEST_CODE);
            }
        }
    }

    // Must be called from your Activity's onRequestPermissionsResult!
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            if (grantResults.length > 0) {
                for (int res : grantResults) {
                    if (res != PackageManager.PERMISSION_GRANTED) {
                        allGranted = false;
                        break;
                    }
                }
            } else {
                allGranted = false;
            }

            if (allGranted) {
                Log.d("BLEManager", "Permissions granted, starting BLE scan.");
                startScan();
            } else {
                Log.e("BLEManager", "Permissions denied by user.");
            }
        }
    }

    // Start BLE scan if permissions granted
    public void startScan() {
        if (!hasPermissions()) {
            Log.e("BLEManager", "Missing permissions, requesting...");
            requestPermissions();
            return;
        }

        if (bluetoothLeScanner == null) {
            Log.e("BLEManager", "BluetoothLeScanner is null");
            return;
        }

        if (scanCallback == null) {
            scanCallback = new ScanCallback() {
                @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    BluetoothDevice device = result.getDevice();
                    String name = device.getName();
                    String address = device.getAddress();
                    Log.d("BLEManager", "Device found: " + name + " - " + address);

                    // Example: send device info to Unity
                    // UnityPlayer.UnitySendMessage("GameObjectName", "OnDeviceFound", name + "|" + address);
                }

                @Override
                public void onScanFailed(int errorCode) {
                    Log.e("BLEManager", "Scan failed with error: " + errorCode);
                }
            };
        }

        try {
            bluetoothLeScanner.startScan(scanCallback);
            Log.d("BLEManager", "BLE scan started");
        } catch (SecurityException e) {
            Log.e("BLEManager", "Start scan failed due to security exception", e);
        }
    }

    // Stop BLE scan
    public void stopScan() {
        try {
            if (bluetoothLeScanner != null && scanCallback != null) {
                bluetoothLeScanner.stopScan(scanCallback);
                Log.d("BLEManager", "BLE scan stopped");
            }
        } catch (SecurityException e) {
            Log.e("BLEManager", "Stop scan failed due to security exception", e);
        }
    }
}