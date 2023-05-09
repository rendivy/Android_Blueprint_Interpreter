package block

import Instruction
import exceptions.NullPointerExceptionInOperator
import memory.Valuable

class InitializationAndSetVariableBlock(
    instruction: Instruction = Instruction.INIT_AND_SET,
    override var parsed: String = ""
) : BlockEntity(instruction), IWorkingWithVariables, IMainFLowBlock, IGetValuable {
    private var rawInput: String = ""
    override var nextMainFlowBlocks: BlockEntity? = null
    override var previousMainFlowBlocks: BlockEntity? = null
    private var value: Valuable? = null

    override fun getValue(): Valuable {
        return value!!
    }

    fun setValue(value: Valuable){
        this.value = value
    }

    fun getRawInput(): String {
        return rawInput
    }

    fun setRawInput(rawInput: String) {
        this.rawInput = rawInput
        validate()
    }

    fun addNewField(): InitializationAndSetVariableBlock{
        val newNode = InitializationAndSetVariableBlock()
        this.setNextMainFlowBlock(newNode)
        newNode.setPreviousMainFlowBlock(this)
        return newNode
    }

    //TODO: validate user input
    override fun validate() {

    }
}

class GetVariableBlock(
    instruction: Instruction = Instruction.GET_VALUABLE
) : BlockEntity(instruction), IGetValuable {
    private var rawInput: String = ""
    private var variable: Valuable = Valuable(0, Type.INT)

    fun getRawInput(): String {
        return rawInput
    }

    fun setRawInput(rawInput: String) {
        this.rawInput = rawInput
        validate()
    }

    override fun getValue(): Valuable {
        return variable
    }

    fun setValue(value: Valuable) {
        this.variable = value
    }

    //TODO: validate user input
    override fun validate() {

    }
}

