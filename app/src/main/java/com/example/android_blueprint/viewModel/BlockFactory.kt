package com.example.android_blueprint.viewModel

import block.BinaryDivOperatorBlock
import block.BinaryEqualOperatorBlock
import block.BinaryGreaterOperatorBlock
import block.BinaryGreaterOrEqualOperatorBlock
import block.BinaryLessOperatorBlock
import block.BinaryLessOrEqualOperatorBlock
import block.BinaryModOperatorBlock
import block.BinaryMulOperatorBlock
import block.BinaryNotEqualOperatorBlock
import block.BinarySubOperatorBlock
import block.BinarySumOperatorBlock
import block.EndFunctionBlock
import block.EndIfBlock
import block.ForBlock
import block.FunctionBlock
import block.GetVariableBlock
import block.IBinaryOperatorBlock
import block.IUnaryOperatorBlock
import block.IfBlock
import block.InitializationAndSetVariableBlock
import block.PrintBlock
import block.UnaryAbsOperatorBlock
import block.UnaryArccosOperatorBlock
import block.UnaryArccotOperatorBlock
import block.UnaryArcsinOperatorBlock
import block.UnaryArctanOperatorBlock
import block.UnaryCosOperatorBlock
import block.UnaryCotOperatorBlock
import block.UnaryNotOperatorBlock
import block.UnarySinOperatorBlock
import block.UnaryTanOperatorBlock
import block.WhileBlock
import com.example.android_blueprint.model.BlockValue

object BlockFactory {

    fun createWhileBlock(): WhileBlock {
        return WhileBlock()
    }

    fun createForBlock(): ForBlock {
        return ForBlock()
    }

    fun createGetValueBlock(): GetVariableBlock {
        return GetVariableBlock()
    }

    fun createFunctionBlock(): FunctionBlock {
        return FunctionBlock()
    }

    fun createReturnBlock(): EndFunctionBlock {
        return EndFunctionBlock()
    }

    fun createIfBlock(): IfBlock {
        return IfBlock()
    }

    fun createEndifBlock(): EndIfBlock {
        return EndIfBlock()
    }

    fun createInitializationBlock(): InitializationAndSetVariableBlock {
        return InitializationAndSetVariableBlock()
    }

    fun createPrintBlock(): PrintBlock {
        return PrintBlock()
    }

    fun createBinaryOperatorBlock(binaryOperator: BlockValue.BinaryOperator): IBinaryOperatorBlock {
        return when (binaryOperator) {
            BlockValue.BinaryOperator.ADDITION -> BinarySumOperatorBlock()
            BlockValue.BinaryOperator.SUBTRACTION -> BinarySubOperatorBlock()
            BlockValue.BinaryOperator.LESS_OR_EQUAL -> BinaryLessOrEqualOperatorBlock()
            BlockValue.BinaryOperator.LESS -> BinaryLessOperatorBlock()
            BlockValue.BinaryOperator.GREATER -> BinaryGreaterOperatorBlock()
            BlockValue.BinaryOperator.GREATER_OR_EQUAL -> BinaryGreaterOrEqualOperatorBlock()
            BlockValue.BinaryOperator.NOT_EQUAL -> BinaryNotEqualOperatorBlock()
            BlockValue.BinaryOperator.EQUALITY -> BinaryEqualOperatorBlock()
            BlockValue.BinaryOperator.REMAINDER -> BinaryModOperatorBlock()
            BlockValue.BinaryOperator.DIVISION -> BinaryDivOperatorBlock()
            BlockValue.BinaryOperator.MULTIPLICATION -> BinaryMulOperatorBlock()
        }
    }

    fun createUnaryOperatorBlock(unaryOperator: BlockValue.UnaryOperator): IUnaryOperatorBlock {
        return when (unaryOperator) {
            BlockValue.UnaryOperator.INVERSION -> UnaryNotOperatorBlock()
            BlockValue.UnaryOperator.ARCCTG -> UnaryArctanOperatorBlock()
            BlockValue.UnaryOperator.ARCTG -> UnaryArccotOperatorBlock()
            BlockValue.UnaryOperator.ARCCOS -> UnaryArccosOperatorBlock()
            BlockValue.UnaryOperator.ARCSIN -> UnaryArcsinOperatorBlock()
            BlockValue.UnaryOperator.TG -> UnaryTanOperatorBlock()
            BlockValue.UnaryOperator.CTG -> UnaryCotOperatorBlock()
            BlockValue.UnaryOperator.COS -> UnaryCosOperatorBlock()
            BlockValue.UnaryOperator.SIN -> UnarySinOperatorBlock()
            BlockValue.UnaryOperator.ABS -> UnaryAbsOperatorBlock()
        }
    }
}