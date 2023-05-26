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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.android_blueprint.viewModel.BlockViewModel
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

val defaultBranch = BranchEntity(mutableStateOf(0f), mutableStateOf(0f), idStartBlock = -1)
var branchInWorking: BranchEntity = defaultBranch

data class CharacteristicsBlock(
    val xResult: Float,
    val yResult: Float,
)

fun createStartBranch(
    inputBranch: BranchEntity,
    characteristicsBlock: CharacteristicsBlock,
    isMainFlow: Boolean = true,
    idStartBlock: Int
): BranchEntity {
    if (inputBranch.getId() != defaultBranch.getId()) {
        inputBranch.deleteBranch()
    }
    val branch = BranchEntity(
        xStart = mutableStateOf(characteristicsBlock.xResult),
        yStart = mutableStateOf(characteristicsBlock.yResult),
        isMainFlow,
        idStartBlock,
    )
    branchInWorking = branch
    return branch
}

fun createEndBranch(
    inputBranch: BranchEntity,
    characteristicsBlock: CharacteristicsBlock,
    isMainFlow: Boolean,
    idFinishBlock: Int
): BranchEntity {
    return if (branchInWorking.isMainFlowBranch != isMainFlow ||
        branchInWorking.idStartBlock == idFinishBlock
    ) {
        defaultBranch
    } else if(branchInWorking.getId() == defaultBranch.getId()){
        inputBranch
    }
    else {
        tryClearBranches(inputBranch)
        val resultBranch = branchInWorking
        resultBranch.xFinish = mutableStateOf(characteristicsBlock.xResult)
        resultBranch.yFinish =
            mutableStateOf(characteristicsBlock.yResult)
        resultBranch.putInMap()
        resultBranch.drawBranch()
        resultBranch.switchIsConnected()
        resultBranch.idFinishBlock = idFinishBlock
        branchInWorking = defaultBranch
        resultBranch
    }
}

fun updateStartBranch(
    outputBranch: BranchEntity,
    characteristicsBlock: CharacteristicsBlock
): BranchEntity {
    if (!outputBranch.isInMap()) {
        return defaultBranch
    }

    if (outputBranch.getId() != defaultBranch.getId()) {
        outputBranch.xStart.value = characteristicsBlock.xResult
        outputBranch.yStart.value = characteristicsBlock.yResult
        if (outputBranch.getIsConnected()) {
            outputBranch.drawBranch()
        }
    }
    return outputBranch

}

fun updateEndBranch(
    inputBranch: BranchEntity,
    characteristicsBlock: CharacteristicsBlock
): BranchEntity {
    if (!inputBranch.isInMap()) {
        return defaultBranch
    }

    if (inputBranch.getId() != defaultBranch.getId() && inputBranch.getIsConnected()) {
        inputBranch.xFinish.value = characteristicsBlock.xResult
        inputBranch.yFinish.value = characteristicsBlock.yResult
        inputBranch.drawBranch()
    }
    return inputBranch
}

fun tryClearBranches(
    inputBranch: BranchEntity,
) {
    if (inputBranch.getId() != defaultBranch.getId()) {
        inputBranch.deleteBranch()
    }
}

fun deleteAllBranches(
    viewModel: BlockViewModel
) {
    tryClearBranches(viewModel.outputBranch)
    tryClearBranches(viewModel.inputBranch)
    tryClearBranches(viewModel.inputBranchForEndIf)
    tryClearBranches(viewModel.outputBranchTrue)
    tryClearBranches(viewModel.outputBranchFalse)
    tryClearBranches(viewModel.inputSupportFLow)
    tryClearBranches(viewModel.outputSupportFLow)
    tryClearBranches(viewModel.inputSupportFLowLeft)
    tryClearBranches(viewModel.inputSupportFLowRight)
}

@Composable
fun StartBlock(
    value: BlockValue.StartBlock,
    block: StartBlock,
    viewModel: BlockViewModel
) {
    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    viewModel.offsetX.roundToInt(),
                    viewModel.offsetY.roundToInt()
                )
            }
            .onGloballyPositioned { coordinates ->
                viewModel.boxHeight = coordinates.size.height.toFloat()
                viewModel.boxWidth = coordinates.size.width.toFloat()
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    viewModel.offsetX += dragAmount.x
                    viewModel.offsetY += dragAmount.y

                    viewModel.outputBranch = updateStartBranch(
                        viewModel.outputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 2
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
        BreakPoint(
            block = block,
            color = viewModel.color,
            changeBreakPointColor = viewModel::changeBreakPointColor,
            modifier = Modifier.align(Alignment.TopEnd)
        )
        MainFlowTest(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable {
                    setPreviousMainFlowTrueBlock(block)

                    tryClearBranches(viewModel.outputBranch)

                    viewModel.outputBranch = createStartBranch(
                        viewModel.outputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 2,
                        ),
                        idStartBlock = block.getId()
                    )
                }
        )
    }
}

