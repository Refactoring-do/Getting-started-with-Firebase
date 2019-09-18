package com.relearning.bootcamp2019

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class MainActivity : AppCompatActivity() {

    private lateinit var remoteConfig: FirebaseRemoteConfig

    companion object {
        private const val WELCOME_MESSAGE_KEY = "welcome_message"
        private const val COLOR_PRIMARY_KEY = "primary_color"
    }

    private var welcomeTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        welcomeTextView = findViewById(R.id.text)

        remoteConfig = FirebaseRemoteConfig.getInstance()

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0)
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful) {
                    val updated = task.getResult()
                    Log.d("MainActivity", "Config params updated: $updated")
                    Toast.makeText(this, "Fetch and activate succeeded",
                        Toast.LENGTH_SHORT).show()

                    welcomeTextView?.text = remoteConfig.getString(WELCOME_MESSAGE_KEY)
                    val color = Color.parseColor(remoteConfig.getString(COLOR_PRIMARY_KEY))
                    supportActionBar?.setBackgroundDrawable(ColorDrawable(color))
                    window.statusBarColor = color
                } else {
                    Toast.makeText(this, "Fetch failed",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}
