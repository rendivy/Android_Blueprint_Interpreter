package com.example.android_blueprint.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.android_blueprint.ui.theme.ActiveColor
import com.example.android_blueprint.ui.theme.InactiveBreakpointColor
import com.example.android_blueprint.ui.theme.ZeroOffset


class BlockViewModel : ViewModel() {

    var offsetX by mutableStateOf(ZeroOffset)
    var offsetY by mutableStateOf(ZeroOffset)
    var boxHeight by mutableStateOf(ZeroOffset)
    var boxWidth by mutableStateOf(ZeroOffset)
    var color by mutableStateOf(InactiveBreakpointColor)
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