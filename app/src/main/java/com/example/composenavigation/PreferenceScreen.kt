package com.example.composenavigation

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.composenavigation.data.AppSettings
import com.example.composenavigation.data.PressureSensorType
import com.example.composenavigation.data.PressureUnits
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PreferenceScreen(
    navController: NavController,
    context: Context
){
    val myScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Text(text = "Preference Screen")
                },
                backgroundColor = Color.White
            )
        },
    ){
        val expandedSens = remember { mutableStateOf(false) }
        val expandedUnits = remember { mutableStateOf(false) }

        val itemsSens = listOf("China", "Original")
        val itemsUnits = listOf("bar", "psi")

        val selectedSens = remember { mutableStateOf(itemsSens[0]) }
        val selectedUnit = remember { mutableStateOf(itemsUnits[0]) }
        val appSettings = context.dataStore.data.collectAsState(initial = AppSettings()).value

        val pressureTankSwitchChecked = remember { mutableStateOf(false)}

        val showDialog = remember { mutableStateOf(false)}


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ExposedDropdownMenuBox(
                expanded = expandedSens.value,
                onExpandedChange = {
                    expandedSens.value = !expandedSens.value
                }
            ) {
                TextField(
                    readOnly = true,
                    value = if(appSettings.pressureSensType == PressureSensorType.CATLEPILLAR_0_14) "Original" else "China",
                    onValueChange = { },
                    label = { Text("Pressure sensor model") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expandedSens.value
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expandedSens.value,
                    onDismissRequest = {
                        expandedSens.value = false
                    }
                ){
                    itemsSens.forEach{item ->
                        DropdownMenuItem(
                            onClick = {
                                println("selected item: $item")
//                                selectedSens.value = item
                                expandedSens.value = false

                                myScope.launch {
                                    if (item == "China"){
                                        context.dataStore.updateData {
                                            it.copy(pressureSensType = PressureSensorType.CHINA_0_20)
                                        }
                                    }
                                    else if (item == "Original"){
                                        context.dataStore.updateData {
                                            it.copy(pressureSensType = PressureSensorType.CATLEPILLAR_0_14)
                                        }
                                    }
                                }

                            }
                        ) {
                            Text(text = item)
                        }
                    }
                }

            }

            Spacer(modifier = Modifier.height(10.dp))

            ExposedDropdownMenuBox(
                expanded = expandedUnits.value,
                onExpandedChange = {
                    expandedUnits.value = !expandedUnits.value
                }
            ) {
                TextField(
                    readOnly = true,
                    value = if(appSettings.pressureUnits == PressureUnits.BAR) "bar" else "psi",
                    onValueChange = { },
                    label = { Text("Pressure Units") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expandedUnits.value
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expandedUnits.value,
                    onDismissRequest = {
                        expandedUnits.value = false
                    }
                ){
                    itemsUnits.forEach{item ->
                        DropdownMenuItem(
                            onClick = {
                                println("selected item: $item")
                                //selectedUnit.value = item
                                expandedUnits.value = false

                                myScope.launch {
                                    if (item == "bar"){
                                        context.dataStore.updateData {
                                            it.copy(pressureUnits = PressureUnits.BAR)
                                        }
                                    }
                                    else if (item == "psi"){
                                        context.dataStore.updateData {
                                            it.copy(pressureUnits = PressureUnits.PSI)
                                        }
                                    }
                                }
                            }
                        ) {
                            Text(text = item)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Text(text = "Show pressure in tank")
                Spacer(modifier=Modifier.width(10.dp))
                Switch(
                    checked = appSettings.useTank,
                    onCheckedChange = {isChecked ->
                        //pressureTankSwitchChecked.value = isChecked

                        myScope.launch {
                            context.dataStore.updateData {
                                it.copy(useTank = isChecked)
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    navController.navigateUp()
                }
            ) {
                Text(text = "Save and return")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    //navController.navigate("startscreen")
                    showDialog.value = true
                }
            ) {
                Text(text = "Remove device from memory")
            }

            if (showDialog.value){
                AlertDialog(
                    onDismissRequest = {
                        showDialog.value = false
                    },
                    title = {
                        Text(text = "Delete device")
                    },
                    text = {
                        Text(text = "Are you sure to remove device?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                myScope.launch {
                                    context.dataStore.updateData {
                                        it.copy(deviceAddress = "00:00:00:00:00:00")
                                    }
                                }
                                showDialog.value = false
                                navController.navigate("startscreen"){
                                    popUpTo("startscreen"){
                                        inclusive = true
                                    }
                                }
                            }
                        ) {
                            Text(text = "Delete")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showDialog.value = false
                            }
                        ) {
                            Text(text = "Cancel")
                        }
                    }

                )
            }
        }
    }

}

@Preview
@Composable
private fun PreviewPreferenceScreen(){
    PreferenceScreen(navController = rememberNavController(), context = LocalContext.current)
}