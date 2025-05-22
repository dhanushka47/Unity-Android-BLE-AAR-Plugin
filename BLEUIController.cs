using UnityEngine;
using UnityEngine.UI;
using TMPro; 
public class BLEUIController : MonoBehaviour
{
    public AndroidBLEManager bleManager;
    public TextMeshProUGUI logText;

    public void OnRequestPermissionsClicked()
    {
        if (bleManager.HasPermissions())
        {
            logText.text = "Permissions already granted.";
        }
        else
        {
            logText.text = "Requesting permissions...";
            bleManager.RequestPermissions();
        }
    }

    public void OnStartScanClicked()
    {
        if (!bleManager.HasPermissions())
        {
            logText.text = "Permissions missing, request them first.";
            return;
        }

        logText.text = "Starting BLE scan...";
        bleManager.StartScan();
    }

    public void OnStopScanClicked()
    {
        logText.text = "Stopping BLE scan...";
        bleManager.StopScan();
    }

    // This will be called from BLEManager via UnitySendMessage when devices found
    public void OnDeviceFound(string deviceInfo)
    {
        logText.text += "\nDevice: " + deviceInfo;
    }
}