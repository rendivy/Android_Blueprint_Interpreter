package com.example.android_blueprint.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import block.BlockEntity
import block.EndBlock
import block.EndFunctionBlock
import block.EndIfBlock
import block.ForBlock
import block.FunctionBlock
import block.GetVariableBlock
import block.IBinaryOperatorBlock
import block.IMainFLowBlock
import block.IUnaryOperatorBlock
import block.IfBlock
import block.InitializationVariableBlock
import block.PrintBlock
import block.SetVariableBlock
import block.StartBlock
import block.WhileBlock
import com.example.android_blueprint.model.BlockValue
import com.example.android_blueprint.model.BranchEntity
import com.example.android_blueprint.ui.theme.BlockHeight
import com.example.android_blueprint.ui.theme.BlockShape
import com.example.android_blueprint.ui.theme.BlockWidth
import com.example.android_blueprint.ui.theme.BorderBlockWidth
import com.example.android_blueprint.ui.theme.ComplexBlockColor
import com.example.android_blueprint.ui.theme.OperatorBlockColor
import com.example.android_blueprint.ui.theme.TextFieldBlockWidth
import com.example.android_blueprint.viewModel.setBottomFlowOperator
import com.example.android_blueprint.viewModel.setEndifBottomFlow
import com.example.android_blueprint.viewModel.setEndifTopFlow
import com.example.android_blueprint.viewModel.setMainFlow
import com.example.android_blueprint.viewModel.setPreviousMainFlowFalseBlock
import com.example.android_blueprint.viewModel.setPreviousMainFlowTrueBlock
import com.example.android_blueprint.viewModel.setPreviousSupportFlowBlock
import com.example.android_blueprint.viewModel.setTopFlowOperator
import com.example.android_blueprint.viewModel.setUnaryOperatorFlow
import kotlin.math.roundToInt

val defaultBranch = BranchEntity(mutableStateOf(0f), mutableStateOf(0f))

var branchInWorking: BranchEntity = defaultBranch

data class CharacteristicsBlock(
    var offsetX: Float,
    var offsetY: Float,
    var boxHeight: Float,
    var boxWidth: Float,
)

fun createStartBranch(
    characteristicsBlock: CharacteristicsBlock
): BranchEntity {
    val branch = BranchEntity(
        xStart = mutableStateOf(characteristicsBlock.offsetX + characteristicsBlock.boxWidth),
        yStart = mutableStateOf(characteristicsBlock.offsetY + characteristicsBlock.boxHeight / 2)
    )
    branchInWorking = branch
    return branch
}

fun updateStartBranch(
    outputBranch: BranchEntity,
    characteristicsBlock: CharacteristicsBlock
): BranchEntity {
    if (outputBranch.getId() != defaultBranch.getId()) {
        outputBranch.xStart.value = characteristicsBlock.offsetX + characteristicsBlock.boxWidth
        outputBranch.yStart.value =
            characteristicsBlock.offsetY + characteristicsBlock.boxHeight / 2
        if (outputBranch.getIsConnected()) {
            outputBranch.drawBranch()
        }
    }
    return outputBranch

}

fun createEndBranch(
    inputBranch: BranchEntity,
    characteristicsBlock: CharacteristicsBlock
): BranchEntity {
    return if (inputBranch.getId() != defaultBranch.getId() && inputBranch.getIsConnected()) {
        inputBranch.switchIsConnected()
        inputBranch.deleteBranch()
        defaultBranch
    } else if (inputBranch.getId() == branchInWorking.getId()) {
        defaultBranch
    } else {
        val resultBranch = branchInWorking
        resultBranch.xFinish = mutableStateOf(characteristicsBlock.offsetX)
        resultBranch.yFinish =
            mutableStateOf(characteristicsBlock.offsetY + characteristicsBlock.boxHeight / 2)
        resultBranch.putInMap()
        resultBranch.drawBranch()
        resultBranch.switchIsConnected()
        branchInWorking = defaultBranch
        resultBranch
    }
}

fun updateEndBranch(
    inputBranch: BranchEntity,
    characteristicsBlock: CharacteristicsBlock
): BranchEntity {
    if (inputBranch.getId() != defaultBranch.getId() && inputBranch.getIsConnected()) {
        inputBranch.xFinish.value = characteristicsBlock.offsetX
        inputBranch.yFinish.value =
            characteristicsBlock.offsetY + characteristicsBlock.boxHeight / 2
        inputBranch.drawBranch()
    }
    return inputBranch
}


