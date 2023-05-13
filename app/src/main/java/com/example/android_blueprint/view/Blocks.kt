package com.example.android_blueprint.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_blueprint.model.BlockValue
import com.example.android_blueprint.ui.theme.BlockShape
import com.example.android_blueprint.ui.theme.ComplexBlockTextSize
import com.example.android_blueprint.ui.theme.DefaultPadding
import com.example.android_blueprint.ui.theme.FlowSize
import com.example.android_blueprint.ui.theme.FlowTextSize
import com.example.android_blueprint.ui.theme.MainFlowShape
import com.example.android_blueprint.ui.theme.OperatorsTextColor
import com.example.android_blueprint.ui.theme.OperatorsTextSize
import com.example.android_blueprint.ui.theme.TextPaddingForFlow
import com.example.android_blueprint.ui.theme.neueMedium
import com.example.android_blueprint.viewModel.InfiniteFieldViewModel

@Composable
fun SetBlock(
    value: Any,
    addBlock: ((blockValue: Any) -> Unit)? = null
) {
    val infiniteFieldViewModel: InfiniteFieldViewModel = viewModel()
    if (addBlock == null) {
        when (value) {
            BlockValue.Operator.INVERSION -> UnaryMovableOperatorBlock(value = value as BlockValue.Operator)
            is BlockValue.Operator -> BinaryMovableOperatorBlock(value = value)
            is BlockValue.InitializationBlock -> MovableInitializationBlock(
                value = value,
                addVariable = infiniteFieldViewModel::addVariable,
                removeAtIndex = infiniteFieldViewModel::removeAtIndex,
                valueChange = infiniteFieldViewModel::valueChange
            )

            is BlockValue.BranchBlock -> MovableBranchBlock(value = value)
            is BlockValue.PrintBlock -> MovablePrintBlock(value = value)
        }
    } else {
        when (value) {
            BlockValue.Operator.INVERSION -> UnaryFixedOperatorBlock(value = value as BlockValue.Operator, addBlock = addBlock)
            is BlockValue.Operator -> BinaryFixedOperatorBlock(value = value, addBlock = addBlock)
            is BlockValue.InitializationBlock -> FixedInitializationBlock(
                value,
                addBlock = addBlock
            )

            is BlockValue.BranchBlock -> FixedBranchBlock(value = value, addBlock = addBlock)
            is BlockValue.PrintBlock -> FixedPrintBlock(value = value, addBlock = addBlock)
        }
    }
}


@Composable
fun TextForFlow(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = FlowTextSize,
        color = Color.White,
        modifier = modifier.padding(top = TextPaddingForFlow)
    )
}

@Composable
fun OperatorText(modifier: Modifier, text: String) {
    Text(
        text = text,
        fontFamily = neueMedium,
        fontSize = OperatorsTextSize,
        color = OperatorsTextColor,
        modifier = modifier
    )
}

@Composable
fun ComplexBlockText(modifier: Modifier, text: String) {
    Text(
        text = text,
        fontFamily = neueMedium,
        fontSize = ComplexBlockTextSize,
        color = OperatorsTextColor,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}


@Composable
fun SupportingFlow(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(DefaultPadding)
            .clip(BlockShape)
            .size(FlowSize)
            .background(Color.White)
    )
}

@Composable
fun MainFlow(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(DefaultPadding)
            .clip(MainFlowShape)
            .size(FlowSize)
            .background(Color.White)
    )
}
