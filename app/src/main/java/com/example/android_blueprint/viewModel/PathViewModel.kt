package com.example.android_blueprint.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel
import com.example.android_blueprint.model.PathModel

class PathViewModel: ViewModel() {
    companion object{
        var pathData = mutableStateMapOf<Int, Path>()
        var pathHashMap = mutableStateMapOf<Int, PathModel>()
        var isConnectorClicked: MutableState<Boolean> = mutableStateOf(false)
        var buttonPressedBlockId: Int = 0
    }
}