package dev.bltucker.composecanvasplayground.weightpicker

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.withRotation
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.roundToInt

data class ScaleStyle(
    val scaleWidth: Dp = 100.dp,
    val radius: Dp = 550.dp,
    val singleStepLineColor: Color = Color.LightGray,
    val fiveStepLineColor: Color = Color.Green,
    val tenStepLineColor: Color = Color.Black,
    val singleLineLength: Dp = 15.dp,
    val fiveLineLength: Dp = 25.dp,
    val tenLineLength: Dp = 35.dp,
    val scaleIndicatorColor: Color = Color.Green,
    val indicatorLineLength: Dp = 60.dp,
    val textSize: TextUnit = 18.sp
    )

@Composable
fun Scale(modifier: Modifier = Modifier,
          style: ScaleStyle = ScaleStyle(),
          minWeight: Int = 20,
          maxWeight: Int = 250,
          initialWeight: Int = 80,
          onWeightChange: (Int) -> Unit = {}

) {
    val radius = style.radius
    val scaleWidth = style.scaleWidth


    var center by remember{
        mutableStateOf(Offset.Zero)
    }

    var circleCenter by remember{
        mutableStateOf(Offset.Zero)
    }

    var currentAngle by remember{
        mutableFloatStateOf(0f)
    }

    var dragStartedAngle by remember{
        mutableFloatStateOf(0f)
    }

    var oldAngle by remember{
        mutableFloatStateOf(currentAngle)
    }

    Canvas(modifier = modifier
        .pointerInput(true) {
            detectDragGestures(
                onDragStart = { offset ->
                    dragStartedAngle = -atan2(
                         center.x - offset.x,
                         center.y - offset.y
                    ) * (180f / PI.toFloat())
                },
                onDragEnd = {
                    oldAngle = currentAngle
                }
            ) { change, _ ->

                val touchAngle = -atan2(
                    center.x - change.position.x,
                    center.y - change.position.y
                ) * (180f / PI.toFloat())


                val newAngle = oldAngle + (touchAngle - dragStartedAngle)

                currentAngle = newAngle.coerceIn(
                    minimumValue = initialWeight.toFloat() - maxWeight.toFloat(),
                    maximumValue = initialWeight.toFloat() - minWeight.toFloat()
                )

                onWeightChange((initialWeight - currentAngle).roundToInt())
            }
        }){
        center = this.center
        circleCenter = Offset(center.x, scaleWidth.toPx() / 2f + radius.toPx())

        val outterRadius = radius.toPx() + (scaleWidth.toPx() / 2f)
        val innerRadius = radius.toPx() - (scaleWidth.toPx() / 2f)


        //need native canvas to draw shadows
        drawContext.canvas.nativeCanvas.apply{
            drawCircle(
                circleCenter.x,
                circleCenter.y,
                radius.toPx(),
                Paint().apply{
                    strokeWidth = scaleWidth.toPx()
                    color = android.graphics.Color.WHITE
                    setStyle(Paint.Style.STROKE)
                    setShadowLayer(
                        60f,
                        0f,
                        0f,
                        android.graphics.Color.argb(50, 0, 0, 0)
                    )
                },
            )
        }


        //draw scale lines
        for(i in minWeight..maxWeight){
            val angleInRadians = (i - initialWeight + currentAngle - 90) * (Math.PI / 180f).toFloat()

            val (lineColor, lineLength) = when{
                i % 10 == 0 -> style.tenStepLineColor to style.tenLineLength
                i % 5 == 0 -> style.fiveStepLineColor to style.fiveLineLength
                else -> style.singleStepLineColor to style.singleLineLength
            }

            val lineStart = Offset(
                x = (outterRadius - lineLength.toPx()) * kotlin.math.cos(angleInRadians) + circleCenter.x,
                y = (outterRadius - lineLength.toPx()) * kotlin.math.sin(angleInRadians) + circleCenter.y
            )

            val lineEnd = Offset(
                x = outterRadius * kotlin.math.cos(angleInRadians) + circleCenter.x,
                y = outterRadius  * kotlin.math.sin(angleInRadians) + circleCenter.y
            )

            drawContext.canvas.nativeCanvas.apply{
                if(i % 10 == 0){
                    val textX = (outterRadius - lineLength.toPx() - 5.dp.toPx() - style.textSize.toPx()) * kotlin.math.cos(angleInRadians) + circleCenter.x
                    val textY = (outterRadius - lineLength.toPx() - 5.dp.toPx() - style.textSize.toPx()) * kotlin.math.sin(angleInRadians) + circleCenter.y

                    withRotation(degrees = angleInRadians * (180f / Math.PI.toFloat()) + 90f,
                        pivotX = textX,
                        pivotY = textY) {
                        drawText(
                            abs(i).toString(),
                            textX,
                            textY,
                            Paint().apply{
                                textSize = style.textSize.toPx()
                                textAlign = Paint.Align.CENTER
                                color = android.graphics.Color.BLACK
                            }
                        )
                    }


                }
            }


            drawLine(
                color = lineColor,
                start = lineStart,
                end = lineEnd,
                strokeWidth = 1.dp.toPx()
            )

            val topMiddle = Offset(
                x = circleCenter.x,
                y = circleCenter.y - innerRadius + 16.dp.toPx() - style.indicatorLineLength.toPx()
            )

            val bottomLeft = Offset(
                x = circleCenter.x - 4f,
                y = circleCenter.y - innerRadius
            )

            val bottomRight = Offset(
                x = circleCenter.x + 4f,
                y = circleCenter.y - innerRadius
            )

            val indicator = Path().apply{
                moveTo(topMiddle.x, topMiddle.y)
                lineTo(bottomLeft.x, bottomLeft.y)
                lineTo(bottomRight.x, bottomRight.y)
                lineTo(topMiddle.x, topMiddle.y)
            }

            drawPath(indicator,
                color = style.scaleIndicatorColor)

        }




    }

}


@Preview(showBackground = true)
@Composable
private fun ScalePreview(){
    Box(modifier = Modifier.fillMaxSize()){
        Scale(Modifier.fillMaxWidth().height(300.dp).align(alignment = Alignment.BottomCenter),
            style = ScaleStyle(scaleWidth = 150.dp))
    }
}