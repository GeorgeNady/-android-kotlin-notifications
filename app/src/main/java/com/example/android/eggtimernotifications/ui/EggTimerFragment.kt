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

package com.example.android.eggtimernotifications.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.eggtimernotifications.R
import com.example.android.eggtimernotifications.databinding.FragmentEggTimerBinding
import com.google.firebase.messaging.FirebaseMessaging

class EggTimerFragment : Fragment() {

    private val binding by lazy { FragmentEggTimerBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this).get(EggTimerViewModel::class.java) }
    val TOPIC = "breakfast"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        with(binding) {
            lifecycleOwner = this@EggTimerFragment.viewLifecycleOwner
            eggTimerViewModel = viewModel
        }

        // TODO: Step 1.7 call create channel
        createChannel(
            getString(R.string.egg_notification_channel_id),
            getString(R.string.egg_notification_channel_name)
        )

        // TODO: Step 3.1 create channel for FCM
        createChannel(
            getString(R.string.breakfast_notification_channel_id),
            getString(R.string.breakfast_notification_channel_name)
        )

        // TODO: Step 3.4 call subscribe topics on start
        subscribeTopic()
        return binding.root
    }

    private fun createChannel(channelId: String, channelName: String) {
        // TODO: Step 1.6 START create a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                lightColor = Color.YELLOW
                enableVibration(true)
                description = "Time for breakfast"
                setShowBadge(false)
            }

            val notificationManager = requireActivity().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        // TODO: Step 1.6 END create a channel

    }

    private fun subscribeTopic() {
        // [Start subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("breakfast").addOnCompleteListener {
            var msg = getString(R.string.message_subscribed, "breakfast")
            if (!it.isSuccessful) {
                msg = getString(R.string.message_subscribe_failed, "breakfast")
            }
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }
        // [End subscribe_topics]
    }

    companion object {
        fun newInstance() = EggTimerFragment()
    }
}

