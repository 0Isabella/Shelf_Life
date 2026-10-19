package com.example.shelflife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.shelflife.navigation.AppNavigation
import com.example.shelflife.ui.theme.ShelflifeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShelflifeTheme {
                AppNavigation()
            }
        }
    }
}