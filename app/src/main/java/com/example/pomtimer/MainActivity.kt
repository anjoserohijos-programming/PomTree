package com.example.pomtimer

import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ProgressIndicatorDefaults.ProgressAnimationSpec
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.Key.Companion.Window
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.*
import java.util.UUID

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Main()

        }
    }
}

@Composable
fun Main()  {
    var minutes by remember { mutableStateOf(2) }
    var seconds by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (minutes > 0 || seconds > 0) {
                delay(1000)
                if (seconds > 0) {
                    seconds--
                } else if (minutes >= 0) {
                    minutes--
                    seconds = 59
                }
            }
            isRunning = false
        }
    }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row (modifier = Modifier.fillMaxWidth()){
                Button(onClick = {
                    minutes = 24
                    seconds = 59
                }, modifier = Modifier.padding(horizontal = 10.dp)) {
                   Text("Pomodoro")
                }
                Button(onClick = {
                                 minutes = 4
                    seconds = 59
                }, modifier = Modifier.padding(horizontal = 10.dp)) {
                    Text("Short break")
                }
                Button(onClick = {minutes = 14
                                 seconds = 59}, modifier = Modifier.padding(horizontal = 10.dp)) {
                    Text("Long break")
                }
            }
            Canvas(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .padding(8.dp)
            ) {
                drawArc(
                    color = Color.Gray,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                )
                val totalSeconds = minutes * 60 + seconds
            }
            Row(
                modifier = Modifier.padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "%02d:%02d".format(minutes, seconds),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(16.dp))
                if (isRunning) {
                    Button(onClick = { isRunning = false }) {
                        Text(text = "Stop")
                    }
                } else {
                    Button(onClick = { isRunning = true }) {
                        Text(text = "Start")
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ){
                TaskbarList(5)
            }
        }
    }
}

    @Composable
    fun TaskbarList(items: Int){
        LazyColumn {

        }
    }

    @Composable
    fun TaskItem(itemName: String) {

        Row(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()){
            Text(itemName)
        }
    }

