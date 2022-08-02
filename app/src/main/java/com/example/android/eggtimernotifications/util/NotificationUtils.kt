/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.eggtimernotifications.util

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.android.eggtimernotifications.MainActivity
import com.example.android.eggtimernotifications.R
import com.example.android.eggtimernotifications.receiver.CancelReceiver
import com.example.android.eggtimernotifications.receiver.SnoozeReceiver
import kotlinx.coroutines.NonCancellable.cancel

// Notification ID.
private val NOTIFICATION_ID = 0
private var MUTABLE_NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val CANCEL_REQUEST_CODE = 1
private val FLAGS = 0

// TODO: Step 1.1 extension function to send messages (GIVEN)
/**
 * Builds and delivers the notification.
 *
 * @param applicationContext
 * @param messageBody
 */
@SuppressLint("UnspecifiedImmutableFlag")
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity
    // TODO: Step 1.11 create intent
    val intent = Intent(applicationContext, MainActivity::class.java).apply {
        flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
    }

    // TODO: Step 1.12 create PendingIntent
    val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, FLAG_IMMUTABLE)

    // TODO: Step 2.0 add style
    val eggImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.cooked_egg
    )
    val bigPictureStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(eggImage)
        .bigLargeIcon(null)

    // TODO: Step 2.2.1 add snooze action
    val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
    val snoozePendingIntent = PendingIntent.getBroadcast(applicationContext, REQUEST_CODE, snoozeIntent, PendingIntent.FLAG_ONE_SHOT)
    val snoozeAction = NotificationCompat.Action(R.drawable.egg_icon,applicationContext.getString(R.string.snooze), snoozePendingIntent)

    // TODO: Step 2.2.2 add cancel action
    val cancelIntent = Intent(applicationContext, CancelReceiver::class.java)
    val cancelPendingIntent = PendingIntent.getBroadcast(applicationContext, CANCEL_REQUEST_CODE, cancelIntent, PendingIntent.FLAG_ONE_SHOT)
    val cancelAction = NotificationCompat.Action(R.drawable.ic_cancel,applicationContext.getString(android.R.string.cancel), cancelPendingIntent)

    // TODO: Step 1.2 get an instance of NotificationCompat.Builder
    // Build the notification
    val notificationCompat = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.egg_notification_channel_id)
    )

    // TODO: Step 1.8 use the new 'breakfast' notification channel

    // TODO: Step 1.3 set title, text and icon to builder
        .setSmallIcon(R.drawable.egg_icon)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)

    // TODO: Step 1.13 set content intent
        .setContentIntent(pendingIntent)

        // TODO: Step 2.1 add style to builder
        .setStyle(bigPictureStyle)
        .setLargeIcon(eggImage)

        // TODO: Step 2.3 add snooze action
        .addAction(snoozeAction)
        .addAction(cancelAction)

        // TODO: Step 2.5 set priority
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)

    // TODO: Step 1.4 call notify
    notify(NOTIFICATION_ID, notificationCompat.build())
}

// TODO: Step 1.14 Cancel all notifications
fun NotificationManager.cancelAllNotifications() {
    cancelAll()
}
