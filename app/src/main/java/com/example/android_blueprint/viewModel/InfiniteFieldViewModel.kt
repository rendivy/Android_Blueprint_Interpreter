package com.example.android_blueprint.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
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
import block.BlockEntity
import block.IBinaryOperatorBlock
import block.IUnaryOperatorBlock
import block.InitializationAndSetVariableBlock
import block.PrintBlock
import block.UnaryNotOperatorBlock
import com.example.android_blueprint.model.BlockValue
import com.example.android_blueprint.model.Transform
import com.example.android_blueprint.ui.theme.InitialOffset
import com.example.android_blueprint.ui.theme.InitialScale

class InfiniteFieldViewModel : ViewModel() {
    val blocks = mutableStateListOf<Any>()
    var transform by mutableStateOf(Transform(InitialScale, InitialOffset))
    fun addBlock(blockValue: Any) {
        blocks.add(blockValue)
    }

    fun createInitializationBlock(): InitializationAndSetVariableBlock {
        return InitializationAndSetVariableBlock()
    }

    fun createPrintBlock(): PrintBlock {
        return PrintBlock()
    }

    fun <T> createBinaryOperatorBlockInstance(binaryOperator: BlockValue.BinaryOperator): T where T : BlockEntity, T : IBinaryOperatorBlock {
        return when (binaryOperator) {
            BlockValue.BinaryOperator.ADDITION -> BinarySumOperatorBlock() as T
            BlockValue.BinaryOperator.SUBTRACTION -> BinarySubOperatorBlock() as T
            BlockValue.BinaryOperator.LESS_OR_EQUAL -> BinaryLessOrEqualOperatorBlock() as T
            BlockValue.BinaryOperator.LESS -> BinaryLessOperatorBlock() as T
            BlockValue.BinaryOperator.GREATER -> BinaryGreaterOperatorBlock() as T
            BlockValue.BinaryOperator.GREATER_OR_EQUAL -> BinaryGreaterOrEqualOperatorBlock() as T
            BlockValue.BinaryOperator.NOT_EQUAL -> BinaryNotEqualOperatorBlock() as T
            BlockValue.BinaryOperator.EQUALITY -> BinaryEqualOperatorBlock() as T
            BlockValue.BinaryOperator.REMAINDER -> BinaryModOperatorBlock() as T
            BlockValue.BinaryOperator.DIVISION -> BinaryDivOperatorBlock() as T
            BlockValue.BinaryOperator.MULTIPLICATION -> BinaryMulOperatorBlock() as T
        }
    }

    fun <T> createUnaryOperatorBlockInstance(unaryOperator: BlockValue.UnaryOperator): T where T : BlockEntity, T : IUnaryOperatorBlock {
        return when (unaryOperator) {
            BlockValue.UnaryOperator.INVERSION -> UnaryNotOperatorBlock() as T
        }
    }

    fun changeTransform(zoomChange: Float, offsetChange: Offset) {
        transform = transform.copy(
            scale = transform.scale * zoomChange,
            offset = transform.offset + offsetChange
        )
    }

    fun addVariable(list: List<String>): List<String> {
        return list + listOf("")
    }

    fun removeAtIndex(list: List<String>, indexToRemove: Int): List<String> {
        return list.filterIndexed { index, _ -> index != indexToRemove }
    }

    fun valueChange(list: List<String>, index: Int, value: String): List<String> {
        return list.mapIndexed { i, old -> if (i == index) value else old }
    }

}