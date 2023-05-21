package com.example.android_blueprint.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.android_blueprint.model.PathModel

class ConsoleViewModel: ViewModel() {
    companion object {
        var consoleText by mutableStateOf("")
    }
}