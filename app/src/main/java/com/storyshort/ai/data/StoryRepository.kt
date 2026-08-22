package com.storyshort.ai.data

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

class StoryRepository(context: Context) {
    private val prefs = context.getSharedPreferences("story_history", Context.MODE_PRIVATE)
    init {
        Log.i("StoryShortStartup", "StoryRepository initialized; persistence store opened without mutation")
    }
    fun getAll(): List<Story> = runCatching {
        Log.i("StoryShortStartup", "StoryRepository.getAll: read-only history load")
        val array = JSONArray(prefs.getString("items", "[]"))
        (0 until array.length()).map { fromJson(array.getJSONObject(it)) }.sortedByDescending { it.createdAt }
    }.getOrDefault(emptyList())
    fun save(story: Story) { val items = getAll().filterNot { it.id == story.id } + story; write(items) }
    fun delete(id: Long) {
        Log.i("StoryShortDelete", "StoryRepository.delete: deleting only explicitly requested id=$id")
        write(getAll().filterNot { it.id == id })
    }
    private fun write(items: List<Story>) = prefs.edit().putString("items", JSONArray(items.map { toJson(it) }).toString()).apply()
    private fun toJson(s: Story) = JSONObject().apply {
        put("id", s.id); put("title", s.title); put("story", s.story); put("dialogue", s.dialogue)
        put("scene", s.scene); put("videoPrompt", s.videoPrompt); put("hashtags", s.hashtags); put("createdAt", s.createdAt)
    }
    private fun fromJson(o: JSONObject) = Story(o.getLong("id"), o.getString("title"), o.getString("story"), o.getString("dialogue"), o.getString("scene"), o.getString("videoPrompt"), o.getString("hashtags"), o.getLong("createdAt"))
}