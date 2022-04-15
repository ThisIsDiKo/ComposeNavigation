package com.example.composenavigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SecondScreen(navController: NavController){
    val scaffoldState = rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {Text("Top App Bar")},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions= {
                    IconButton(onClick = {
                        println("hello")
                    }) {
                        Icon(Icons.Filled.Settings, contentDescription = "")
                    }

                },
                backgroundColor = MaterialTheme.colors.primary)
        }
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "This is second screen")
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    navController.navigate("thirdscreen")
                }
            ) {
                Text(text = "Go to next screen")
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    navController.navigate("preferencescreen")
                }
            ) {
                Text(text = "Go to preference screen")
            }
        }
    }
}