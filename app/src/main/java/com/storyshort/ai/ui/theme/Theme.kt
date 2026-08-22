package com.storyshort.ai.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Dark = darkColorScheme(
    background = Color(0xFF08090C), surface = Color(0xFF111319), surfaceVariant = Color(0xFF1A1D25),
    primary = Color(0xFF5DE6F5), secondary = Color(0xFFB28CFF), onPrimary = Color(0xFF061013),
    onBackground = Color(0xFFF3F5F7), onSurface = Color(0xFFF3F5F7), onSurfaceVariant = Color(0xFF9BA2AF)
)
@Composable fun StoryShortTheme(content: @Composable () -> Unit) = MaterialTheme(colorScheme = Dark, content = content)