package com.example.android_blueprint.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.android_blueprint.ui.theme.ActiveColor
import com.example.android_blueprint.ui.theme.InactiveBreakpointColor
import com.example.android_blueprint.view.defaultBranch

class BlockViewModel : ViewModel() {

    var offsetX by mutableStateOf(0f)
    var offsetY by mutableStateOf(0f)
    var boxHeight by mutableStateOf(0f)
    var boxWidth by mutableStateOf(0f)
    var color by mutableStateOf(Color.LightGray.copy(alpha = 0.5f))
    var outputBranch = defaultBranch
    var inputBranch = defaultBranch
    var inputBranchForEndIf = defaultBranch
    var outputBranchTrue = defaultBranch
    var outputBranchFalse = defaultBranch
    var inputSupportFLow = defaultBranch
    var outputSupportFLow = defaultBranch
    var inputSupportFLowLeft = defaultBranch
    var inputSupportFLowRight = defaultBranch

    fun changeBreakPointColor() {
        color = if (color == InactiveBreakpointColor) {
            ActiveColor
        } else {
            InactiveBreakpointColor
        }
    }
}