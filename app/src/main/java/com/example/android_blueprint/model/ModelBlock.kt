

fun main() {
    val first = 5
    val second = 2
    val third = 3
    val firstConstBlock = ConstantBlock(first)
    val secondConstBlock = ConstantBlock(second)
    val thirdConstBlock = ConstantBlock(third)
    val firstSumBlock = SumBlock(firstConstBlock, thirdConstBlock)
    val secondSumBlock = SumBlock(secondConstBlock, thirdConstBlock)
    val firstDifferenceBlock = DifferenceBlock(firstSumBlock, secondSumBlock)
    println(firstSumBlock.getValue())
    println(secondSumBlock.getValue())
    println(firstDifferenceBlock.getValue())
    println(secondConstBlock.getValue())
    val firstMultiplicationBlock = MultiplicationBlock(firstDifferenceBlock, secondConstBlock)
    println(firstMultiplicationBlock.getValue())
    val firstDivisionBlock = DivisionBlock(firstMultiplicationBlock, firstDifferenceBlock)
    println(firstDivisionBlock.getValue())
}

//Block.kt

abstract class Block<T> {
    private var id = 0
    fun getId(): Int {
        return id
    }
    init {
        id = BlockManager.nextId()
    }
    abstract fun execute(): Block<T>
}

object BlockManager {
    private var nextId = 0
    fun nextId(): Int {
        return nextId++
    }
}

interface BlockWithExpression<T> {
    fun getExpression(): Expression<T>
    fun getValue() : T{
        return getExpression().interpret()
    }
}

//ConstantBlock.kt

class ConstantBlock<T : Any>(private val value: T) : Block<T>(), BlockWithExpression<T> {
    override fun getExpression(): Expression<T> {
        @Suppress("UNCHECKED_CAST")
        return when(value::class){
            Int::class -> NumericExpression(value as Int) as Expression<T>
            Double::class -> NumericExpression(value as Double) as Expression<T>
            Float::class -> NumericExpression(value as Float) as Expression<T>
            Long::class -> NumericExpression(value as Long) as Expression<T>
            Short::class -> NumericExpression(value as Short) as Expression<T>
            Byte::class -> NumericExpression(value as Byte) as Expression<T>
            Boolean::class -> BooleanExpression(value as Boolean) as Expression<T>
            else -> throw IllegalArgumentException("Unknown type")
        }
    }
    override fun execute(): Block<T> {
        return this
    }
}

//NumericOperatorBlock.kt

class SumBlock<T : Number>(
    private val leftOperator: BlockWithExpression<T>,
    private val rightOperator: BlockWithExpression<T>
) : Block<T>(), BlockWithExpression<T> {
    init {
        require(
            leftOperator.getExpression().interpret()::class ==
                    rightOperator.getExpression().interpret()::class)
        { "Both operands must have the same type" }
    }
    override fun getExpression(): Expression<T> {
        return SumExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): Block<T> {
        return this
    }
}

class DifferenceBlock<T : Number>(
    private val leftOperator: BlockWithExpression<T>,
    private val rightOperator: BlockWithExpression<T>
) : Block<T>(), BlockWithExpression<T> {
    init {
        require(
            leftOperator.getExpression().interpret()::class ==
                    rightOperator.getExpression().interpret()::class)
        { "Both operands must have the same type" }
    }
    override fun getExpression(): Expression<T> {
        return DifferenceExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): Block<T> {
        return this
    }
}

class MultiplicationBlock<T : Number>(
    private val leftOperator: BlockWithExpression<T>,
    private val rightOperator: BlockWithExpression<T>
) : Block<T>(), BlockWithExpression<T> {
    init {
        require(
            leftOperator.getExpression().interpret()::class ==
                   rightOperator.getExpression().interpret()::class)
        { "Both operands must have the same type" }
    }
    override fun getExpression(): Expression<T> {
        return MultiplicationExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): Block<T> {
        return this
    }
}

