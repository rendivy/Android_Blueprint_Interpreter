package com.example.android_blueprint.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
import block.EndBlock
import block.EndIfBlock
import block.IBinaryOperatorBlock
import block.IUnaryOperatorBlock
import block.IfBlock
import block.InitializationAndSetVariableBlock
import block.PrintBlock
import block.StartBlock
import block.UnaryNotOperatorBlock
import com.example.android_blueprint.model.BlockValue
import com.example.android_blueprint.model.FieldBlock
import com.example.android_blueprint.model.Transform
import com.example.android_blueprint.ui.theme.InitialOffset
import com.example.android_blueprint.ui.theme.InitialScale

class InfiniteFieldViewModel : ViewModel() {
    private var currentIndex = 0
    val startBlock = StartBlock()
    val endBlock = EndBlock()
    val blocks = mutableStateListOf<FieldBlock>()
    var deleteMode by mutableStateOf(false)
    var transform by mutableStateOf(Transform(InitialScale, InitialOffset))
    fun addBlock(blockValue: Any) {
        if (blockValue == BlockValue.IfBlock) {
            blocks.add(FieldBlock(value = blockValue, index = currentIndex++))
            blocks.add(FieldBlock(value = BlockValue.EndifBlock, index = currentIndex++))
        } else {
            blocks.add(FieldBlock(value = blockValue, index = currentIndex++))
        }
    }

    fun deleteMovableBlock(index: Int) {
        blocks[index] = FieldBlock()
    }

    fun getDeleteButtonColor(): Color {
        return if (deleteMode) {
            Color.Red
        } else {
            Color.Gray
        }
    }

    fun changeMode() {
        deleteMode = !deleteMode
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
            else -> UnaryNotOperatorBlock() as T
        }
    }

    fun changeTransform(zoomChange: Float, offsetChange: Offset) {
        transform = transform.copy(
            scale = transform.scale * zoomChange,
            offset = transform.offset + offsetChange
        )
    }
}