@Composable
fun EndBlock(
    value: BlockValue.EndBlock,
    block: EndBlock,
    viewModel: BlockViewModel
) {


    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    viewModel.offsetX.roundToInt(),
                    viewModel.offsetY.roundToInt()
                )
            }
            .onGloballyPositioned { coordinates ->
                viewModel.boxHeight = coordinates.size.height.toFloat()
                viewModel.boxWidth = coordinates.size.width.toFloat()
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    viewModel.offsetX += dragAmount.x
                    viewModel.offsetY += dragAmount.y

                    viewModel.inputBranch = updateEndBranch(
                        viewModel.inputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 2,
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
        BreakPoint(
            block = block,
            color = viewModel.color,
            changeBreakPointColor = viewModel::changeBreakPointColor,
            modifier = Modifier.align(Alignment.TopEnd)
        )
        MainFlowTest2(modifier = Modifier
            .align(Alignment.CenterStart)
            .clickable {
                setMainFlow(block)

                viewModel.inputBranch = createEndBranch(
                    viewModel.inputBranch,
                    CharacteristicsBlock(
                        viewModel.offsetX,
                        viewModel.offsetY + viewModel.boxHeight / 2,
                    ),
                    true,
                    block.getId()
                )
            })
    }
}

@Composable
fun MovablePrintBlock(
    value: BlockValue.PrintBlock,
    block: PrintBlock,
    modifier: Modifier,
    viewModel: BlockViewModel
) {

    Column(
        modifier = Modifier
            .offset { IntOffset(viewModel.offsetX.roundToInt(), viewModel.offsetY.roundToInt()) }
            .onGloballyPositioned { coordinates ->
                viewModel.boxHeight = coordinates.size.height.toFloat()
                viewModel.boxWidth = coordinates.size.width.toFloat()
            }
            .clip(BlockShape)
            .width(BlockWidth)
            .background(ComplexBlockColor)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    viewModel.offsetX += dragAmount.x
                    viewModel.offsetY += dragAmount.y

                    viewModel.inputBranch = updateEndBranch(
                        viewModel.inputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 2.35f,
                        )
                    )
                    viewModel.outputBranch = updateStartBranch(
                        viewModel.outputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 2.35f,
                        )
                    )
                    viewModel.inputSupportFLow = updateEndBranch(
                        viewModel.inputSupportFLow,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 1.2f,
                        )
                    )
                }
            }
            .then(modifier)
    ) {
        Box {
            ComplexBlockText(modifier = value.modifier, text = value.text)
            BreakPoint(
                block = block,
                color = viewModel.color,
                changeBreakPointColor = viewModel::changeBreakPointColor,
                modifier = Modifier.align(Alignment.TopEnd),

                )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            MainFlowTest(
                modifier = Modifier.clickable {
                    setMainFlow(block)

                    viewModel.inputBranch = createEndBranch(
                        viewModel.inputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 2.35f,
                        ),
                        true,
                        block.getId()
                    )
                },

                )
            MainFlowTest2(
                modifier = Modifier.clickable {
                    setPreviousMainFlowTrueBlock(block)

                    viewModel.outputBranch = createStartBranch(
                        viewModel.outputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 2.35f,
                        ),
                        idStartBlock = block.getId()
                    )
                },
            )
        }
        SupportingFlow(
            modifier = Modifier
                .align(Alignment.Start)
                .clickable {
                    setUnaryOperatorFlow(block)

                    viewModel.inputSupportFLow = createEndBranch(
                        viewModel.inputSupportFLow,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 1.2f,
                        ),
                        false,
                        block.getId()
                    )
                })
    }
}


