package com.example.android_blueprint.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.android_blueprint.view.defaultBranch

class PathViewModel : ViewModel() {

    var offsetX by mutableStateOf(0f)
    var offsetY by mutableStateOf(0f)
    var boxHeight by mutableStateOf(0f)
    var boxWidth by mutableStateOf(0f)
    var outputBranch = defaultBranch
    var inputBranch = defaultBranch


}