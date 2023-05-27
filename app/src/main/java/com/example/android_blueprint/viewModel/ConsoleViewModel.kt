package com.example.android_blueprint.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.example.android_blueprint.ui.theme.EMPTY_STRING
import com.example.android_blueprint.ui.theme.PrimaryColor

class ConsoleViewModel : ViewModel() {
    companion object {
        var defaultTextColor = PrimaryColor
        var consoleText by mutableStateOf(EMPTY_STRING)
        var debugText by mutableStateOf(EMPTY_STRING)
    }
}