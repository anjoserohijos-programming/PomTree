package com.example.pomtimer

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.*
import java.util.UUID
import kotlin.math.ceil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Main()
        }
    }
}
@Composable
fun  MyImage(){
    val img1 = "R.drawable.small_seed_level_0"
    val img2 = "R.drawable.small_sapling_level_1_1"
    val img3 = "R.drawable.small_sapling_level_2"
    val img4 = "R.drawable.small_sapling_level_3"
    val img5 = "R.drawable.small_sapling_level_4"
    var checkedTodoListCount = 8
    var maxTodoListCount = 10
    var img2Per = kotlin.math.ceil(maxTodoListCount * 0.2)
    var img3Per = kotlin.math.ceil(maxTodoListCount * 0.5)
    var img4Per = kotlin.math.ceil(maxTodoListCount * 0.8)
    if (checkedTodoListCount == maxTodoListCount){
        Image(painter = painterResource(id = R.drawable.small_sapling_level_4), contentDescription = "Image")
    }else if(checkedTodoListCount >= img4Per){
        Image(painter = painterResource(id = R.drawable.small_sapling_level_3), contentDescription = "Image")
    }
    else if(checkedTodoListCount >= img3Per){
        Image(painter = painterResource(id = R.drawable.small_sapling_level_2), contentDescription = "Image")
    }
    else if(checkedTodoListCount >= img2Per){
        Image(painter = painterResource(id = R.drawable.small_sapling_level_1_1), contentDescription = "Image")
    }
    else{
        Image(painter = painterResource(id = R.drawable.small_seed_level_0), contentDescription = "Image")
    }

}


@Composable
fun Main()  {
    var minutes by remember { mutableStateOf(25) }
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
//            Canvas(
//                modifier = Modifier
//                    .size(200.dp)
//                    .clip(CircleShape)
//                    .padding(8.dp)
//            ) {
//
//                val totalSeconds = minutes * 60 + seconds
//            }
            Column(modifier = Modifier .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                MyImage()
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

