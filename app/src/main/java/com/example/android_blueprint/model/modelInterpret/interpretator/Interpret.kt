package interpretator

import exceptions.IndexOutOfRangeError
import Instruction
import rpn.Notation
import exceptions.NullPointerExceptionInOperator
import exceptions.OperationError
import exceptions.RuntimeError
import exceptions.StackCorruptionError
import Type
import block.*
import com.example.android_blueprint.viewModel.ConsoleViewModel
import exceptions.NotFoundError
import memory.Memory
import memory.MemoryArea
import memory.Valuable
import memory.Variable
import java.util.*
import kotlin.collections.HashMap
import kotlinx.coroutines.*

class Interpret() {
    var blocks = BlockEntity.getBlocks()
    var input = ""
    var waitingForInput = false

    var debug = true
    var stepInto = false
    var stepTo = true
    private var isRunning = false
    private var isRunningBlock = false

    fun isRunningInBlock(): Boolean {
        return isRunningBlock
    }

    fun isRunning(): Boolean {
        return isRunning
    }

    fun switchStepInto() {
        stepInto = !stepInto
    }

    fun switchStepTo() {
        stepTo = !stepTo
    }

    fun switchDebug() {
        debug = !debug
    }

    private var memory = Memory(null, "GLOBAL_SCOPE")

    data class FunctionEntity(
        var name: String,
        var block: FunctionBlock,
        var variables: List<String>,
        var memory: Memory
    )

    data class CallFunctionEntity(
        var name: String,
        var variables: List<String>
    )

    private var parseMap: MutableMap<String, List<String>> = mutableMapOf()
    private var loopStack: Stack<BlockEntity> = Stack()
    private var functionMemoryStack: Stack<Memory> = Stack()
    private var functionHashMap: HashMap<String, FunctionEntity> = HashMap()
    private var functionName = mutableListOf<String>()

    fun run(startBlock: StartBlock) {
        blocks = BlockEntity.getBlocks()
        blocks.forEach {
            it.validate()
        }
        initializationsFunction()
        isRunning = true
        parse(startBlock)
    }

    private fun initializationsFunction() {
        blocks.forEach {
            if (it is FunctionBlock) {
                parseInitializationFunction(it)
            }
        }
        functionHashMap.forEach() {
            Notation.functionName.add(it.key)
            functionName.add(it.key)
        }
    }

    private fun parseForLoop(block: BlockEntity): List<String> {
        val forBlock = block as ForBlock
        return splitRawInput(forBlock.getRawInput())
    }

    private fun splitRawInput(str: String): List<String> {
        var isString = false
        val parsed = mutableListOf<String>()
        var tempStr = ""

        for (symbol in str) {
            if (symbol == '"') {
                isString = !isString
            }

            if (symbol == ',' && !isString) {
                parsed.add(tempStr)
                tempStr = ""
                continue
            }
            tempStr += symbol
        }
        parsed.add(tempStr)
        return parsed
    }

    private fun parseInitializationFunction(block: FunctionBlock) {
        val nameFunction = block
            .getRawInput()
            .substringBefore("(")
        val variables = block
            .getRawInput()
            .substringAfter("(")
            .substringBefore(")")
        val parse = splitRawInput(variables)
        val functionEntity = FunctionEntity(
            nameFunction,
            block,
            parse,
            Memory(memory, "FUNCTION SCOPE: $nameFunction")
        )
        functionHashMap[nameFunction] = functionEntity
    }

    private fun parseCallFunction(str: String): CallFunctionEntity {
        val nameFunction = str
            .substringBefore("(")
        val variables = str
            .substringAfter("(")
            .dropLast(1)
        val parse = splitRawInput(variables)
        return CallFunctionEntity(nameFunction, parse)
    }

