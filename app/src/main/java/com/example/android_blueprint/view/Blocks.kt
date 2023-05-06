package com.example.android_blueprint.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.android_blueprint.model.BlockValue
import com.example.android_blueprint.ui.theme.BlockShape
import com.example.android_blueprint.ui.theme.BlockWidth
import com.example.android_blueprint.ui.theme.ComplexBlockTextSize
import com.example.android_blueprint.ui.theme.OperatorsTextColor
import com.example.android_blueprint.ui.theme.OperatorsTextSize
import com.example.android_blueprint.ui.theme.neueMedium
import kotlin.math.roundToInt


@Composable
fun SetBlock(
    value: Any,
    addBlock: ((blockValue: Any) -> Unit)? = null,
) {

    when (value) {
        is BlockValue.Operator -> OperatorBlock(addBlock = addBlock, value = value)
        is BlockValue.InitializationBlock -> InitializationBlock(addBlock = addBlock, value = value)
    }
}


@Composable
fun OperatorBlock(
    value: BlockValue.Operator,
    addBlock: ((blockValue: Any) -> Unit)?
) {
    if (addBlock != null) {
        OperatorBlockWrapper(
            value = value,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    addBlock(value)
                })
        )
    } else {
        var offsetX by rememberSaveable { mutableStateOf(0f) }
        var offsetY by rememberSaveable { mutableStateOf(0f) }
        OperatorBlockWrapper(
            value = value,
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .width(BlockWidth)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }
        )
    }
}

@Composable
fun InitializationBlock(
    value: BlockValue.InitializationBlock,
    addBlock: ((blockValue: Any) -> Unit)?,
) {
    if (addBlock != null) {
        InitializationBlockWrapper(
            value = value,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    addBlock(value)
                })
        )
    } else {
        var offsetX by rememberSaveable { mutableStateOf(0f) }
        var offsetY by rememberSaveable { mutableStateOf(0f) }
        InitializationBlockWrapper(
            value = value,
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .width(BlockWidth)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }
        )
    }
}


@Composable
fun OperatorText(modifier: Modifier, text: String) {
    Text(
        text = text,
        fontFamily = neueMedium,
        fontSize = OperatorsTextSize,
        color = OperatorsTextColor,
        modifier = modifier
    )
}

@Composable
fun ComplexBlockText(modifier: Modifier, text: String) {
    Text(
        text = text,
        fontFamily = neueMedium,
        fontSize = ComplexBlockTextSize,
        color = OperatorsTextColor,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}


@Composable
fun SupportingFlow(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(9.dp)
            .clip(BlockShape)
            .size(12.dp)
            .background(Color.White)
    )
}

@Composable
fun MainFlow(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(9.dp)
            .clip(
                CutCornerShape(
                    topStart = 0.dp,
                    topEnd = 40.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 40.dp
                )
            )
            .size(12.dp)
            .background(Color.White)
    )
}