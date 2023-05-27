package com.example.android_blueprint.viewModel

import block.BinaryDivOperatorBlock
import block.BinaryEqualOperatorBlock
import block.BinaryGreaterOperatorBlock
import block.BinaryGreaterOrEqualOperatorBlock
import block.BinaryLessOperatorBlock
import block.BinaryLessOrEqualOperatorBlock
import block.BinaryLogOperatorBlock
import block.BinaryModOperatorBlock
import block.BinaryMulOperatorBlock
import block.BinaryNotEqualOperatorBlock
import block.BinaryPowOperatorBlock
import block.BinarySubOperatorBlock
import block.BinarySumOperatorBlock
import block.BreakBlock
import block.CallFunctionBlock
import block.ContinueBlock
import block.EndFunctionBlock
import block.EndIfBlock
import block.ForBlock
import block.FunctionBlock
import block.GetVariableBlock
import block.IBinaryOperatorBlock
import block.IMainFLowBlock
import block.IUnaryOperatorBlock
import block.IfBlock
import block.InitializationVariableBlock
import block.PrintBlock
import block.SetVariableBlock
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

    private fun createCallFunction(): CallFunctionBlock {
        return CallFunctionBlock()
    }
    private fun createWhileBlock(): WhileBlock {
        return WhileBlock()
    }

    private fun createForBlock(): ForBlock {
        return ForBlock()
    }

    private fun createGetValueBlock(): GetVariableBlock {
        return GetVariableBlock()
    }

    private fun createFunctionBlock(): FunctionBlock {
        return FunctionBlock()
    }

    private fun createReturnBlock(): EndFunctionBlock {
        return EndFunctionBlock()
    }

    private fun createIfBlock(): IfBlock {
        return IfBlock()
    }

    private fun createEndifBlock(): EndIfBlock {
        return EndIfBlock()
    }

    private fun createInitializationBlock(): InitializationVariableBlock {
        return InitializationVariableBlock()
    }

    private fun createSetBlock(): SetVariableBlock {
        return SetVariableBlock()
    }

    private fun createContinueBlock(): IMainFLowBlock {
        return ContinueBlock()
    }

    private fun createBreakBlock(): IMainFLowBlock {
        return BreakBlock()
    }


    private fun createPrintBlock(): PrintBlock {
        return PrintBlock()
    }

    private fun createBinaryOperatorBlock(binaryOperator: BlockValue.BinaryOperator): IBinaryOperatorBlock {
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
            BlockValue.BinaryOperator.LOG -> BinaryLogOperatorBlock()
            BlockValue.BinaryOperator.POW -> BinaryPowOperatorBlock()
        }
    }

    private fun createUnaryOperatorBlock(unaryOperator: BlockValue.UnaryOperator): IUnaryOperatorBlock {
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

    fun createObj(blockValue: Any): Any? {
        return when (blockValue) {

            is BlockValue.CallFunctionBlock -> createCallFunction()

            is BlockValue.SetBlock -> createSetBlock()

            is BlockValue.ContinueBlock -> createContinueBlock()

            is BlockValue.BreakBlock -> createBreakBlock()

            is BlockValue.ReturnBlock -> createReturnBlock()

            is BlockValue.FunctionBlock -> createFunctionBlock()

            is BlockValue.GetValueBlock -> createGetValueBlock()

            is BlockValue.ForBlock -> createForBlock()

            is BlockValue.WhileBlock -> createWhileBlock()

            is BlockValue.UnaryOperator -> createUnaryOperatorBlock(unaryOperator = blockValue)

            is BlockValue.BinaryOperator -> createBinaryOperatorBlock(binaryOperator = blockValue)

            is BlockValue.InitializationBlock -> createInitializationBlock()

            is BlockValue.IfBlock -> createIfBlock()

            is BlockValue.EndifBlock -> createEndifBlock()

            is BlockValue.PrintBlock -> createPrintBlock()
            else -> null
        }
    }

}