package com.example.android_blueprint.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import com.example.android_blueprint.model.Blocks
import com.example.android_blueprint.model.Transform
import com.example.android_blueprint.ui.theme.InitialOffset
import com.example.android_blueprint.ui.theme.InitialScale

class InfiniteFieldViewModel : ViewModel() {
    val blocks by mutableStateOf(mutableListOf<Blocks>())
    val transform by mutableStateOf(Transform(InitialScale, InitialOffset))
    fun addBlock(height: Dp, color: Color) {
        blocks.add(Blocks(height = height, color = color))
    }
}