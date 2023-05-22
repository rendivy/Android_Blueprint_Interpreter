package com.example.android_blueprint.viewModel

import block.BlockEntity
import block.BlockEntity.Companion.getBlocks
import block.IBinaryOperatorBlock
import block.IBranchesBlock
import block.IMainFLowBlock
import block.IUnaryOperatorBlock
import block.StartBlock
import com.example.android_blueprint.model.PreviousBlocks


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

fun setPreviousMainFlowTrueBlock(block: BlockEntity) {
    InfiniteFieldViewModel.previousBlocks = PreviousBlocks(previousMainFlowTrueBlock = block)
}

fun setPreviousMainFlowFalseBlock(block: BlockEntity) {
    InfiniteFieldViewModel.previousBlocks = PreviousBlocks(previousMainFlowFalseBlock = block)
}

fun setPreviousSupportFlowBlock(block: BlockEntity) {
    InfiniteFieldViewModel.previousBlocks = PreviousBlocks(previousSupportFlowBlock = block)
}

fun start(startBlock: StartBlock) {
    val interpreter = interpretator.Interpret(BlockEntity.getBlocks())
    interpreter.run(startBlock)
}