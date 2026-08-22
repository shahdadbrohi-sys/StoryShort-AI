package com.storyshort.ai.data

data class Story(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val story: String,
    val dialogue: String,
    val scene: String,
    val videoPrompt: String,
    val hashtags: String,
    val createdAt: Long = System.currentTimeMillis()
)