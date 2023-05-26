package com.example.android_blueprint.viewModel

import androidx.compose.runtime.mutableStateOf
import com.example.android_blueprint.model.BranchEntity
import com.example.android_blueprint.model.CharacteristicsBlock



val defaultBranch = BranchEntity(mutableStateOf(0f), mutableStateOf(0f), idStartBlock = -1)
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