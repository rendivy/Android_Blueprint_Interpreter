package Block

import Instruction
import Valuable

abstract class BlockEntity(
    private val id: Int,
    private val instruction: Instruction = Instruction.OPERATOR_BLOCK
) {
    private val previousMainFlowBlocksId: MutableList<BlockEntity> = mutableListOf()
    private val previousSupportingFlowBlocksId: MutableList<BlockEntity> = mutableListOf()
    private val nextMainFlowBlocksId: MutableList<BlockEntity> = mutableListOf()

    fun getId(): Int {
        return id
    }

    fun getInstruction(): Instruction {
        return instruction
    }

    fun addPreviousMainFlowBlock(block: BlockEntity) {
        previousMainFlowBlocksId.add(block)
    }

    fun addPreviousSupportingFlowBlock(block: BlockEntity) {
        previousSupportingFlowBlocksId.add(block)
    }

    fun addNextMainFlowBlock(block: BlockEntity) {
        nextMainFlowBlocksId.add(block)
    }

    fun getPreviousMainFlowBlocks(): List<BlockEntity> {
        return previousMainFlowBlocksId
    }

    fun getPreviousSupportingFlowBlocks(): List<BlockEntity> {
        return previousSupportingFlowBlocksId
    }

    fun getNextMainFlowBlocks(): List<BlockEntity> {
        return nextMainFlowBlocksId
    }

    fun removePreviousMainFlowBlock(blockId: Int) {
        previousMainFlowBlocksId.removeIf { it.getId() == blockId }
    }

    fun removePreviousSupportingFlowBlock(blockId: Int) {
        previousSupportingFlowBlocksId.removeIf { it.getId() == blockId }
    }

    fun removeNextMainFlowBlock(blockId: Int) {
        nextMainFlowBlocksId.removeIf { it.getId() == blockId }
    }

    abstract fun validate()
}

interface IUnaryOperatorBlock{
    var valuable: BlockEntity?
    fun getValue(): Valuable
    fun setOperator(valuable: BlockEntity){
        this.valuable = valuable
    }
}

interface IBinaryOperatorBlock{
    var leftValuable: BlockEntity?
    var rightValuable: BlockEntity?
    fun getValue(): Valuable
    fun setLeftOperator(valuable: BlockEntity) {
        leftValuable = valuable
    }
    fun setRightOperator(valuable: BlockEntity) {
        rightValuable = valuable
    }
}

interface IGetValuable{
    fun getValue(): Valuable
}

fun BlockEntity.getValueFromOperatorBlocks(): Valuable {
    return when (this) {
        is IUnaryOperatorBlock -> this.getValue()
        is IBinaryOperatorBlock -> this.getValue()
        is IGetValuable -> this.getValue()
        else -> throw Exception("BlockEntity is not an operator block")
    }
}

fun checkIfBlockIsNullOrThrow(block: BlockEntity?) {
    if (block == null) {
        throw NullPointerException("BlockEntity is null")
    }
}
