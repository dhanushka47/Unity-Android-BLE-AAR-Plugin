

# 🔌 Unity Android BLE AAR Plugin

A comprehensive example and guide for creating a custom Android AAR plugin to enable Bluetooth Low Energy (BLE) scanning in Unity apps. This project handles runtime permissions, BLE device scanning, and communicates scan results back to Unity for UI display.

---

## 🚀 Features

- BLE scanning with Android 6+ and Android 12+ permission handling
- Scan callbacks sent from Java plugin to Unity via `UnitySendMessage`
- Full Android Studio project to build AAR plugin
- Unity C# scripts to call plugin methods and display BLE scan results
- Ready-to-use integration setup for Unity projects

---

## 📁 Repository Structure

Unity-Android-BLE-AAR-Plugin/
├── AndroidPlugin/             # Android Studio project for BLE plugin
│   ├── src/main/java/com/excel/unityplugin/BLEManager.java
│   ├── AndroidManifest.xml
│   └── build.gradle
├── UnityProject/              # Unity example project
│   ├── Assets/
│   │   ├── Plugins/Android/BLEPlugin.aar   # Compiled AAR plugin (add after build)
│   │   └── Scripts/
│   │       ├── AndroidBLEManager.cs
│   │       └── BLEUIController.cs
├── README.md                  # This file

---

## ⚙️ Building the AAR Plugin

1. Open the `AndroidPlugin` folder in Android Studio.
2. Confirm the `build.gradle` file contains:
   ```groovy
   plugins {
       id 'com.android.library'
   }

   android {
       namespace 'com.excel.unityplugin'
       compileSdk 35

       defaultConfig {
           minSdk 26
           targetSdk 35
           versionCode 1
           versionName "1.0"
       }

       buildTypes {
           release {
               minifyEnabled false
           }
       }

       compileOptions {
           sourceCompatibility JavaVersion.VERSION_11
           targetCompatibility JavaVersion.VERSION_11
       }
   }

	3.	Implement BLE scanning and permission logic inside BLEManager.java.
	4.	Make sure all required permissions are declared in AndroidManifest.xml.
	5.	Build the project (Build > Make Module).
	6.	Locate your AAR file at:

AndroidPlugin/build/outputs/aar/AndroidPlugin-release.aar


	7.	Rename this file to BLEPlugin.aar and copy it into your Unity project under:

UnityProject/Assets/Plugins/Android/



⸻

🧩 Unity Integration
	1.	Copy BLEPlugin.aar to:

Assets/Plugins/Android/


	2.	Add the following C# scripts inside Assets/Scripts/:
	•	AndroidBLEManager.cs — Calls Java BLE plugin functions via JNI.
	•	BLEUIController.cs — UI handling for scanning and displaying devices.
	3.	Set up Unity UI buttons for:
	•	Requesting permissions
	•	Starting BLE scan
	•	Stopping BLE scan
	4.	Make sure your Unity scene has a GameObject with the BLEUIController script attached. Connect UI buttons to corresponding public methods:
	•	OnRequestPermissionsClicked()
	•	OnStartScanClicked()
	•	OnStopScanClicked()
	5.	When BLE devices are found, BLEManager sends device info back to Unity using:

UnityPlayer.UnitySendMessage("GameObjectName", "OnDeviceFound", deviceName + "|" + deviceAddress);

This triggers the OnDeviceFound(string deviceInfo) method in Unity for UI update.

⸻

📲 Required Android Permissions

Add these permissions in AndroidManifest.xml to ensure BLE scanning works on all supported Android versions:

<uses-permission android:name="android.permission.BLUETOOTH" />
<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

<!-- For Android 12+ -->
<uses-permission android:name="android.permission.BLUETOOTH_SCAN" />
<uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />


⸻

📋 Notes & Tips
	•	BLE scanning requires location permissions due to Android security policy.
	•	On Android 12+, explicit BLUETOOTH_SCAN and BLUETOOTH_CONNECT permissions are required.
	•	Handle runtime permission requests gracefully in Unity UI.
	•	You can extend this plugin to support BLE connection and data transfer.

⸻

📷 Screenshots

Add screenshots of your Unity BLE scanning UI and Android Studio setup here.

⸻

🤝 Contributions & Support

Feel free to open issues, submit pull requests, or suggest improvements. Contributions to enhance cross-platform support or extend functionality are welcome!

⸻

📜 License

This project is licensed under the MIT License.

⸻

🙏 Acknowledgments

Inspired by official Android BLE scanning guidelines and Unity native plugin best practices.

⸻

If you find this helpful, please ⭐ the repo!

⸻

Happy BLE Scanning with Unity!
