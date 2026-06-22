package com.master.worker

import android.app.*
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.net.Socket
import kotlin.concurrent.thread

class WorkerService : Service() {

    private val CHANNEL_ID = "MasterWorkerChannel"
    private val HUB_IP = "192.168.1.5" // REPLACE THIS with your Local Hub IP
    private val HUB_PORT = 65432

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Create the notification that keeps this service alive
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Master Worker Active")
            .setContentText("Worker is running in background")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .build()

        // Important: Android 14+ requires the ServiceType flag
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA)
        } else {
            startForeground(1, notification)
        }

        // Launch the socket listener in a background thread
        thread {
            startSocketListener()
        }

        return START_STICKY // Ensures service restarts if Android kills it
    }

    private fun startSocketListener() {
        try {
            val socket = Socket(HUB_IP, HUB_PORT)
            val output = socket.getOutputStream()
            val input = socket.getInputStream()
            
            // Basic heartbeat/listening loop
            val buffer = ByteArray(1024)
            while (true) {
                val bytesRead = input.read(buffer)
                if (bytesRead == -1) break
                
                // Logic: Handle incoming command from Hub
                // val command = String(buffer, 0, bytesRead)
                // if (command == "capture") { /* Trigger Camera Module */ }
            }
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
            // In a real build, we'd add a retry mechanism here
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Worker Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
