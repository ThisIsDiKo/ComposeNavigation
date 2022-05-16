package com.example.composenavigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AirBagComposable(
    modifier: Modifier,
    text: String
) {
    Text(
        text = text,
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
                shape = AirBagShape()
                clip = true
            }
            .background(color = Color.Gray)
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = AirBagShape()
            )
            .padding(start = 40.dp, top = 32.dp, end = 40.dp, bottom = 32.dp)
    )
}


class AirBagShape() : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            // Draw your custom path here
            path = drawAirBagPath(size = size)
        )
    }

}

fun drawAirBagPath(size: Size): Path{
    val radius = size.height / 4.0f

    return Path().apply {
        reset()

        arcTo(
            rect = Rect(
                left = -radius,
                top = -radius,
                right = radius,
                bottom = radius
            ).translate(Offset(radius, radius)),
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
                bottom = 2f*radius
            ),//.translate(Offset(0f, radius)),
            startAngleDegrees = -90.0f,
            sweepAngleDegrees = 180.0f,
            forceMoveTo = false
        )
        lineTo(x = size.width-radius, y = 2f * radius)
        arcTo(
            rect = Rect(
                left = size.width-2f*radius,
                top = 2f*radius,
                right = size.width,
                bottom = size.height
            ),//.translate(Offset(0f, radius)),
            startAngleDegrees = -90.0f,
            sweepAngleDegrees = 180.0f,
            forceMoveTo = false
        )
        lineTo(x = radius, y = size.height)

        arcTo(
            rect = Rect(
                left = 0f,
                top = 2f*radius,
                right = 2f*radius,
                bottom = size.height
            ),//.translate(Offset(0f, radius)),
            startAngleDegrees = 90.0f,
            sweepAngleDegrees = 180.0f,
            forceMoveTo = false
        )
        lineTo(x = radius, y = 2f * radius)

        close()
    }
}

@Preview
@Composable
fun AirbagPreview(){
    AirBagComposable(modifier = Modifier.height(40.dp).width(40.dp), text = "10")
}