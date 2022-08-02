package com.example.android.eggtimernotifications.service

import android.app.NotificationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.android.eggtimernotifications.util.sendNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson

class EggFirebaseMessagingService: FirebaseMessagingService() {


    // TODO: Step 3.2 log registration token
    // [START on_new_token]
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String?) {
        Log.d(TAG, "onNewToken: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer()
    }


    /**
     * Called when message was received
     *
     * @param remoteMessage Object representing the message received from the firebase
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        // [START receive_message]
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage?.from}")

        // TODO: Step 3.5 check messages for data
        // Check if message contains a data payload.
        remoteMessage?.data?.let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            Log.d(TAG, "Message data payload keys: ${it.keys}")
            Log.d(TAG, "Message data payload values: ${it.values}")
            val message = Gson().fromJson(it.toString(), Payload::class.java)
            Log.d(TAG, "Message data payload message: $message")

        }

        // TODO: Step 3.5 check messages for notifications and call sendNotification
        val message = Gson().fromJson(remoteMessage?.data.toString(), Payload::class.java)
        remoteMessage?.notification?.let {
            val notificationManager = ContextCompat.getSystemService(this@EggFirebaseMessagingService, NotificationManager::class.java) as NotificationManager
            notificationManager.sendNotification("eggs: ${message.eggs}", this@EggFirebaseMessagingService)
        }

        // [END receive_message]
    }

    companion object {
        const val TAG = "EggFCMService"
    }

}

data class Payload(
    var eggs: Int
)