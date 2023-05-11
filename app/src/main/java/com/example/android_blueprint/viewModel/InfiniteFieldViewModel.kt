package com.example.android_blueprint.viewModel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.example.android_blueprint.model.Transform
import com.example.android_blueprint.ui.theme.InitialOffset
import com.example.android_blueprint.ui.theme.InitialScale

class InfiniteFieldViewModel : ViewModel() {
    val blocks = mutableStateListOf<Any>()
    var transform by mutableStateOf(Transform(InitialScale, InitialOffset))
    fun addBlock(blockValue: Any) {
        blocks.add(blockValue)
    }

    fun changeTransform(zoomChange: Float, offsetChange: Offset) {
        transform = transform.copy(
            scale = transform.scale * zoomChange,
            offset = transform.offset + offsetChange
        )
    }

    fun addVariable(list: List<String>): List<String> {
        return list + listOf("")
    }

    fun removeAtIndex(list: List<String>, indexToRemove: Int): List<String> {
        return list.filterIndexed { index, _ -> index != indexToRemove }
    }

    fun valueChange(list: List<String>, index: Int, value: String): List<String> {
        return list.mapIndexed { i, old -> if (i == index) value else old }
    }

}