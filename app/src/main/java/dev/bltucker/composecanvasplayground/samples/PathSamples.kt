package dev.bltucker.composecanvasplayground.samples

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
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