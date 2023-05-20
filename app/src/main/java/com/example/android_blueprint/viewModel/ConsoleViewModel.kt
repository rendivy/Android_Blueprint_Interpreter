package com.example.android_blueprint.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ConsoleViewModel: ViewModel() {
    companion object {
        var consoleText by mutableStateOf("")
    }
}