@Composable
fun BinaryMovableOperatorBlock(
    value: BlockValue.BinaryOperator,
    block: IBinaryOperatorBlock,
    modifier: Modifier,
    viewModel: BlockViewModel
) {
    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    viewModel.offsetX.roundToInt(),
                    viewModel.offsetY.roundToInt()
                )
            }
            .onGloballyPositioned { coordinates ->
                viewModel.boxHeight = coordinates.size.height.toFloat()
                viewModel.boxWidth = coordinates.size.width.toFloat()
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    viewModel.offsetX += dragAmount.x
                    viewModel.offsetY += dragAmount.y

                    viewModel.inputSupportFLowLeft = updateEndBranch(
                        viewModel.inputSupportFLowLeft,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 5f,
                        )
                    )
                    viewModel.inputSupportFLowRight = updateEndBranch(
                        viewModel.inputSupportFLowRight,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 1.2f,
                        )
                    )
                    viewModel.outputSupportFLow = updateStartBranch(
                        viewModel.outputSupportFLow,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 2,
                        )
                    )

                }
            }
            .width(BlockWidth)
            .clip(BlockShape)
            .background(OperatorBlockColor)
            .heightIn(min = BlockHeight)
            .then(modifier)


    ) {
        BreakPoint(
            block = block as BlockEntity,
            color = viewModel.color,
            changeBreakPointColor = viewModel::changeBreakPointColor,
            modifier = Modifier.align(Alignment.TopEnd)
        )
        BinaryOperatorText(modifier = Modifier.align(Alignment.Center), text = value.text)
        SupportingFlow(modifier = Modifier.clickable {
            setTopFlowOperator(block)
            viewModel.inputSupportFLowLeft = createEndBranch(
                viewModel.inputSupportFLowLeft,
                CharacteristicsBlock(
                    viewModel.offsetX,
                    viewModel.offsetY + viewModel.boxHeight / 5f,
                ),
                false,
                (block as BlockEntity).getId()
            )
        })
        SupportingFlow(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .clickable {
                    setBottomFlowOperator(block)
                    viewModel.inputSupportFLowRight = createEndBranch(
                        viewModel.inputSupportFLowRight,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 1.2f,
                        ),
                        false,
                        (block as BlockEntity).getId()
                    )
                })
        SupportingFlow(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable {
                    setPreviousSupportFlowBlock(block as BlockEntity)

                    viewModel.outputSupportFLow = createStartBranch(
                        viewModel.outputSupportFLow,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 2,
                        ),
                        false,
                        block.getId()
                    )
                })
    }
}

@Composable
fun UnaryMovableOperatorBlock(
    value: BlockValue.UnaryOperator,
    block: IUnaryOperatorBlock,
    modifier: Modifier,
    viewModel: BlockViewModel
) {

    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    viewModel.offsetX.roundToInt(),
                    viewModel.offsetY.roundToInt()
                )
            }
            .onGloballyPositioned { coordinates ->
                viewModel.boxHeight = coordinates.size.height.toFloat()
                viewModel.boxWidth = coordinates.size.width.toFloat()
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    viewModel.offsetX += dragAmount.x
                    viewModel.offsetY += dragAmount.y

                    viewModel.inputSupportFLow = updateEndBranch(
                        viewModel.inputSupportFLow,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 2,
                        )
                    )
                    viewModel.outputSupportFLow = updateStartBranch(
                        viewModel.outputSupportFLow,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 2,
                        )
                    )

                }
            }
            .width(BlockWidth)
            .clip(BlockShape)
            .background(OperatorBlockColor)
            .heightIn(min = BlockHeight)
            .then(modifier)
    ) {
        BreakPoint(
            block = block as BlockEntity,
            color = viewModel.color,
            changeBreakPointColor = viewModel::changeBreakPointColor,
            modifier = Modifier.align(Alignment.TopEnd)
        )
        UnaryOperatorText(modifier = Modifier.align(Alignment.Center), text = value.text)
        SupportingFlow(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable {
                    setUnaryOperatorFlow(block)

                    viewModel.inputSupportFLow = createEndBranch(
                        viewModel.inputSupportFLow,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 2,
                        ),
                        false,
                        (block as BlockEntity).getId()
                    )
                })
        SupportingFlow(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable {
                    setPreviousSupportFlowBlock(block as BlockEntity)

                    viewModel.outputSupportFLow = createStartBranch(
                        viewModel.outputSupportFLow,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 2,
                        ),
                        false,
                        block.getId()
                    )
                })
    }
}

