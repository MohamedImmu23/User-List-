package com.example.simplebasicapp.ui.animation

import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

@Composable
fun FadeInAnimation(content: @Composable () -> Unit) {
    val alpha: Float by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000), label = ""
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.alpha(alpha)) {
            content()
        }
    }
}