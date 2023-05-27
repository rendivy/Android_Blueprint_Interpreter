package com.example.android_blueprint.model

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.android_blueprint.ui.theme.ActionColor
import com.example.android_blueprint.ui.theme.IfBlockColor


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
        LESS_OR_EQUAL("<="),
        LOG("log"),
        POW("pow")
    }

    enum class UnaryOperator(val text: String) {
        INVERSION("not"),
        ABS("abs"),
        SIN("sin"),
        COS("cos"),
        TG("tg"),
        CTG("ctg"),
        ARCSIN("arcsin"),
        ARCCOS("arccos"),
        ARCTG("arctg"),
        ARCCTG("arcctg")
    }




    object InitializationBlock : BlockValue(
        text = "Initialization",
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(ActionColor, ActionColor),
                    start = Offset(0f, 0f),
                    end = Offset(100f, 0f)
                )
            )
    )

    object SetBlock : BlockValue(
        text = "Set",
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(ActionColor, ActionColor),
                    start = Offset(0f, 0f),
                    end = Offset(100f, 0f)
                )
            )
    )

    object IfBlock : BlockValue(
        text = "If",
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(ActionColor, ActionColor),
                    start = Offset(0f, 0f),
                    end = Offset(100f, 0f)
                )
            )
    )

    object EndifBlock : BlockValue(
        text = "Endif",
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(ActionColor, ActionColor),
                    start = Offset(0f, 0f),
                    end = Offset(100f, 0f)
                )
            )
    )

    object PrintBlock : BlockValue(
        text = "Print",
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(ActionColor, ActionColor),
                    start = Offset(0f, 0f),
                    end = Offset(100f, 0f)
                )
            )
    )

    object StartBlock : BlockValue(
        text = "Start",
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(ActionColor, ActionColor),
                    start = Offset(0f, 0f),
                    end = Offset(100f, 0f)
                )
            )
    )

    object EndBlock : BlockValue(
        text = "End",
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(ActionColor, ActionColor),
                    start = Offset(0f, 0f),
                    end = Offset(100f, 0f)
                )
            )
    )

    object ForBlock : BlockValue(
        text = "For",
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(ActionColor, ActionColor),
                    start = Offset(0f, 0f),
                    end = Offset(100f, 0f)
                )
            )
    )

    object WhileBlock : BlockValue(
        text = "While",
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(ActionColor, ActionColor),
                    start = Offset(0f, 0f),
                    end = Offset(100f, 0f)
                )
            )
    )

    object GetValueBlock : BlockValue(
        text = "Get value",
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(ActionColor, ActionColor),
                    start = Offset(0f, 0f),
                    end = Offset(100f, 0f)
                )
            )
    )

    object FunctionBlock : BlockValue(
        text = "Function",
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(ActionColor, ActionColor),
                    start = Offset(0f, 0f),
                    end = Offset(100f, 0f)
                )
            )
    )

    object BreakBlock : BlockValue(
        text = "Break",
        modifier = Modifier
            .fillMaxWidth()
    )

    object ContinueBlock : BlockValue(
        text = "Continue",
        modifier = Modifier
            .fillMaxWidth()
    )


    object ReturnBlock : BlockValue(
        text = "Return",
        modifier = Modifier
            .fillMaxWidth()
    )

}
