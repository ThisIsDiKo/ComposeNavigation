package com.example.composenavigation

import android.Manifest
import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.datastore.dataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.example.composenavigation.data.AppSettings
import com.example.composenavigation.presentation.BleManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.welie.blessed.ConnectionState
import kotlinx.coroutines.launch

//DA:3A:0B:BA:D1:CF

@ExperimentalPermissionsApi
@Composable
fun StartScreen(
    context: Context,
    navController: NavController
){

    val macAddress = remember {
        mutableStateOf("DA:3A:0B:BA:D1:CF")
    }
    val appSettings = context.dataStore.data.collectAsState(initial = AppSettings()).value

    //val bleManager = BleManager.getInstance(context)

    val myScope = rememberCoroutineScope()

    val connectEnabled = remember{
        mutableStateOf(false)
    }
    val showPermissionInfoDialog = remember {
        mutableStateOf(false)
    }
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    val lifecycleOwner = LocalLifecycleOwner.current

    val connectionIndicator = remember {
        mutableStateOf(false)
    }


    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver{ _, event ->
                if (event == Lifecycle.Event.ON_RESUME){
                    if (!permissionState.allPermissionsGranted){
                        showPermissionInfoDialog.value = true
                    }
                    else{
                        connectEnabled.value = true
                    }
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if(showPermissionInfoDialog.value){
            if (permissionState.allPermissionsGranted){
                showPermissionInfoDialog.value = false
                connectEnabled.value = true
            }
            AlertDialog(
                onDismissRequest = {
                    permissionState.launchMultiplePermissionRequest()
                    showPermissionInfoDialog.value = false
                },
                title = {
                    Text(text = "Location permission required")
                },
                text = {
                    Text("Starting from Android M (6.0), the system requires apps to be granted" +
                            "location access in order to scan for BLE devices.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            permissionState.launchMultiplePermissionRequest()
                            showPermissionInfoDialog.value = false
                        }) {
                        Text("Ok")
                    }
                }
            )
        }


        Text(text = "This is start screen")
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                myScope.launch {
                    try{
                        println("Trying to connect to ${appSettings.deviceAddress}")
                        BleManager.blePeripheral = BleManager.bleManager?.getPeripheral(appSettings.deviceAddress)
                        if (BleManager.blePeripheral != null){
                            connectionIndicator.value = true
                            BleManager.bleManager?.connectPeripheral(BleManager.blePeripheral!!)
                        }
                        else {
                            println("Myperipheral is null")
                        }
                        if(BleManager.blePeripheral?.getState() == ConnectionState.CONNECTED){
                            println("My peripheral connected")
                            connectionIndicator.value = false
                            navController.navigate("controlscreen")
                        }
                        else{
                            BleManager.blePeripheral = null
                        }
                    }
                    catch (e: Exception){
                        println("Connection failed: ${e.toString()}")
                        connectionIndicator.value = false
                    }

                }
                //navController.navigate("controlscreen")
            },
            enabled = connectEnabled.value
        ) {
            Text(text = "Go to control screen")
        }
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = macAddress.value,
            onValueChange = {
                macAddress.value = it
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                myScope.launch {
                    context.dataStore.updateData {
                        it.copy(deviceAddress = macAddress.value)
                    }
                    println("Mac address updated to ${macAddress.value}")
                }
            }
        ) {
            Text(text = "Save new MAC address")
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                myScope.launch {
                    navController.navigate("preferencescreen")
                }
            }
        ) {
            Text(text = "Go to preference screen")
        }
        Spacer(modifier = Modifier.height(20.dp))

        if (connectionIndicator.value){
            CircularProgressIndicator()
        }

    }
}