@Composable
fun MovableIfBlock(
    value: BlockValue.IfBlock,
    block: IfBlock,
    modifier: Modifier,
    viewModel: BlockViewModel
) {
    Column(
        modifier = Modifier
            .offset {
                IntOffset(
                    viewModel.offsetX.roundToInt(),
                    viewModel.offsetY.roundToInt()
                )
            }
            .onGloballyPositioned { coordinates ->
                viewModel.boxHeight = coordinates.size.height.toFloat()
                viewModel.boxWidth = coordinates.size.width.toFloat()
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    viewModel.offsetX += dragAmount.x
                    viewModel.offsetY += dragAmount.y

                    viewModel.inputBranch = updateEndBranch(
                        viewModel.inputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 2.15f,
                        )
                    )
                    viewModel.outputBranchTrue = updateStartBranch(
                        viewModel.outputBranchTrue,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 2.15f,
                        )
                    )
                    viewModel.outputBranchFalse = updateStartBranch(
                        viewModel.outputBranchFalse,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 1.2f,
                        )
                    )
                    viewModel.inputSupportFLow = updateEndBranch(
                        viewModel.inputSupportFLow,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 1.2f,
                        )
                    )
                }
            }
            .width(BlockWidth)
            .clip(BlockShape)
            .background(ComplexBlockColor)
            .heightIn(min = BlockHeight)
            .then(modifier)


    ) {
        Box {
            ComplexBlockText(modifier = value.modifier, text = value.text)
            BreakPoint(
                block = block,
                color = viewModel.color,
                changeBreakPointColor = viewModel::changeBreakPointColor,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            MainFlow(modifier = Modifier
                .align(Alignment.TopStart)
                .clickable {
                    setMainFlow(block)

                    viewModel.inputBranch = createEndBranch(
                        viewModel.inputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 2.15f,
                        ),
                        true,
                        block.getId()
                    )
                })
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                TextForFlow(text = "True")
                MainFlow(modifier = Modifier.clickable {
                    setPreviousMainFlowTrueBlock(block)

                    viewModel.outputBranchTrue = createStartBranch(
                        viewModel.outputBranchTrue,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 2.15f,
                        ),
                        idStartBlock = block.getId()
                    )
                })
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier.align(Alignment.TopStart)) {
                SupportingFlow(modifier = Modifier.clickable {
                    setUnaryOperatorFlow(block)

                    viewModel.inputSupportFLow = createEndBranch(
                        viewModel.inputSupportFLow,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 1.2f,
                        ),
                        false,
                        (block as BlockEntity).getId()
                    )
                })
                TextForFlow(text = "Condition")
            }
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                TextForFlow(text = "False")
                MainFlow(modifier = Modifier.clickable {
                    setPreviousMainFlowFalseBlock(block)

                    viewModel.outputBranchFalse = createStartBranch(
                        viewModel.outputBranchFalse,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 1.2f,
                        ),
                        idStartBlock = block.getId()
                    )
                })
            }
        }

    }
}

@Composable
fun MovableEndifBLock(
    value: BlockValue.EndifBlock,
    block: EndIfBlock,
    modifier: Modifier,
    viewModel: BlockViewModel
) {
    Box(
        modifier = Modifier
            .offset {
                IntOffset(
                    viewModel.offsetX.roundToInt(),
                    viewModel.offsetY.roundToInt()
                )
            }
            .onGloballyPositioned { coordinates ->
                viewModel.boxHeight = coordinates.size.height.toFloat()
                viewModel.boxWidth = coordinates.size.width.toFloat()
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    viewModel.offsetX += dragAmount.x
                    viewModel.offsetY += dragAmount.y

                    viewModel.inputBranch = updateEndBranch(
                        viewModel.inputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 2,
                        )
                    )
                    viewModel.inputBranchForEndIf = updateEndBranch(
                        viewModel.inputBranchForEndIf,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 1.25f,
                        )
                    )
                    viewModel.outputBranch = updateStartBranch(
                        viewModel.outputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 2,
                        )
                    )
                }
            }
            .width(BorderBlockWidth)
            .clip(BlockShape)
            .background(ComplexBlockColor)
            .heightIn(min = BlockHeight)
            .background(ComplexBlockColor)
            .then(modifier)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        BreakPoint(
            block = block,
            color = viewModel.color,
            changeBreakPointColor = viewModel::changeBreakPointColor,
            modifier = Modifier.align(Alignment.TopEnd)
        )
        MainFlow(modifier = Modifier
            .align(Alignment.CenterStart)
            .clickable {
                setEndifTopFlow(block)

                viewModel.inputBranch = createEndBranch(
                    viewModel.inputBranch,
                    CharacteristicsBlock(
                        viewModel.offsetX,
                        viewModel.offsetY + viewModel.boxHeight / 2,
                    ),
                    true,
                    block.getId()
                )
            })
        MainFlow(modifier = Modifier
            .align(Alignment.BottomStart)
            .clickable {
                setEndifBottomFlow(block)

                viewModel.inputBranchForEndIf = createEndBranch(
                    viewModel.inputBranchForEndIf,
                    CharacteristicsBlock(
                        viewModel.offsetX,
                        viewModel.offsetY + viewModel.boxHeight / 1.25f,
                    ),
                    true,
                    block.getId()
                )
            })
        MainFlow(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable {
                    setPreviousMainFlowTrueBlock(block)

                    viewModel.outputBranch = createStartBranch(
                        viewModel.outputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 2,
                        ),
                        idStartBlock = block.getId()
                    )
                })
    }
}

