package dev.bltucker.composecanvasplayground.samples

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun SquarePath(){
    Canvas(modifier = Modifier.size(400.dp, 400.dp)){
        val path = Path().apply{
            moveTo(100f, 100f)
            lineTo(100f, 500f)
            lineTo(500f, 500f)
            lineTo(500f, 100f)
            close()
        }

        drawPath(path = path,
            color = Color.Blue,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

@Preview
@Composable
fun QuadraticBezierCurves(){
    Canvas(modifier = Modifier.size(400.dp, 400.dp)){
        val path = Path().apply{
            moveTo(100f, 100f)
            lineTo(100f, 500f)
            lineTo(500f, 500f)
            quadraticBezierTo(800f, 300f, 500f, 100f)
            close()
        }

        drawPath(path = path,
            color = Color.Blue,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

@Preview
@Composable
fun CubicBezierCurves(){
    Canvas(modifier = Modifier.size(400.dp, 400.dp)){
        val path = Path().apply{
            moveTo(100f, 100f)
            lineTo(100f, 500f)
            lineTo(500f, 500f)
            cubicTo(800f, 500f, 800f, 100f, 500f, 100f)
            close()
        }

        drawPath(path = path,
            color = Color.Blue,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}


@Preview
@Composable
fun PathOperation() {
    var pathOperation by remember { mutableStateOf(PathOperation.Xor) }
    var squareStyle by remember { mutableStateOf<DrawStyle>(Stroke(width = 2f)) }
    var circleStyle by remember { mutableStateOf<DrawStyle>(Stroke(width = 2f)) }
    var operationStyle by remember { mutableStateOf<DrawStyle>(Stroke(width = 2f)) }

    val density = LocalDensity.current

    val transition = rememberInfiniteTransition(label = "")
    val animationProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1_000),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    LaunchedEffect(animationProgress) {
        if (animationProgress == 0f) {
            pathOperation = getRandomPathOperation()
            squareStyle = if (Math.random() < 0.5) Stroke(width = with(density) { 2.dp.toPx() }) else Fill
            circleStyle = if (Math.random() < 0.5) Stroke(width = with(density) { 2.dp.toPx() }) else Fill
            operationStyle = if (Math.random() < 0.5) Stroke(width = with(density) { 2.dp.toPx() }) else Fill
        }
    }

    Canvas(modifier = Modifier.size(400.dp, 400.dp)) {

        val squareWithoutOp = Path().apply {
            addRect(Rect(Offset(200f, 200f), Size(200f, 200f)))
        }
        val circle = Path().apply {
            addOval(Rect(Offset(200f, 200f), 100f))
        }
        val pathWithOp = Path().apply {
            op(squareWithoutOp, circle, pathOperation)
        }
        drawPath(
            path = squareWithoutOp,
            color = Color.Red,
            style = squareStyle
        )
        drawPath(
            path = circle,
            color = Color.Blue,
            style = circleStyle
        )
        drawPath(
            path = pathWithOp,
            color = Color.Green,
            style = operationStyle

        )

    }
}

private fun getRandomPathOperation(): PathOperation {
    val operations = listOf(
        PathOperation.Difference,
        PathOperation.Intersect,
        PathOperation.Union,
        PathOperation.Xor,
        PathOperation.ReverseDifference
    )
    return operations.random()
}


@Preview
@Composable
fun AnimatedPath(modifier: Modifier = Modifier) {

    val pathPortion = remember {
        Animatable(initialValue = 0f)
    }
    LaunchedEffect(key1 = true) {
        pathPortion.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 2500
            )
        )
    }
    val path = Path().apply {
        moveTo(100f, 100f)
        quadraticBezierTo(400f, 400f, 100f, 400f)
    }
    val outPath = Path()
    //creates a new path from an existing path
        //from just a portion of the original path
    PathMeasure().apply {
        setPath(path, false)
        getSegment(0f, pathPortion.value * length, outPath, true)
    }

    Canvas(modifier = Modifier.size(400.dp, 400.dp)){
        drawPath(
            path = outPath,
            color = Color.Red,
            style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}