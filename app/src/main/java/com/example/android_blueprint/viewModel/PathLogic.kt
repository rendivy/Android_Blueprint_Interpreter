package com.example.android_blueprint.viewModel

import androidx.compose.runtime.mutableStateOf
import com.example.android_blueprint.model.BranchEntity
import com.example.android_blueprint.model.BranchType
import com.example.android_blueprint.model.CharacteristicsBlock
import com.example.android_blueprint.ui.theme.ZeroOffset


val defaultBranch = BranchEntity(mutableStateOf(ZeroOffset), mutableStateOf(ZeroOffset), idStartBlock = -1)
var branchInWorking: BranchEntity = defaultBranch


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
        mutableStateOf(characteristicsBlock.xResult),
        mutableStateOf(characteristicsBlock.yResult),
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
    return if (branchInWorking.getId() == defaultBranch.getId()) {
        inputBranch
    } else if (branchInWorking.isMainFlowBranch != isMainFlow ||
        branchInWorking.idStartBlock == idFinishBlock
    ) {
        defaultBranch
    } else {
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
    if (outputBranch.idFinishBlock == -1) {
        outputBranch.xStart.value = characteristicsBlock.xResult
        outputBranch.yStart.value = characteristicsBlock.yResult
        return outputBranch
    }

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

fun selectBranch(
    branchType: BranchType,
    viewModel: BlockViewModel
): BranchEntity {
    return when (branchType) {
        BranchType.MainFlowOutput -> viewModel.outputBranch
        BranchType.MainFlowInput -> viewModel.inputBranch
        BranchType.SupportFlowOutput -> viewModel.outputSupportFLow
        BranchType.SupportFlowInput -> viewModel.inputSupportFLow
        BranchType.MainFlowInputForEndIf -> viewModel.inputBranchForEndIf
        BranchType.MainFlowOutputTrue -> viewModel.outputBranchTrue
        BranchType.MainFlowOutputFalse -> viewModel.outputBranchFalse
        BranchType.SupportFlowInputLeft -> viewModel.inputSupportFLowLeft
        BranchType.SupportFlowInputRight -> viewModel.inputSupportFLowRight
    }
}

fun selectCreateStartBranch(
    viewModel: BlockViewModel,
    branchType: BranchType,
    isMainFlow: Boolean,
    idStartBlock: Int,
    scale: Float
): BranchEntity {
    val startX = viewModel.offsetX + viewModel.boxWidth
    val startY = viewModel.offsetY + viewModel.boxHeight / scale

    return createStartBranch(
        selectBranch(branchType, viewModel),
        CharacteristicsBlock(startX, startY),
        isMainFlow,
        idStartBlock
    )
}

fun selectUpdateStartBranch(
    viewModel: BlockViewModel,
    branchType: BranchType,
    scale: Float
): BranchEntity {
    val startX = viewModel.offsetX + viewModel.boxWidth
    val startY = viewModel.offsetY + viewModel.boxHeight / scale

    return updateStartBranch(
        selectBranch(branchType, viewModel),
        CharacteristicsBlock(startX, startY)
    )
}

fun selectCreateEndBranch(
    viewModel: BlockViewModel,
    branchType: BranchType,
    isMainFlow: Boolean,
    idFinishBlock: Int,
    scale: Float
): BranchEntity {
    val endX = viewModel.offsetX
    val endY = viewModel.offsetY + viewModel.boxHeight / scale

    return createEndBranch(
        selectBranch(branchType, viewModel),
        CharacteristicsBlock(endX, endY),
        isMainFlow,
        idFinishBlock
    )
}

fun selectUpdateEndBranch(
    viewModel: BlockViewModel,
    branchType: BranchType,
    scale: Float
): BranchEntity {
    val endX = viewModel.offsetX
    val endY = viewModel.offsetY + viewModel.boxHeight / scale

    return updateEndBranch(
        selectBranch(branchType, viewModel),
        CharacteristicsBlock(endX, endY)
    )
}