    private fun runFunction(rawFunction: String): Valuable? {
        val callFunctionEntity = parseCallFunction(rawFunction)

        val functionEntity = functionHashMap[callFunctionEntity.name]
            ?: throw NotFoundError("Function ${callFunctionEntity.name} not found")

        if (functionEntity.variables.size != callFunctionEntity.variables.size) {
            throw NullPointerExceptionInOperator(
                "Function ${callFunctionEntity.name}" +
                        " have ${functionEntity.variables.size}" +
                        " variables, but ${callFunctionEntity.variables.size}" +
                        " variables passed"
            )
        }
        functionMemoryStack.push(memory)
        memory = functionEntity.memory
        for (i in functionEntity.variables.indices) {
            val variable = functionEntity.variables[i]
            val value = callFunctionEntity.variables[i]
            parseExpressionString("$variable=$value", true)
        }
        return parse(functionEntity.block)
    }

    private fun printMemory() {
        var currentMemory = memory
        while (currentMemory.previousMemory != null) {
            ConsoleViewModel.debugText = currentMemory.scope + '\n'
            for ((key, value) in currentMemory.stack) {
                ConsoleViewModel.debugText += "$key: $value\n"
            }
            currentMemory = currentMemory.previousMemory!!
        }
        ConsoleViewModel.debugText = currentMemory.scope + '\n'
        for ((key, value) in currentMemory.stack) {
            ConsoleViewModel.debugText += "$key: $value\n"
        }
    }

    private fun skipLoop(): BlockEntity {
        val loopBlock = loopStack.peek()
        if (loopBlock.getInstruction() == Instruction.FOR) {
            val parse = parseForLoop(loopBlock)
            parseExpressionString(parse[2])
        }
        return loopBlock
    }

