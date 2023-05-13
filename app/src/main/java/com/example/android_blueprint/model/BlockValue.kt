package com.example.android_blueprint.model

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


sealed class BlockValue(
    val text: String,
    val modifier: Modifier,
) {
    enum class BinaryOperator(val text: String) {
        SUBTRACTION("-"),
        MULTIPLICATION("×"),
        DIVISION("÷"),
        REMAINDER("%"),
        ADDITION("+"),
        EQUALITY("=="),
        NOT_EQUAL("!="),
        GREATER(">"),
        LESS("<"),
        GREATER_OR_EQUAL(">="),
        LESS_OR_EQUAL("<=")
    }

    enum class UnaryOperator(val text: String) {
        INVERSION("Not"),
    }

    object InitializationBlock : BlockValue(
        text = "Initialization",
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color.LightGray, Color.Gray),
                    start = Offset(0f, 0f),
                    end = Offset(100f, 0f)
                )
            )
    )

    object BranchBlock : BlockValue(
        text = "Branch",
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color.LightGray, Color.Gray),
                    start = Offset(0f, 0f),
                    end = Offset(100f, 0f)
                )
            )
    )

    object PrintBlock: BlockValue(
        text = "Print",
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color.LightGray, Color.Gray),
                    start = Offset(0f, 0f),
                    end = Offset(100f, 0f)
                )
            )
    )

}
