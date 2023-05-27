package com.example.android_blueprint.viewModel

import androidx.compose.ui.graphics.Color
import block.BlockEntity
import block.EndIfBlock
import block.IBinaryOperatorBlock
import block.IBranchesBlock
import block.IMainFLowBlock
import block.IUnaryOperatorBlock
import block.StartBlock
import com.example.android_blueprint.model.PreviousBlocks
import com.example.android_blueprint.ui.theme.EMPTY_STRING
import com.example.android_blueprint.ui.theme.PrimaryColor
import interpretator.Interpret
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


fun setBottomFlowOperator(block: IBinaryOperatorBlock) {
    if (InfiniteFieldViewModel.previousBlocks.previousSupportFlowBlock == null) return
    block.setRightOperator(InfiniteFieldViewModel.previousBlocks.previousSupportFlowBlock!!)
    InfiniteFieldViewModel.previousBlocks = PreviousBlocks()
}

fun setTopFlowOperator(block: IBinaryOperatorBlock) {
    if (InfiniteFieldViewModel.previousBlocks.previousSupportFlowBlock == null) return
    block.setLeftOperator(InfiniteFieldViewModel.previousBlocks.previousSupportFlowBlock!!)
    InfiniteFieldViewModel.previousBlocks = PreviousBlocks()
}

fun setUnaryOperatorFlow(block: IUnaryOperatorBlock) {
    if (InfiniteFieldViewModel.previousBlocks.previousSupportFlowBlock == null) return
    block.setOperator(InfiniteFieldViewModel.previousBlocks.previousSupportFlowBlock!!)
}

fun setMainFlow(block: IMainFLowBlock) {
    if (InfiniteFieldViewModel.previousBlocks.previousMainFlowTrueBlock is IBranchesBlock) {
        (InfiniteFieldViewModel.previousBlocks.previousMainFlowTrueBlock as IBranchesBlock).setTrueBranchExpression(
            block as BlockEntity
        )
    } else if (InfiniteFieldViewModel.previousBlocks.previousMainFlowFalseBlock is IBranchesBlock) {
        (InfiniteFieldViewModel.previousBlocks.previousMainFlowFalseBlock as IBranchesBlock).setFalseBranchExpression(
            block as BlockEntity
        )
    } else if (InfiniteFieldViewModel.previousBlocks.previousMainFlowTrueBlock != null) {
        block.setPreviousMainFlowBlock(InfiniteFieldViewModel.previousBlocks.previousMainFlowTrueBlock!!)
    }
    InfiniteFieldViewModel.previousBlocks = PreviousBlocks()
}

fun setEndifTopFlow(block: EndIfBlock) {
    if (InfiniteFieldViewModel.previousBlocks.previousMainFlowTrueBlock != null) {
        block.setTrueExpressionBranch(
            InfiniteFieldViewModel.previousBlocks.previousMainFlowTrueBlock!!,
            flag = true
        )
    } else if (InfiniteFieldViewModel.previousBlocks.previousMainFlowFalseBlock != null) {
        block.setTrueExpressionBranch(
            InfiniteFieldViewModel.previousBlocks.previousMainFlowFalseBlock!!,
            flag = false
        )
    }
    InfiniteFieldViewModel.previousBlocks = PreviousBlocks()
}

fun setEndifBottomFlow(block: EndIfBlock) {
    if (InfiniteFieldViewModel.previousBlocks.previousMainFlowTrueBlock != null) {
        block.setFalseExpressionBranch(
            InfiniteFieldViewModel.previousBlocks.previousMainFlowTrueBlock!!,
            flag = true
        )
    } else if (InfiniteFieldViewModel.previousBlocks.previousMainFlowFalseBlock != null) {
        block.setFalseExpressionBranch(
            InfiniteFieldViewModel.previousBlocks.previousMainFlowFalseBlock!!,
            flag = false
        )
    }
    InfiniteFieldViewModel.previousBlocks = PreviousBlocks()
}


fun setPreviousMainFlowTrueBlock(block: BlockEntity) {
    InfiniteFieldViewModel.previousBlocks = PreviousBlocks(previousMainFlowTrueBlock = block)
}

fun setPreviousMainFlowFalseBlock(block: BlockEntity) {
    InfiniteFieldViewModel.previousBlocks = PreviousBlocks(previousMainFlowFalseBlock = block)
}

fun setPreviousSupportFlowBlock(block: BlockEntity) {
    InfiniteFieldViewModel.previousBlocks = PreviousBlocks(previousSupportFlowBlock = block)
}

@OptIn(DelicateCoroutinesApi::class)
fun start(
    startBlock: StartBlock,
    interpreter: Interpret,
    openDebugger: () -> Unit,
    closeDebugger: () -> Unit,
) {
    if (BlockEntity.checkBreakPointInBlocks()) {
        openDebugger()
    }
    GlobalScope.launch {
        ConsoleViewModel.consoleText = EMPTY_STRING
        ConsoleViewModel.defaultTextColor = PrimaryColor
        try {
            interpreter.run(startBlock)
            closeDebugger()
        } catch (e: Exception) {
            ConsoleViewModel.defaultTextColor = Color.Red
            ConsoleViewModel.consoleText += e.message.toString()
        }
    }
}