    private fun parse(startBlock: BlockEntity): Valuable? {
        var currentBlock = startBlock
        while (currentBlock.getInstruction() != Instruction.END_POINT) {
            if (debug) {
                debugging(currentBlock)
            }
            when (currentBlock.getInstruction()) {
                Instruction.START_POINT -> {}

                Instruction.INITIALIZATION -> {
                    val parse =
                        splitRawInput((currentBlock as InitializationVariableBlock).getRawInput())
                    for (raw in parse) {
                        parseExpressionString(raw, true)
                    }
                }

                Instruction.SET -> {
                    val parse = splitRawInput((currentBlock as SetVariableBlock).getRawInput())
                    for (raw in parse) {
                        currentBlock.setValue(parseExpressionString(raw))
                    }
                }

                Instruction.PRINT -> {
                    initGetVariableBlock(currentBlock as IGetValuable)
                    (currentBlock as IExecutable).execute()
                }

                Instruction.IF -> {
                    memory = Memory(memory, "IF SCOPE")
                    initGetVariableBlock(currentBlock as IGetValuable)
                    (currentBlock as IExecutable).execute()
                }

                Instruction.END_IF -> {
                    memory = memory.previousMemory!!
                }

                Instruction.FOR -> {
                    val parse = parseForLoop(currentBlock)
                    if (loopStack.isEmpty() || currentBlock != loopStack.peek()) {
                        loopStack.add(currentBlock)
                        memory = Memory(memory, "FOR SCOPE")
                        parseExpressionString(parse[0], true)
                    }
                    if (parseExpressionString(parse[1]).value == "true") {
                        (currentBlock as ForBlock).nextMainFlowBlocks =
                            currentBlock.getTrueBranchExpression()!!
                    } else {
                        if ((currentBlock as ForBlock).getFalseBranchExpression() == null && !loopStack.isEmpty()) {
                            currentBlock.nextMainFlowBlocks =
                                (loopStack.peek() as IMainFLowBlock).previousMainFlowBlocks
                        } else {
                            currentBlock.nextMainFlowBlocks =
                                currentBlock.getFalseBranchExpression()!!
                        }
                        memory = memory.previousMemory!!
                        loopStack.pop()
                    }
                }

                Instruction.WHILE -> {
                    if (loopStack.isEmpty() || currentBlock != loopStack.peek()) {
                        loopStack.add(currentBlock)
                        memory = Memory(memory, "WHILE SCOPE")
                    }
                    if (parseExpressionString((currentBlock as WhileBlock).getRawInput()).value == "true") {
                        currentBlock.nextMainFlowBlocks = currentBlock.getTrueBranchExpression()!!
                    } else {
                        if (currentBlock.getFalseBranchExpression() == null && !loopStack.isEmpty()) {
                            currentBlock.nextMainFlowBlocks =
                                (loopStack.peek() as IMainFLowBlock).previousMainFlowBlocks
                        } else {
                            currentBlock.nextMainFlowBlocks =
                                currentBlock.getFalseBranchExpression()!!
                        }
                        memory = memory.previousMemory!!
                        loopStack.pop()
                    }
                }

                Instruction.FUNC -> {}

                Instruction.FUNC_CALL -> {
                    runFunction((currentBlock as CallFunctionBlock).getRawInput())
                }

                Instruction.RETURN -> {
                    if ((currentBlock as EndFunctionBlock).valuable == null) {
                        memory = functionMemoryStack.pop()
                        return null
                    }
                    initGetVariableBlock(currentBlock as IGetValuable)
                    val returnedValuable = currentBlock.getValue()
                    memory = functionMemoryStack.pop()
                    return returnedValuable
                }

                Instruction.CONTINUE -> {
                    if (loopStack.isEmpty()) {
                        throw Exception("Unknown instruction")
                    }
                    currentBlock = skipLoop()
                }

                Instruction.BREAK -> {
                    if (loopStack.isEmpty()) {
                        throw Exception("Unknown instruction")
                    }
                    currentBlock = skipLoop()
                    if (currentBlock is ForBlock) {
                        currentBlock.nextMainFlowBlocks = currentBlock.getFalseBranchExpression()!!
                    } else if (currentBlock is WhileBlock) {
                        currentBlock.nextMainFlowBlocks = currentBlock.getFalseBranchExpression()!!
                        memory = memory.previousMemory!!
                        loopStack.pop()
                    }
                }

                Instruction.INPUT -> {
                    runBlocking {
                        suspensionUserWhenInput()
                    }
                    parseExpressionString((currentBlock as InputBlock).getRawInput() + "=" + input)
                    input = ""
                }

                else -> {
                    throw Exception("Unknown instruction")
                }
            }
            if (!loopStack.isEmpty() &&
                ((currentBlock as IMainFLowBlock).nextMainFlowBlocks == null
                        || (currentBlock as IMainFLowBlock).nextMainFlowBlocks == loopStack.peek())
            ) {
                currentBlock = skipLoop()
            } else {
                currentBlock = (currentBlock as IMainFLowBlock).nextMainFlowBlocks!!
            }
        }
        isRunning = false
        return null
    }

    private fun debugging(currentBlock: BlockEntity) {
        when {
            stepInto -> {
                stepInto = false
                isRunningBlock = true
                printMemory()
                runBlocking {
                    suspensionUserWhenDebugging()
                }
                isRunningBlock = false
            }

            stepTo -> {
                if (currentBlock.getBreakPoint()) {
                    stepTo = false
                    isRunningBlock = true
                    printMemory()
                    runBlocking {
                        suspensionUserWhenDebugging()
                    }
                    isRunningBlock = false
                }
            }
        }
    }

    private suspend fun suspensionUserWhenInput() {
        while (true) {
            if (input.isNotEmpty()) {
                break
            }
            delay(1000)
        }
    }

    private suspend fun suspensionUserWhenDebugging() {
        while (true) {
            if (stepTo || stepInto) {
                break
            }
            delay(1000)
        }
    }

    private fun initGetVariableBlock(startBlockToBypassTree: IGetValuable) {
        var tempStack = Stack<IGetValuable>()
        tempStack.push(startBlockToBypassTree)
        postOrderDFS(tempStack)
    }