@Composable
fun StartBlock(
    value: BlockValue.StartBlock,
    block: StartBlock,
) {
    var offsetX by rememberSaveable { mutableStateOf(0f) }
    var offsetY by rememberSaveable { mutableStateOf(0f) }
    var boxHeight by remember { mutableStateOf(0f) }
    var boxWidth by remember { mutableStateOf(0f) }
    var outputBranch = defaultBranch

    Box(
        modifier = Modifier
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

                    outputBranch = updateStartBranch(
                        outputBranch,
                        CharacteristicsBlock(
                            offsetX, offsetY, boxHeight, boxWidth
                        )
                    )
                }
            }
            .heightIn(min = BlockHeight)
            .clip(BlockShape)
            .width(BorderBlockWidth)
            .background(ComplexBlockColor)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        MainFlowTest(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable {
                    setPreviousMainFlowTrueBlock(block)

                    outputBranch = createStartBranch(
                        CharacteristicsBlock(
                            offsetX, offsetY, boxHeight, boxWidth
                        )
                    )
                }
        )
    }
}

@Composable
fun EndBlock(
    value: BlockValue.EndBlock,
    block: EndBlock,
) {
    var offsetX by rememberSaveable { mutableStateOf(0f) }
    var offsetY by rememberSaveable { mutableStateOf(0f) }
    var boxHeight by remember { mutableStateOf(0f) }
    var boxWidth by remember { mutableStateOf(0f) }
    var inputBranch = defaultBranch

    Box(
        modifier = Modifier
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

                    inputBranch = updateEndBranch(
                        inputBranch,
                        CharacteristicsBlock(
                            offsetX, offsetY, boxHeight, boxWidth
                        )
                    )
                }
            }
            .heightIn(min = BlockHeight)
            .clip(BlockShape)
            .width(BorderBlockWidth)
            .background(ComplexBlockColor)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        MainFlowTest2(modifier = Modifier
            .align(Alignment.CenterStart)
            .clickable {
                setMainFlow(block)

                inputBranch = createEndBranch(
                    inputBranch,
                    CharacteristicsBlock(
                        offsetX, offsetY, boxHeight, boxWidth
                    )
                )
            })
    }
}

@Composable
fun MovablePrintBlock(
    value: BlockValue.PrintBlock,
    block: PrintBlock,
    modifier: Modifier,
    offsetX: Float,
    offsetY: Float,
    boxHeight: Float,
    boxWidth: Float,
    ) {
    var inputBranch = defaultBranch
    var outputBranch = defaultBranch

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
            MainFlowTest(
                modifier = Modifier.clickable {
                    setMainFlow(block)

                    outputBranch = createStartBranch(
                        CharacteristicsBlock(
                            offsetX, offsetY, boxHeight, boxWidth
                        )
                    )
                     },

                )
            MainFlowTest2(
                modifier = Modifier.clickable {
                    setPreviousMainFlowTrueBlock(block)
                    inputBranch = createEndBranch(
                        inputBranch,
                        CharacteristicsBlock(
                            offsetX, offsetY, boxHeight, boxWidth
                        )
                    )
                                              },
            )
        }
        SupportingFlow(
            modifier = Modifier
                .align(Alignment.Start)
                .clickable { setUnaryOperatorFlow(block) })
    }
}


@Composable
fun BinaryMovableOperatorBlock(
    value: BlockValue.BinaryOperator,
    block: IBinaryOperatorBlock,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .width(BlockWidth)
            .background(OperatorBlockColor)
    ) {
        BinaryOperatorText(modifier = Modifier.align(Alignment.Center), text = value.text)
        SupportingFlow(modifier = Modifier.clickable { setTopFlowOperator(block) })
        SupportingFlow(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .clickable { setBottomFlowOperator(block) })
        SupportingFlow(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable { setPreviousSupportFlowBlock(block as BlockEntity) })
    }
}

@Composable
fun UnaryMovableOperatorBlock(
    value: BlockValue.UnaryOperator,
    block: IUnaryOperatorBlock,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .width(BlockWidth)
            .background(OperatorBlockColor)
    ) {
        UnaryOperatorText(modifier = Modifier.align(Alignment.Center), text = value.text)
        SupportingFlow(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable { setUnaryOperatorFlow(block) })
        SupportingFlow(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable { setPreviousSupportFlowBlock(block as BlockEntity) })
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
            MainFlow(modifier = Modifier
                .align(Alignment.TopStart)
                .clickable { setMainFlow(block) })
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                TextForFlow(text = "True")
                MainFlow(modifier = Modifier.clickable { setPreviousMainFlowTrueBlock(block) })
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier.align(Alignment.TopStart)) {
                SupportingFlow(modifier = Modifier.clickable { setUnaryOperatorFlow(block) })
                TextForFlow(text = "Condition")
            }
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                TextForFlow(text = "False")
                MainFlow(modifier = Modifier.clickable { setPreviousMainFlowFalseBlock(block) })
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
        MainFlow(modifier = Modifier
            .align(Alignment.CenterStart)
            .clickable { setEndifTopFlow(block) })
        MainFlow(modifier = Modifier
            .align(Alignment.BottomStart)
            .clickable { setEndifBottomFlow(block) })
        MainFlow(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable { setPreviousMainFlowTrueBlock(block) })
    }
}

