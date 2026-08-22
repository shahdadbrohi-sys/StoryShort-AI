package com.storyshort.ai.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.storyshort.ai.StoryViewModel
import com.storyshort.ai.data.Story
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StoryShortApp(vm: StoryViewModel) {
    var tab by remember { mutableIntStateOf(0) }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF0C0D12), tonalElevation = 0.dp) {
                NavigationBarItem(selected = tab == 0, onClick = { tab = 0 }, icon = { Icon(Icons.Default.AutoAwesome, null) }, label = { Text("HOME") })
                NavigationBarItem(selected = tab == 1, onClick = { tab = 1; vm.refresh() }, icon = { Icon(Icons.Default.History, null) }, label = { Text("HISTORY") })
            }
        }
    ) { padding ->
        AnimatedContent(targetState = tab, modifier = Modifier.padding(padding), transitionSpec = { fadeIn() togetherWith fadeOut() }, label = "destination") {
            if (it == 0) HomeScreen(vm) else HistoryScreen(vm) { tab = 0 }
        }
    }
}

@Composable
private fun HomeScreen(vm: StoryViewModel) {
    val context = LocalContext.current
    LazyColumn(contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 28.dp, bottom = 28.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        item {
            Text("STORYSHORT AI", letterSpacing = 3.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
            Spacer(Modifier.height(9.dp))
            Text("Create 10-Second\nStories in Seconds", fontSize = 31.sp, lineHeight = 36.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Turn one spark into a ready-to-shoot short.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 15.sp)
        }
        item {
            Label("YOUR STORY IDEA")
            OutlinedTextField(value = vm.idea, onValueChange = { vm.idea = it }, placeholder = { Text("Enter your story idea...", color = Color(0xFF6F7580)) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 112.dp), minLines = 3, maxLines = 5, shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary, unfocusedBorderColor = Color(0xFF30343D)),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text))
        }
        item { ChoiceGroup("CHARACTER STYLE", listOf("Cartoon", "Realistic", "3D Animation", "Cinematic"), vm.style) { vm.style = it } }
        item { ChoiceGroup("STORY MOOD", listOf("Funny", "Emotional", "Dramatic", "Adventure"), vm.mood) { vm.mood = it } }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Label("DURATION"); Text("10 SECONDS", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary, fontSize = 13.sp)
            }
        }
        item {
            Button(onClick = { vm.generate(); copyToast(context, "Story generated") }, enabled = vm.idea.isNotBlank(), modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
                Icon(Icons.Default.AutoAwesome, null, Modifier.size(19.dp)); Spacer(Modifier.width(10.dp)); Text("GENERATE STORY", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            }
        }
        vm.current?.let { story ->
            item { StoryResults(story, context, vm) }
        }
    }
}

@Composable private fun Label(text: String) = Text(text, fontSize = 11.sp, letterSpacing = 1.4.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 9.dp))

@Composable private fun ChoiceGroup(label: String, choices: List<String>, selected: String, onSelected: (String) -> Unit) {
    Column { Label(label); Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        choices.forEach { choice ->
            FilterChip(selected = selected == choice, onClick = { onSelected(choice) }, label = { Text(choice, fontSize = 12.sp) }, modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp), colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF223D46), selectedLabelColor = MaterialTheme.colorScheme.primary))
        }
    } }
}

