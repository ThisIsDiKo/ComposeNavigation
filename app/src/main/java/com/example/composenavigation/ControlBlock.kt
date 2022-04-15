package com.example.composenavigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ControlBlock(
    modifier: Modifier,
    buttonWidth: Dp,
    spacerHeight: Dp,
    backgroundColor: Color,
    onUpPressed: () -> Unit,
    onDownPressed: () -> Unit,
    onUpDownReleased: () -> Unit
){

    Column(
        modifier = modifier
    ) {
//        Button(
//            onClick = { onUpPressed() },
//            modifier = Modifier
//                .width(buttonWidth)
//                .height(buttonWidth)
//        ) {
//            Text(text = "Up")
//        }

        CustomIconButton(
            modifier = Modifier
                .height(buttonWidth)
                .width(buttonWidth)
                .border(
                    width = 2.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(5.dp)
                ),
            icon = Icons.Default.KeyboardArrowUp,
            onPress = {
                onUpPressed()
            },
            onRelease = {
                onUpDownReleased()
            }
        )

        Spacer(modifier = Modifier.height(spacerHeight))

        CustomIconButton(
            modifier = Modifier
                .height(buttonWidth)
                .width(buttonWidth)
                .border(
                    width = 2.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(5.dp)
                ),
            icon = Icons.Default.KeyboardArrowDown,
            onPress = {
                onDownPressed()
            },
            onRelease = {
                onUpDownReleased()
            }
        )

    }
}