@Composable
fun MovableInitializationBlock(
    value: BlockValue.InitializationBlock,
    block: InitializationVariableBlock,
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
            MainFlow(modifier = Modifier.clickable { setMainFlow(block) })
            MainFlow(modifier = Modifier.clickable { setPreviousMainFlowTrueBlock(block) })
        }
        TextFieldForVariable(
            value = "name or *name[n]",
            modifier = Modifier.fillMaxWidth(),
            block = block
        )
    }
}


@Composable
fun MovableSetBlock(
    value: BlockValue.SetBlock,
    block: SetVariableBlock,
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
            MainFlow(modifier = Modifier.clickable { setMainFlow(block) })
            MainFlow(modifier = Modifier.clickable { setPreviousMainFlowTrueBlock(block) })
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextFieldForVariable(
                value = "name = value",
                modifier = Modifier.weight(1f),
                block = block
            )
            SupportingFlow(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable { setPreviousSupportFlowBlock(block) })
        }
    }
}


@Composable
fun MovableForBlock(
    value: BlockValue.ForBlock,
    modifier: Modifier,
    block: ForBlock
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
            MainFlow(modifier = Modifier.clickable { setMainFlow(block) })
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                TextForFlow(text = "loop")
                MainFlow(modifier = Modifier.clickable { setPreviousMainFlowTrueBlock(block) })
            }
        }
        TextFieldForVariable(
            value = "conditional",
            modifier = Modifier.fillMaxWidth(),
            block = block
        )
        Row(modifier = Modifier.align(Alignment.End)) {
            TextForFlow(text = "endloop")
            MainFlow(modifier = Modifier.clickable { setPreviousMainFlowFalseBlock(block) })
        }
    }
}

@Composable
fun MovableWhileBlock(
    value: BlockValue.WhileBlock,
    modifier: Modifier,
    block: WhileBlock
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
            MainFlow(modifier = Modifier.clickable { setMainFlow(block) })
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                TextForFlow(text = "loop")
                MainFlow(modifier = Modifier.clickable { setPreviousMainFlowTrueBlock(block) })
            }
        }
        TextFieldForVariable(
            value = "conditional",
            modifier = Modifier.fillMaxWidth(),
            block = block
        )
        Row(modifier = Modifier.align(Alignment.End)) {
            TextForFlow(text = "endloop")
            MainFlow(modifier = Modifier.clickable { setPreviousMainFlowFalseBlock(block) })
        }
    }
}


@Composable
fun MovableGetValueBlock(
    value: BlockValue.GetValueBlock,
    modifier: Modifier,
    block: GetVariableBlock
) {
    Column(
        modifier = modifier
            .width(TextFieldBlockWidth)
            .background(ComplexBlockColor)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        Row {
            TextFieldForVariable(
                value = "expression",
                modifier = Modifier.weight(1f),
                block = block
            )
            SupportingFlow(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable { setPreviousSupportFlowBlock(block) })
        }
    }
}

@Composable
fun MovableFunctionBlock(
    value: BlockValue.FunctionBlock,
    modifier: Modifier,
    block: FunctionBlock
) {
    Column(
        modifier = modifier
            .width(TextFieldBlockWidth)
            .background(ComplexBlockColor)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        Row {
            TextFieldForVariable(
                value = "name(args)",
                modifier = Modifier.weight(1f),
                block = block
            )
            MainFlow(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable { setPreviousMainFlowTrueBlock(block) })
        }
    }
}

@Composable
fun MovableReturnBlock(
    value: BlockValue.ReturnBlock,
    modifier: Modifier,
    block: EndFunctionBlock
) {
    Column(
        modifier = modifier
            .width(BorderBlockWidth)
            .background(ComplexBlockColor)
    ) {
        MainFlow(modifier = Modifier.clickable { setMainFlow(block) })
        ComplexBlockText(
            modifier = value.modifier.align(Alignment.CenterHorizontally),
            text = value.text
        )
        Row {
            SupportingFlow(modifier = Modifier.clickable { setUnaryOperatorFlow(block) })
            TextForFlow(text = "value")
        }
    }
}

@Composable
fun MovableContinueOrBreakBlock(
    value: BlockValue,
    modifier: Modifier,
    block: IMainFLowBlock
) {
    Column(
        modifier = modifier
            .width(BorderBlockWidth)
            .background(ComplexBlockColor)
    ) {
        MainFlow(modifier = Modifier.clickable { setMainFlow(block) })
        ComplexBlockText(
            modifier = value.modifier.align(Alignment.CenterHorizontally),
            text = value.text
        )
    }
}


