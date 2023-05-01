package com.example.android_blueprint.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import com.example.android_blueprint.model.Blocks
import com.example.android_blueprint.model.Transform
import com.example.android_blueprint.ui.theme.InitialOffset
import com.example.android_blueprint.ui.theme.InitialScale

class InfiniteFieldViewModel : ViewModel() {
    val blocks by mutableStateOf(mutableListOf<Blocks>())
    var transform by mutableStateOf(Transform(InitialScale, InitialOffset))
    fun addBlock(height: Dp, color: Color) {
        blocks.add(Blocks(height = height, color = color))
    }

    fun changeTransform(zoomChange: Float, offsetChange: Offset) {
        transform = transform.copy(
            scale = transform.scale * zoomChange,
            offset = transform.offset + offsetChange
        )
    }

}