@Composable private fun StoryResults(story: Story, context: Context, vm: StoryViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("YOUR STORY", fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp, color = MaterialTheme.colorScheme.primary)
        StoryCard("TITLE", story.title, context)
        StoryCard("STORY", story.story, context, timeline = true)
        StoryCard("DIALOGUE", story.dialogue, context)
        StoryCard("SCENE", story.scene, context)
        StoryCard("VIDEO PROMPT", story.videoPrompt, context)
        StoryCard("HASHTAGS", story.hashtags, context)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = { copyToast(context, copyStory(story)) }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(13.dp)) { Icon(Icons.Default.ContentCopy, null, Modifier.size(17.dp)); Spacer(Modifier.width(7.dp)); Text("COPY ALL") }
            OutlinedButton(onClick = { vm.regenerate() }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(13.dp)) { Icon(Icons.Default.Refresh, null, Modifier.size(17.dp)); Spacer(Modifier.width(7.dp)); Text("REGENERATE") }
        }
        TextButton(onClick = vm::clear, modifier = Modifier.fillMaxWidth()) { Text("CLEAR", color = MaterialTheme.colorScheme.error, letterSpacing = 1.sp) }
    }
}

@Composable private fun StoryCard(title: String, content: String, context: Context, timeline: Boolean = false) {
    Card(shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF111319)), border = ButtonDefaults.outlinedButtonBorder) {
        Column(Modifier.padding(17.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(title, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp, fontSize = 11.sp)
                TextButton(onClick = { copyToast(context, content) }, contentPadding = PaddingValues(horizontal = 4.dp)) { Icon(Icons.Default.ContentCopy, null, Modifier.size(15.dp)); Spacer(Modifier.width(5.dp)); Text("COPY", fontSize = 11.sp) }
            }
            if (timeline) {
                Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    listOf("HOOK", "TWIST", "FINALE").forEachIndexed { index, text ->
                        Text(text, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (index == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                        if (index < 2) { Text("  →  ", color = MaterialTheme.colorScheme.primary) }
                    }
                }
            }
            Text(content, fontSize = 14.sp, lineHeight = 21.sp, color = MaterialTheme.colorScheme.onBackground)
        }
    }
}

@Composable private fun HistoryScreen(vm: StoryViewModel, onOpen: () -> Unit) {
    val context = LocalContext.current
    LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { Text("HISTORY", letterSpacing = 3.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 14.sp); Spacer(Modifier.height(8.dp)); Text("Your story vault", fontSize = 30.sp, fontWeight = FontWeight.Bold) }
        if (vm.history.isEmpty()) item { EmptyHistory() }
        items(vm.history, key = { it.id }) { story ->
            Card(shape = RoundedCornerShape(17.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF111319))) {
                Column(Modifier.padding(16.dp)) {
                    Text(story.title, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    Text(SimpleDateFormat("MMM d, yyyy  ·  h:mm a", Locale.getDefault()).format(Date(story.createdAt)), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, modifier = Modifier.padding(top = 5.dp))
                    Row(Modifier.padding(top = 14.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { vm.open(story); onOpen() }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(11.dp), contentPadding = PaddingValues(0.dp)) { Text("OPEN", fontSize = 12.sp) }
                        IconButton(onClick = { copyToast(context, copyStory(story)) }) { Icon(Icons.Default.ContentCopy, "Copy") }
                        IconButton(onClick = { vm.remove(story.id) }) { Icon(Icons.Default.DeleteOutline, "Delete", tint = MaterialTheme.colorScheme.error) }
                    }
                }
            }
        }
    }
}

@Composable private fun EmptyHistory() {
    Box(Modifier.fillMaxWidth().padding(top = 80.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) { Icon(Icons.Default.AutoAwesome, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(42.dp)); Spacer(Modifier.height(14.dp)); Text("Your story vault is empty", fontSize = 17.sp, fontWeight = FontWeight.Bold); Text("Generated stories will appear here.", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 5.dp)) }
    }
}

private fun copyStory(s: Story): String = "TITLE\n${s.title}\n\nSTORY\n${s.story}\n\nDIALOGUE\n${s.dialogue}\n\nSCENE\n${s.scene}\n\nVIDEO PROMPT\n${s.videoPrompt}\n\nHASHTAGS\n${s.hashtags}"
private fun copyToast(context: Context, text: String) { val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager; clipboard.setPrimaryClip(ClipData.newPlainText("StoryShort AI", text)); Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show() }