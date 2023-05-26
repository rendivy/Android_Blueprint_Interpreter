package com.example.android_blueprint.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import block.EndFunctionBlock
import block.EndIfBlock
import block.ForBlock
import block.FunctionBlock
import block.GetVariableBlock
import block.IBinaryOperatorBlock
import block.IHaveUserInput
import block.IMainFLowBlock
import block.IUnaryOperatorBlock
import block.IfBlock
import block.InitializationVariableBlock
import block.PrintBlock
import block.SetVariableBlock
import block.WhileBlock
import com.example.android_blueprint.model.BlockValue
import com.example.android_blueprint.model.FieldBlock
import com.example.android_blueprint.ui.theme.BinaryOperatorsTextSize
import com.example.android_blueprint.ui.theme.BlockHeight
import com.example.android_blueprint.ui.theme.BlockShape
import com.example.android_blueprint.ui.theme.ComplexBlockColor
import com.example.android_blueprint.ui.theme.ComplexBlockTextSize
import com.example.android_blueprint.ui.theme.DefaultPadding
import com.example.android_blueprint.ui.theme.FlowSize
import com.example.android_blueprint.ui.theme.FlowTextSize
import com.example.android_blueprint.ui.theme.MainFlowShape
import com.example.android_blueprint.ui.theme.OperatorsTextColor
import com.example.android_blueprint.ui.theme.PaddingForPlaceholderText
import com.example.android_blueprint.ui.theme.PlaceholderTextColor
import com.example.android_blueprint.ui.theme.TextPaddingForFlow
import com.example.android_blueprint.ui.theme.UnaryOperatorsTextSize
import com.example.android_blueprint.ui.theme.neuMedium
import com.example.android_blueprint.viewModel.InfiniteFieldViewModel
import com.example.android_blueprint.viewModel.PathViewModel
import kotlin.math.roundToInt

@Composable
fun SetMovableBlock(
    fieldBlock: FieldBlock,
    infiniteFieldViewModel: InfiniteFieldViewModel,
) {
    if (fieldBlock.value == -1) return
    var offsetX by rememberSaveable { mutableStateOf(0f) }
    var offsetY by rememberSaveable { mutableStateOf(0f) }
    var boxHeight by remember { mutableStateOf(0f) }
    var boxWidth by remember { mutableStateOf(0f) }
    val modifier = Modifier
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            if (infiniteFieldViewModel.deleteMode) {
                infiniteFieldViewModel.deleteMovableBlock(fieldBlock.index, fieldBlock.block!!)
            }
        }

    when (fieldBlock.value) {

        is BlockValue.ContinueBlock, BlockValue.BreakBlock -> MovableContinueOrBreakBlock(
            value = fieldBlock.value as BlockValue,
            block = fieldBlock.block as IMainFLowBlock,
            modifier = modifier,
            viewModel = fieldBlock.pathViewModel!!
        )

        is BlockValue.SetBlock -> MovableSetBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as SetVariableBlock,
            modifier = modifier,
            viewModel = fieldBlock.pathViewModel!!
        )

        is BlockValue.ReturnBlock -> MovableReturnBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as EndFunctionBlock,
            modifier = modifier,
            viewModel = fieldBlock.pathViewModel!!
        )

        is BlockValue.FunctionBlock -> MovableFunctionBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as FunctionBlock,
            modifier = modifier,
            viewModel = fieldBlock.pathViewModel!!
        )

        is BlockValue.GetValueBlock -> MovableGetValueBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as GetVariableBlock,
            modifier = modifier,
            viewModel = fieldBlock.pathViewModel!!
        )

        is BlockValue.ForBlock -> MovableForBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as ForBlock,
            modifier = modifier,
            viewModel = fieldBlock.pathViewModel!!
        )

        is BlockValue.WhileBlock -> MovableWhileBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as WhileBlock,
            modifier = modifier,
            viewModel = fieldBlock.pathViewModel!!
        )

        is BlockValue.UnaryOperator -> UnaryMovableOperatorBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as IUnaryOperatorBlock,
            modifier = modifier,
            viewModel = fieldBlock.pathViewModel!!
        )

        is BlockValue.BinaryOperator -> BinaryMovableOperatorBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as IBinaryOperatorBlock,
            modifier = modifier,
            viewModel = fieldBlock.pathViewModel!!
        )

        is BlockValue.InitializationBlock -> MovableInitializationBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as InitializationVariableBlock,
            modifier = modifier,
            viewModel = fieldBlock.pathViewModel!!
        )

        is BlockValue.IfBlock -> MovableIfBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as IfBlock,
            modifier = modifier,
            viewModel = fieldBlock.pathViewModel!!
        )

        is BlockValue.EndifBlock -> MovableEndifBLock(
            value = fieldBlock.value,
            block = fieldBlock.block as EndIfBlock,
            modifier = modifier,
            viewModel = fieldBlock.pathViewModel!!
        )

        is BlockValue.PrintBlock -> MovablePrintBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as PrintBlock,
            modifier = modifier,
            viewModel = fieldBlock.pathViewModel!!
        )
    }
}

