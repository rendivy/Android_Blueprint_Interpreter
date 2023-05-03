import Block.ViewBlock

class Interpret(_View_blocks: List<ViewBlock>? = null){
    var blocks = _View_blocks?.toMutableList()
    var output = ""
    var input = ""
    var waitingForInput = false
    var memory = Memory(null, "GLOBAL_SCOPE")
    var debug = false

    private var parseMap: MutableMap<String, List<String>> = mutableMapOf()
    private var appliedConditions: MutableList<Boolean> = mutableListOf()
    private var cycleLines: MutableList<Int> = mutableListOf()
    private var functionLines = mutableMapOf<String, MutableList<Int>>()
    private var currentFunction = mutableListOf("GLOBAL")
    private var functionsVarsMap = mutableMapOf<String, MutableList<String>>()
    private var funcVarsLines = mutableListOf<Int>()
    private var args = mutableListOf<List<String>>()
    private var forLines = mutableListOf<Int>()

    private fun pushToLocalMemory(name: String, type: Type = Type.UNDEFINED, valueViewBlock: ViewBlock) {
        valueViewBlock as Valuable

        if (type == Type.LIST) {
            val block = valueViewBlock.clone()
            block.type = type
            memory.push(name, block)
            return
        }

        valueViewBlock.type = type
        memory.push(name, valueViewBlock)
    }

    private fun tryPushToAnyMemory(
        memory: Memory,
        name: String,
        type: Type,
        valueViewBlock: ViewBlock
    ): Boolean {
        valueViewBlock as Valuable
        valueViewBlock.type = type

        if (memory.get(name) != null) {
            memory.push(name, valueViewBlock)
            return true
        }

        if (memory.prevMemory == null) {
            memory.throwStackError(name)
            return false
        }

        return tryPushToAnyMemory(memory.prevMemory, name, type, valueViewBlock)
    }

    private fun getValue(data: String): ViewBlock? {
        return when {
            data in listOf("abs", "exp", "sorted", "ceil", "floor", "len") -> {
                null
            }
            data == "rand()" -> {
                Valuable(Math.random(), Type.DOUBLE)
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

    private fun tryFindInMemory(memory: Memory, viewBlock: ViewBlock): Valuable {
        viewBlock as Variable
        val address = viewBlock.name
        val value = memory.get(address)

        if (value != null) {
            return value
        }

        if (memory.prevMemory == null) {
            memory.throwStackError(address)
            throw Exception()
        }

        return tryFindInMemory(memory.prevMemory, viewBlock)
    }

    fun parseRawBlock(raw: String, initialize: Boolean = false): Valuable {
        val data = parseMap[raw] ?: Notation.convertToRpn(Notation.tokenizeString(raw))
        parseMap[raw] = data

        val stack = mutableListOf<ViewBlock>()
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