class DivisionBlock<T : Number>(
    private val leftOperator: BlockWithExpression<T>,
    private val rightOperator: BlockWithExpression<T>
) : Block<Double>(), BlockWithExpression<Double> {
    init {
        require(
            leftOperator.getExpression().interpret()::class ==
                    rightOperator.getExpression().interpret()::class)
        { "Both operands must have the same type" }
    }
    override fun getExpression(): Expression<Double> {
        return DivisionExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): Block<Double> {
        return this
    }
}

class ModBlock<T : Number>(
    private val leftOperator: BlockWithExpression<T>,
    private val rightOperator: BlockWithExpression<T>
) : Block<T>(), BlockWithExpression<T> {
    init {
        require(
            leftOperator.getExpression().interpret()::class ==
                    rightOperator.getExpression().interpret()::class)
        { "Both operands must have the same type" }
    }
    override fun getExpression(): Expression<T> {
        return ModExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): Block<T> {
        return this
    }
}

//BooleanOperatorBlock.kt

class AndBlock(
    private val leftOperator: BlockWithExpression<Boolean>,
    private val rightOperator: BlockWithExpression<Boolean>
) : Block<Boolean>(), BlockWithExpression<Boolean> {
    override fun getExpression(): Expression<Boolean> {
        return AndExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): Block<Boolean> {
        return this
    }
}

class OrBlock(
    private val leftOperator: BlockWithExpression<Boolean>,
    private val rightOperator: BlockWithExpression<Boolean>
) : Block<Boolean>(), BlockWithExpression<Boolean> {
    override fun getExpression(): Expression<Boolean> {
        return OrExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): Block<Boolean> {
        return this
    }
}

class NotBlock(
    private val operator: BlockWithExpression<Boolean>
) : Block<Boolean>(), BlockWithExpression<Boolean> {
    override fun getExpression(): Expression<Boolean> {
        return NotExpression(operator.getExpression())
    }
    override fun execute(): Block<Boolean> {
        return this
    }
}

class EqualBlock<T : Any>(
    private val leftOperator: BlockWithExpression<T>,
    private val rightOperator: BlockWithExpression<T>
) : Block<Boolean>(), BlockWithExpression<Boolean> {
    init {
        require(
            leftOperator.getExpression().interpret()::class ==
                    rightOperator.getExpression().interpret()::class)
        { "Both operands must have the same type" }
    }
    override fun getExpression(): Expression<Boolean> {
        return EqualExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): Block<Boolean> {
        return this
    }
}

class LessThenBlock<T : Number>(
    private val leftOperator: BlockWithExpression<T>,
    private val rightOperator: BlockWithExpression<T>
) : Block<Boolean>(), BlockWithExpression<Boolean> {
    init {
        require(
            leftOperator.getExpression().interpret()::class ==
                    rightOperator.getExpression().interpret()::class)
        { "Both operands must have the same type" }
    }
    override fun getExpression(): Expression<Boolean> {
        return LessThenExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): Block<Boolean> {
        return this
    }
}

class GreaterThenBlock<T : Number>(
    private val leftOperator: BlockWithExpression<T>,
    private val rightOperator: BlockWithExpression<T>
) : Block<Boolean>(), BlockWithExpression<Boolean> {
    init {
        require(
            leftOperator.getExpression().interpret()::class ==
                    rightOperator.getExpression().interpret()::class)
        { "Both operands must have the same type" }
    }
    override fun getExpression(): Expression<Boolean> {
        return GreaterThenExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): Block<Boolean> {
        return this
    }
}

class LessThenOrEqualBlock<T : Number>(
    private val leftOperator: BlockWithExpression<T>,
    private val rightOperator: BlockWithExpression<T>
) : Block<Boolean>(), BlockWithExpression<Boolean> {
    init {
        require(
            leftOperator.getExpression().interpret()::class ==
                    rightOperator.getExpression().interpret()::class)
        { "Both operands must have the same type" }
    }
    override fun getExpression(): Expression<Boolean> {
        return LessThenOrEqualExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): Block<Boolean> {
        return this
    }
}

class GreaterThenOrEqualBlock<T : Number>(
    private val leftOperator: BlockWithExpression<T>,
    private val rightOperator: BlockWithExpression<T>
) : Block<Boolean>(), BlockWithExpression<Boolean> {
    init {
        require(
            leftOperator.getExpression().interpret()::class ==
                    rightOperator.getExpression().interpret()::class)
        { "Both operands must have the same type" }
    }
    override fun getExpression(): Expression<Boolean> {
        return GreaterThenOrEqualExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): Block<Boolean> {
        return this
    }
}

