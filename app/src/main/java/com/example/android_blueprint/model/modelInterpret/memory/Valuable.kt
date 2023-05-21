package memory

import Instruction
import Type
import exceptions.TypeError
import kotlin.math.*

class Valuable (varValue: Any, var type: Type): MemoryArea(Instruction.VAL){
    var value: String = varValue.toString()
    var array: MutableList<Valuable> = mutableListOf()

    private val numericTypes = listOf(Type.INT, Type.DOUBLE)

    fun clone(): Valuable {
        val valuable = Valuable(value, type)
        valuable.array = array
        return valuable
    }

    fun getLength(): Valuable {
        val types = listOf(Type.LIST, Type.STRING)

        return when (type){
            !in types -> throw TypeError("len() be applied to type $type")
            Type.LIST -> Valuable(array.size, Type.INT)
            else -> Valuable(value.length, Type.INT)
        }
    }

    operator fun unaryPlus(): Valuable {
        return when (type){
            Type.STRING -> {
                when (value.contains('.')){
                    true -> Valuable(convertToDouble(this), Type.DOUBLE)
                    false -> Valuable(convertToInt(this), Type.INT)
                }
            }
            Type.UNDEFINED -> throw TypeError("Unary plus can't be applied to type $type")
            else -> this
        }
    }

    operator fun unaryMinus(): Valuable {
        return when (type) {
            Type.INT -> Valuable(-value.toInt(), type)
            Type.DOUBLE -> Valuable(-value.toDouble(), type)
            else -> throw TypeError("Unary minus can't be applied to type $type")
        }
    }

    operator fun plus(operand: Valuable): Valuable {
        return when{
            type == Type.STRING && operand.type == Type.STRING -> {
                Valuable(value + operand.value, type)
            }
            type in numericTypes && operand.type in numericTypes ->{
                when (checkDoubling(this, operand)) {
                    true -> Valuable(value.toDouble() + operand.value.toDouble(), Type.DOUBLE)
                    false -> Valuable(value.toInt() + operand.value.toInt(), Type.INT)
                }
            }
            else -> {
                throw TypeError("Expected $type but found ${operand.type}")
            }
        }
    }

    operator fun minus(operand: Valuable): Valuable {
        if (type in numericTypes && operand.type in numericTypes) {
            return when(checkDoubling(this, operand)){
                true -> Valuable(value.toDouble() - operand.value.toDouble(), Type.DOUBLE)
                false -> Valuable(value.toInt() - operand.value.toInt(), Type.INT)
            }
        }
        throw TypeError("Expected INT or FLOAT but found $type and ${operand.type}")
    }

    operator fun times(operand: Valuable): Valuable {
        return when {
            type in numericTypes && operand.type in numericTypes ->{
                when {
                    checkDoubling(this, operand) -> {
                        Valuable(value.toFloat() * operand.value.toFloat(), Type.DOUBLE)
                    }
                    else -> {
                        Valuable(value.toInt() * operand.value.toInt(), Type.INT)
                    }
                }
            }
            type == Type.INT && operand.type == Type.STRING -> {
                Valuable(operand.value.repeat(value.toInt()), operand.type)
            }
            type == Type.STRING && operand.type == Type.INT -> {
                Valuable(value.repeat(operand.value.toInt()), type)
            }
            type != Type.STRING -> {
                throw TypeError("Expected INT but found ${operand.type}")
            }
            else -> {
                throw TypeError("Expected INT but found $type")
            }
        }
    }

    operator fun div(operand: Valuable): Valuable {
        if (type in numericTypes && operand.type in numericTypes) {
            return Valuable(value.toDouble() / operand.value.toDouble(), Type.DOUBLE)
        }
        throw TypeError("Expected INT or FLOAT but found ${operand.type}")
    }

    fun intDiv(operand: Valuable): Valuable {
        if (type in numericTypes && operand.type in numericTypes) {
            return Valuable((value.toDouble() / operand.value.toDouble()).toInt(), Type.INT)
        }
        throw TypeError("Expected INT or FLOAT but found ${operand.type}")
    }

