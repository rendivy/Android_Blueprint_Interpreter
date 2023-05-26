package com.example.android_blueprint.model

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import android.content.res.Resources;
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.android_blueprint.R


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
        text = Resources.getSystem().getString(R.string.Initialization),
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

    object SetBlock : BlockValue(
        text = Resources.getSystem().getString(R.string.Set),
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

    object IfBlock : BlockValue(
        text = Resources.getSystem().getString(R.string.If),
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

    object EndifBlock : BlockValue(
        text = Resources.getSystem().getString(R.string.EndIf),
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

    object PrintBlock : BlockValue(
        text = Resources.getSystem().getString(R.string.Print),
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

    object StartBlock : BlockValue(
        text = Resources.getSystem().getString(R.string.Start),
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

    object EndBlock : BlockValue(
        text = Resources.getSystem().getString(R.string.End),
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

    object ForBlock : BlockValue(
        text = Resources.getSystem().getString(R.string.For),
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

    object WhileBlock : BlockValue(
        text = Resources.getSystem().getString(R.string.While),
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

    object GetValueBlock : BlockValue(
        text =Resources.getSystem().getString(R.string.GetValue),
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

    object FunctionBlock : BlockValue(
        text = Resources.getSystem().getString(R.string.Function),
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

    object BreakBlock : BlockValue(
        text = Resources.getSystem().getString(R.string.Break),
        modifier = Modifier
            .fillMaxWidth()
    )

    object ContinueBlock : BlockValue(
        text = Resources.getSystem().getString(R.string.Continue),
        modifier = Modifier
            .fillMaxWidth()
    )


    object ReturnBlock : BlockValue(
        text = Resources.getSystem().getString(R.string.Return),
        modifier = Modifier
            .fillMaxWidth()
    )

}
