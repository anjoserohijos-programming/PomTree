package com.example.pomtimer

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
                }, modifier = Modifier.padding(horizontal = 10.dp)) {
                   Text("Pomodoro")
                }
                Button(onClick = {
                                 minutes = 5
                    seconds = 0
                    isRunning = false
                }, modifier = Modifier.padding(horizontal = 10.dp)) {
                    Text("Short break")
                }
                Button(onClick = {minutes = 15
                                 seconds = 0
                    isRunning = false}, modifier = Modifier.padding(horizontal = 10.dp)) {
                    Text("Long break")
                }
            }
            Canvas(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .padding(8.dp)
            ) {

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
            TaskbarList(
                taskItemList = listOf<Task>(
                    Task(0, UUID.randomUUID().toString(),"", true),
                    Task(1, UUID.randomUUID().toString(),"", true)
                            ,Task(1, UUID.randomUUID().toString(),"", true)
                    )
                )
            }
        }
}
}

@Composable
    fun TaskbarList(taskItemList: List<Task>){
        Column {
            for(i in taskItemList){
                TaskItem(itemId = i.getItemId(), itemName = i.getItemName(), itemDescription = i.getItemDescription(), isItemFinished = i.getIsItemFinished())
            }
        }
    }

class Task(itemId: Int, itemName: String, itemDescription: String, isItemFinished: Boolean) {
    private var itemId: Int
    private var itemName: String
    private var itemDescription: String
    private var isItemFinished: Boolean

    init {
        this.itemId = itemId
        this.itemName = itemName
        this.itemDescription = itemDescription
        this.isItemFinished = isItemFinished
    }


    fun getItemId (): Int{
        return this.itemId
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
    fun TaskItem(itemId: Int, itemName: String, itemDescription: String, isItemFinished: Boolean) {
        Row(modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .padding(10.dp)
            .background(color = Color.Yellow)
        ){
            Text("$itemId")
            Column(modifier = Modifier.fillMaxSize()) {

            }
        }
    }

