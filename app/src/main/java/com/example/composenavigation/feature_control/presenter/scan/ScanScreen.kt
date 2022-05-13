package com.example.composenavigation.feature_control.presenter.scan

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ScanScreen(
    viewModel: ScanScreenViewModel = hiltViewModel()
){
    val isScanning = remember {
        mutableStateOf(false)
    }

    val blePeripherals = viewModel.bleDevices

    Column(
        modifier = Modifier.fillMaxSize()
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

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ){
            items(items = blePeripherals){ device ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .border(
                            width = 2.dp,
                            color = Color.Red
                        )
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