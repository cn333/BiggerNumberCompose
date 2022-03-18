package com.example.biggernumbercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BiggerNumber()
        }
    }
}

class BiggerNumberViewModel: ViewModel() {
    private var _leftNum = 0
    private var _rightNum = 0
    private val _points = MutableLiveData(0)

    init {
        assignRandomNumbers()
    }

    val leftNum: Int
        get() = _leftNum

    val rightNum: Int
        get() = _rightNum

    val points: LiveData<Int>
        get() = _points

    fun assignRandomNumbers() {
        _leftNum = Random.nextInt(10)
        _rightNum = _leftNum
        while (_rightNum == _leftNum)
            _rightNum = Random.nextInt(10)
    }

    fun checkAnswer(leftNumSelected: Boolean): Boolean {
        val isAnswerCorrect = if (leftNumSelected) _leftNum > _rightNum else _rightNum > _leftNum
        _points.value?.let {
            _points.value = if (isAnswerCorrect) it.plus(1) else it.minus(1)
        }
        return isAnswerCorrect
    }
}

@Composable
fun BiggerNumber() {
    val viewModel: BiggerNumberViewModel = viewModel()
    val points = viewModel.points.observeAsState()
    var isCorrect by remember { mutableStateOf(true) }

    val checkAnswer = { isLeftButtonSelected: Boolean ->
        isCorrect = viewModel.checkAnswer(isLeftButtonSelected)
        viewModel.assignRandomNumbers()
    }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = if (isCorrect) Color.Green else Color.Red),
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
            NumberButton(number = viewModel.leftNum, onClick = { checkAnswer(true) })
            NumberButton(number = viewModel.rightNum, onClick = { checkAnswer(false) })
        }

        Text(
            text = "Points: ${points.value}",
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