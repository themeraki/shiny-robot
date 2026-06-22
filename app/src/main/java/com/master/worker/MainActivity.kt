package com.master.worker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val CAMERA_PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Check for permission, if granted, start service immediately
        checkPermissions()
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) 
            != PackageManager.PERMISSION_GRANTED) {
            
            // Request the permission from the user
            ActivityCompat.requestPermissions(
                this, 
                arrayOf(Manifest.permission.CAMERA), 
                CAMERA_PERMISSION_CODE
            )
        } else {
            // Permission already granted, launch the service
            startWorker()
        }
    }

    // Handle the user's response to the permission dialog
    override fun onRequestPermissionsResult(
        requestCode: Int, 
        permissions: Array<out String>, 
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE && grantResults.isNotEmpty() 
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startWorker()
        }
    }

    private fun startWorker() {
        val intent = Intent(this, WorkerService::class.java)
        // Android 8.0+ requires startForegroundService
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        // Close the app UI after starting the service in the background
        finish()
    }
}
