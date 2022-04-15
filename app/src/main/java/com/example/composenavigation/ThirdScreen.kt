package com.example.composenavigation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ThirdScreen(navController: NavController){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "This is third screen")
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                Log.w("HELLO", "backstack: ${navController.previousBackStackEntry}")
                navController.navigate("topappbarscreen")
            }
        ) {
            Text(text = "Go to next screen")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                navController.navigate("preferencescreen")
            }
        ) {
            Text(text = "Go to preference screen")
        }
    }
}