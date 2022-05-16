package com.example.composenavigation.feature_control.presenter.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ScanScreen(
    navController: NavController,
    viewModel: ScanScreenViewModel = hiltViewModel()
){
    val isScanning = remember {
        mutableStateOf(false)
    }

    val scaffoldState = rememberScaffoldState()

    val blePeripherals = viewModel.bleDevices
    val showConnectionDialog = viewModel.showConnectionDialogState

    LaunchedEffect(key1 = true){
        viewModel.readApplicationSettings()
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is ScanScreenViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is ScanScreenViewModel.UiEvent.NavigateTo -> {
                    navController.navigate(event.destination){
                        popUpTo("scanscreen"){
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        if (showConnectionDialog.value){
            Dialog(
                onDismissRequest = { /*TODO*/ },
                DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                Box(
                    contentAlignment= Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .background(White, shape = RoundedCornerShape(12.dp))
                ) {
                    Column {
                        CircularProgressIndicator(modifier = Modifier.padding(6.dp, 0.dp, 0.dp, 0.dp))
                        Text(text = "Connecting...", Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp))
                    }
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                if (isScanning.value){
                    viewModel.stopScanning()
                }
                else {
                    viewModel.startScanning()
                }
                isScanning.value = !isScanning.value
            }) {
                Text(
                    text = if (isScanning.value) "Stop scan" else "Start scan"
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                viewModel.disconnectFromPeripheral()
            }) {
                Text(
                    text = "Disconnect"
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ){
                items(items = blePeripherals){ device ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(5.dp)
                            .border(
                                width = 2.dp,
                                color = Color.Red
                            )
                            .clickable {
                                viewModel.deviceSelected(device)
                            }

                    ){
                        Text(text = device.MAC)
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = device.name)
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = "${device.rssi}")
                    }
                }
            }
        }
    }
}