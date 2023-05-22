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
import androidx.compose.runtime.SideEffect
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
import block.IUnaryOperatorBlock
import block.IfBlock
import block.InitializationAndSetVariableBlock
import block.PrintBlock
import block.WhileBlock
import com.example.android_blueprint.model.BlockValue
import com.example.android_blueprint.model.FieldBlock
import com.example.android_blueprint.model.PathModel
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
import com.example.android_blueprint.ui.theme.neueMedium
import com.example.android_blueprint.viewModel.BlockFactory
import com.example.android_blueprint.viewModel.ConsoleViewModel
import com.example.android_blueprint.viewModel.InfiniteFieldViewModel
import com.example.android_blueprint.viewModel.PathViewModel
import kotlin.math.roundToInt

@Composable
fun SetMovableBlock(
    fieldBlock: FieldBlock,
    infiniteFieldViewModel: InfiniteFieldViewModel
) {
    if (fieldBlock.value == -1) return
    var offsetX by rememberSaveable { mutableStateOf(0f) }
    var offsetY by rememberSaveable { mutableStateOf(0f) }
    var boxHeight by remember { mutableStateOf(0f) }
    var boxWidth by remember { mutableStateOf(0f) }
    var isPathInConnectorTest: MutableState<Boolean> = remember { mutableStateOf(false) }
    val modifier = Modifier
        .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
        .onGloballyPositioned { coordinates ->
            boxHeight = coordinates.size.height.toFloat()
            boxWidth = coordinates.size.width.toFloat()
        }
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                offsetX += dragAmount.x
                offsetY += dragAmount.y
                if (isPathInConnectorTest.value && PathViewModel.pathHashMap[PathViewModel.buttonPressedBlockId]!!.pathList.isNotEmpty()) {
                    PathViewModel.pathHashMap[PathViewModel.buttonPressedBlockId]!!.pathList[0].value =
                        offsetX
                    PathViewModel.pathHashMap[PathViewModel.buttonPressedBlockId]!!.pathList[1].value =
                        offsetY + boxHeight / 2
                    PathViewModel.pathHashMap[PathViewModel.buttonPressedBlockId]!!.pathList[2].value =
                        offsetX + boxWidth
                    PathViewModel.pathHashMap[PathViewModel.buttonPressedBlockId]!!.pathList[3].value =
                        offsetY + boxHeight / 2
                    updatePathInMap(
                        PathViewModel.pathHashMap[PathViewModel.buttonPressedBlockId]!!,
                        PathViewModel.buttonPressedBlockId
                    )
                }
            }
        }
        .heightIn(min = BlockHeight)
        .clip(BlockShape)
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            if (infiniteFieldViewModel.deleteMode) {
                infiniteFieldViewModel.deleteMovableBlock(fieldBlock.index, fieldBlock.block!!)
            }
        }

    when (fieldBlock.value) {

        is BlockValue.ReturnBlock -> MovableReturnBlock(
            value = fieldBlock.value,
            modifier = modifier,
            block = fieldBlock.block as EndFunctionBlock
        )

        is BlockValue.FunctionBlock -> MovableFunctionBlock(
            value = fieldBlock.value,
            modifier = modifier,
            block = fieldBlock.block as FunctionBlock
        )

        is BlockValue.GetValueBlock -> MovableGetValueBlock(
            value = fieldBlock.value,
            modifier = modifier,
            block = fieldBlock.block as GetVariableBlock
        )

        is BlockValue.ForBlock -> MovableForBlock(
            value = fieldBlock.value,
            modifier = modifier,
            block =  fieldBlock.block as ForBlock
        )

        is BlockValue.WhileBlock -> MovableWhileBlock(
            value = fieldBlock.value,
            modifier = modifier,
            block =  fieldBlock.block as WhileBlock
        )

        is BlockValue.UnaryOperator -> UnaryMovableOperatorBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as IUnaryOperatorBlock,
            modifier = modifier
        )

        is BlockValue.BinaryOperator -> BinaryMovableOperatorBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as IBinaryOperatorBlock,
            modifier = modifier
        )

        is BlockValue.InitializationBlock -> MovableInitializationBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as InitializationAndSetVariableBlock,
            modifier = modifier
        )

        is BlockValue.IfBlock -> MovableIfBlock(
            value = fieldBlock.value,
            block = fieldBlock.block as IfBlock,
            modifier = modifier
        )

        is BlockValue.EndifBlock -> MovableEndifBLock(
            value = fieldBlock.value,
            block =  fieldBlock.block as EndIfBlock,
            modifier = modifier
        )

        is BlockValue.PrintBlock -> MovablePrintBlock(
            value = fieldBlock.value,
            block =  fieldBlock.block as PrintBlock,
            modifier = modifier,
            flag = isPathInConnectorTest,
            offsetX = offsetX,
            offsetY = offsetY,
            boxHeight = boxHeight,
            boxWidth = boxWidth
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
        fontFamily = neueMedium,
        fontSize = BinaryOperatorsTextSize,
        color = OperatorsTextColor,
        modifier = modifier
    )
}