@Composable
fun SetFixedBlock(
    value: Any,
    addBlock: ((blockValue: Any) -> Unit)
) {

    val modifier = Modifier
        .clip(BlockShape)
        .background(ComplexBlockColor)
        .fillMaxWidth()
        .clickable(onClick = {
            addBlock(value)
        })

    when (value) {

        is BlockValue.ContinueBlock, BlockValue.BreakBlock -> FixedContinueOrBreakBlock(
            value = value as BlockValue,
            modifier = modifier
        )

        is BlockValue.SetBlock -> FixedSetBlock(value = value, modifier = modifier)

        is BlockValue.ReturnBlock -> FixedReturnBlock(value = value, modifier = modifier)

        is BlockValue.FunctionBlock -> FixedFunctionBlock(value = value, modifier = modifier)

        is BlockValue.GetValueBlock -> FixedGetValueBlock(value = value, modifier = modifier)

        is BlockValue.WhileBlock, BlockValue.ForBlock -> FixedLoopBlock(
            value = value as BlockValue,
            modifier = modifier
        )

        is BlockValue.UnaryOperator -> UnaryFixedOperatorBlock(
            value = value,
            modifier = modifier
        )

        is BlockValue.BinaryOperator -> BinaryFixedOperatorBlock(
            value = value,
            modifier = modifier
        )

        is BlockValue.InitializationBlock -> FixedInitializationBlock(
            value,
            modifier = modifier
        )

        is BlockValue.IfBlock -> FixedBranchBlock(value = value, modifier = modifier)
        is BlockValue.PrintBlock -> FixedPrintBlock(value = value, modifier = modifier)
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
fun BinaryOperatorText(modifier: Modifier, text: String) {
    Text(
        text = text,
        fontFamily = neuMedium,
        fontSize = BinaryOperatorsTextSize,
        color = OperatorsTextColor,
        modifier = modifier
    )
}

@Composable
fun UnaryOperatorText(modifier: Modifier, text: String) {
    Text(
        text = text,
        fontFamily = neuMedium,
        fontSize = UnaryOperatorsTextSize,
        color = OperatorsTextColor,
        modifier = modifier
    )
}


@Composable
fun ComplexBlockText(modifier: Modifier, text: String) {
    Text(
        text = text,
        fontFamily = neuMedium,
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
fun MainFlowTest(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(DefaultPadding)
            .clip(MainFlowShape)
            .size(FlowSize)
            .background(Color.White)
    )
}


@Composable
fun MainFlowTest2(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(DefaultPadding)
            .clip(MainFlowShape)
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
    )
}

@Composable
fun TextFieldForVariable(value: String, modifier: Modifier, block: IHaveUserInput) {
    val focusManager = LocalFocusManager.current
    var text by rememberSaveable { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = {
            text = it
            block.setUserInput(text)
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        placeholder = { placeholderText(text = value) },
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            backgroundColor = ComplexBlockColor,
            cursorColor = Color.White,
            focusedIndicatorColor = ComplexBlockColor,
            unfocusedIndicatorColor = ComplexBlockColor,
            disabledIndicatorColor = ComplexBlockColor,
        ),
        textStyle = TextStyle(fontSize = ComplexBlockTextSize),
        modifier = modifier
    )
}

@Composable
fun placeholderText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontFamily = neuMedium,
        fontSize = ComplexBlockTextSize,
        color = PlaceholderTextColor,
        textAlign = TextAlign.Center,
        modifier = modifier
            .padding(top = PaddingForPlaceholderText)
    )
}