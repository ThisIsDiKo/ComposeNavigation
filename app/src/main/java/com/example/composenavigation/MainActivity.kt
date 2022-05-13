package com.example.composenavigation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.dataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composenavigation.data.AppSettingsSerializer
import com.example.composenavigation.feature_control.presenter.SimpleScreen
import com.example.composenavigation.feature_control.presenter.scan.ScanScreen
import com.example.composenavigation.presentation.BleManager
import com.example.composenavigation.ui.theme.ComposeNavigationTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

val Context.dataStore by dataStore("app-settings-controller.json", AppSettingsSerializer)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //BleManager.getInstance(applicationContext)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        setContent {
            ComposeNavigationTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = MaterialTheme.colors.isLight

                SideEffect {
                    // Update all of the system bar colors to be transparent, and use
                    // dark icons if we're in light theme
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = useDarkIcons
                    )

                    // setStatusBarsColor() and setNavigationBarColor() also exist
                }
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "scanscreen") {
                    composable("startscreen") { StartScreen(navController = navController, context = applicationContext) }
                    composable("preferencescreen") { PreferenceScreen(navController = navController, context = applicationContext) }
                    composable("controlscreen") { ControlScreen(navController)}
                    composable("simplescreen"){ SimpleScreen()}
                    composable("scanscreen"){ScanScreen()}
                }
            }
        }
    }
}