@Composable
fun MovableInitializationBlock(
    value: BlockValue.InitializationBlock,
    block: InitializationVariableBlock,
    modifier: Modifier,
    viewModel: BlockViewModel
) {

    Column(
        modifier = Modifier
            .offset {
                IntOffset(
                    viewModel.offsetX.roundToInt(),
                    viewModel.offsetY.roundToInt()
                )
            }
            .onGloballyPositioned { coordinates ->
                viewModel.boxHeight = coordinates.size.height.toFloat()
                viewModel.boxWidth = coordinates.size.width.toFloat()
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    viewModel.offsetX += dragAmount.x
                    viewModel.offsetY += dragAmount.y

                    viewModel.inputBranch = updateEndBranch(
                        viewModel.inputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 2,
                        )
                    )
                    viewModel.outputBranch = updateStartBranch(
                        viewModel.outputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 2,
                        )
                    )
                }
            }
            .width(TextFieldBlockWidth)
            .clip(BlockShape)
            .background(ComplexBlockColor)
            .heightIn(min = BlockHeight)
            .then(modifier)

    ) {
        Box {
            ComplexBlockText(modifier = value.modifier, text = value.text)
            BreakPoint(
                block = block,
                color = viewModel.color,
                changeBreakPointColor = viewModel::changeBreakPointColor,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            MainFlow(modifier = Modifier.clickable {
                setMainFlow(block)

                viewModel.inputBranch = createEndBranch(
                    viewModel.inputBranch,
                    CharacteristicsBlock(
                        viewModel.offsetX,
                        viewModel.offsetY + viewModel.boxHeight / 2,
                    ),
                    true,
                    block.getId()
                )
            })
            MainFlow(modifier = Modifier.clickable {
                setPreviousMainFlowTrueBlock(block)

                viewModel.outputBranch = createStartBranch(
                    viewModel.outputBranch,
                    CharacteristicsBlock(
                        viewModel.offsetX + viewModel.boxWidth,
                        viewModel.offsetY + viewModel.boxHeight / 2,
                    ),
                    idStartBlock = block.getId()
                )
            })
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
    modifier: Modifier,
    viewModel: BlockViewModel
) {
    Column(
        modifier = Modifier
            .offset {
                IntOffset(
                    viewModel.offsetX.roundToInt(),
                    viewModel.offsetY.roundToInt()
                )
            }
            .onGloballyPositioned { coordinates ->
                viewModel.boxHeight = coordinates.size.height.toFloat()
                viewModel.boxWidth = coordinates.size.width.toFloat()
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    viewModel.offsetX += dragAmount.x
                    viewModel.offsetY += dragAmount.y

                    viewModel.inputBranch = updateEndBranch(
                        viewModel.inputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 2.75f,
                        )
                    )
                    viewModel.outputBranch = updateStartBranch(
                        viewModel.outputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 2.75f,
                        )
                    )
                    viewModel.outputSupportFLow = updateStartBranch(
                        viewModel.outputSupportFLow,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 1.35f,
                        )
                    )
                }
            }
            .width(TextFieldBlockWidth)
            .clip(BlockShape)
            .background(ComplexBlockColor)
            .heightIn(min = BlockHeight)
            .then(modifier)
    ) {
        Box {
            ComplexBlockText(modifier = value.modifier, text = value.text)
            BreakPoint(
                block = block,
                color = viewModel.color,
                changeBreakPointColor = viewModel::changeBreakPointColor,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            MainFlow(modifier = Modifier.clickable {
                setMainFlow(block)

                viewModel.inputBranch = createEndBranch(
                    viewModel.inputBranch,
                    CharacteristicsBlock(
                        viewModel.offsetX,
                        viewModel.offsetY + viewModel.boxHeight / 2.75f,
                    ),
                    true,
                    block.getId()
                )
            })
            MainFlow(modifier = Modifier.clickable {
                setPreviousMainFlowTrueBlock(block)

                viewModel.outputBranch = createStartBranch(
                    viewModel.outputBranch,
                    CharacteristicsBlock(
                        viewModel.offsetX + viewModel.boxWidth,
                        viewModel.offsetY + viewModel.boxHeight / 2.75f,
                    ),
                    idStartBlock = block.getId()
                )
            })
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
                    .clickable {
                        setPreviousSupportFlowBlock(block)

                        viewModel.outputSupportFLow = createStartBranch(
                            viewModel.outputSupportFLow,
                            CharacteristicsBlock(
                                viewModel.offsetX + viewModel.boxWidth,
                                viewModel.offsetY + viewModel.boxHeight / 1.35f,
                            ),
                            false,
                            idStartBlock = block.getId()
                        )
                    })
        }
    }
}