@Composable
fun UnaryOperatorText(modifier: Modifier, text: String) {
    Text(
        text = text,
        fontFamily = neueMedium,
        fontSize = UnaryOperatorsTextSize,
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
fun MainFlowTest(
    modifier: Modifier = Modifier,
    pathModel: PathModel,
    flag: MutableState<Boolean>,
    offsetX: Float, offsetY: Float, boxHeight: Float,
    boxWidth: Float,
    blockId: Int

) {

    Box(
        modifier = modifier
            .padding(DefaultPadding)
            .clip(MainFlowShape)
            .size(FlowSize)
            .background(Color.White)
            .clickable(onClick = {
                PathViewModel.pathHashMap[pathModel.pathId] = pathModel
                flag.value = true
                PathViewModel.isConnectorClicked.value = true
                pathModel.isPathConnected = true
                PathViewModel.buttonPressedBlockId = blockId
                PathViewModel.pathHashMap[PathViewModel.buttonPressedBlockId]!!.pathList[0].value =
                    offsetX + boxWidth
                PathViewModel.pathHashMap[PathViewModel.buttonPressedBlockId]!!.pathList[1].value =
                    offsetY + boxHeight / 2


            })
    )
}


@Composable
fun MainFlowTest2(
    modifier: Modifier = Modifier, flag: MutableState<Boolean>,
    offsetX: Float, offsetY: Float, boxHeight: Float
) {
    var color = if (flag.value) Color.Blue else Color.White
    Box(
        modifier = modifier
            .padding(DefaultPadding)
            .clip(MainFlowShape)
            .size(FlowSize)
            .background(color)
            .clickable(onClick = {
                if (PathViewModel.isConnectorClicked.value) {
                    PathViewModel.isConnectorClicked.value = false
                    flag.value = true
                    PathViewModel.pathHashMap[PathViewModel.buttonPressedBlockId]!!.pathList[2].value =
                        offsetX
                    PathViewModel.pathHashMap[PathViewModel.buttonPressedBlockId]!!.pathList[3].value =
                        offsetY + boxHeight / 2
                    updatePathInMap(
                        PathViewModel.pathHashMap[PathViewModel.buttonPressedBlockId]!!,
                        PathViewModel.buttonPressedBlockId
                    )
                } else {
                    PathViewModel.pathHashMap[1]!!.pathList = mutableListOf()
                    updatePathInMap(
                        PathViewModel.pathHashMap[1]!!,
                        PathViewModel.buttonPressedBlockId
                    )
                }
            })

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
        fontFamily = neueMedium,
        fontSize = ComplexBlockTextSize,
        color = PlaceholderTextColor,
        textAlign = TextAlign.Center,
        modifier = modifier
            .padding(top = PaddingForPlaceholderText)
    )
}