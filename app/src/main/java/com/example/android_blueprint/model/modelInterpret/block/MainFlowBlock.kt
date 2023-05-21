package block

import Instruction
import exceptions.NullPointerExceptionInOperator
import com.example.android_blueprint.viewModel.ConsoleViewModel
import memory.Valuable

class StartBlock(
    instruction: Instruction = Instruction.START_POINT
) : BlockEntity(instruction), IMainFLowBlock {
    override var nextMainFlowBlocks: BlockEntity? = null
    override var previousMainFlowBlocks: BlockEntity? = null

    override fun validate() {
        if (nextMainFlowBlocks == null) {
            throw NullPointerExceptionInOperator("Start block must have next block")
        }
    }
}

class EndBlock(
    instruction: Instruction = Instruction.END_POINT
) : BlockEntity(instruction), IMainFLowBlock {
    override var nextMainFlowBlocks: BlockEntity? = null
    override var previousMainFlowBlocks: BlockEntity? = null

    override fun validate() {
        if (previousMainFlowBlocks == null) {
            throw NullPointerExceptionInOperator("End block must have previous block")
        }
    }
}

class PrintBlock(
    instruction: Instruction = Instruction.PRINT
) : BlockEntity(instruction), IMainFLowBlock, IUnaryOperatorBlock, IExecutable {
    override var valuable: BlockEntity? = null
    override var nextMainFlowBlocks: BlockEntity? = null
    override var previousMainFlowBlocks: BlockEntity? = null
    private fun printValue() {
        val curr = (valuable as IGetValuable).getValue()
        if (curr.array.isEmpty()) {
            ConsoleViewModel.consoleText += curr.value  + '\n'
            println(curr.value)
        } else {
            for (i in curr.array.indices) {
                ConsoleViewModel.consoleText += curr.array[i].value + " "
                print(curr.array[i].value + " ")
            }
            ConsoleViewModel.consoleText += '\n'
            println()
        }
    }

    override fun execute() {
        printValue()
    }

    override fun getValue(): Valuable {
        return when (valuable) {
            is IGetValuable -> {
                (valuable as IGetValuable).getValue()
            }

            else -> {
                throw NullPointerExceptionInOperator("Print block must have valuable")
            }

        }
    }

    override fun validate() {
        if (previousMainFlowBlocks == null) {
            throw NullPointerExceptionInOperator("Print block must have previous block")
        }
    }
}


class IfBlock(
    instruction: Instruction = Instruction.IF
) : BlockEntity(instruction), IUnaryOperatorBlock, IExecutable, IMainFLowBlock, IBranchesBlock {
    override var valuable: BlockEntity? = null

    override var nextMainFlowBlocks: BlockEntity? = null
    override var previousMainFlowBlocks: BlockEntity? = null

    override var falseExpressionBranch: BlockEntity? = null
    override var trueExpressionBranch: BlockEntity? = null

    override fun getValue(): Valuable {
        return (valuable as IGetValuable).getValue()
    }

    override fun execute() {
        val valuableExpressionInIf = getValue()
        nextMainFlowBlocks = if (valuableExpressionInIf.convertToBool(valuableExpressionInIf)) {
            trueExpressionBranch
        } else {
            falseExpressionBranch
        }
    }

    override fun validate() {
        if (previousMainFlowBlocks == null) {
            throw NullPointerExceptionInOperator("If block must have previous block")
        }
        if (trueExpressionBranch == null || falseExpressionBranch == null) {
            throw NullPointerExceptionInOperator("If block must have two expression branches")
        }
    }
}

class EndIfBlock(
    instruction: Instruction = Instruction.END_IF
) : BlockEntity(instruction), IMainFLowBlock {
    override var nextMainFlowBlocks: BlockEntity? = null

    override var previousMainFlowBlocks: BlockEntity? = null

    data class EndIfBlock(
        var trueExpressionBranch: BlockEntity?,
        var falseExpressionBranch: BlockEntity?
    )

    var endIfBlock = EndIfBlock(null, null)

    fun setTrueExpressionBranch(block: BlockEntity) {
        endIfBlock.trueExpressionBranch = block
    }

    fun setFalseExpressionBranch(block: BlockEntity) {
        endIfBlock.falseExpressionBranch = block
    }

    fun getFalseExpressionBranch(): BlockEntity? {
        return endIfBlock.falseExpressionBranch
    }

    fun getTrueExpressionBranch(): BlockEntity? {
        return endIfBlock.trueExpressionBranch
    }

    fun deleteTrueExpressionBranch() {
        (endIfBlock.trueExpressionBranch as IMainFLowBlock).nextMainFlowBlocks = null
        endIfBlock.trueExpressionBranch = null
    }

    fun deleteFalseExpressionBranch() {
        (endIfBlock.falseExpressionBranch as IMainFLowBlock).nextMainFlowBlocks = null
        endIfBlock.falseExpressionBranch = null
    }

    override fun validate() {
        if (endIfBlock.trueExpressionBranch == null || endIfBlock.falseExpressionBranch == null) {
            throw NullPointerExceptionInOperator("EndIf block must have two expression branches")
        }
    }
}