@Composable
fun MovableForBlock(
    value: BlockValue.ForBlock,
    modifier: Modifier,
    block: ForBlock,
    viewModel: BlockViewModel
) {
    Column(
        modifier = Modifier
            .offset {
                IntOffset(
                    viewModel.offsetX.roundToInt(),
                    viewModel.offsetY.roundToInt()
                )
            }
            .onGloballyPositioned { coordinates ->
                viewModel.boxHeight = coordinates.size.height.toFloat()
                viewModel.boxWidth = coordinates.size.width.toFloat()
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    viewModel.offsetX += dragAmount.x
                    viewModel.offsetY += dragAmount.y

                    viewModel.inputBranch = updateEndBranch(
                        viewModel.inputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 3.45f,
                        )
                    )
                    viewModel.outputBranchTrue = updateStartBranch(
                        viewModel.outputBranchTrue,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 3.45f,
                        )
                    )
                    viewModel.outputBranchFalse = updateStartBranch(
                        viewModel.outputBranchFalse,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 1.12f,
                        )
                    )
                }
            }
            .width(TextFieldBlockWidth)
            .clip(BlockShape)
            .background(ComplexBlockColor)
            .heightIn(min = BlockHeight)
            .then(modifier)
    ) {
        Box {
            ComplexBlockText(modifier = value.modifier, text = value.text)
            BreakPoint(
                block = block,
                color = viewModel.color,
                changeBreakPointColor = viewModel::changeBreakPointColor,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            MainFlow(modifier = Modifier.clickable {
                setMainFlow(block)

                viewModel.inputBranch = createEndBranch(
                    viewModel.inputBranch,
                    CharacteristicsBlock(
                        viewModel.offsetX,
                        viewModel.offsetY + viewModel.boxHeight / 3.45f,
                    ),
                    true,
                    block.getId()
                )
            })
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                TextForFlow(text = "loop")
                MainFlow(modifier = Modifier.clickable {
                    setPreviousMainFlowTrueBlock(block)

                    viewModel.outputBranchTrue = createStartBranch(
                        viewModel.outputBranchTrue,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 3.45f,
                        ),
                        idStartBlock = block.getId()
                    )
                })
            }
        }
        TextFieldForVariable(
            value = "conditional",
            modifier = Modifier.fillMaxWidth(),
            block = block
        )
        Row(modifier = Modifier.align(Alignment.End)) {
            TextForFlow(text = "endloop")
            MainFlow(modifier = Modifier.clickable {
                setPreviousMainFlowFalseBlock(block)

                viewModel.outputBranchFalse = createStartBranch(
                    viewModel.outputBranchFalse,
                    CharacteristicsBlock(
                        viewModel.offsetX + viewModel.boxWidth,
                        viewModel.offsetY + viewModel.boxHeight / 1.12f,
                    ),
                    idStartBlock = block.getId()
                )
            })
        }
    }
}

