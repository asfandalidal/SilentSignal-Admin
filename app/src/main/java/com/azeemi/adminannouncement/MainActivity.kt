package com.azeemi.adminannouncement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.azeemi.adminannouncement.presentation.screen.AdminScreen
import com.azeemi.adminannouncement.ui.theme.AdminAnnouncementTheme
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AndroidThreeTen.init(this)
        setContent {
            AdminAnnouncementTheme {
                AdminScreen()
            }
        }
    }
}


