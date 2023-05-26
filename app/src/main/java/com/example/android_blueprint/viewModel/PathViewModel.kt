package com.example.android_blueprint.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
    var outputBranchTrue = defaultBranch
    var outputBranchFalse = defaultBranch
    var inputSupportFLow = defaultBranch
    var outputSupportFLow = defaultBranch
    var inputSupportFLowLeft = defaultBranch
    var inputSupportFLowRight = defaultBranch

}