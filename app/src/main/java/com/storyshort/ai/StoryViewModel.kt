package com.storyshort.ai

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.storyshort.ai.data.Story
import com.storyshort.ai.data.StoryGenerator
import com.storyshort.ai.data.StoryRepository

class StoryViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = StoryRepository(app)
    private val generator = StoryGenerator()
    init {
        Log.i("StoryShortStartup", "StoryViewModel initialized; loading history read-only")
    }
    var idea by mutableStateOf("")
    var style by mutableStateOf("Cartoon")
    var mood by mutableStateOf("Funny")
    var current by mutableStateOf<Story?>(null)
    var history by mutableStateOf(repo.getAll())
        private set
    fun generate() { current = generator.generate(idea, style, mood); current?.let { repo.save(it); refresh() } }
    fun clear() { idea = ""; current = null }
    fun regenerate() { if (idea.isNotBlank()) generate() }
    fun open(story: Story) { current = story; idea = story.title }
    fun remove(id: Long) {
        Log.i("StoryShortDelete", "Explicit history delete requested for id=$id")
        repo.delete(id)
        refresh()
    }
    fun refresh() { history = repo.getAll() }
}