class BinarySumOperatorBlock(
    instruction: Instruction = Instruction.OPERATOR_BLOCK
) : BlockEntity(instruction), IBinaryOperatorBlock {
    override var leftValuable: BlockEntity? = null
    override var rightValuable: BlockEntity? = null
    override fun getValue(): Valuable {
        try {
            validate()
            return leftValuable!!.getValueFromOperatorBlocks()
                .plus(rightValuable!!.getValueFromOperatorBlocks())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun validate() {
        if (leftValuable == null || rightValuable == null)
            throw NullPointerExceptionInOperator("Left or right valuable is null ${this.getId()}")
    }
}

class BinarySubOperatorBlock(
    instruction: Instruction = Instruction.OPERATOR_BLOCK
) : BlockEntity(instruction), IBinaryOperatorBlock {
    override var leftValuable: BlockEntity? = null
    override var rightValuable: BlockEntity? = null
    override fun getValue(): Valuable {
        try {
            validate()
            return leftValuable!!.getValueFromOperatorBlocks()
                .minus(rightValuable!!.getValueFromOperatorBlocks())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun validate() {
        if (leftValuable == null || rightValuable == null)
            throw NullPointerExceptionInOperator("Left or right valuable is null ${this.getId()}")
    }
}

class BinaryMulOperatorBlock(
    instruction: Instruction = Instruction.OPERATOR_BLOCK
) : BlockEntity(instruction), IBinaryOperatorBlock {
    override var leftValuable: BlockEntity? = null
    override var rightValuable: BlockEntity? = null
    override fun getValue(): Valuable {
        try {
            validate()
            return leftValuable!!.getValueFromOperatorBlocks()
                .times(rightValuable!!.getValueFromOperatorBlocks())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun validate() {
        if (leftValuable == null || rightValuable == null)
            throw NullPointerExceptionInOperator("Left or right valuable is null ${this.getId()}")
    }
}

class BinaryDivOperatorBlock(
    instruction: Instruction = Instruction.OPERATOR_BLOCK
) : BlockEntity(instruction), IBinaryOperatorBlock {
    override var leftValuable: BlockEntity? = null
    override var rightValuable: BlockEntity? = null
    override fun getValue(): Valuable {
        try {
            validate()
            return leftValuable!!.getValueFromOperatorBlocks()
                .div(rightValuable!!.getValueFromOperatorBlocks())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun validate() {
        if (leftValuable == null || rightValuable == null)
            throw NullPointerExceptionInOperator("Left or right valuable is null ${this.getId()}")
    }
}

class BinaryModOperatorBlock(
    instruction: Instruction = Instruction.OPERATOR_BLOCK
) : BlockEntity(instruction), IBinaryOperatorBlock {
    override var leftValuable: BlockEntity? = null
    override var rightValuable: BlockEntity? = null
    override fun getValue(): Valuable {
        try {
            validate()
            return leftValuable!!.getValueFromOperatorBlocks()
                .rem(rightValuable!!.getValueFromOperatorBlocks())
        } catch (e: Exception) {
            throw e
        }
    }

    override fun validate() {
        if (leftValuable == null || rightValuable == null)
            throw NullPointerExceptionInOperator("Left or right valuable is null ${this.getId()}")
    }
}

class BinaryEqualOperatorBlock(
    instruction: Instruction = Instruction.OPERATOR_BLOCK
) : BlockEntity(instruction), IBinaryOperatorBlock {
    override var leftValuable: BlockEntity? = null
    override var rightValuable: BlockEntity? = null
    override fun getValue(): Valuable {
        try {
            validate()
            return Valuable(
                leftValuable!!.getValueFromOperatorBlocks()
                    .equals(rightValuable!!.getValueFromOperatorBlocks()), Type.BOOL
            )
        } catch (e: Exception) {
            throw e
        }
    }

    override fun validate() {
        if (leftValuable == null || rightValuable == null)
            throw NullPointerExceptionInOperator("Left or right valuable is null ${this.getId()}")
    }
}

class BinaryNotEqualOperatorBlock(
    instruction: Instruction = Instruction.OPERATOR_BLOCK
) : BlockEntity(instruction), IBinaryOperatorBlock {
    override var leftValuable: BlockEntity? = null
    override var rightValuable: BlockEntity? = null
    override fun getValue(): Valuable {
        try {
            validate()
            return Valuable(
                !leftValuable!!.getValueFromOperatorBlocks()
                    .equals(rightValuable!!.getValueFromOperatorBlocks()), Type.BOOL
            )
        } catch (e: Exception) {
            throw e
        }
    }

    override fun validate() {
        if (leftValuable == null || rightValuable == null)
            throw NullPointerExceptionInOperator("Left or right valuable is null ${this.getId()}")
    }
}

class BinaryGreaterOperatorBlock(
    instruction: Instruction = Instruction.OPERATOR_BLOCK
) : BlockEntity(instruction), IBinaryOperatorBlock {
    override var leftValuable: BlockEntity? = null
    override var rightValuable: BlockEntity? = null
    override fun getValue(): Valuable {
        try {
            validate()
            return Valuable(
                leftValuable!!.getValueFromOperatorBlocks()
                    .compareTo(rightValuable!!.getValueFromOperatorBlocks()) == 1, Type.BOOL
            )
        } catch (e: Exception) {
            throw e
        }
    }

    override fun validate() {
        if (leftValuable == null || rightValuable == null)
            throw NullPointerExceptionInOperator("Left or right valuable is null ${this.getId()}")
    }
}

class BinaryLessOperatorBlock(
    instruction: Instruction = Instruction.OPERATOR_BLOCK
) : BlockEntity(instruction), IBinaryOperatorBlock {
    override var leftValuable: BlockEntity? = null
    override var rightValuable: BlockEntity? = null
    override fun getValue(): Valuable {
        try {
            validate()
            return Valuable(
                leftValuable!!.getValueFromOperatorBlocks()
                    .compareTo(rightValuable!!.getValueFromOperatorBlocks()) == -1, Type.BOOL
            )
        } catch (e: Exception) {
            throw e
        }
    }

    override fun validate() {
        if (leftValuable == null || rightValuable == null)
            throw NullPointerExceptionInOperator("Left or right valuable is null ${this.getId()}")
    }
}

class BinaryGreaterOrEqualOperatorBlock(
    instruction: Instruction = Instruction.OPERATOR_BLOCK
) : BlockEntity(instruction), IBinaryOperatorBlock {
    override var leftValuable: BlockEntity? = null
    override var rightValuable: BlockEntity? = null

    override fun getValue(): Valuable {
        try {
            validate()
            return Valuable(
                leftValuable!!.getValueFromOperatorBlocks()
                    .compareTo(rightValuable!!.getValueFromOperatorBlocks()) >= 0, Type.BOOL
            )
        } catch (e: Exception) {
            throw e
        }
    }

    override fun validate() {
        if (leftValuable == null || rightValuable == null)
            throw NullPointerExceptionInOperator("Left or right valuable is null ${this.getId()}")
    }
}

class BinaryLessOrEqualOperatorBlock(
    instruction: Instruction = Instruction.OPERATOR_BLOCK
) : BlockEntity(instruction), IBinaryOperatorBlock {
    override var leftValuable: BlockEntity? = null
    override var rightValuable: BlockEntity? = null

    override fun getValue(): Valuable {
        try {
            validate()
            return Valuable(
                leftValuable!!.getValueFromOperatorBlocks()
                    .compareTo(rightValuable!!.getValueFromOperatorBlocks()) <= 0, Type.BOOL
            )
        } catch (e: Exception) {
            throw e
        }
    }

    override fun validate() {
        if (leftValuable == null || rightValuable == null)
            throw NullPointerExceptionInOperator("Left or right valuable is null ${this.getId()}")
    }
}

class UnaryNotOperatorBlock(
    instruction: Instruction = Instruction.OPERATOR_BLOCK
) : BlockEntity(instruction), IUnaryOperatorBlock {
    override var valuable: BlockEntity? = null
    override fun getValue(): Valuable {
        try {
            validate()
            return Valuable(
                !valuable!!.getValueFromOperatorBlocks()
                    .convertToBool(valuable!!.getValueFromOperatorBlocks()), Type.BOOL
            )
        } catch (e: Exception) {
            throw e
        }
    }

    override fun validate() {
        if (valuable == null)
            throw NullPointerExceptionInOperator("memory.Valuable is null")
    }
}

