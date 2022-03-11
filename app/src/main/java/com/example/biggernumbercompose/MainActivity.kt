package com.example.biggernumbercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BiggerNumber()
        }
    }
}

fun randomPairOfNumbers(): Pair<Int, Int> {
    val r = Random()
    val first = r.nextInt(10)
    var second = first
    while (second == first) {
        second = r.nextInt(10)
    }
    return Pair(first, second)
}

@Composable
fun BiggerNumber() {
    var isCorrect by remember { mutableStateOf(true) }
    var points by remember { mutableStateOf(0) }
    var (leftNum, rightNum) = randomPairOfNumbers()
    val checkAnswer = { isLeftButtonSelected: Boolean ->
        isCorrect = if (isLeftButtonSelected) leftNum > rightNum else rightNum > leftNum
        if (isCorrect) {
            points++
        } else {
            points--
        }
        val pair = randomPairOfNumbers()
        leftNum = pair.first
        rightNum = pair.second
    }
    Column(
        modifier = Modifier.fillMaxHeight().background(color = if (isCorrect) Color.Green else Color.Red ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Press the button with larger number to earn a points",
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NumberButton(number = leftNum, onClick = { checkAnswer(true) })
            NumberButton(number = rightNum, onClick = { checkAnswer(false) })
        }
        Text(
            text = "Points: $points",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}

@Composable
fun NumberButton(number: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick
    ) {
        Text(text = "$number", fontSize = 30.sp)
    }
}