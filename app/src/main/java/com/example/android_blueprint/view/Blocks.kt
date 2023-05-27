package com.example.android_blueprint.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import block.BlockEntity
import block.CallFunctionBlock
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
import com.example.android_blueprint.ui.theme.ActionColor
import com.example.android_blueprint.ui.theme.BackgroundColor
import com.example.android_blueprint.ui.theme.BinaryOperatorsTextSize
import com.example.android_blueprint.ui.theme.BlockShape
import com.example.android_blueprint.ui.theme.BreakPointPadding
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
import com.example.android_blueprint.ui.theme.NotSingleTextSize
import com.example.android_blueprint.ui.theme.neuMedium
import com.example.android_blueprint.viewModel.InfiniteFieldViewModel

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
            interactionSource = remember {
                MutableInteractionSource()
            }
        ) {
            if (infiniteFieldViewModel.deleteMode) {
                infiniteFieldViewModel.deleteMovableBlock(
                    fieldBlock.index,
                    fieldBlock.block!!,
                    fieldBlock.pathViewModel!!
                )
            }
        }

    when (fieldBlock.value) {

        is BlockValue.CallFunctionBlock -> MovableCallFunctionBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as CallFunctionBlock,
            viewModel = fieldBlock.pathViewModel!!,
            modifier = modifier
        )

        is BlockValue.ContinueBlock, BlockValue.BreakBlock -> MovableContinueOrBreakBlock(
            value = fieldBlock.value as BlockValue,
            block = fieldBlock.block as IMainFLowBlock,
            viewModel = fieldBlock.pathViewModel!!,
            modifier = modifier
        )

        is BlockValue.SetBlock -> MovableSetBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as SetVariableBlock,
            viewModel = fieldBlock.pathViewModel!!,
            modifier = modifier
        )

        is BlockValue.ReturnBlock -> MovableReturnBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as EndFunctionBlock,
            viewModel = fieldBlock.pathViewModel!!,
            modifier = modifier
        )

        is BlockValue.FunctionBlock -> MovableFunctionBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as FunctionBlock,
            viewModel = fieldBlock.pathViewModel!!,
            modifier = modifier
        )

        is BlockValue.GetValueBlock -> MovableGetValueBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as GetVariableBlock,
            viewModel = fieldBlock.pathViewModel!!,
            modifier = modifier
        )

        is BlockValue.ForBlock -> MovableForBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as ForBlock,
            viewModel = fieldBlock.pathViewModel!!,
            modifier = modifier
        )

        is BlockValue.WhileBlock -> MovableWhileBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as WhileBlock,
            viewModel = fieldBlock.pathViewModel!!,
            modifier = modifier
        )

        is BlockValue.UnaryOperator -> UnaryMovableOperatorBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as IUnaryOperatorBlock,
            viewModel = fieldBlock.pathViewModel!!,
            modifier = modifier
        )

        is BlockValue.BinaryOperator -> BinaryMovableOperatorBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as IBinaryOperatorBlock,
            viewModel = fieldBlock.pathViewModel!!,
            modifier = modifier
        )

        is BlockValue.InitializationBlock -> MovableInitializationBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as InitializationVariableBlock,
            viewModel = fieldBlock.pathViewModel!!,
            modifier = modifier
        )

        is BlockValue.IfBlock -> MovableIfBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as IfBlock,
            viewModel = fieldBlock.pathViewModel!!,
            modifier = modifier
        )

        is BlockValue.EndifBlock -> MovableEndifBLock(
            value = fieldBlock.value,
            block = fieldBlock.block as EndIfBlock,
            viewModel = fieldBlock.pathViewModel!!,
            modifier = modifier
        )

        is BlockValue.PrintBlock -> MovablePrintBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as PrintBlock,
            viewModel = fieldBlock.pathViewModel!!,
            modifier = modifier
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

        is BlockValue.CallFunctionBlock -> FixedCallFunctionBlock(
            value = value,
            modifier = modifier
        )

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
        modifier = modifier.padding(top = TextPaddingForFlow),
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun BinaryOperatorText(modifier: Modifier, text: String, fontSize: TextUnit = BinaryOperatorsTextSize) {
    Text(
        text = text,
        fontFamily = neuMedium,
        fontSize = fontSize,
        color = OperatorsTextColor,
        modifier = modifier,
        )
}

@Composable
fun UnaryOperatorText(modifier: Modifier, text: String) {
    Text(
        text = text,
        fontFamily = neuMedium,
        fontSize = NotSingleTextSize,
        color = OperatorsTextColor,
        modifier = modifier,
        fontWeight = FontWeight.Bold
    )
}


@Composable
fun ComplexBlockText(
    text: String, color: Color = BackgroundColor, modifier: Modifier = Modifier.background(
        Brush.linearGradient(
            colors = listOf(ActionColor, ActionColor),
            start = Offset(0f, 0f),
            end = Offset(100f, 0f)
        )
    )
) {
    Text(
        text = text,
        fontFamily = neuMedium,
        fontSize = ComplexBlockTextSize,
        color = color,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
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
            .background(ActionColor)
    )
}


@Composable
fun MainFlow(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .padding(DefaultPadding)
            .clip(MainFlowShape)
            .size(FlowSize)
            .background(Color.White)
            .then(modifier)
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

@Composable
fun BreakPoint(
    block: BlockEntity,
    color: Color,
    changeBreakPointColor: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .padding(BreakPointPadding)
            .clip(BlockShape)
            .size(FlowSize)
            .background(color)
            .clickable {
                block.switchBreakPoint()
                changeBreakPointColor()
            }
    )
}
