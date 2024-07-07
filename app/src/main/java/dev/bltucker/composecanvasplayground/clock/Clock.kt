package dev.bltucker.composecanvasplayground.clock

import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.time.LocalDateTime


@Composable
fun Clock(modifier: Modifier = Modifier) {
    val longTickMarkLengthDp = 30.dp
    val shortTickMarkDp = 20.dp

    val longTickMarkColor = Color.Black
    val shortTickMarkColor = Color.Gray

    val secondHandColor = Color.Red
    val minuteHandColor = Color.Black
    val hourHandColor = Color.Black


    var currentTime by remember {
        mutableStateOf(LocalDateTime.now())
    }

    LaunchedEffect(Unit) {
        while (true){
            currentTime = LocalDateTime.now()
            delay(1_000)
        }
    }

    BoxWithConstraints(modifier = modifier.padding(16.dp)) {
        val widthPx = constraints.maxWidth
        val heightPx = constraints.maxHeight
        val radiusPx = widthPx.toFloat() / 2f

        Canvas(modifier = Modifier.fillMaxSize()){

            val clockCenter = this.center

            //need native canvas to draw shadows
            drawContext.canvas.nativeCanvas.apply{
                drawCircle(
                    clockCenter.x,
                    clockCenter.y,
                    radiusPx,
                    Paint().apply{
                        strokeWidth = 1.dp.toPx()
                        color = android.graphics.Color.WHITE
                        style = Paint.Style.STROKE
                        setShadowLayer(
                            60f,
                            0f,
                            0f,
                            android.graphics.Color.argb(50, 0, 0, 0)
                        )
                    },
                )
            }


            drawCircle(radius =  radiusPx, color = Color.White)


            for(i in 0..360 step 6){
                val tickMarkAngle = (i) * (Math.PI / 180f).toFloat()
//                Log.d("TickAngle", "i = $i, tickMarkAngle = $tickMarkAngle")

                val (lineColor, lineLength) = when{
                    i % 5 == 0 -> {
                        longTickMarkColor to longTickMarkLengthDp
                    }
                    else -> {
                        shortTickMarkColor to shortTickMarkDp
                    }
                }

                val tickMarkStart = Offset(
                    x = (radiusPx - lineLength.toPx()) * kotlin.math.cos(tickMarkAngle) + center.x,
                    y = (radiusPx - lineLength.toPx()) * kotlin.math.sin(tickMarkAngle) + center.y
                )

                val stickMarkEnd = Offset(
                    x = radiusPx * kotlin.math.cos(tickMarkAngle) + center.x,
                    y = radiusPx  * kotlin.math.sin(tickMarkAngle) + center.y
                )

                drawLine(
                    color = lineColor,
                    start = tickMarkStart,
                    end = stickMarkEnd,
                    strokeWidth = 1.dp.toPx()
                )

            }

            /*
            val tickMarkStart = Offset(
                    x = (radiusPx - lineLength.toPx()) * kotlin.math.cos(tickMarkAngle) + center.x,
                    y = (radiusPx - lineLength.toPx()) * kotlin.math.sin(tickMarkAngle) + center.y
                )

                val stickMarkEnd = Offset(
                    x = radiusPx * kotlin.math.cos(tickMarkAngle) + center.x,
                    y = radiusPx  * kotlin.math.sin(tickMarkAngle) + center.y
                )
             */

            val secondHandLineLengthPx = radiusPx - longTickMarkLengthDp.toPx()
            val secondHandAngle = (currentTime.second * 6) * (Math.PI / 180f).toFloat()
            Log.d("SecondAngle", "currentSecond = ${currentTime.second }, secondHandAngle = $secondHandAngle")


            val secondHandStartOffset = Offset(center.x, center.y)
            val secondHandEndOffset = Offset(
                x  = (center.x - secondHandLineLengthPx) * kotlin.math.cos(secondHandAngle) + center.x,
                y = (center.y -  secondHandLineLengthPx) * kotlin.math.sin(secondHandAngle) - center.y
            )

            drawLine(color = secondHandColor,
                start = secondHandStartOffset,
                end = secondHandEndOffset,

                )


            

        }//endCanvas
    }

}


@Preview(showBackground = true)
@Composable
private fun ClockPreview(){
    Clock(modifier = Modifier.fillMaxSize())
}