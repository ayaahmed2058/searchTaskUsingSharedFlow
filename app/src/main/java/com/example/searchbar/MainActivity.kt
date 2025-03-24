package com.example.searchbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect as LaunchedEffect1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchScreen()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchScreen() {
    val names = listOf("Aya", "Ahmed", "Mohamed", "ITI", "android", "Smart", "village")
    val searchView = remember { MutableSharedFlow<String>() }
    var userInput = remember { mutableStateOf("") }
    var searchNames =  remember { mutableStateOf(names) }

    LaunchedEffect1(Unit) {
        searchView
            .debounce(100)
            .collect { query ->
                searchNames.value = names.filter { it.startsWith(query, ignoreCase = true) }
            }
    }

    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = userInput.value,
            onValueChange = { name ->
                userInput.value = name
                coroutineScope.launch {
                    searchView.emit(name)
                }
            },
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .size(60.dp)
                .padding(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        searchNames.value.forEach { name ->
            Text(text = name)
        }

    }
}