class ForBlock(
    instruction: Instruction = Instruction.FOR
) : BlockEntity(instruction), IMainFLowBlock, IBranchesBlock {
    override var nextMainFlowBlocks: BlockEntity? = null
    override var previousMainFlowBlocks: BlockEntity? = null

    override var falseExpressionBranch: BlockEntity? = null
    override var trueExpressionBranch: BlockEntity? = null

    private var rawInput = ""

    fun setRawInput(rawInput: String) {
        this.rawInput = rawInput
    }

    fun getRawInput(): String {
        return rawInput
    }

    override fun validate() {
        if (previousMainFlowBlocks == null) {
            throw NullPointerExceptionInOperator("For block must have previous block")
        }
        if (rawInput == "") {
            throw NullPointerExceptionInOperator("For block must have expression")
        }
    }
}

class WhileBlock(
    instruction: Instruction = Instruction.WHILE
) : BlockEntity(instruction), IMainFLowBlock, IBranchesBlock {
    override var nextMainFlowBlocks: BlockEntity? = null
    override var previousMainFlowBlocks: BlockEntity? = null

    override var falseExpressionBranch: BlockEntity? = null
    override var trueExpressionBranch: BlockEntity? = null

    private var rawInput = ""

    fun setRawInput(rawInput: String) {
        this.rawInput = rawInput
    }

    fun getRawInput(): String {
        return rawInput
    }

    override fun validate() {
        if (previousMainFlowBlocks == null) {
            throw NullPointerExceptionInOperator("While block must have previous block")
        }
        if (rawInput == "") {
            throw NullPointerExceptionInOperator("While block must have expression")
        }
    }
}

class FunctionBlock(
    instruction: Instruction = Instruction.FUNC
) : BlockEntity(instruction), IMainFLowBlock {
    override var nextMainFlowBlocks: BlockEntity? = null
    override var previousMainFlowBlocks: BlockEntity? = null

    private var rawInput = ""

    fun setRawInput(rawInput: String) {
        this.rawInput = rawInput
    }

    fun getRawInput(): String {
        return rawInput
    }

    override fun validate() {
        if (nextMainFlowBlocks == null) {
            throw NullPointerExceptionInOperator("End loop Block must have previous block")
        }
        if (rawInput == "") {
            throw NullPointerExceptionInOperator("Function block must have expression")
        }
    }
}

class EndFunctionBlock(
    instruction: Instruction = Instruction.RETURN
) : BlockEntity(instruction), IMainFLowBlock, IExecutable, IUnaryOperatorBlock {
    override var nextMainFlowBlocks: BlockEntity? = null
    override var previousMainFlowBlocks: BlockEntity? = null

    override var valuable: BlockEntity? = null

    override fun getValue(): Valuable {
        if(valuable == null) return Valuable(1, Type.UNDEFINED)
        return (valuable as IGetValuable).getValue()
    }

    override fun execute() {

    }

    override fun validate() {
        if (previousMainFlowBlocks == null) {
            throw NullPointerExceptionInOperator("End loop Block must have previous block")
        }
    }
}

class CallFunctionBlock(
    instruction: Instruction = Instruction.FUNC_CALL
) : BlockEntity(instruction), IMainFLowBlock {
    override var nextMainFlowBlocks: BlockEntity? = null
    override var previousMainFlowBlocks: BlockEntity? = null

    private var rawInput = ""

    fun setRawInput(rawInput: String) {
        this.rawInput = rawInput
    }

    fun getRawInput(): String {
        return rawInput
    }

    override fun validate() {
        if (nextMainFlowBlocks == null) {
            throw NullPointerExceptionInOperator("End loop Block must have previous block")
        }
        if (rawInput == "") {
            throw NullPointerExceptionInOperator("Function block must have expression")
        }
    }
}

class BreakBlock(
    instruction: Instruction = Instruction.BREAK
) : BlockEntity(instruction), IMainFLowBlock {
    override var nextMainFlowBlocks: BlockEntity? = null
    override var previousMainFlowBlocks: BlockEntity? = null

    override fun validate() {
        if (nextMainFlowBlocks == null) {
            throw NullPointerExceptionInOperator("Break block must have previous block")
        }
    }
}

class ContinueBlock(
    instruction: Instruction = Instruction.CONTINUE
) : BlockEntity(instruction), IMainFLowBlock {
    override var nextMainFlowBlocks: BlockEntity? = null
    override var previousMainFlowBlocks: BlockEntity? = null

    override fun validate() {
        if (nextMainFlowBlocks == null) {
            throw NullPointerExceptionInOperator("Continue block must have previous block")
        }
    }
}