    operator fun rem(operand: Valuable): Valuable {
        if (type in numericTypes && operand.type in numericTypes) {
            val value = value.toDouble() / operand.value.toDouble()
            return when(value - floor(value)){
                0.0 -> Valuable(value.toInt(), Type.INT)
                else -> Valuable(value, Type.DOUBLE)
            }
        }
        throw TypeError("Expected INT or FLOAT but found ${operand.type}")
    }

    operator fun compareTo(operand: Valuable): Int {
        val dif = try {
            value.toDouble() - operand.value.toDouble()
        } catch (e: Exception) {
            throw TypeError("Expected INT or FLOAT but found $type and ${operand.type}")
        }
        return when {
            dif < 0 -> -1
            dif > 0 -> 1
            else -> 0
        }
    }

    override fun equals(other: Any?): Boolean {
        other as Valuable
        return other.type == type && other.value == value
    }

    fun and(operand: Valuable): Valuable {
        val value1 = convertToBool(operand)
        val value2 = convertToBool(this)

        return Valuable(value1 && value2, Type.BOOL)
    }

    fun or(operand: Valuable): Valuable {
        val value1 = convertToBool(operand)
        val value2 = convertToBool(this)

        return Valuable(value1 || value2, Type.BOOL)
    }

    fun convertToBool(valuable: Valuable): Boolean {
        return when (valuable.type) {
            Type.BOOL -> valuable.value.toBoolean()
            Type.INT -> valuable.value.toInt() != 0
            Type.DOUBLE -> valuable.value.toDouble() != 0.0
            Type.STRING -> valuable.value != ""
            Type.LIST -> valuable.array.isNotEmpty()
            Type.UNDEFINED -> false
        }
    }

    fun convertToDouble(valuable: Valuable): Double {
        return when (valuable.type) {
            Type.BOOL -> {
                if (valuable.value == "true") {
                    return 1.0
                }
                0.0
            }
            Type.INT -> valuable.value.toDouble()
            Type.DOUBLE -> valuable.value.toDouble()
            Type.STRING -> {
                try {
                    valuable.value.toDouble()
                } catch (e: Exception) {
                    throw TypeError("Expected number-containing string but ${valuable.value} was found")
                }
            }
            else -> throw TypeError("Expected convertible to Double type but ${valuable.type} was found")
        }
    }

    fun convertToString(valuable: Valuable): String {
        return when (valuable.type) {
            Type.LIST -> {
                valuable.array.toString()
            }
            Type.UNDEFINED -> {
                throw TypeError("Expected not-null type but ${valuable.type} was found")
            }
            else -> {
                valuable.value
            }
        }
    }

    fun convertToArray(valuable: Valuable): List<Valuable> {
        if (valuable.type != Type.STRING) {
            throw TypeError("Expected type STRING but ${valuable.type} was found")
        }

        val arr = valuable.value.toList()
        val valArr = mutableListOf<Valuable>()
        for (value in arr) {
            valArr.add(Valuable(value, Type.STRING))
        }
        return valArr
    }

    fun convertToInt(valuable: Valuable): Int {
        return when (valuable.type) {
            Type.BOOL -> {
                if (valuable.value == "true") {
                    return 1
                }
                0
            }
            Type.INT -> valuable.value.toInt()
            Type.DOUBLE -> valuable.value.toDouble().toInt()
            Type.STRING -> {
                try {
                    valuable.value.toDouble().toInt()
                } catch (e: Exception) {
                    throw TypeError("Expected number-containing string but ${valuable.value} was found")
                }
            }
            else -> throw TypeError("Expected convertible to INT type but ${valuable.type} was found")
        }
    }

    private fun checkDoubling(val1: Valuable, val2: Valuable): Boolean {
        return val1.type == Type.DOUBLE || val2.type == Type.DOUBLE
    }

    fun absolute(): Valuable {
        return when(type) {
            Type.INT -> Valuable(abs(value.toInt()), type)
            Type.DOUBLE -> Valuable(abs(value.toDouble()), type)
            else -> throw TypeError("Expected INT or DOUBLE but found $type")
        }
    }

    fun exponent(): Valuable {
        return when(type in numericTypes) {
            true -> Valuable(exp(value.toDouble()), Type.DOUBLE)
            else -> throw TypeError("Expected INT or DOUBLE but found $type")
        }
    }

