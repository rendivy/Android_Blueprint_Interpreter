package com.example.android_blueprint.view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import block.BlockEntity
import block.EndBlock
import block.EndIfBlock
import block.IBinaryOperatorBlock
import block.IUnaryOperatorBlock
import block.IfBlock
import block.InitializationAndSetVariableBlock
import block.PrintBlock
import block.StartBlock
import com.example.android_blueprint.model.BlockValue
import com.example.android_blueprint.ui.theme.BlockHeight
import com.example.android_blueprint.ui.theme.BlockShape
import com.example.android_blueprint.ui.theme.BlockWidth
import com.example.android_blueprint.ui.theme.BorderBlockWidth
import com.example.android_blueprint.ui.theme.ComplexBlockColor
import com.example.android_blueprint.ui.theme.OperatorBlockColor
import com.example.android_blueprint.ui.theme.TextFieldBlockWidth
import kotlin.math.roundToInt


@Composable
fun StartBlock(
    value: BlockValue.StartBlock,
    block: StartBlock,
) {
    var offsetX by rememberSaveable { mutableStateOf(0f) }
    var offsetY by rememberSaveable { mutableStateOf(0f) }

    Box(
        modifier = Modifier
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
            .width(BorderBlockWidth)
            .background(ComplexBlockColor)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        MainFlow(modifier = Modifier.align(Alignment.CenterEnd))
    }
}

@Composable
fun EndBlock(
    value: BlockValue.EndBlock,
    block: EndBlock,
) {
    var offsetX by rememberSaveable { mutableStateOf(0f) }
    var offsetY by rememberSaveable { mutableStateOf(0f) }
    Box(
        modifier = Modifier
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
            .width(BorderBlockWidth)
            .background(ComplexBlockColor)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        MainFlow(modifier = Modifier.align(Alignment.CenterStart))
    }
}

@Composable
fun <T> BinaryMovableOperatorBlock(
    value: BlockValue.BinaryOperator,
    block: T,
    modifier: Modifier
) where T : BlockEntity, T : IBinaryOperatorBlock {
    Box(
        modifier = modifier
            .width(BlockWidth)
            .background(OperatorBlockColor)
    ) {
        BinaryOperatorText(modifier = Modifier.align(Alignment.Center), text = value.text)
        SupportingFlow()
        SupportingFlow(modifier = Modifier.align(Alignment.BottomStart))
        SupportingFlow(modifier = Modifier.align(Alignment.CenterEnd))
    }
}

@Composable
fun <T> UnaryMovableOperatorBlock(
    value: BlockValue.UnaryOperator,
    block: T,
    modifier: Modifier
) where T : BlockEntity, T : IUnaryOperatorBlock {
    Box(
        modifier = modifier
            .width(BlockWidth)
            .background(OperatorBlockColor)
    ) {
        UnaryOperatorText(modifier = Modifier.align(Alignment.Center), text = value.text)
        SupportingFlow(modifier = Modifier.align(Alignment.CenterStart))
        SupportingFlow(modifier = Modifier.align(Alignment.CenterEnd))
    }
}

@Composable
fun MovablePrintBlock(
    value: BlockValue.PrintBlock,
    block: PrintBlock,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .width(BlockWidth)
            .background(ComplexBlockColor)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            MainFlow()
            MainFlow()
        }
        SupportingFlow(modifier = Modifier.align(Alignment.Start))
    }
}

@Composable
fun MovableIfBlock(
    value: BlockValue.IfBlock,
    block: IfBlock,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .width(BlockWidth)
            .background(ComplexBlockColor)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            MainFlow(modifier = Modifier.align(Alignment.TopStart))
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                TextForFlow(text = "True")
                MainFlow()
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier.align(Alignment.TopStart)) {
                SupportingFlow()
                TextForFlow(text = "Condition")
            }
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                TextForFlow(text = "False")
                MainFlow()
            }
        }

    }
}

@Composable
fun MovableEndifBLock(
    value: BlockValue.EndifBlock,
    block: EndIfBlock,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .width(BorderBlockWidth)
            .background(ComplexBlockColor)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        MainFlow(modifier = Modifier.align(Alignment.CenterStart))
        MainFlow(modifier = Modifier.align(Alignment.BottomStart))
        MainFlow(modifier = Modifier.align(Alignment.CenterEnd))
    }
}

@Composable
fun MovableInitializationBlock(
    value: BlockValue.InitializationBlock,
    block: InitializationAndSetVariableBlock,
    modifier: Modifier
) {

    Column(
        modifier = modifier
            .width(TextFieldBlockWidth)
            .background(ComplexBlockColor)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            MainFlow()
            MainFlow()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextFieldForVariable(value = "name = value", modifier = Modifier.weight(1f))
            SupportingFlow(modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}

@Composable
fun MovableLoopBlock(
    value: BlockValue,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .width(TextFieldBlockWidth)
            .background(ComplexBlockColor)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            MainFlow()
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                TextForFlow(text = "loop")
                MainFlow()
            }
        }
        TextFieldForVariable(value = "conditional", modifier = Modifier.fillMaxWidth())
        Row(modifier = Modifier.align(Alignment.End)) {
            TextForFlow(text = "endloop")
            MainFlow()
        }
    }
}


@Composable
fun MovableGetValueBlock(
    value: BlockValue.GetValueBlock,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .width(TextFieldBlockWidth)
            .background(ComplexBlockColor)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        Row {
            TextFieldForVariable(value = "expression", modifier = Modifier.weight(1f))
            SupportingFlow(modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}

@Composable
fun MovableFunctionBlock(
    value: BlockValue.FunctionBlock,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .width(TextFieldBlockWidth)
            .background(ComplexBlockColor)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        Row {
            TextFieldForVariable(value = "name(args)", modifier = Modifier.weight(1f))
            MainFlow(modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}

@Composable
fun MovableReturnBlock(
    value: BlockValue.ReturnBlock,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .width(BorderBlockWidth)
            .background(ComplexBlockColor)
    ) {
        MainFlow()
        ComplexBlockText(
            modifier = value.modifier.align(Alignment.CenterHorizontally),
            text = value.text
        )
        Row {
            SupportingFlow()
            TextForFlow(text = "value")
        }
    }
}