package com.storyshort.ai

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.storyshort.ai.ui.StoryShortApp
import com.storyshort.ai.ui.theme.StoryShortTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("StoryShortStartup", "MainActivity.onCreate: launching StoryShortApp; no history deletion requested")
        enableEdgeToEdge()
        setContent { StoryShortTheme { StoryShortApp(viewModel()) } }
    }
}