    private fun postOrderDFS(
        stack: Stack<IGetValuable>,
        visited: MutableSet<IGetValuable> = mutableSetOf()
    ) {
        while (!stack.isEmpty()) {
            val current = stack.peek()
            if (current is IBinaryOperatorBlock) {
                if (current.leftValuable !is GetVariableBlock &&
                    !visited.contains(current.leftValuable as IGetValuable)
                ) {
                    stack.push(current.leftValuable as IGetValuable)
                } else if (current.rightValuable !is GetVariableBlock &&
                    !visited.contains(current.rightValuable as IGetValuable)
                ) {
                    stack.push(current.rightValuable as IGetValuable)
                } else if (current.leftValuable is GetVariableBlock &&
                    !visited.contains(current.leftValuable as IGetValuable)
                ) {
                    (current.leftValuable as GetVariableBlock)
                        .setValue(parseExpressionString((current.leftValuable as GetVariableBlock).getRawInput()))
                    visited.add(current.leftValuable as IGetValuable)
                } else if (current.rightValuable is GetVariableBlock &&
                    !visited.contains(current.rightValuable as IGetValuable)
                ) {
                    (current.rightValuable as GetVariableBlock)
                        .setValue(parseExpressionString((current.rightValuable as GetVariableBlock).getRawInput()))
                    visited.add(current.rightValuable as IGetValuable)
                } else {
                    visited.add(current)
                    stack.pop()
                }
            } else if (current is IUnaryOperatorBlock) {
                if (current.valuable !is GetVariableBlock &&
                    !visited.contains(current.valuable as IGetValuable)
                ) {
                    stack.push(current.valuable as IGetValuable)
                } else if (current.valuable is GetVariableBlock &&
                    !visited.contains(current.valuable as IGetValuable)
                ) {
                    (current.valuable as GetVariableBlock)
                        .setValue(parseExpressionString((current.valuable as GetVariableBlock).getRawInput()))
                    visited.add(current.valuable as IGetValuable)
                } else {
                    visited.add(current)
                    stack.pop()
                }
            } else if (current is IGetValuable) {
                visited.add(current)
                stack.pop()
            }
        }
    }

    private fun pushToLocalMemory(
        name: String,
        type: Type = Type.UNDEFINED,
        valueMemoryArea: MemoryArea
    ) {
        valueMemoryArea as Valuable

        if (type == Type.LIST) {
            val block = valueMemoryArea.clone()
            block.type = type
            memory.push(name, block)
            return
        }

        valueMemoryArea.type = type
        memory.push(name, valueMemoryArea)
    }

    private fun tryPushToAnyMemory(
        memory: Memory,
        name: String,
        type: Type,
        valueMemoryArea: MemoryArea
    ): Boolean {
        valueMemoryArea as Valuable
        valueMemoryArea.type = type

        if (memory.get(name) != null) {
            memory.push(name, valueMemoryArea)
            return true
        }

        if (memory.previousMemory == null) {
            memory.throwStackError(name)
            return false
        }

        return tryPushToAnyMemory(memory.previousMemory, name, type, valueMemoryArea)
    }

    private fun getValue(data: String): MemoryArea? {
        return when {
            data in listOf(
                "abs", "exp", "sorted", "ceil", "floor", "len",
                ".toInt()", ".toFloat()", ".toBool()", ".toString()", ".sort()", ".toList()"
            ) -> {
                null
            }

            data == "rand()" -> {
                Valuable(Math.random(), Type.DOUBLE)
            }


            (Regex("\\b([\\d]+\\.?[\\d]+|\\w[\\w\\d_]*|\".*\")\\((?:[^()]|\\((?:[^()]|\\((?:[^()]|\\((?:[^()]|\\((?:[^()])*\\))*\\))*\\))*\\))*\\)(?!\\))").containsMatchIn(
                data
            )) -> {
                runFunction(data)
            }

            data in listOf("true", "false") -> {
                Valuable(data, Type.BOOL)
            }

            data.last() == '"' && data.first() == '"' -> {
                Valuable(data.replace("\"", ""), Type.STRING)
            }

            data.contains("^[A-Za-z]+\$".toRegex()) -> {
                Variable(data)
            }

            else -> {
                when {
                    data.contains("[\\d]+\\.[\\d]+".toRegex()) -> Valuable(data, Type.DOUBLE)
                    data.contains("[\\d]+".toRegex()) -> Valuable(data, Type.INT)
                    else -> null
                }
            }
        }
    }

