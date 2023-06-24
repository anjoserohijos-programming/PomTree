package com.example.pomtimer

import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ProgressIndicatorDefaults.ProgressAnimationSpec
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            PomodoroTimer()
        }
    }
}
@Composable
fun PomodoroTimer() {
    //val progress = remember { mutableStateOf(timeRemaining.toFloat()) }
    val seconds = 60
    var progress by remember { mutableStateOf(seconds.toFloat()) }
    val animatedProgress = animateFloatAsState(
        targetValue = (progress/seconds),
        animationSpec = ProgressAnimationSpec
    ).value
    Column {
        Text(text = "Pomodoro Timer")
        LinearProgressIndicator(progress = animatedProgress,
        )
    }

    LaunchedEffect(key1 = seconds) {
        progress = seconds.toFloat()

        launch{
            while (progress > 0) {
                progress -= 1f
                delay(1000)
            }
        }

    }
}