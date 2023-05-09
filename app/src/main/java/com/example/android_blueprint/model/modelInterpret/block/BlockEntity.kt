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

        fun updateBlock(block: BlockEntity) {
            val oldBlock = Blocks.find { it.getId() == block.getId() }
            when (block.getInstruction()) {
                Instruction.IF -> {
                    (oldBlock as IfBlock).previousMainFlowBlocks?.let {
                        (it as IMainFLowBlock).setNextMainFlowBlock(
                            block
                        )
                    }
                    oldBlock.getTrueExpressionBranch()?.let { (it as IMainFLowBlock).setPreviousMainFlowBlock(block) }
                    oldBlock.getFalseExpressionBranch()?.let { (it as IMainFLowBlock).setPreviousMainFlowBlock(block) }
                }

                Instruction.END_IF -> {
                    (oldBlock as EndIfBlock).nextMainFlowBlocks?.let {
                        (it as IMainFLowBlock).setPreviousMainFlowBlock(
                            block
                        )
                    }
                    oldBlock.getFalseExpressionBranch()?.let { (it as IMainFLowBlock).setNextMainFlowBlock(block) }
                    oldBlock.getTrueExpressionBranch()?.let { (it as IMainFLowBlock).setNextMainFlowBlock(block) }
                }

                else -> {
                    when (oldBlock) {
                        is IMainFLowBlock -> {
                            (oldBlock as IMainFLowBlock).nextMainFlowBlocks?.let {
                                (it as IMainFLowBlock).setPreviousMainFlowBlock(
                                    block
                                )
                            }
                            (oldBlock as IMainFLowBlock).previousMainFlowBlocks?.let {
                                (it as IMainFLowBlock).setNextMainFlowBlock(
                                    block
                                )
                            }
                        }

                        is IUnaryOperatorBlock -> {
                            updateOperatorBlocks(oldBlock, block)
                        }

                        is IBinaryOperatorBlock -> {
                            updateOperatorBlocks(oldBlock, block)
                        }
                    }
                }
            }
            Blocks.removeIf { it.getId() == block.getId() }
            Blocks.add(block)
            block.validate()
        }

        private fun updateOperatorBlocks(oldBlock: BlockEntity, newBlock: BlockEntity) {
            Blocks.forEach() {
                if (it is IBinaryOperatorBlock) {
                    if (it.leftValuable == oldBlock) {
                        it.leftValuable = newBlock
                    }
                    if (it.rightValuable == oldBlock) {
                        it.rightValuable = newBlock
                    }
                }
                if (it is IUnaryOperatorBlock) {
                    if (it.valuable == oldBlock) {
                        it.valuable = newBlock
                    }
                }
            }
        }

        private fun deleteOperatorBlock(block: BlockEntity) {
            Blocks.forEach() {
                if (it is IBinaryOperatorBlock) {
                    if (it.leftValuable == block) {
                        it.leftValuable = null
                    }
                    if (it.rightValuable == block) {
                        it.rightValuable = null
                    }
                }
                if (it is IUnaryOperatorBlock) {
                    if (it.valuable == block) {
                        it.valuable = null
                    }
                }
            }
        }

        fun deleteBlock(block: BlockEntity) {
            when (block) {
                is IfBlock -> {
                    if (block.previousMainFlowBlocks != null) {
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

                is IUnaryOperatorBlock -> {
                    deleteOperatorBlock(block)
                }

                is IBinaryOperatorBlock -> {
                    deleteOperatorBlock(block)

                }
            }
            Blocks.removeIf { it.getId() == block.getId() }
        }
    }

    private val id = nextId++

    init {
        Blocks.add(this)
    }

    fun getId(): Int {
        return id
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
    }

    fun setNextMainFlowBlock(block: BlockEntity) {
        nextMainFlowBlocks = block
    }
}

interface IExecutable {
    fun execute()
}

fun BlockEntity.getValueFromOperatorBlocks(): Valuable {
    return when (this) {
        is IUnaryOperatorBlock -> this.getValue()
        is IBinaryOperatorBlock -> this.getValue()
        is IGetValuable -> this.getValue()
        else -> throw Exception("BlockEntity is not an operator block")
    }
}
