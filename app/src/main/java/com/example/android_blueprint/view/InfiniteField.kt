package com.example.android_blueprint.view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.example.android_blueprint.model.BlockDimension
import com.example.android_blueprint.model.Transform
import com.example.android_blueprint.ui.theme.BackgroundColor
import com.example.android_blueprint.ui.theme.BlockShape
import com.example.android_blueprint.ui.theme.BlockTextSize
import com.example.android_blueprint.ui.theme.BlockWidth
import com.example.android_blueprint.ui.theme.neueMedium
import kotlin.math.roundToInt

@Composable
fun InfiniteField(
    blocks: MutableList<BlockDimension>,
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
        for (block in blocks) {
            Block(height = block.height, color = block.color, symbol = block.symbol)
        }
    }
}


@Composable
fun Block(height: Dp, color: Color, symbol: String) {
    var offsetX by rememberSaveable { mutableStateOf(0f) }
    var offsetY by rememberSaveable { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .clip(BlockShape)
            .background(color)
            .height(height)
            .width(BlockWidth)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
    ) {
        Text(
            text = symbol,
            fontFamily = neueMedium,
            fontSize = BlockTextSize,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

