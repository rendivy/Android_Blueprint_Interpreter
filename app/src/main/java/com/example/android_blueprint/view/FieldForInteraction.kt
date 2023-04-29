package com.example.android_blueprint.view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.android_blueprint.ui.theme.InitialOffset
import com.example.android_blueprint.ui.theme.InitialScale
import kotlin.math.roundToInt

@Composable
fun InfiniteField() {
    var scale by remember { mutableStateOf(InitialScale) }
    var offset by remember { mutableStateOf(InitialOffset) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }
    Column(modifier = Modifier
        .transformable(state = state)
        .background(Color.White)
        .fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxHeight(0.1f)
                .fillMaxWidth()
                .background(Color.Yellow)
        )

        Box(
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .fillMaxHeight(0.88f)
                .fillMaxWidth()
                .background(Color.Gray)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Yellow)
        )
    }
}


@Composable
fun SomeBlock() {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .background(Color.Blue)
            .size(50.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
    )
}