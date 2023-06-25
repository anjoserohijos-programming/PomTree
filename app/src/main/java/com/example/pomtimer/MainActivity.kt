package com.example.pomtimer

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    val taskItemList = mutableListOf<Task>()
    var progress = remember { mutableStateOf(taskItemList.size) }
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (minutes >= 0 || seconds > 0) {
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
        isRunning = false

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
                    minutes = 25
                    seconds = 0
                    isRunning = false
                }) {
                   Text("Pomodoro")
                }
                Button(onClick = {
                                 minutes = 5
                    seconds = 0
                    isRunning = false
                }) {
                    Text("Short break")
                }
                Button(onClick = {minutes = 15
                                 seconds = 0
                    isRunning = false}) {
                    Text("Long break")
                }
            }
            Canvas(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .padding(8.dp)
            ) {
                //dito banda yung tree growth algorithm eyy
                //tree growth based dun sa value ng progressbar

                //cc: marc
                val totalSeconds = minutes * 60 + seconds
            }
            Row(
                modifier = Modifier.padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //dito banda yung progress bar, progress ng task list na nakacheck
                //cc: anjo
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
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Tasks: 0/${progress.value}")
            Button(onClick = {
                taskItemList.add(Task(itemName = UUID.randomUUID().toString(), itemDescription = "SampleDesc", isItemFinished = false))
            }){
                Text(text = "+")
            }
            LinearProgressIndicator(

            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ){
            TaskbarList(taskItemList = taskItemList)
        }
        }
}
}

@Composable
    fun TaskbarList(taskItemList: List<Task>){
    val scrollState = rememberScrollState()
        Column (modifier = Modifier.verticalScroll(scrollState)){
            for(i in taskItemList){
                TaskItem(itemName = i.getItemName(), itemDescription = i.getItemDescription(), isItemFinished = i.getIsItemFinished())
            }
        }
    }

class Task(itemName: String, itemDescription: String, isItemFinished: Boolean) {
    private var itemName: String
    private var itemDescription: String
    private var isItemFinished: Boolean

    init {
        this.itemName = itemName
        this.itemDescription = itemDescription
        this.isItemFinished = isItemFinished
    }

    fun getItemName(): String{
        return this.itemName
    }

    fun getItemDescription(): String{
        return this.itemDescription
    }
    fun getIsItemFinished(): Boolean{
        return this.isItemFinished
    }
}
    @Composable
    fun TaskItem(itemName: String, itemDescription: String, isItemFinished: Boolean) {
        Row(modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .padding(10.dp)
            .background(color = Color.Yellow)
        ){
            Column(modifier = Modifier.fillMaxSize()) {
                //dito yung designnn ng Task Item
                //mga variables nyoo is itemId, itemName, itemDescription, tas isItemFinished
                //cc: shaira & JC uwuuu
            }
        }
    }

