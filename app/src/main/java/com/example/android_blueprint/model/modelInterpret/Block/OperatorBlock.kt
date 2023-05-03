package Block

import Instruction
import Valuable

class InitializationVariableBlock(id: Int, instruction: Instruction) : BlockEntity(id, instruction) {
    private var rawInput: String = ""
    fun getRawInput(): String {
        return rawInput
    }

    fun setRawInput(rawInput: String) {
        this.rawInput = rawInput
        validate()
    }
    //TODO: validate user input
    override fun validate() {

    }
}

class VariableChangeBlock(id: Int, instruction: Instruction) : BlockEntity(id, instruction) {
    private var rawInput: String = ""
    fun getRawInput(): String {
        return rawInput
    }

    fun setRawInput(rawInput: String) {
        this.rawInput = rawInput
        validate()
    }
    //TODO: validate user input
    override fun validate() {

    }
}

class GetVariableBlock(id: Int, instruction: Instruction) : BlockEntity(id, instruction), IGetValuable {
    private var rawInput: String = ""
    fun getRawInput(): String {
        return rawInput
    }

    fun setRawInput(rawInput: String) {
        this.rawInput = rawInput
        validate()
    }

    override fun getValue(): Valuable {
        //TODO: get value from variable
        return Valuable(123, Type.INT)
    }

    override fun validate() {

    }
}

class BinarySumOperatorBlock(id: Int, instruction: Instruction) : BlockEntity(id, instruction), IBinaryOperatorBlock{
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

    }
}

class BinarySubOperatorBlock(id: Int, instruction: Instruction) : BlockEntity(id, instruction), IBinaryOperatorBlock{
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

    }
}

class BinaryMulOperatorBlock(id: Int, instruction: Instruction) : BlockEntity(id, instruction), IBinaryOperatorBlock{
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

    }
}

class BinaryDivOperatorBlock(id: Int, instruction: Instruction) : BlockEntity(id, instruction), IBinaryOperatorBlock{
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

    }
}

class BinaryModOperatorBlock(id: Int, instruction: Instruction) : BlockEntity(id, instruction), IBinaryOperatorBlock{
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

    }
}

class BinaryEqualOperatorBlock(id: Int, instruction: Instruction) : BlockEntity(id, instruction), IBinaryOperatorBlock{
    override var leftValuable: BlockEntity? = null
    override var rightValuable: BlockEntity? = null
    override fun getValue(): Valuable {
        try {
            validate()
            return Valuable(leftValuable!!.getValueFromOperatorBlocks()
                .equals(rightValuable!!.getValueFromOperatorBlocks()), Type.BOOL)
        } catch (e: Exception) {
            throw e
        }
    }
    override fun validate() {

    }
}

class BinaryNotEqualOperatorBlock(id: Int, instruction: Instruction) : BlockEntity(id, instruction), IBinaryOperatorBlock{
    override var leftValuable: BlockEntity? = null
    override var rightValuable: BlockEntity? = null
    override fun getValue(): Valuable {
        try {
            validate()
            return Valuable(!leftValuable!!.getValueFromOperatorBlocks()
                .equals(rightValuable!!.getValueFromOperatorBlocks()), Type.BOOL)
        } catch (e: Exception) {
            throw e
        }
    }
    override fun validate() {

    }
}

class BinaryGreaterOperatorBlock(id: Int, instruction: Instruction) : BlockEntity(id, instruction), IBinaryOperatorBlock{
    override var leftValuable: BlockEntity? = null
    override var rightValuable: BlockEntity? = null
    override fun getValue(): Valuable {
        try {
            validate()
            return Valuable(leftValuable!!.getValueFromOperatorBlocks()
                .compareTo(rightValuable!!.getValueFromOperatorBlocks()) == 1, Type.BOOL)
        } catch (e: Exception) {
            throw e
        }
    }
    override fun validate() {

    }
}

class BinaryLessOperatorBlock(id: Int, instruction: Instruction) : BlockEntity(id, instruction), IBinaryOperatorBlock{
    override var leftValuable: BlockEntity? = null
    override var rightValuable: BlockEntity? = null
    override fun getValue(): Valuable {
        try {
            validate()
            return Valuable(leftValuable!!.getValueFromOperatorBlocks()
                .compareTo(rightValuable!!.getValueFromOperatorBlocks()) == -1, Type.BOOL)
        } catch (e: Exception) {
            throw e
        }
    }
    override fun validate() {

    }
}

class BinaryGreaterOrEqualOperatorBlock(id: Int, instruction: Instruction) : BlockEntity(id, instruction), IBinaryOperatorBlock{
    override var leftValuable: BlockEntity? = null
    override var rightValuable: BlockEntity? = null

    override fun getValue(): Valuable {
        try {
            validate()
            return Valuable(leftValuable!!.getValueFromOperatorBlocks()
                .compareTo(rightValuable!!.getValueFromOperatorBlocks()) >= 0, Type.BOOL)
        } catch (e: Exception) {
            throw e
        }
    }
    override fun validate() {

    }
}

class BinaryLessOrEqualOperatorBlock(id: Int, instruction: Instruction) : BlockEntity(id, instruction), IBinaryOperatorBlock{
    override var leftValuable: BlockEntity? = null
    override var rightValuable: BlockEntity? = null

    override fun getValue(): Valuable {
        try {
            validate()
            return Valuable(leftValuable!!.getValueFromOperatorBlocks()
                .compareTo(rightValuable!!.getValueFromOperatorBlocks()) <= 0, Type.BOOL)
        } catch (e: Exception) {
            throw e
        }
    }
    override fun validate() {

    }
}

class UnaryNotOperatorBlock(id: Int, instruction: Instruction) : BlockEntity(id, instruction), IUnaryOperatorBlock{
    override var valuable: BlockEntity? = null
    override fun getValue(): Valuable {
        try {
            validate()
            return Valuable(!valuable!!.getValueFromOperatorBlocks()
                .convertToBool(valuable!!.getValueFromOperatorBlocks()), Type.BOOL)
        } catch (e: Exception) {
            throw e
        }
    }
    override fun validate() {

    }
}

fun checkIfValuableIsNullOrThrow(valuable: Valuable?) {
    if (valuable == null) {
        throw NullPointerException("Valuable is null")
    }
}