package com.releaseit.ktorsse.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.releaseit.ktorsse.data.SseDataSource
import com.releaseit.shared_models.sse.SseEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val sseDataSource: SseDataSource by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val scope = rememberCoroutineScope()
                    var items by remember { mutableStateOf(listOf<SseEvent>()) }
                    var error by remember { mutableStateOf("") }

                    LaunchedEffect(true) {
                        scope.launch {
                            try {
                                sseDataSource.observe().collectLatest {
                                    items = items.plus(it)
                                }
                            } catch (e: Exception) {
                                error = e.localizedMessage ?: "error"
                            }
                        }
                    }
                    SseView(items, error)
                }
            }
        }
    }
}

@Composable
fun SseView(sseItems: List<SseEvent>, error: String) {
    if (error.isNotBlank()) {
        Text(text = "Some error happened: $error")
    } else {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(sseItems) {
                Text(text = it.toString())
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        SseView(sseItems = emptyList(), error = "Some error")
    }
}