//Expression.kt

interface Expression<T>{
    fun interpret(): T
}

//BooleanExpression.kt

class BooleanExpression(private val boolean: Boolean): Expression<Boolean>{
    override fun interpret(): Boolean {
        return boolean
    }
}

class AndExpression(
    private val leftExpression: Expression<Boolean>,
    private val rightExpression: Expression<Boolean>
) : Expression<Boolean> {
    override fun interpret(): Boolean {
        return leftExpression.interpret() && rightExpression.interpret()
    }
}

class OrExpression(
    private val leftExpression: Expression<Boolean>,
    private val rightExpression: Expression<Boolean>
) : Expression<Boolean> {
    override fun interpret(): Boolean {
        return leftExpression.interpret() || rightExpression.interpret()
    }
}

class NotExpression(
    private val expression: Expression<Boolean>
) : Expression<Boolean> {
    override fun interpret(): Boolean {
        return !expression.interpret()
    }
}

class EqualExpression<T : Any>(
    private val leftExpression: Expression<T>,
    private val rightExpression: Expression<T>
) : Expression<Boolean> {
    override fun interpret(): Boolean {
        return leftExpression.interpret() == rightExpression.interpret()
    }
}

class LessThenExpression<T : Number>(
    private val leftExpression: Expression<T>,
    private val rightExpression: Expression<T>
) : Expression<Boolean> {
    override fun interpret(): Boolean {
        return leftExpression.interpret().toDouble() < rightExpression.interpret().toDouble()
    }
}

class GreaterThenExpression<T : Number>(
    private val leftExpression: Expression<T>,
    private val rightExpression: Expression<T>
) : Expression<Boolean> {
    override fun interpret(): Boolean {
        return leftExpression.interpret().toDouble() > rightExpression.interpret().toDouble()
    }
}

class LessThenOrEqualExpression<T : Number>(
    private val leftExpression: Expression<T>,
    private val rightExpression: Expression<T>
) : Expression<Boolean> {
    override fun interpret(): Boolean {
        return leftExpression.interpret().toDouble() <= rightExpression.interpret().toDouble()
    }
}

class GreaterThenOrEqualExpression<T : Number>(
    private val leftExpression: Expression<T>,
    private val rightExpression: Expression<T>
) : Expression<Boolean> {
    override fun interpret(): Boolean {
        return leftExpression.interpret().toDouble() >= rightExpression.interpret().toDouble()
    }
}

//NumericExpression.kt

class NumericExpression<T : Number>(private val number: T): Expression<T>{
    override fun interpret(): T {
        return number
    }
}