@Composable
fun MovableWhileBlock(
    value: BlockValue.WhileBlock,
    modifier: Modifier,
    block: WhileBlock,
    viewModel: BlockViewModel
) {
    Column(
        modifier = Modifier
            .offset {
                IntOffset(
                    viewModel.offsetX.roundToInt(),
                    viewModel.offsetY.roundToInt()
                )
            }
            .onGloballyPositioned { coordinates ->
                viewModel.boxHeight = coordinates.size.height.toFloat()
                viewModel.boxWidth = coordinates.size.width.toFloat()
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    viewModel.offsetX += dragAmount.x
                    viewModel.offsetY += dragAmount.y

                    viewModel.inputBranch = updateEndBranch(
                        viewModel.inputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 3.45f,
                        )
                    )
                    viewModel.outputBranchTrue = updateStartBranch(
                        viewModel.outputBranchTrue,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 3.45f,
                        )
                    )
                    viewModel.outputBranchFalse = updateStartBranch(
                        viewModel.outputBranchFalse,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 1.12f,
                        )
                    )
                }
            }
            .width(TextFieldBlockWidth)
            .clip(BlockShape)
            .background(ComplexBlockColor)
            .heightIn(min = BlockHeight)
            .then(modifier)
    ) {
        Box {
            ComplexBlockText(modifier = value.modifier, text = value.text)
            BreakPoint(
                block = block,
                color = viewModel.color,
                changeBreakPointColor = viewModel::changeBreakPointColor,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            MainFlow(modifier = Modifier.clickable {
                setMainFlow(block)

                viewModel.inputBranch = createEndBranch(
                    viewModel.inputBranch,
                    CharacteristicsBlock(
                        viewModel.offsetX,
                        viewModel.offsetY + viewModel.boxHeight / 3.45f,
                    ),
                    true,
                    block.getId()
                )
            })
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                TextForFlow(text = "loop")
                MainFlow(modifier = Modifier.clickable {
                    setPreviousMainFlowTrueBlock(block)

                    viewModel.outputBranchTrue = createStartBranch(
                        viewModel.outputBranchTrue,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 3.45f,
                        ),
                        idStartBlock = block.getId()
                    )
                })
            }
        }
        TextFieldForVariable(
            value = "conditional",
            modifier = Modifier.fillMaxWidth(),
            block = block
        )
        Row(modifier = Modifier.align(Alignment.End)) {
            TextForFlow(text = "endloop")
            MainFlow(modifier = Modifier.clickable {
                setPreviousMainFlowFalseBlock(block)

                viewModel.outputBranchFalse = createStartBranch(
                    viewModel.outputBranchFalse,
                    CharacteristicsBlock(
                        viewModel.offsetX + viewModel.boxWidth,
                        viewModel.offsetY + viewModel.boxHeight / 1.12f,
                    ),
                    idStartBlock = block.getId()
                )
            })
        }
    }
}


@Composable
fun MovableGetValueBlock(
    value: BlockValue.GetValueBlock,
    modifier: Modifier,
    block: GetVariableBlock,
    viewModel: BlockViewModel
) {
    Column(
        modifier = Modifier
            .offset {
                IntOffset(
                    viewModel.offsetX.roundToInt(),
                    viewModel.offsetY.roundToInt()
                )
            }
            .onGloballyPositioned { coordinates ->
                viewModel.boxHeight = coordinates.size.height.toFloat()
                viewModel.boxWidth = coordinates.size.width.toFloat()
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    viewModel.offsetX += dragAmount.x
                    viewModel.offsetY += dragAmount.y

                    viewModel.outputSupportFLow = updateStartBranch(
                        viewModel.outputSupportFLow,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 1.5f,
                        )
                    )
                }
            }
            .width(TextFieldBlockWidth)
            .clip(BlockShape)
            .background(ComplexBlockColor)
            .heightIn(min = BlockHeight)
            .then(modifier)
    ) {
        Box {
            ComplexBlockText(modifier = value.modifier, text = value.text)
            BreakPoint(
                block = block,
                color = viewModel.color,
                changeBreakPointColor = viewModel::changeBreakPointColor,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
        Row {
            TextFieldForVariable(
                value = "expression",
                modifier = Modifier.weight(1f),
                block = block
            )
            SupportingFlow(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable {
                        setPreviousSupportFlowBlock(block)

                        viewModel.outputSupportFLow = createStartBranch(
                            viewModel.outputSupportFLow,
                            CharacteristicsBlock(
                                viewModel.offsetX + viewModel.boxWidth,
                                viewModel.offsetY + viewModel.boxHeight / 1.5f,
                            ),
                            false,
                            block.getId()
                        )
                    })
        }
    }
}

@Composable
fun MovableFunctionBlock(
    value: BlockValue.FunctionBlock,
    modifier: Modifier,
    block: FunctionBlock,
    viewModel: BlockViewModel
) {
    Column(
        modifier = Modifier
            .offset {
                IntOffset(
                    viewModel.offsetX.roundToInt(),
                    viewModel.offsetY.roundToInt()
                )
            }
            .onGloballyPositioned { coordinates ->
                viewModel.boxHeight = coordinates.size.height.toFloat()
                viewModel.boxWidth = coordinates.size.width.toFloat()
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    viewModel.offsetX += dragAmount.x
                    viewModel.offsetY += dragAmount.y

                    viewModel.outputBranch = updateStartBranch(
                        viewModel.outputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX + viewModel.boxWidth,
                            viewModel.offsetY + viewModel.boxHeight / 1.5f,
                        )
                    )
                }
            }
            .width(TextFieldBlockWidth)
            .clip(BlockShape)
            .background(ComplexBlockColor)
            .heightIn(min = BlockHeight)
            .then(modifier)
    ) {
        Box {
            ComplexBlockText(modifier = value.modifier, text = value.text)
            BreakPoint(
                block = block,
                color = viewModel.color,
                changeBreakPointColor = viewModel::changeBreakPointColor,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
        Row {
            TextFieldForVariable(
                value = "name(args)",
                modifier = Modifier.weight(1f),
                block = block
            )
            MainFlow(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable {
                        setPreviousMainFlowTrueBlock(block)

                        viewModel.outputBranch = createStartBranch(
                            viewModel.outputBranch,
                            CharacteristicsBlock(
                                viewModel.offsetX + viewModel.boxWidth,
                                viewModel.offsetY + viewModel.boxHeight / 1.5f,
                            ),
                            idStartBlock = block.getId()
                        )
                    })
        }
    }
}

@Composable
fun MovableReturnBlock(
    value: BlockValue.ReturnBlock,
    modifier: Modifier,
    block: EndFunctionBlock,
    viewModel: BlockViewModel
) {
    Column(
        modifier = Modifier
            .offset {
                IntOffset(
                    viewModel.offsetX.roundToInt(),
                    viewModel.offsetY.roundToInt()
                )
            }
            .onGloballyPositioned { coordinates ->
                viewModel.boxHeight = coordinates.size.height.toFloat()
                viewModel.boxWidth = coordinates.size.width.toFloat()
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    viewModel.offsetX += dragAmount.x
                    viewModel.offsetY += dragAmount.y

                    viewModel.inputBranch = updateEndBranch(
                        viewModel.inputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 5,
                        )
                    )
                    viewModel.inputSupportFLow = updateEndBranch(
                        viewModel.inputSupportFLow,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 1.2f,
                        )
                    )
                }
            }
            .width(BorderBlockWidth)
            .clip(BlockShape)
            .background(ComplexBlockColor)
            .heightIn(min = BlockHeight)
            .then(modifier)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            MainFlow(modifier = Modifier.clickable {
                setMainFlow(block)

                viewModel.inputBranch = createEndBranch(
                    viewModel.inputBranch,
                    CharacteristicsBlock(
                        viewModel.offsetX,
                        viewModel.offsetY + viewModel.boxHeight / 5,
                    ),
                    true,
                    (block as BlockEntity).getId()
                )
            })
            BreakPoint(
                block = block,
                color = viewModel.color,
                changeBreakPointColor = viewModel::changeBreakPointColor,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
        ComplexBlockText(
            modifier = value.modifier.align(Alignment.CenterHorizontally),
            text = value.text
        )
        Row {
            SupportingFlow(modifier = Modifier.clickable {
                setUnaryOperatorFlow(block)

                viewModel.inputSupportFLow = createEndBranch(
                    viewModel.inputSupportFLow,
                    CharacteristicsBlock(
                        viewModel.offsetX,
                        viewModel.offsetY + viewModel.boxHeight / 1.2f,
                    ),
                    false,
                    (block as BlockEntity).getId()
                )
            })
            TextForFlow(text = "value")
        }
    }
}

