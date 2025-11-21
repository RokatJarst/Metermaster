package com.example.metermaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.metermaster.ui.AppNavHost
import com.example.metermaster.ui.theme.MeterMasterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeterMasterTheme {
                AppNavHost()
            }
        }
    }
}
