package dev.bltucker.composecanvasplayground.weightpicker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun WeightPickerScreen(modifier: Modifier = Modifier,
                       onExitClicked: () -> Unit) {
    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){

        val initialWeight = 80
        var weight by remember{
            mutableIntStateOf(initialWeight)
        }

        Button(onClick = {
            onExitClicked()
        }){
            Text(text = "Exit")
        }


        Text(text = "Weight Picker")

        Text(text = "Current Weight: $weight KG")

        Scale(modifier = Modifier.fillMaxWidth().height(300.dp),
            initialWeight = initialWeight,
            onWeightChange = { updatedWeight ->
                weight = updatedWeight
            })
    }

}



