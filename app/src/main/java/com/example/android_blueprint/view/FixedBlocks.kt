package com.example.android_blueprint.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.android_blueprint.model.BlockValue
import com.example.android_blueprint.ui.theme.BlockHeight
import com.example.android_blueprint.ui.theme.HeightOfSmallBlocks

@Composable
fun BinaryFixedOperatorBlock(
    value: BlockValue.BinaryOperator,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .height(BlockHeight)
    ) {
        BinaryOperatorText(modifier = Modifier.align(Alignment.Center), text = value.text)
        SupportingFlow()
        SupportingFlow(modifier = Modifier.align(Alignment.BottomStart))
        SupportingFlow(modifier = Modifier.align(Alignment.CenterEnd))
    }
}

@Composable
fun UnaryFixedOperatorBlock(
    value: BlockValue.UnaryOperator,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .height(BlockHeight)
    ) {
        UnaryOperatorText(modifier = Modifier.align(Alignment.Center), text = value.text)
        SupportingFlow(modifier = Modifier.align(Alignment.CenterStart))
        SupportingFlow(modifier = Modifier.align(Alignment.CenterEnd))
    }
}

@Composable
fun FixedPrintBlock(
    value: BlockValue.PrintBlock,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .heightIn(min = BlockHeight)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            MainFlow()
            MainFlow()
        }
        SupportingFlow(modifier = Modifier.align(Alignment.Start))
    }
}


@Composable
fun FixedBranchBlock(
    value: BlockValue.IfBlock,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .heightIn(min = BlockHeight)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            MainFlow(modifier = Modifier.align(Alignment.TopStart))
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                TextForFlow(text = "True")
                MainFlow()
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier.align(Alignment.TopStart)) {
                SupportingFlow()
                TextForFlow(text = "Condition")
            }
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                TextForFlow(text = "False")
                MainFlow()
            }
        }

    }
}


@Composable
fun FixedInitializationBlock(
    value: BlockValue.InitializationBlock,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .heightIn(min = BlockHeight)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            MainFlow()
            MainFlow()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            placeholderText(
                text = "name = value", modifier = Modifier
                    .weight(1f)
            )
            SupportingFlow(modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}

@Composable
fun FixedLoopBlock(
    value: BlockValue,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .heightIn(min = BlockHeight)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            MainFlow()
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                TextForFlow(text = "loop")
                MainFlow()
            }
        }
        placeholderText(text = "conditional", modifier = Modifier.fillMaxWidth())
        Row(modifier = Modifier.align(Alignment.End)) {
            TextForFlow(text = "endloop")
            MainFlow()
        }
    }
}

@Composable
fun FixedGetValueBlock(
    value: BlockValue.GetValueBlock,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .heightIn(min = HeightOfSmallBlocks)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        Row {
            placeholderText(text = "expression", modifier = Modifier.weight(1f))
            SupportingFlow()
        }
    }
}

@Composable
fun FixedFunctionBlock(
    value: BlockValue.FunctionBlock,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .heightIn(min = HeightOfSmallBlocks)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        Row {
            placeholderText(text = "name(args)", modifier = Modifier.weight(1f))
            MainFlow()
        }
    }
}

@Composable
fun FixedReturnBlock(
    value: BlockValue.ReturnBlock,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .heightIn(min = BlockHeight)
    ) {
        MainFlow()
        ComplexBlockText(
            modifier = value.modifier.align(Alignment.CenterHorizontally),
            text = value.text
        )
        Row {
            SupportingFlow()
            TextForFlow(text = "value")
        }
    }
}