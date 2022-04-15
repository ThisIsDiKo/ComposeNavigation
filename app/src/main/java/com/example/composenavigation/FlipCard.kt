package com.example.composenavigation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun FlipCardComposable(){
    var cardFace by remember {
        mutableStateOf(CardFace.Front)
    }

    FlipCard(
        cardFace = cardFace,
        onClick = { cardFace = cardFace.next},
        modifier = Modifier
            .fillMaxWidth(.5f)
            .aspectRatio(1f),
        front = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Front",
                    style = MaterialTheme.typography.h3,
                )
            }
        },
        back = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Blue),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Back",
                    style = MaterialTheme.typography.h3,
                )
            }
        },
    )
}

enum class CardFace(val angle: Float){
    Front(0f){
        override val next: CardFace
            get() = Back
    },
    Back(180f){
        override val next: CardFace
            get() = Front
    };

    abstract val next: CardFace
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FlipCard(
    cardFace: CardFace,
    onClick: (CardFace) -> Unit,
    modifier: Modifier = Modifier,
    back: @Composable () -> Unit = {},
    front: @Composable () -> Unit = {},
){
    val rotation = animateFloatAsState(
        targetValue = cardFace.angle,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        )
    )
    Card(
        onClick = {onClick(cardFace)},
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 12f * density
            }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            if (rotation.value <= 90f){
                Box(
                    modifier = Modifier.fillMaxSize()
                ){
                    front()
                }

            }
            else {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .graphicsLayer {
                            rotationY = 180f
                        }
                ){
                    back()
                }
            }
        }
    }
}