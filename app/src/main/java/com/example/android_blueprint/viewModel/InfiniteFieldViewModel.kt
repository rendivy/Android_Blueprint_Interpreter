package com.example.android_blueprint.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel

class InfiniteFieldViewModel : ViewModel() {
    val blocks by mutableStateOf(mutableListOf<Blocks>())

    fun addBlock(height: Dp, color: Color) {
        blocks.add(Blocks(height = height, color = color))
    }
}