fun <T : Number> getCastExpressionValue(
    leftExpression : Expression<T>,
    rightExpression: Expression<T>,
    operator: String
): T{
    @Suppress("UNCHECKED_CAST")
    return when(operator){
        "+" -> (
                when(leftExpression.interpret()::class){
                    Int::class -> (leftExpression.interpret().toInt() + rightExpression.interpret().toInt()) as T
                    Double::class -> (leftExpression.interpret().toDouble() + rightExpression.interpret().toDouble()) as T
                    Float::class -> (leftExpression.interpret().toFloat() + rightExpression.interpret().toFloat()) as T
                    Long::class -> (leftExpression.interpret().toLong() + rightExpression.interpret().toLong()) as T
                    Short::class -> (leftExpression.interpret().toShort() + rightExpression.interpret().toShort()) as T
                    Byte::class -> (leftExpression.interpret().toByte() + rightExpression.interpret().toByte()) as T
                    else -> throw IllegalArgumentException("Unknown type")
                }
                )
        "-" -> (
                when(leftExpression.interpret()::class){
                    Int::class -> (leftExpression.interpret().toInt() - rightExpression.interpret().toInt()) as T
                    Double::class -> (leftExpression.interpret().toDouble() - rightExpression.interpret().toDouble()) as T
                    Float::class -> (leftExpression.interpret().toFloat() - rightExpression.interpret().toFloat()) as T
                    Long::class -> (leftExpression.interpret().toLong() - rightExpression.interpret().toLong()) as T
                    Short::class -> (leftExpression.interpret().toShort() - rightExpression.interpret().toShort()) as T
                    Byte::class -> (leftExpression.interpret().toByte() - rightExpression.interpret().toByte()) as T
                    else -> throw IllegalArgumentException("Unknown type")
                }
                )
        "*" -> (
                when(leftExpression.interpret()::class){
                    Int::class -> (leftExpression.interpret().toInt() * rightExpression.interpret().toInt()) as T
                    Double::class -> (leftExpression.interpret().toDouble() * rightExpression.interpret().toDouble()) as T
                    Float::class -> (leftExpression.interpret().toFloat() * rightExpression.interpret().toFloat()) as T
                    Long::class -> (leftExpression.interpret().toLong() * rightExpression.interpret().toLong()) as T
                    Short::class -> (leftExpression.interpret().toShort() * rightExpression.interpret().toShort()) as T
                    Byte::class -> (leftExpression.interpret().toByte() * rightExpression.interpret().toByte()) as T
                    else -> throw IllegalArgumentException("Unknown type")
                }
                )
        "%" -> (
                when(leftExpression.interpret()::class){
                    Int::class -> (leftExpression.interpret().toInt() % rightExpression.interpret().toInt()) as T
                    Double::class -> (leftExpression.interpret().toDouble() % rightExpression.interpret().toDouble()) as T
                    Float::class -> (leftExpression.interpret().toFloat() % rightExpression.interpret().toFloat()) as T
                    Long::class -> (leftExpression.interpret().toLong() % rightExpression.interpret().toLong()) as T
                    Short::class -> (leftExpression.interpret().toShort() % rightExpression.interpret().toShort()) as T
                    Byte::class -> (leftExpression.interpret().toByte() % rightExpression.interpret().toByte()) as T
                    else -> throw IllegalArgumentException("Unknown type")
                }
                )
        else -> throw IllegalArgumentException("Unknown operator")
    }
}

class SumExpression<T : Number>(
    private val leftOperand: Expression<T>,
    private val rightOperand: Expression<T>
) : Expression<T> {
    override fun interpret(): T {
        @Suppress("UNCHECKED_CAST")
        return getCastExpressionValue(leftOperand, rightOperand, "+")
    }
}

class DifferenceExpression<T : Number>(
    private val leftOperand: Expression<T>,
    private val rightOperand: Expression<T>
) : Expression<T> {
    override fun interpret(): T {
        @Suppress("UNCHECKED_CAST")
        return getCastExpressionValue(leftOperand, rightOperand, "-")
    }
}

class MultiplicationExpression<T : Number>(
    private val leftOperand: Expression<T>,
    private val rightOperand: Expression<T>
) : Expression<T> {
    override fun interpret(): T {
        @Suppress("UNCHECKED_CAST")
        return getCastExpressionValue(leftOperand, rightOperand, "*")
    }
}

class DivisionExpression(
    private val leftOperand: Expression<out Number>,
    private val rightOperand: Expression<out Number>
) : Expression<Double> {
    override fun interpret(): Double {
        if(rightOperand.interpret().toDouble() == 0.0) throw ArithmeticException("Division by zero")
        return (leftOperand.interpret().toDouble() / rightOperand.interpret().toDouble())
    }
}

class ModExpression<T : Number>(
    private val leftOperand: Expression<T>,
    private val rightOperand: Expression<T>
) : Expression<T> {
    override fun interpret(): T {
        @Suppress("UNCHECKED_CAST")
        return getCastExpressionValue(leftOperand, rightOperand, "%")
    }
}

//BranchOperatorBlock.kt

class BranchOperatorBlock(
    private val condition: Expression<Boolean>,
    private val ifBlock: Block<Boolean>,
    private val elseBlock: Block<Boolean>
) : Block<Boolean>() {
    override fun execute(): Block<Boolean> {
        if (condition.interpret()) {
            return ifBlock
        } else {
            return elseBlock
        }
    }
}
