package com.example.conserjes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.conserjes.Screen.HomeScreen
import com.example.conserjes.navigation.AppNavHost
import com.example.conserjes.ui.theme.ConserjesTheme
import com.example.conserjes.viewmodels.PostViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ConserjesTheme {
                AppNavHost()
            }
        }
    }
}