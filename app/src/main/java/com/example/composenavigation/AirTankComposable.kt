package com.example.composenavigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AirTankComposable(
    modifier: Modifier
){
    Text(
        text = "10.0",
        style = TextStyle(
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
        ),
        textAlign = TextAlign.Center,
        modifier = modifier
            .wrapContentSize()
            .graphicsLayer {
                shadowElevation = 8.dp.toPx()
                shape = AirTankShape()
                clip = true
            }
            .background(color = Color.Gray)
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = AirTankShape()
            )
            .padding(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 10.dp)
    )
}

class AirTankShape() : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            // Draw your custom path here
            path = drawAirTankPath(size = size)
        )
    }
}


fun drawAirTankPath(size: Size): Path {
    val radius = size.height / 2.0f

    return Path().apply {
        reset()

        arcTo(
            rect = Rect(
                left = 0f,
                top = 0f,
                right = 2f * radius,
                bottom = size.height
            ),
            startAngleDegrees = 90.0f,
            sweepAngleDegrees = 180.0f,
            forceMoveTo = false
        )
        lineTo(x = size.width-radius, y = 0f)

        arcTo(
            rect = Rect(
                left = size.width-2f*radius,
                top = 0f,
                right = size.width,
                bottom = size.height
            ),//.translate(Offset(0f, radius)),
            startAngleDegrees = -90.0f,
            sweepAngleDegrees = 180.0f,
            forceMoveTo = false
        )
        lineTo(x = size.width-radius, y = size.height)

        close()
    }
}