package com.example.pomtimer

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.*
import org.w3c.dom.Text
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
    var minutes by remember { mutableStateOf(25) }
    var seconds by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(true) }
    val taskItemList = remember {mutableListOf<Task>()}
    val progress by remember {mutableStateOf(taskItemList.size) }

    val isPomodoroClicked = remember { mutableStateOf(false) }
    var isShortBreakClicked = remember { mutableStateOf(false) }
    val isLongBreakClicked = remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false)}
    var isRecomposeLocked by remember { mutableStateOf(true)}
        LaunchedEffect(isRunning) {
        if (isRunning) {
            while (minutes > 0 || seconds > 0) {
                delay(1000)
                if (seconds > 0) {
                    seconds--
                } else if (minutes > 0) {
                    minutes--
                    seconds = 59
                }
            }
        }
    }
    if(showDialog){
       InputDialogView(onDismiss = {
           showDialog = false
       }, onTaskCreated = {task ->
           taskItemList.add(task)
       })
    }
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){

                Text(
                    text = "Pomodoro",
                    modifier = Modifier
                        .width(100.dp)
                        .clickable {
                            isPomodoroClicked.value = true
                            isShortBreakClicked.value = false
                            isLongBreakClicked.value = false
                            isRunning = false
                            minutes = 25
                            seconds = 0
                        },
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight(500),
                    color = Color(0, 102, 0),
                    textDecoration =  if (isPomodoroClicked.value) {
                        TextDecoration.Underline
                    } else {
                        TextDecoration.None
                    }
                )

                Text(
                    text = "Short Break",
                    modifier = Modifier
                        .width(100.dp)
                        .clickable {
                            isPomodoroClicked.value = false
                            isShortBreakClicked.value = true
                            isLongBreakClicked.value = false
                            isRunning = false
                            minutes = 5
                            seconds = 0
                        },
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight(500),
                    color = Color(0, 102, 0),
                    textDecoration =  if (isShortBreakClicked.value) {
                        TextDecoration.Underline
                    } else {
                        TextDecoration.None
                    }
                )
                Text(
                    text = "Long Break",
                    modifier = Modifier
                        .width(100.dp)

                        .clickable {
                            isPomodoroClicked.value = false
                            isShortBreakClicked.value = false
                            isLongBreakClicked.value = true
                            isRunning = false
                            minutes = 15
                            seconds = 0
                        },
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight(500),
                    color = Color(0, 102, 0),
                    textDecoration =  if (isLongBreakClicked.value) {
                        TextDecoration.Underline
                    } else {
                        TextDecoration.None
                    }
                )
            }
            Box(modifier = Modifier.height(200.dp)){
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

        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .align(alignment = Alignment.CenterHorizontally,), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Tasks: 0/${taskItemList.size}")
            Button(onClick = {
                if(!showDialog){
                    isRecomposeLocked = false
                    showDialog = true
                }
                else{
                    isRecomposeLocked = true
                }


            }){
                Text(text = "+")
            }
        }
            Column(modifier = Modifier.fillMaxHeight()) {
                Surface(modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        val scrollState = rememberScrollState()
                        Column (modifier = Modifier.verticalScroll(scrollState)){
                            for(i in taskItemList){
                                TaskItem(itemName = i.getItemName(), itemDescription = i.getItemDescription(), isItemFinished = i.getIsItemFinished())
                            }
                        }
                    }
                }

                if (isRunning) {
                    Button(
                        onClick = { isRunning = false },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(172, 115, 57)),
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "STOP",
                            color = Color.White,
                            style = TextStyle(shadow = Shadow(color = Color.Black, blurRadius = 5f)),
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Button(
                        onClick = { isRunning = true },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0, 204, 0)),
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "START",
                            color = Color.White,
                            style = TextStyle(shadow = Shadow(color = Color.Black, blurRadius = 5f)),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

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
@Composable
fun InputDialogView(onDismiss: () -> Unit, onTaskCreated: (Task) -> Unit) {
    val context = LocalContext.current
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.padding(8.dp),
            elevation = 8.dp
        ) {
            Column(
                Modifier
                    .background(Color.White)
            ) {
                Text(
                    text = "Enter the task title",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 20.sp
                )
                
                OutlinedTextField(value = taskTitle, onValueChange = {
                    taskTitle = it
                },
                    placeholder = { Text(text = "e.g. Feed my cat.") })

                OutlinedTextField(value = taskDescription, onValueChange = {
                    taskDescription = it
                },
                    placeholder = { Text(text = "e.g. once upon a time.") })
                Row {
                    OutlinedButton(
                        onClick = { onDismiss() },
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Cancel")
                    }

                    Button(
                        onClick = {
                            val task = Task(itemName = taskTitle, taskDescription, false)
                            onTaskCreated(task)
                            onDismiss()
                        },
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .weight(1F)
                    ) {
                        Text(text = "Go")
                    }
                }
            }
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

