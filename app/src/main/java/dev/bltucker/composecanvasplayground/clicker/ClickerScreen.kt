package dev.bltucker.composecanvasplayground.clicker

import android.os.CountDownTimer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random


@Composable
fun ClickerScreen(modifier: Modifier = Modifier, onExitClicked: () -> Unit) {
    var points by remember {
        mutableIntStateOf(0)
    }

    var isTimerRunning by remember{
        mutableStateOf(false)
    }

    Column(modifier = modifier.windowInsetsPadding(WindowInsets.systemBars)){

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween){

            Row{
                Button(onClick = {
                    onExitClicked()
                }){
                    Text(text = "Exit")
                }

                Spacer(Modifier.width(4.dp))

                Button(onClick = {
                    //reset button
                    isTimerRunning = !isTimerRunning
                    points = 0
                }){
                    Text(text = if(isTimerRunning) "Reset" else "Start")
                }
            }


            Text(text = "Points: $points", fontSize = 20.sp, fontWeight = FontWeight.Bold)


            CountDownTimer(isTimerRunning = isTimerRunning){
                isTimerRunning = false
            }

        }


        Clicker(modifier = Modifier.fillMaxSize(),
            enabled = isTimerRunning,
            onClick = {
                points++
            }
        )


    }
}


@Composable
fun CountDownTimer(modifier: Modifier = Modifier,
                   time: Int = 30_000,
                   isTimerRunning: Boolean = false,
                   onTimerFinished: () -> Unit = {}) {
    var currentTime by remember{
        mutableIntStateOf(time)
    }


    LaunchedEffect(key1 = currentTime, key2= isTimerRunning){
        if(!isTimerRunning){
        //reset if not running
            currentTime = time
            return@LaunchedEffect
        }

        if(currentTime > 0){
            delay(1_000)
            //delay first because changing currentTime will cancel and relaunch
            currentTime -= 1000
        } else {
            onTimerFinished()
        }
    }


    Text(modifier = modifier, text =(currentTime / 1000).toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold)

}


@Composable
fun Clicker(modifier: Modifier = Modifier,
            radius: Float = 100f,
            enabled: Boolean = false,
            color: Color = Color.Blue,
            onClick: () -> Unit = {}) {

    BoxWithConstraints(modifier = modifier) {
        //box with constraints helps us get the offset we need
        var ballPosition by remember { mutableStateOf(getRandomOffset(radius = radius,
            width = constraints.maxWidth,
            height = constraints.maxHeight))
        }

        Canvas(modifier = Modifier.fillMaxSize()
            .pointerInput(enabled) {
                if(!enabled) return@pointerInput

                detectTapGestures {
                    val distance = kotlin.math.sqrt(
                        (it.x - ballPosition.x).pow(2) + (it.y - ballPosition.y).pow(2)
                    )

                    if(distance <= radius){
                        onClick()
                        ballPosition = getRandomOffset(radius = radius,
                            width = constraints.maxWidth,
                            height = constraints.maxHeight)
                    }
                }
            }

        ) {
            drawCircle(
                color = color,
                radius = radius,
                center = ballPosition
            )
        }
    }

}


private fun getRandomOffset(radius: Float, width: Int, height: Int): Offset{
    return Offset(
        x = Random.nextInt(radius.roundToInt(), width - radius.roundToInt()).toFloat(),
        y = Random.nextInt(radius.roundToInt(), height - radius.roundToInt()).toFloat()
    )
}