    fun ceil(): Valuable {
        return when(type in numericTypes) {
            true -> Valuable(kotlin.math.ceil(value.toDouble()), Type.INT)
            else -> throw TypeError("Expected INT or DOUBLE but found $type")
        }
    }

    fun floor(): Valuable {
        if (type in numericTypes) {
            return Valuable(floor(value.toDouble()), Type.INT)
        }
        throw TypeError("Expected INT or FLOAT but found $type")
    }

    private fun srt(): MutableList<Valuable> {
        return array.sortedBy { i ->
            try {
                i.value.toDouble()
            } catch (e: Exception) {
                throw TypeError("Expected INT or DOUBLE but found ${i.type}")
            }
        }.toMutableList()
    }

    fun sorted(): Valuable {
        if (type == Type.LIST) {
            val valuable = Valuable(value, type)
            valuable.array = srt()
            return valuable
        }
        throw TypeError("Expected LIST but found $type")
    }

    fun sort(): Valuable {
        if (type == Type.LIST) {
            array = srt()
            return this
        }
        throw TypeError("Expected LIST but found $type")
    }

    fun sin(): Valuable {
        return when(type in numericTypes) {
            true -> Valuable(sin(value.toDouble()), Type.DOUBLE)
            else -> throw TypeError("Expected INT or DOUBLE but found $type")
        }
    }

    fun cos(): Valuable {
        return when(type in numericTypes) {
            true -> Valuable(cos(value.toDouble()), Type.DOUBLE)
            else -> throw TypeError("Expected INT or DOUBLE but found $type")
        }
    }

    fun tan(): Valuable {
        return when(type in numericTypes) {
            true -> Valuable(tan(value.toDouble()), Type.DOUBLE)
            else -> throw TypeError("Expected INT or DOUBLE but found $type")
        }
    }

    fun cot(): Valuable {
        return when(type in numericTypes) {
            true -> Valuable(1/tan(value.toDouble()), Type.DOUBLE)
            else -> throw TypeError("Expected INT or DOUBLE but found $type")
        }
    }

    fun arcsin(): Valuable {
        return when(type in numericTypes) {
            true -> Valuable(asin(value.toDouble()), Type.DOUBLE)
            else -> throw TypeError("Expected INT or DOUBLE but found $type")
        }
    }

    fun arccos(): Valuable {
        return when(type in numericTypes) {
            true -> Valuable(acos(value.toDouble()), Type.DOUBLE)
            else -> throw TypeError("Expected INT or DOUBLE but found $type")
        }
    }

    fun arctan(): Valuable {
        return when(type in numericTypes) {
            true -> Valuable(atan(value.toDouble()), Type.DOUBLE)
            else -> throw TypeError("Expected INT or DOUBLE but found $type")
        }
    }

    fun arccot(): Valuable {
        return when(type in numericTypes) {
            true -> Valuable(1/atan(value.toDouble()), Type.DOUBLE)
            else -> throw TypeError("Expected INT or DOUBLE but found $type")
        }
    }

    fun log(valuable: Valuable): Valuable {
        if(type !in numericTypes && valuable.type !in numericTypes) {
            throw TypeError("Expected INT or DOUBLE but found $type")
        }

        if(valuable.value.toDouble() <= 0 && valuable.value.toDouble() == 1.0 && value.toDouble() <= 0){
            throw TypeError("Does not satisfy the logarithm condition: base ${valuable.value} - number ${value}")
        }

        return when(type in numericTypes) {
            true -> Valuable(log(value.toDouble(), valuable.value.toDouble()), Type.DOUBLE)
            else -> throw TypeError("Expected INT or DOUBLE but found $type")
        }
    }

    fun pow(valuable: Valuable): Valuable {
        return when(type in numericTypes) {
            true -> Valuable(value.toDouble().pow(valuable.value.toDouble()), Type.DOUBLE)
            else -> throw TypeError("Expected INT or DOUBLE but found $type")
        }
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + value.hashCode()
        result = 31 * result + array.hashCode()
        return result
    }

    override fun toString(): String {
        return when (type) {
            Type.LIST -> "${array.toString()} ($type)"
            else -> "$value ($type)"
        }
    }
}