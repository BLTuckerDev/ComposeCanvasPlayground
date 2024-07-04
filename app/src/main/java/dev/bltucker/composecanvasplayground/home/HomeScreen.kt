package dev.bltucker.composecanvasplayground.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.bltucker.composecanvasplayground.common.Screens
import dev.bltucker.composecanvasplayground.common.ui.theme.ComposeCanvasPlaygroundTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier, onScreenSelected: (Screens) -> Unit) {
    Scaffold(modifier = modifier) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center){

            Screens.entries.forEach { screen ->

                Button(modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onScreenSelected(screen)
                    }){
                    Text(text = screen.displayName)
                }


            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview(){
    ComposeCanvasPlaygroundTheme {
        HomeScreen(modifier = Modifier.fillMaxSize(),
            onScreenSelected = { }
        )
    }
}