package com.example.composenavigation


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.example.composenavigation.presentation.ControlViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ControlScreen(
    navController: NavController,
    viewModel: ControlViewModel = viewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val controllerData = viewModel.controllerData.value



    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver{ _, event ->
                if (event == Lifecycle.Event.ON_RESUME){
                    println("Start read values")
                    viewModel.startReadingSensorsValues()
                }
                else if (event == Lifecycle.Event.ON_PAUSE){
                    println("Pause read values")
                    viewModel.stopReadingSensorsValues()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Control Screen")
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("preferencescreen")
                    }) {
                        Icon(Icons.Filled.Settings, contentDescription = "")
                    }
                },
                backgroundColor = Color.White
            )
        },

    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (controlBlock, viewBlock) = createRefs()
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(viewBlock) {
                        top.linkTo(parent.top)
                        bottom.linkTo(controlBlock.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ){
                val (text1, text2, tankPressure) = createRefs()
                AirBagComposable(
                    text = controllerData.pressure1,
                    modifier = Modifier
                        .constrainAs(text1){
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(text2.start, margin = 5.dp)
                        },
                )
                AirBagComposable(
                    text = controllerData.pressure2,
                    modifier = Modifier
                        .constrainAs(text2){
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(text1.end, margin = 5.dp)
                            end.linkTo(parent.end)
                        },
                )
                AirTankComposable(
                    modifier = Modifier
                        .constrainAs(tankPressure){
                            top.linkTo(text1.bottom, margin = 20.dp)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
            }
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(controlBlock) {
                        bottom.linkTo(parent.bottom, margin = 30.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ){
                val (block1, block2, block3) = createRefs()

                ControlBlock(
                    modifier = Modifier
                        .constrainAs(block1){
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            //start.linkTo(parent.start)
                            end.linkTo(block2.start, margin = 15.dp)
                        },
                    buttonWidth = 100.dp,
                    spacerHeight = 15.dp,
                    backgroundColor=Color.Cyan,
                    onUpPressed = {
                        viewModel.writeOutputCharacteristic("0001")
                    },
                    onDownPressed = {
                        viewModel.writeOutputCharacteristic("0010")
                    },
                    onUpDownReleased =  {
                        viewModel.writeOutputCharacteristic("0000")
                    },
                )

                ControlBlock(
                    modifier = Modifier
                        .constrainAs(block2){
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    buttonWidth = 110.dp,
                    spacerHeight = 15.dp,
                    backgroundColor=Color.Cyan,
                    onUpPressed = {
                        viewModel.writeOutputCharacteristic("0101")
                    },
                    onDownPressed = {
                        viewModel.writeOutputCharacteristic("1010")
                    },
                    onUpDownReleased =  {
                        viewModel.writeOutputCharacteristic("0000")
                    },
                )
                val v = LocalView.current
                ControlBlock(
                    modifier = Modifier
                        .constrainAs(block3){
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(block2.end, margin = 15.dp)
                            //end.linkTo(parent.end)
                        },
                    buttonWidth = 100.dp,
                    spacerHeight = 15.dp,
                    backgroundColor=Color.Cyan,
                    onUpPressed = {
                        viewModel.writeOutputCharacteristic("0100")
                    },
                    onDownPressed = {
                        viewModel.writeOutputCharacteristic("1000")
                    },
                    onUpDownReleased =  {
                        viewModel.writeOutputCharacteristic("0000")
                    },
                )
            }
        }

    }
}