    private fun tryFindInMemory(memory: Memory, memoryArea: MemoryArea): Valuable {
        memoryArea as Variable
        val address = memoryArea.name
        val value = memory.get(address)

        if (value != null) {
            return value
        }

        if (memory.previousMemory == null) {
            memory.throwStackError(address)
            throw Exception()
        }

        return tryFindInMemory(memory.previousMemory, memoryArea)
    }

    private fun parseExpressionString(raw: String, initialize: Boolean = false): Valuable {
        val data = parseMap[raw] ?: Notation.convertToRpn(Notation.tokenizeString(raw))
        parseMap[raw] = data

        val stack = mutableListOf<MemoryArea>()
        val unary = listOf(
            "±", "∓", ".toInt()", ".toFloat()", ".toBool()", ".toString()", ".sort()", ".toList()",
            "abs", "exp", "sorted", "ceil", "floor", "len"
        )

        for (value in data) {
            if (value.isEmpty()) {
                continue
            }
            val parsedValue = getValue(value)
            if (parsedValue != null) {
                stack.add(parsedValue)
            } else {
                try {
                    var operand2 = try {
                        stack.removeLast()
                    } catch (e: Exception) {
                        throwOperationError("Expected correct expression but bad operation was found")
                    }

                    if (operand2 is Variable) {
                        try {
                            operand2 = tryFindInMemory(memory, operand2)
                        } catch (e: StackCorruptionError) {
                            throw RuntimeError("${e.message}")
                        }
                    }
                    operand2 as Valuable

                    if (value in unary) {
                        stack.add(
                            when (value) {
                                "±" -> +operand2
                                "∓" -> -operand2
                                ".toInt()" -> Valuable(operand2.convertToInt(operand2), Type.INT)
                                ".toFloat()" -> Valuable(
                                    operand2.convertToDouble(operand2),
                                    Type.DOUBLE
                                )

                                ".toString()" -> Valuable(
                                    operand2.convertToString(operand2),
                                    Type.STRING
                                )

                                ".toBool()" -> Valuable(operand2.convertToBool(operand2), Type.BOOL)
                                ".toList()" -> {
                                    val array = operand2.convertToArray(operand2).toMutableList()
                                    val listVal = Valuable(array.size, Type.LIST)
                                    listVal.array = array
                                    listVal
                                }

                                ".sort()" -> operand2.sort()
                                "abs" -> operand2.absolute()
                                "exp" -> operand2.exponent()
                                "sorted" -> operand2.sorted()
                                "floor" -> operand2.floor()
                                "ceil" -> operand2.ceil()
                                "len" -> operand2.getLength()
                                else -> {
                                    throwOperationError("Expected correct expression but bad operation was found")
                                    throw Exception()
                                }
                            }
                        )
                        continue
                    }
                    var operand1 = try {
                        stack.removeLast()
                    } catch (e: Exception) {
                        throwOperationError("Expected correct expression but bad operation was found")
                    }

                    if (value in listOf("=", "/=", "+=", "-=", "*=", "%=", "//=")) {
                        if (operand1 is Valuable) {
                            operand1.value = operand2.value
                            operand1.type = operand2.type
                            operand1.array = operand2.array
                        } else if (operand1 is Variable) {
                            val operand = when (value) {
                                "=" -> operand2
                                "/=" -> tryFindInMemory(memory, operand1) / operand2
                                "*=" -> tryFindInMemory(memory, operand1) * operand2
                                "+=" -> tryFindInMemory(memory, operand1) + operand2
                                "-=" -> tryFindInMemory(memory, operand1) - operand2
                                "%=" -> tryFindInMemory(memory, operand1) % operand2
                                "//=" -> tryFindInMemory(memory, operand1).intDiv(operand2)
                                else -> {
                                    throwOperationError("Expected correct expression but bad operation was found")
                                    throw Exception()
                                }
                            }

                            if (initialize) {
                                pushToLocalMemory(operand1.name, operand2.type, operand.clone())
                            } else {
                                tryPushToAnyMemory(
                                    memory,
                                    operand1.name,
                                    operand2.type,
                                    operand.clone()
                                )
                            }
                        }

                        return operand2
                    }

                    if (value == "#") {
                        operand1 as Variable
                        if (initialize) {
                            pushToLocalMemory(operand1.name, Type.LIST, operand2)
                        } else {
                            tryPushToAnyMemory(memory, operand1.name, Type.LIST, operand2)
                        }

                        return operand2
                    }

                    if (operand1 is Variable) {
                        try {
                            operand1 = tryFindInMemory(memory, operand1)
                        } catch (e: StackCorruptionError) {
                            throw RuntimeError("${e.message}")
                        }
                    }
                    operand1 as Valuable

                    val result: Valuable? = when (value) {
                        "?" -> {
                            try {
                                operand1.array[operand2.value.toInt()]
                            } catch (e: IndexOutOfBoundsException) {
                                throw IndexOutOfRangeError("${e.message}")
                            }
                        }

                        "+" -> operand1 + operand2
                        "-" -> operand1 - operand2
                        "*" -> operand1 * operand2
                        "/" -> operand1 / operand2
                        "//" -> operand1.intDiv(operand2)
                        "%" -> operand1 % operand2

                        "&&" -> operand1.and(operand2)
                        "||" -> operand1.or(operand2)

                        "==" -> Valuable(operand1 == operand2, Type.BOOL)
                        "!=" -> Valuable(operand1 != operand2, Type.BOOL)
                        "<" -> Valuable(operand1 < operand2, Type.BOOL)
                        ">" -> Valuable(operand1 > operand2, Type.BOOL)
                        "<=" -> Valuable(operand1 < operand2 || operand1 == operand2, Type.BOOL)
                        ">=" -> Valuable(operand1 > operand2 || operand1 == operand2, Type.BOOL)
                        "=" -> {
                            if (initialize) {
                                pushToLocalMemory(operand1.value, operand2.type, operand2.clone())
                            } else {
                                tryPushToAnyMemory(
                                    memory,
                                    operand1.value,
                                    operand2.type,
                                    operand2.clone()
                                )
                            }
                            operand2
                        }

                        else -> null
                    }

                    stack.add(result!!)
                } catch (e: Exception) {
                    throw RuntimeError("${e.message}\nAt expression: $raw")
                }
            }
        }

        if (stack.isEmpty()) {
            throwOperationError("Expected correct expression but bad operation was found", raw)
        }
        if (stack.size > 1) {
            throwOperationError("Expected correct expression but bad operation was found", raw)
        }
        val last = stack.removeLast()
        if (last is Variable) {
            try {
                return tryFindInMemory(memory, last)
            } catch (e: StackCorruptionError) {
                throw RuntimeError("${e.message}\nAt expression: $raw")
            }
        }
        return last as Valuable
    }

    private fun throwOperationError(message: String, raw: String = "") {
        try {
            throw OperationError(message)
        } catch (e: Exception) {
            if (raw.isEmpty()) {
                throw RuntimeError("${e.message}")
            }
            throw RuntimeError("${e.message}\nAt expression: $raw")
        }
    }
}