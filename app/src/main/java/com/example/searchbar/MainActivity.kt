package com.example.searchbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchScreen()
        }
    }
}

@OptIn(FlowPreview::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchScreen() {
    val names = listOf("Aya", "Ahmed", "Mohamed", "ITI", "android", "Smart", "village")
    val searchView = remember { MutableSharedFlow<String>() }
    var userInput = remember { mutableStateOf(value = "")}
    var searchNames =  remember { mutableStateOf(names) }

    LaunchedEffect(Unit) {
        searchView
            .debounce(100)
            .collect { query ->
                searchNames.value = names.filter { it.contains(query, ignoreCase = true) }
            }
    }

    val coroutineScope = rememberCoroutineScope()
    Column (
        modifier = Modifier.fillMaxSize()
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        TextField(
            value = userInput.value,
            onValueChange = {
                userInput.value = it
                coroutineScope.launch {
                    searchView.emit(it)
                }
            },
            label = { Text(text = "Search") },
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(searchNames.value.size) { index ->
                Text(
                    text = searchNames.value[index],
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
        }

    }
}
