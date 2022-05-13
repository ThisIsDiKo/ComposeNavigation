package com.example.composenavigation.feature_control.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.composenavigation.feature_control.domain.model.OutputsModel
import timber.log.Timber


@Composable
fun SimpleScreen(
    viewModel: SimpleScreenViewModel = hiltViewModel()
){
    val textContent = viewModel.messageContent.value
    val isScanning = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                val outputs = OutputsModel(10)
                outputs.setOutput(0, true)
                outputs.setOutput(2, true)
                outputs.setOutput(4, true)
                outputs.setOutput(6, true)
                outputs.setOutput(8, true)
                outputs.setOutput(9, true)
                val b = outputs.convertOutputsToBytes()
                Timber.e("Got byte array ${b[3].toUByte().toString(2)} ${b[2].toUByte().toString(2)} ${b[1].toUByte().toString(2)} ${b[0].toUByte().toString(2)}")
            }
        ) {
            Text("Click me")
        }
        Button(onClick = {
            viewModel.onConnectClicked()
        }) {
            Text("Connect")
        }
        Button(onClick = {
            viewModel.onDisconnectClicked()
        }) {
            Text("Disconnect")
        }
        Button(onClick = {
            viewModel.onReadClicked()
        }) {
            Text("Read values")
        }
        Button(onClick = {
            viewModel.onWriteClicked()
        }) {
            Text("write values")
        }
        Button(onClick = {
            if (isScanning.value){
                //TODO: stop scan
                viewModel.onStopScanClicked()
            }
            else {
                //TODO: start scan
                viewModel.onStartScanClicked()
            }
            isScanning.value = !isScanning.value
        }) {
            Text(
                text = if (isScanning.value) "Stop scan" else "Start scan"
            )
        }
        Text(text = viewModel.messageContent.value)
    }
}