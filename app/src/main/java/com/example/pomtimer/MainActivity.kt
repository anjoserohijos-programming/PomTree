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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
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
    val progress by remember {mutableStateOf(taskItemList.size)}

    val isPomodoroClicked = remember { mutableStateOf(false) }
    var isShortBreakClicked = remember { mutableStateOf(false) }
    val isLongBreakClicked = remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false)}
    var isRecomposeLocked by remember { mutableStateOf(true)}
    var finishedTaskCount by remember { mutableStateOf(0) }
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
        // callback function: bago lang to saken HAHAHA pero ayun imbis na
        // taskItemList.add (InputDialogView())
        // iimplement na lang yung callback function (sa loob ng InputDialogView) para
        // sa loob ng InputDialogView mattrack ung changes, hindi dito sa scope ng code na to.
        // following the logic ( open dialog -> wait for input -> apply the output -> close the dialog)
        // meaning mag-"recompose" lang sya kapag done na yung callback function
       InputDialogView(onDismiss = {
           showDialog = false
       }, onTaskCreated = {
               task -> taskItemList.add(task)
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
            Box(modifier = Modifier.height(200.dp), contentAlignment = Alignment.Center) {
                // Show a sample image
                Image(
                    painter = painterResource(R.drawable.small_sapling_level_3),
                    contentDescription = "Sample Image",
                    modifier = Modifier
                        .height(120.dp)
                        .width(120.dp)
                )

                // Show progress arc from 0-100 using Canvas
                Canvas(modifier = Modifier
                    .height(150.dp)
                    .width(150.dp)) {
                    val strokeWidth = 4.dp.toPx()
                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    val radius = (size.minDimension - strokeWidth) / 2
                    val startAngle = -90f

                    drawArc(
                        color = Color.Gray,
                        startAngle = startAngle,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(strokeWidth)
                    )

                    // Example progress value (can be dynamic)
                    if(taskItemList.size > 0 ){
                        drawArc(
                            color = Color(0, 204, 0),
                            startAngle = startAngle,
                            sweepAngle = ((finishedTaskCount / taskItemList.size).toFloat()) * 360f,
                            useCenter = false,
                            style = Stroke(strokeWidth)
                        )
                    }

                }
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

        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .align(alignment = Alignment.CenterHorizontally,), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Tasks: $finishedTaskCount/${taskItemList.size}")
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
                                TaskItem(itemName = i.getItemName(), itemDescription = i.getItemDescription(), isItemFinished = i.getIsItemFinished(),
                                    isChecked = {
                                        finishedTaskCount++
                                }, isUnchecked = {
                                    finishedTaskCount--
                                })
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
    fun TaskItem(itemName: String, itemDescription: String, isItemFinished: Boolean, isChecked: () -> Unit, isUnchecked: () -> Unit) {
        var checkBoxState by remember { mutableStateOf(false) }
        Row(modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .padding(10.dp)
            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(2.dp),)
        ){
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)) {
                //dito yung designnn ng Task Item
                //mga variables nyoo is itemId, itemName, itemDescription, tas isItemFinished
                //wag na pala yung itemId, di na magagamit yun, itemName na lang saka itemDescription saka isItemFinished
                //bale combine lang dito ng checkbox, itemName sa taas, tas itemDescription sa baba HAHAHAH
                //sesend ako itsura sa messenger haha
                // tyy mga priiiiiii letsgooo

                //cc: shaira & JC uwuuu
                Row(modifier = Modifier.fillMaxHeight(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
                    Checkbox(checked = checkBoxState, onCheckedChange = {
                        checkBoxState = it
                        if(checkBoxState){
                            isChecked()
                        }
                        else{

                        }
                    }, colors = CheckboxDefaults.colors(checkedColor = Color(153, 77, 0), uncheckedColor = Color(0, 204, 0)))
                    Column( modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Top) {
                        Text(itemName,)
                        Text(itemDescription)
                    }
                }
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
                    fontSize = 16.sp
                )

                OutlinedTextField(modifier = Modifier.padding(8.dp),
                    value = taskTitle, onValueChange = {
                        taskTitle = it
                    },
                    placeholder = { Text(text = "e.g. Feed my cat.") })
                Text(
                    text = "Enter the task description",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 16.sp
                )
                OutlinedTextField(modifier = Modifier.padding(8.dp),
                    value = taskDescription, onValueChange = {
                        taskDescription = it
                    },
                    placeholder = { Text(text = "e.g. Describe your task here.") })
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
    //implementation by Marc
    //edited by: Anjo
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

