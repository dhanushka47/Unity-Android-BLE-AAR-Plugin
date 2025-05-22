using UnityEngine;

public class AndroidBLEManager : MonoBehaviour
{
    private AndroidJavaObject bleManager;
    private AndroidJavaObject currentActivity;

    void Awake()
    {
        // Get current Android activity from Unity
        using (AndroidJavaClass unityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer"))
        {
            currentActivity = unityPlayer.GetStatic<AndroidJavaObject>("currentActivity");
        }

        // Create instance of BLEManager Java class
        bleManager = new AndroidJavaObject("com.excel.unityplugin.BLEManager", currentActivity);
    }

    // Check if permissions are granted
    public bool HasPermissions()
    {
        if (bleManager == null) return false;
        return bleManager.Call<bool>("hasPermissions");
    }

    // Request permissions from user
    public void RequestPermissions()
    {
        if (bleManager == null) return;
        bleManager.Call("requestPermissions");
    }

    // Start BLE scanning
    public void StartScan()
    {
        if (bleManager == null) return;
        bleManager.Call("startScan");
    }

    // Stop BLE scanning
    public void StopScan()
    {
        if (bleManager == null) return;
        bleManager.Call("stopScan");
    }

    // This method can be called from Java (plugin) using UnitySendMessage to pass device info to Unity
    public void OnDeviceFound(string deviceInfo)
    {
        Debug.Log("Device Found: " + deviceInfo);
        // You can parse deviceInfo (name|address) and update your UI here
    }
}