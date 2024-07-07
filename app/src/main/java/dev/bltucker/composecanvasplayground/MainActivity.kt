package dev.bltucker.composecanvasplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.bltucker.composecanvasplayground.clicker.ClickerScreen
import dev.bltucker.composecanvasplayground.clock.ClockScreen
import dev.bltucker.composecanvasplayground.common.Screens
import dev.bltucker.composecanvasplayground.common.ui.theme.ComposeCanvasPlaygroundTheme
import dev.bltucker.composecanvasplayground.home.HomeScreen
import dev.bltucker.composecanvasplayground.weightpicker.WeightPickerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var selectedScreen by rememberSaveable {
                mutableStateOf(Screens.HOME)
            }

            ComposeCanvasPlaygroundTheme {
                when (selectedScreen) {
                    Screens.HOME -> {
                        HomeScreen(
                            modifier = Modifier.fillMaxSize(),
                            onScreenSelected = {
                                selectedScreen = it
                            }
                        )
                    }

                    Screens.WEIGHT_PICKER -> {
                        WeightPickerScreen(modifier = Modifier.fillMaxSize(),
                            onExitClicked = {
                                selectedScreen = Screens.HOME
                            })
                    }
                    Screens.CLOCK -> {

                        ClockScreen(modifier = Modifier.fillMaxSize(),
                            onExitClicked = {
                                selectedScreen = Screens.HOME
                            })
                    }
                    Screens.PICKER -> TODO()
                    Screens.TIC_TAC_TOE -> TODO()
                    Screens.IMAGE_REVEAL -> TODO()
                    Screens.CLICKER -> {
                        ClickerScreen(modifier = Modifier.fillMaxSize(),
                            onExitClicked = {
                                selectedScreen = Screens.HOME
                            }
                        )
                    }
                }
            }
        }
    }
}
