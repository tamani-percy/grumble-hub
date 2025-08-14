package com.example.grumblehub.features.home.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SparklesText(
    words: List<String>,
    modifier: Modifier = Modifier,
    intervalMillis: Long = 5_000L,
    textStyle: TextStyle = TextStyle(
        fontSize = 17.sp,
        fontWeight = FontWeight.Light,
        color = Color.Gray
    ),
    sparklePalette: List<Color> = listOf(
        Color(0xFFFF80B5),
        Color(0xFFFFC2E1),
        Color(0xFFB39DFF),
        Color(0xFFE8D7FF)
    ),
    sparkleCount: Int = 16,        // how many particles alive at once
    spawnEveryMs: Int = 180,       // how often a new sparkle spawns
    sparkleLifespanMs: Int = 900   // how long one sparkle lives
) {
    require(words.isNotEmpty())

    // cycle words
    var index by remember { mutableIntStateOf(0) }
    LaunchedEffect(words, intervalMillis) {
        while (true) {
            kotlinx.coroutines.delay(intervalMillis)
            index = (index + 1) % words.size
        }
    }

    // we’ll capture per-character bounding boxes for better sparkle anchoring
    var layout by remember { mutableStateOf<TextLayoutResult?>(null) }
    val charBoxes = remember(layout) {
        val l = layout ?: return@remember emptyList<androidx.compose.ui.geometry.Rect>()
        (0 until l.layoutInput.text.text.length).map { i -> l.getBoundingBox(i) }
    }

    // particle state
    data class Sparkle(
        val id: Long,
        val startTime: Long,
        val life: Int,
        val anchor: Int,       // character index
        val angle: Float,      // radians for outward drift
        val speed: Float,      // px/s
        val size: Float,       // px
        val color: Color
    )

    val now = remember { mutableStateOf(System.currentTimeMillis()) }
    // tick clock (~60fps)
    LaunchedEffect(Unit) {
        while (true) {
            now.value = System.currentTimeMillis()
            withFrameNanos { } // let Compose sync to frame
        }
    }

    val particles = remember { mutableStateListOf<Sparkle>() }
    var lastSpawn by remember { mutableLongStateOf(0L) }

    // spawner
    LaunchedEffect(now.value, words[index]) {
        val t = now.value
        // remove expired
        particles.removeAll { t - it.startTime > it.life }
        // spawn new if needed
        if (t - lastSpawn > spawnEveryMs && particles.size < sparkleCount && charBoxes.isNotEmpty()) {
            lastSpawn = t
            val rnd = Random(t)
            val anchor = rnd.nextInt(charBoxes.size)
            particles += Sparkle(
                id = t,
                startTime = t,
                life = sparkleLifespanMs,
                anchor = anchor,
                angle = rnd.nextFloat() * (Math.PI * 2).toFloat(),
                speed = rnd.nextFloat() * 40f + 30f, // px per second
                size = rnd.nextFloat() * 8f + 10f,
                color = sparklePalette[rnd.nextInt(sparklePalette.size)]
            )
        }
    }

    Box(modifier, contentAlignment = Alignment.CenterStart) {
        AnimatedContent(
            targetState = words[index],
            transitionSpec = { fadeIn(tween(250)) with fadeOut(tween(200)) },
            label = "word"
        ) { word ->
            BasicText(
                text = word,
                style = textStyle,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                onTextLayout = { layout = it }
            )
        }

        // sparkles canvas (drawn on top, using char bounding boxes)
        if (charBoxes.isNotEmpty()) {
            Canvas(Modifier.matchParentSize()) {
                val t = now.value
                particles.forEach { s ->
                    val progress = ((t - s.startTime).coerceAtLeast(0)).toFloat() / s.life
                    val fadeIn = (progress * 2f).coerceIn(0f, 1f)
                    val fadeOut = (1f - progress).coerceIn(0f, 1f)
                    val alpha = (fadeIn * fadeOut)

                    val anchor = s.anchor.coerceIn(0, charBoxes.lastIndex)
                    val box = charBoxes[anchor]
                    val start = Offset(
                        x = (box.left + box.right) / 2f + Random(s.id).nextFloat() * 6f - 3f,
                        y = (box.top + box.bottom) / 2f + Random(s.id + 7).nextFloat() * 6f - 3f
                    )

                    val drift = (s.speed * (s.life * progress) / 1000f)
                    val pos = Offset(
                        x = start.x + cos(s.angle) * drift,
                        y = start.y + sin(s.angle) * drift
                    )

                    // small rotation & scale pulse
                    val scale = 0.8f + 0.4f * sin(progress * Math.PI * 2).toFloat()
                    val rotation = progress * 180f

                    drawStar(
                        center = pos,
                        size = s.size * scale,
                        color = s.color.copy(alpha = alpha),
                        strokeColor = Color(0x33000000),
                        rotationDeg = rotation
                    )
                }
            }
        }
    }
}

/** Draws a cute 8-point star (like in your screenshot). */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawStar(
    center: Offset,
    size: Float,
    color: Color,
    strokeColor: Color,
    rotationDeg: Float
) {
    val points = 8
    val outer = size / 2f
    val inner = outer * 0.45f
    val path = Path()
    val rot = Math.toRadians(rotationDeg.toDouble()).toFloat()

    fun pt(i: Int): Offset {
        val angle = rot + (i * (Math.PI / points)).toFloat()
        val r = if (i % 2 == 0) outer else inner
        return Offset(center.x + cos(angle) * r, center.y + sin(angle) * r)
    }

    path.moveTo(pt(0).x, pt(0).y)
    for (i in 1 until points * 2) {
        val p = pt(i)
        path.lineTo(p.x, p.y)
    }
    path.close()
    drawPath(path = path, color = color)
    // subtle outline
    drawPath(path = path, color = strokeColor)
}