@Composable
fun MovableContinueOrBreakBlock(
    value: BlockValue,
    modifier: Modifier,
    block: IMainFLowBlock,
    viewModel: BlockViewModel
) {
    Column(
        modifier = Modifier
            .offset {
                IntOffset(
                    viewModel.offsetX.roundToInt(),
                    viewModel.offsetY.roundToInt()
                )
            }
            .onGloballyPositioned { coordinates ->
                viewModel.boxHeight = coordinates.size.height.toFloat()
                viewModel.boxWidth = coordinates.size.width.toFloat()
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    viewModel.offsetX += dragAmount.x
                    viewModel.offsetY += dragAmount.y

                    viewModel.inputBranch = updateEndBranch(
                        viewModel.inputBranch,
                        CharacteristicsBlock(
                            viewModel.offsetX,
                            viewModel.offsetY + viewModel.boxHeight / 5,
                        )
                    )
                }
            }
            .width(BorderBlockWidth)
            .clip(BlockShape)
            .background(ComplexBlockColor)
            .heightIn(min = BlockHeight)
            .then(modifier)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            MainFlow(modifier = Modifier.clickable {
                setMainFlow(block)

                viewModel.inputBranch = createEndBranch(
                    viewModel.inputBranch,
                    CharacteristicsBlock(
                        viewModel.offsetX,
                        viewModel.offsetY + viewModel.boxHeight / 5,
                    ),
                    true,
                    (block as BlockEntity).getId()
                )
            })
            BreakPoint(
                block = block as BlockEntity,
                color = viewModel.color,
                changeBreakPointColor = viewModel::changeBreakPointColor,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
        ComplexBlockText(
            modifier = value.modifier.align(Alignment.CenterHorizontally),
            text = value.text
        )
    }
}


