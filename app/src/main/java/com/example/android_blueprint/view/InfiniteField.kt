package com.example.android_blueprint.view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import com.example.android_blueprint.model.BlockValue
import com.example.android_blueprint.model.Transform
import com.example.android_blueprint.ui.theme.BackgroundColor

@Composable
fun InfiniteField(
    blocks: MutableList<Any>,
    transform: Transform,
    changeTransform: (zoomChange: Float, offsetChange: Offset) -> Unit
) {
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        changeTransform(zoomChange, offsetChange)
    }
    Box(
        modifier = Modifier
            .transformable(state = state)
            .background(BackgroundColor)
            .fillMaxSize()
            .graphicsLayer(
                scaleX = transform.scale,
                scaleY = transform.scale,
                translationX = transform.offset.x,
                translationY = transform.offset.y
            )
    )
    {
        Row(modifier = Modifier.align(Alignment.Center)) {
            SetBlock(value = BlockValue.StartBlock)
            SetBlock(value = BlockValue.EndBlock)
        }

        for (block in blocks) {
            SetBlock(value = block)
        }
    }
}


