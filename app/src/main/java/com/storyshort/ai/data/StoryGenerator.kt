package com.storyshort.ai.data

class StoryGenerator {
    fun generate(idea: String, style: String, mood: String): Story {
        val subject = idea.trim().ifBlank { "a mysterious little moment" }
        val lead = when (style) {
            "Cartoon" -> "a bright-eyed cartoon hero"
            "Realistic" -> "a natural, expressive protagonist"
            "3D Animation" -> "a charming 3D animated character"
            else -> "a cinematic, larger-than-life character"
        }
        val tone = when (mood) {
            "Funny" -> "with playful comic timing"
            "Emotional" -> "with a tender, heartfelt turn"
            "Dramatic" -> "with rising tension and a bold reveal"
            else -> "with a brave, unexpected discovery"
        }
        val title = subject.split(" ").take(5).joinToString(" ").replaceFirstChar { it.uppercase() } +
            when (mood) { "Funny" -> " Goes Wrong"; "Emotional" -> " Finds Home"; "Dramatic" -> " Has One Chance"; else -> " Beyond the Map" }
        return Story(
            title = title,
            story = "00:00–00:03  Hook\n$lead spots something impossible about $subject.\n\n" +
                "00:03–00:07  Action / Twist\nThey rush toward it, $tone — then the ordinary world flips upside down.\n\n" +
                "00:07–00:10  Ending / Punchline\nThe truth lands in one unforgettable beat, leaving a smile and one question: what happens next?",
            dialogue = "“Wait… that was supposed to happen?”\n“Maybe it was waiting for us.”",
            scene = "Environment: a richly textured setting built around $subject.\nCharacter positions: $lead in the foreground, discovery just behind them.\nCamera composition: vertical medium-wide frame with a clear center silhouette.\nLighting: soft cyan rim light against deep violet shadows.\nExpressions: curious eyes shift to surprise, then a confident grin.\nMovement: a quick glance, committed step, and a precise reveal with natural follow-through.",
            videoPrompt = "Create a polished 9:16 vertical video, exactly 10 seconds long. Feature $lead in an environment shaped by $subject. Begin with a close curiosity hook, follow with a smooth push-in as the character moves toward the discovery, then reveal the twist with a controlled camera orbit. Use cinematic cyan and purple lighting, expressive facial animation, realistic motion, believable weight and timing, consistent character design, clean continuity between shots, and a satisfying final reaction. No text overlays, no jump cuts, exactly 10 seconds.",
            hashtags = listOf("#StoryShortAI", "#ShortStory", "#AIStory", "#${style.replace(" ", "")}", "#${mood}", "#VideoPrompt", "#CreatorIdeas").joinToString(" ")
        )
    }
}