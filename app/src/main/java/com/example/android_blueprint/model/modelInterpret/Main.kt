import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

/*    val listViewsBlock = ArrayList<ViewBlock<*>>();
    listViewsBlock.add(ViewBlock(7, "MultiplicationBlock", "", 0, listOf(3, 4)))
    listViewsBlock.add(ViewBlock(4, "SubtractBlock", "", 0, listOf(3, 6)))
    listViewsBlock.add(ViewBlock(3, "SumBlock", "", 0, listOf(0, 1)))
    listViewsBlock.add(ViewBlock(6, "ConstantBlock", "c", 4, listOf()))
    listViewsBlock.add(ViewBlock(0, "ConstantBlock", "a", 1, listOf()))
    listViewsBlock.add(ViewBlock(1, "ConstantBlock", "b", 2, listOf()))
    val listBlock = postOrderDFS(listViewsBlock, listViewsBlock[0])
    println(listBlock[5].execute())*/

fun main() {
    var parseMap: MutableMap<String, List<String>> = mutableMapOf()
    val raw = "100>160"
    val interpreter = Interpret()
    val value = interpreter.parseRawBlock(raw)
    println(value.value)
}



/*
class ViewBlock<T : Any>(
    private val id: Int,
    private val type: String,
    private val variableName: String,
    private val value: T,
    private val listID: List<Int>){
    fun getId(): Int {
        return id
    }
    fun getType(): String {
        return type
    }
    fun getListID(): List<Int> {
        return listID
    }
    fun getValue(): T {
        return value
    }
    fun getVariableName(): String {
        return variableName
    }
}

fun postOrderDFS(listBlock: List<ViewBlock<*>>, rootBlock: ViewBlock<*>): List<Block.Block<*>>{
    val stack = Stack<ViewBlock<*>>()
    val visited = HashSet<ViewBlock<*>>()
    val result = ArrayList<Block.Block<*>>()
    stack.push(rootBlock)
    while (!stack.isEmpty()) {
        val current = stack.peek()
        val children = current.getListID()
        if (visited.contains(current)) {
            if(!result.contains(getBlock(current, result))){
                result.add(getBlock(current, result))
            }
            stack.pop()
        } else if(children.isEmpty()) {
            result.add(ConstantBlock(current.getValue(), current.getId(), current.getVariableName()))
            stack.pop()
        }else {
            visited.add(current)
            for (child in children.reversed()) {
                for(block in listBlock){
                    if(block.getId() == child && !visited.contains(block)){
                        stack.push(block)
                    }
                }
            }
        }
    }
    return result
}

fun getBlock(block: ViewBlock<*>, listBlock: List<Block.Block<*>>): Block.Block<*>{
    lateinit var leftBlock: Block.Block<*>
    lateinit var rightBlock: Block.Block<*>
    for (i in listBlock){
        if(i.getId() == block.getListID()[0]){
            leftBlock = i
        }
        if(i.getId() == block.getListID()[1]){
            rightBlock = i
        }
    }
    @Suppress("UNCHECKED_CAST")
    return when(block.getType()){
        "SumBlock" -> SumBlock(leftBlock as BlockWithExpression<Number>,
            rightBlock as BlockWithExpression<Number>,
            block.getId())
        "SubtractBlock" -> SubtractBlock(leftBlock as BlockWithExpression<Number>,
            rightBlock as BlockWithExpression<Number>,
            block.getId())
        "MultiplicationBlock" -> MultiplicationBlock(leftBlock as BlockWithExpression<Number>,
            rightBlock as BlockWithExpression<Number>,
            block.getId())
        "DivisionBlock" -> DivisionBlock(leftBlock as BlockWithExpression<Number>,
            rightBlock as BlockWithExpression<Number>,
            block.getId())
        "ModBlock" -> ModBlock(leftBlock as BlockWithExpression<Int>,
            rightBlock as BlockWithExpression<Int>,
            block.getId())
        else -> throw IllegalArgumentException("Unknown type")
    }
}

//Block.Block.kt

abstract class Block.Block<T>(private val id: Int) {
    fun getId(): Int {
        return id
    }
    abstract fun execute(): T
}

interface BlockWithExpression<T> {
    fun getExpression(): Expression<T>
    fun getValue() : T{
        return getExpression().interpret()
    }
}

//InputValueBlock.kt

class InputValueBlock<T : Any>(private val value: String,
                               id: Int
) : Block.Block<T>(id), BlockWithExpression<T> {
    override fun getExpression(): Expression<T> {
        @Suppress("UNCHECKED_CAST")
        val result = toNumericValue()
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
    override fun execute(): T {
        return getExpression().interpret()
    }
    private fun toNumericValue(): Number{
        //TODO: convert string to numeric value
        return 0
    }

}

//ConstantBlock.kt

class ConstantBlock<T : Any>(private val value: T,
                             id: Int,
                             private val name: String
) : Block.Block<T>(id), BlockWithExpression<T> {
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
    fun getName(): String{
        return name
    }
    override fun execute(): T {
        return getExpression().interpret()
    }
}

//VariableBlock.kt

class VariableBlock<T : Any>(private var value: T,
                             private val name: String,
                             id: Int
) : Block.Block<T>(id), BlockWithExpression<T> {
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
    override fun execute(): T {
        return getExpression().interpret()
    }
    fun getName(): String{
        return name
    }
    fun setValue(value: T){
        this.value = value
    }
}

//NumericOperatorBlock.kt

class SumBlock<T : Number>(
    private val leftOperator: BlockWithExpression<T>,
    private val rightOperator: BlockWithExpression<T>, id: Int
) : Block.Block<T>(id), BlockWithExpression<T> {
    init {
        require(
            leftOperator.getExpression().interpret()::class ==
                    rightOperator.getExpression().interpret()::class)
        { "Both operands must have the same type" }
    }
    override fun getExpression(): Expression<T> {
        return SumExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): T {
        return getExpression().interpret()
    }
}

class SubtractBlock<T : Number>(
    private val leftOperator: BlockWithExpression<T>,
    private val rightOperator: BlockWithExpression<T>, id: Int
) : Block.Block<T>(id), BlockWithExpression<T> {
    init {
        require(
            leftOperator.getExpression().interpret()::class ==
                    rightOperator.getExpression().interpret()::class)
        { "Both operands must have the same type" }
    }
    override fun getExpression(): Expression<T> {
        return DifferenceExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): T {
        return getExpression().interpret()
    }
}

class MultiplicationBlock<T : Number>(
    private val leftOperator: BlockWithExpression<T>,
    private val rightOperator: BlockWithExpression<T>, id: Int
) : Block.Block<T>(id), BlockWithExpression<T> {
    init {
        require(
            leftOperator.getExpression().interpret()::class ==
                   rightOperator.getExpression().interpret()::class)
        { "Both operands must have the same type" }
    }
    override fun getExpression(): Expression<T> {
        return MultiplicationExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): T {
        return getExpression().interpret()
    }
}

class DivisionBlock<T : Number>(
    private val leftOperator: BlockWithExpression<T>,
    private val rightOperator: BlockWithExpression<T>, id: Int
) : Block.Block<Double>(id), BlockWithExpression<Double> {
    init {
        require(
            leftOperator.getExpression().interpret()::class ==
                    rightOperator.getExpression().interpret()::class)
        { "Both operands must have the same type" }
    }
    override fun getExpression(): Expression<Double> {
        return DivisionExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): Double {
        return getExpression().interpret()
    }
}

class ModBlock<T : Number>(
    private val leftOperator: BlockWithExpression<T>,
    private val rightOperator: BlockWithExpression<T>, id: Int
) : Block.Block<T>(id), BlockWithExpression<T> {
    init {
        require(
            leftOperator.getExpression().interpret()::class ==
                    rightOperator.getExpression().interpret()::class)
        { "Both operands must have the same type" }
    }
    override fun getExpression(): Expression<T> {
        return ModExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): T {
        return getExpression().interpret()
    }
}

//BooleanOperatorBlock.kt

class AndBlock(
    private val leftOperator: BlockWithExpression<Boolean>,
    private val rightOperator: BlockWithExpression<Boolean>, id: Int
) : Block.Block<Boolean>(id), BlockWithExpression<Boolean> {
    override fun getExpression(): Expression<Boolean> {
        return AndExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): Boolean {
        return getExpression().interpret()
    }
}

class OrBlock(
    private val leftOperator: BlockWithExpression<Boolean>,
    private val rightOperator: BlockWithExpression<Boolean>, id: Int
) : Block.Block<Boolean>(id), BlockWithExpression<Boolean> {
    override fun getExpression(): Expression<Boolean> {
        return OrExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): Boolean {
        return getExpression().interpret()
    }
}

class NotBlock(
    private val operator: BlockWithExpression<Boolean>, id: Int
) : Block.Block<Boolean>(id), BlockWithExpression<Boolean> {
    override fun getExpression(): Expression<Boolean> {
        return NotExpression(operator.getExpression())
    }
    override fun execute(): Boolean {
        return getExpression().interpret()
    }
}

class EqualBlock<T : Any>(
    private val leftOperator: BlockWithExpression<T>,
    private val rightOperator: BlockWithExpression<T>, id: Int
) : Block.Block<Boolean>(id), BlockWithExpression<Boolean> {
    init {
        require(
            leftOperator.getExpression().interpret()::class ==
                    rightOperator.getExpression().interpret()::class)
        { "Both operands must have the same type" }
    }
    override fun getExpression(): Expression<Boolean> {
        return EqualExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): Boolean {
        return getExpression().interpret()
    }
}

class LessThenBlock<T : Number>(
    private val leftOperator: BlockWithExpression<T>,
    private val rightOperator: BlockWithExpression<T>, id: Int
) : Block.Block<Boolean>(id), BlockWithExpression<Boolean> {
    init {
        require(
            leftOperator.getExpression().interpret()::class ==
                    rightOperator.getExpression().interpret()::class)
        { "Both operands must have the same type" }
    }
    override fun getExpression(): Expression<Boolean> {
        return LessThenExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): Boolean {
        return getExpression().interpret()
    }
}

class GreaterThenBlock<T : Number>(
    private val leftOperator: BlockWithExpression<T>,
    private val rightOperator: BlockWithExpression<T>, id: Int
) : Block.Block<Boolean>(id), BlockWithExpression<Boolean> {
    init {
        require(
            leftOperator.getExpression().interpret()::class ==
                    rightOperator.getExpression().interpret()::class)
        { "Both operands must have the same type" }
    }
    override fun getExpression(): Expression<Boolean> {
        return GreaterThenExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): Boolean {
        return getExpression().interpret()
    }
}

class LessThenOrEqualBlock<T : Number>(
    private val leftOperator: BlockWithExpression<T>,
    private val rightOperator: BlockWithExpression<T>, id: Int
) : Block.Block<Boolean>(id), BlockWithExpression<Boolean> {
    init {
        require(
            leftOperator.getExpression().interpret()::class ==
                    rightOperator.getExpression().interpret()::class)
        { "Both operands must have the same type" }
    }
    override fun getExpression(): Expression<Boolean> {
        return LessThenOrEqualExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): Boolean {
        return getExpression().interpret()
    }
}

class GreaterThenOrEqualBlock<T : Number>(
    private val leftOperator: BlockWithExpression<T>,
    private val rightOperator: BlockWithExpression<T>, id: Int
) : Block.Block<Boolean>(id), BlockWithExpression<Boolean> {
    init {
        require(
            leftOperator.getExpression().interpret()::class ==
                    rightOperator.getExpression().interpret()::class)
        { "Both operands must have the same type" }
    }
    override fun getExpression(): Expression<Boolean> {
        return GreaterThenOrEqualExpression(leftOperator.getExpression(), rightOperator.getExpression())
    }
    override fun execute(): Boolean {
        return getExpression().interpret()
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

*/

