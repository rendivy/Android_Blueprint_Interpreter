package com.example.android_blueprint.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.example.android_blueprint.model.BlockValue
import com.example.android_blueprint.model.FieldBlock
import com.example.android_blueprint.ui.theme.BlockHeight
import com.example.android_blueprint.ui.theme.BlockShape
import com.example.android_blueprint.ui.theme.ComplexBlockTextSize
import com.example.android_blueprint.ui.theme.DefaultPadding
import com.example.android_blueprint.ui.theme.FlowSize
import com.example.android_blueprint.ui.theme.FlowTextSize
import com.example.android_blueprint.ui.theme.MainFlowShape
import com.example.android_blueprint.ui.theme.OperatorsTextColor
import com.example.android_blueprint.ui.theme.OperatorsTextSize
import com.example.android_blueprint.ui.theme.TextPaddingForFlow
import com.example.android_blueprint.ui.theme.neueMedium
import com.example.android_blueprint.viewModel.InfiniteFieldViewModel
import kotlin.math.roundToInt

@Composable
fun SetMovableBlock(
    fieldBlock: FieldBlock,
    infiniteFieldViewModel: InfiniteFieldViewModel
) {
    if (fieldBlock.value == -1) return

    var offsetX by rememberSaveable { mutableStateOf(0f) }
    var offsetY by rememberSaveable { mutableStateOf(0f) }

    val modifier = Modifier
        .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                offsetX += dragAmount.x
                offsetY += dragAmount.y
            }
        }
        .heightIn(min = BlockHeight)
        .clip(BlockShape)
        .clickable(enabled = infiniteFieldViewModel.deleteMode) {
            infiniteFieldViewModel.deleteMovableBlock(fieldBlock.index)
        }

    when (fieldBlock.value) {

        is BlockValue.UnaryOperator -> UnaryMovableOperatorBlock(
            value = fieldBlock.value,
            block = infiniteFieldViewModel.createUnaryOperatorBlockInstance(fieldBlock.value),
            modifier = modifier
        )

        is BlockValue.BinaryOperator -> BinaryMovableOperatorBlock(
            value = fieldBlock.value,
            block = infiniteFieldViewModel.createBinaryOperatorBlockInstance(fieldBlock.value),
            modifier = modifier
        )

        is BlockValue.InitializationBlock -> MovableInitializationBlock(
            value = fieldBlock.value,
            addVariable = infiniteFieldViewModel::addVariable,
            removeAtIndex = infiniteFieldViewModel::removeAtIndex,
            valueChange = infiniteFieldViewModel::valueChange,
            block = infiniteFieldViewModel.createInitializationBlock(),
            modifier = modifier
        )

        is BlockValue.IfBlock -> MovableIfBlock(
            value = fieldBlock.value,
            block = infiniteFieldViewModel.createIfBlock(),
            modifier = modifier
        )

        is BlockValue.EndifBlock -> MovableEndifBLock(
            value = fieldBlock.value,
            block = infiniteFieldViewModel.createEndifBlock(),
            modifier = modifier
        )

        is BlockValue.PrintBlock -> MovablePrintBlock(
            value = fieldBlock.value,
            block = infiniteFieldViewModel.createPrintBlock(),
            modifier = modifier
        )
    }
}

@Composable
fun SetFixedBlock(
    value: Any,
    addBlock: ((blockValue: Any) -> Unit)
) {
    when (value) {
        is BlockValue.UnaryOperator -> UnaryFixedOperatorBlock(
            value = value,
            addBlock = addBlock
        )

        is BlockValue.BinaryOperator -> BinaryFixedOperatorBlock(
            value = value,
            addBlock = addBlock
        )

        is BlockValue.InitializationBlock -> FixedInitializationBlock(
            value,
            addBlock = addBlock
        )

        is BlockValue.IfBlock -> FixedBranchBlock(value = value, addBlock = addBlock)
        is BlockValue.PrintBlock -> FixedPrintBlock(value = value, addBlock = addBlock)
    }
}


@Composable
fun TextForFlow(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = FlowTextSize,
        color = Color.White,
        modifier = modifier.padding(top = TextPaddingForFlow)
    )
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
fun SupportingFlow(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(DefaultPadding)
            .clip(BlockShape)
            .size(FlowSize)
            .background(Color.White)
    )
}

@Composable
fun MainFlow(
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier
            .padding(DefaultPadding)
            .clip(MainFlowShape)
            .size(FlowSize)
            .background(Color.White)
            .clickable(onClick = {})

    )
}



val blockViewModel = BlockViewModel()

@Composable
fun MainFlowTest(
    modifier: Modifier = Modifier, connectionCoordinate: MutableList<MutableState<Float>>
) {

    Box(
        modifier = modifier
            .padding(DefaultPadding)
            .clip(MainFlowShape)
            .size(FlowSize)
            .background(Color.White)
            .clickable(onClick = {
                blockViewModel.currentPath.add(Pair(1, connectionCoordinate))
                isConnectorClicked.value = true

            })
    )


}


@Composable
fun MainFlowTest2(
    modifier: Modifier = Modifier, connectorPath: MutableList<MutableState<Float>>,
    blockIsConnected: MutableState<Boolean>
) {

    Box(modifier = modifier
        .padding(DefaultPadding)
        .clip(MainFlowShape)
        .size(FlowSize)
        .background(Color.White)
        .clickable(onClick = {
            isConnectorClicked.value = false
            blockIsConnected.value = true
            if (connectorPath.isNotEmpty()) {
                updatePathInMap(
                    connectorPath[0].value, connectorPath[2].value,
                    connectorPath[1].value, connectorPath[3].value, 1
                )
                blockViewModel.currentPath.clear()
                blockViewModel.currentPath.add(Pair(1, connectorPath))
            }
        })

    )
}