package block

import Instruction
import memory.Valuable
import java.io.Serializable

abstract class BlockEntity(
    private val instruction: Instruction = Instruction.OPERATOR_BLOCK
) : Serializable {
    companion object {
        private var nextId = 0

        private var Blocks = mutableListOf<BlockEntity>()
        fun getBlocks(): List<BlockEntity> {
            return Blocks
        }

        fun deletePreviousBlockMainFlowBranch(block: BlockEntity) {
            if ((block as IMainFLowBlock).previousMainFlowBlocks != null) {
                (block.previousMainFlowBlocks!! as IMainFLowBlock).nextMainFlowBlocks = null
                block.previousMainFlowBlocks = null
            }
        }

        private fun deleteOperatorBlock(block: BlockEntity) {
            Blocks.forEach() {
                if (it is IBinaryOperatorBlock) {
                    if (it.leftValuable == block) {
                        it.leftValuable = null
                        return
                    }
                    if (it.rightValuable == block) {
                        it.rightValuable = null
                        return
                    }
                }
                if (it is IUnaryOperatorBlock) {
                    if (it.valuable == block) {
                        it.valuable = null
                        return
                    }
                }
            }
        }

        fun deleteBlock(block: BlockEntity) {
            when (block) {
                is IBranchesBlock -> {
                    if ((block as IMainFLowBlock).previousMainFlowBlocks != null) {
                        (block.previousMainFlowBlocks!! as IMainFLowBlock).nextMainFlowBlocks = null
                    }
                    if (block.getTrueExpressionBranch() != null) {
                        (block.getTrueExpressionBranch()!! as IMainFLowBlock).previousMainFlowBlocks = null
                    }
                    if (block.getFalseExpressionBranch() != null) {
                        (block.getFalseExpressionBranch()!! as IMainFLowBlock).previousMainFlowBlocks = null
                    }
                }

                is EndIfBlock -> {
                    if (block.nextMainFlowBlocks != null) {
                        (block.nextMainFlowBlocks!! as IMainFLowBlock).previousMainFlowBlocks = null
                    }
                    if (block.getTrueExpressionBranch() != null) {
                        (block.getTrueExpressionBranch()!! as IMainFLowBlock).nextMainFlowBlocks = null
                    }
                    if (block.getFalseExpressionBranch() != null) {
                        (block.getFalseExpressionBranch()!! as IMainFLowBlock).nextMainFlowBlocks = null
                    }
                }

                is IMainFLowBlock -> {
                    if ((block as IMainFLowBlock).previousMainFlowBlocks != null) {
                        (block.previousMainFlowBlocks!! as IMainFLowBlock).nextMainFlowBlocks = null
                    }
                    if ((block as IMainFLowBlock).nextMainFlowBlocks != null) {
                        (block.nextMainFlowBlocks!! as IMainFLowBlock).previousMainFlowBlocks = null
                    }
                }

                is IUnaryOperatorBlock, is IBinaryOperatorBlock -> {
                    deleteOperatorBlock(block)
                }
            }
            Blocks.removeIf { it.getId() == block.getId() }
        }
    }

    private val id = nextId++
    private var breakPoint = false

    init {
        Blocks.add(this)
    }

    fun getId(): Int {
        return id
    }

    fun setBreakPoint(breakPoint: Boolean) {
        this.breakPoint = breakPoint
    }

    fun getBreakPoint(): Boolean {
        return breakPoint
    }

    fun getInstruction(): Instruction {
        return instruction
    }

    abstract fun validate()
}

interface IUnaryOperatorBlock : IGetValuable {
    var valuable: BlockEntity?
    fun setOperator(valuable: BlockEntity) {
        this.valuable = valuable
    }
}

interface IBinaryOperatorBlock : IGetValuable {
    var leftValuable: BlockEntity?
    var rightValuable: BlockEntity?
    fun setLeftOperator(valuable: BlockEntity) {
        leftValuable = valuable
    }

    fun setRightOperator(valuable: BlockEntity) {
        rightValuable = valuable
    }
}

interface IGetValuable {
    fun getValue(): Valuable
}

interface IWorkingWithVariables {
    var parsed: String
}

interface IMainFLowBlock {
    var previousMainFlowBlocks: BlockEntity?
    var nextMainFlowBlocks: BlockEntity?

    fun setPreviousMainFlowBlock(block: BlockEntity) {
        previousMainFlowBlocks = block
        (block as IMainFLowBlock).nextMainFlowBlocks = (this as BlockEntity)
    }

    fun setNextMainFlowBlock(block: BlockEntity) {
        nextMainFlowBlocks = block
        (block as IMainFLowBlock).previousMainFlowBlocks = (this as BlockEntity)
    }
}

interface IExecutable {
    fun execute()
}

interface IBranchesBlock {
    var trueExpressionBranch: BlockEntity?
    var falseExpressionBranch: BlockEntity?

    fun setTrueExpressionBranch(block: BlockEntity) {
        trueExpressionBranch = block
        (block as IMainFLowBlock).previousMainFlowBlocks = (this as BlockEntity)
    }

    fun setFalseExpressionBranch(block: BlockEntity) {
        falseExpressionBranch = block
        (block as IMainFLowBlock).previousMainFlowBlocks = (this as BlockEntity)
    }

    fun getTrueExpressionBranch(): BlockEntity? {
        return trueExpressionBranch
    }

    fun getFalseExpressionBranch(): BlockEntity? {
        return falseExpressionBranch
    }
}

fun BlockEntity.getValueFromOperatorBlocks(): Valuable {
    return when (this) {
        is IUnaryOperatorBlock -> this.getValue()
        is IBinaryOperatorBlock -> this.getValue()
        is IGetValuable -> this.getValue()
        else -> throw Exception("BlockEntity is not an operator block")
    }
}
