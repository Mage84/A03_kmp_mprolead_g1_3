package com.amonteiro.a03_kmp_mprolead_g1

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.amonteiro.a03_kmp_mprolead_g1.presentation.AppNavigation
import com.amonteiro.a03_kmp_mprolead_g1.presentation.ui.theme.AppTheme


@Preview
@Composable
fun App() {
    AppTheme {
        AppNavigation()
        //Experience()
//        var showContent by remember { mutableStateOf(false) }
//        Column(
//            modifier = Modifier
//                .background(MaterialTheme.colorScheme.primaryContainer)
//                .safeContentPadding()
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                ) {
//                    Text("Compose: $greeting ")
//                    Text(stringResource(Res.string.my_label))
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Image(painterResource(Res.drawable.error), null)
//                }
//            }
//        }
    }
}

data class Dice(var value: Int = 6) {
    fun roll() {
        value = (1..6).random()
    }
}

@Composable
fun Experience(modifier :Modifier = Modifier) {

    Column(modifier) {

        val diceList = remember { mutableStateOf(listOf(Dice(), Dice(), Dice())) }

        Row { diceList.value.forEach { Text(text = it.value.toString() + " ") } }

        Button(onClick = {
            diceList.value.forEach { it.roll() }
            println(diceList.value)
        }) { Text(text = "forEach") }

        Button(onClick = {
            diceList.value.forEach { it.roll() }
            diceList.value = diceList.value.toList()
            println(diceList.value)
        }) { Text(text = "forEach + toList") }

        Button(onClick = {
            diceList.value = diceList.value.map { it.roll(); it.copy() }
            println(diceList.value)
        }) { Text(text = "map (roll + copy)") }

        Button(onClick = {
            diceList.value = diceList.value.map { it.copy().apply { roll() } }
            println(diceList.value)
        }) { Text(text = "map (copy + roll)") }
    }
}