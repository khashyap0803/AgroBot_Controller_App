package com.example.gemini

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ChatbotScreen(chatbotViewModel: ChatbotViewModel = viewModel()) {
    var userInput by remember { mutableStateOf("") }
    val uiState by chatbotViewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Gemini Chatbot", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Response area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            when (uiState) {
                is UiState.Initial -> {
                    Text("Hi! Ask me anything and I'll try to help.", modifier = Modifier.padding(8.dp))
                }
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is UiState.Success -> {
                    Text(
                        text = (uiState as UiState.Success).outputText,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                is UiState.Error -> {
                    Text(
                        text = "Error: ${(uiState as UiState.Error).errorMessage}",
                        modifier = Modifier.padding(8.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        // Input area
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = userInput,
                onValueChange = { userInput = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type your question here...") },
                singleLine = false
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    chatbotViewModel.sendPrompt(userInput)
                    userInput = ""
                },
                enabled = userInput.isNotBlank()
            ) {
                Text("Send")
            }
        }
    }
}