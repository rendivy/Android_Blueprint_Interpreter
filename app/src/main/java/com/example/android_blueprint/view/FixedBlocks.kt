package com.example.android_blueprint.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.android_blueprint.model.BlockValue
import com.example.android_blueprint.ui.theme.BlockHeight
import com.example.android_blueprint.ui.theme.BlockShape
import com.example.android_blueprint.ui.theme.ComplexBlockColor
import com.example.android_blueprint.ui.theme.OperatorBlockColor

@Composable
fun FixedOperatorBlock(
    value: BlockValue.Operator,
    addBlock: ((blockValue: Any) -> Unit)
) {
    Box(
        modifier = Modifier
            .height(BlockHeight)
            .clip(BlockShape)
            .background(OperatorBlockColor)
            .fillMaxWidth()
            .clickable(onClick = {
                addBlock(value)
            })
    ) {
        OperatorText(modifier = Modifier.align(Alignment.Center), text = value.text)
        SupportingFlow()
        SupportingFlow(modifier = Modifier.align(Alignment.BottomStart))
        SupportingFlow(modifier = Modifier.align(Alignment.CenterEnd))
    }
}

@Composable
fun FixedPrintBlock(
    value: BlockValue.PrintBlock,
    addBlock: ((blockValue: Any) -> Unit)
) {
    Column(
        modifier = Modifier
            .heightIn(min = BlockHeight)
            .clip(BlockShape)
            .background(ComplexBlockColor)
            .fillMaxWidth()
            .clickable(onClick = {
                addBlock(value)
            })
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
    value: BlockValue.BranchBlock,
    addBlock: ((blockValue: Any) -> Unit)
) {
    Column(
        modifier = Modifier
            .heightIn(min = BlockHeight)
            .clip(BlockShape)
            .background(ComplexBlockColor)
            .fillMaxWidth()
            .clickable(onClick = {
                addBlock(value)
            })
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
    addBlock: ((blockValue: Any) -> Unit)
) {
    Column(
        modifier = Modifier
            .heightIn(min = BlockHeight)
            .clip(BlockShape)
            .background(ComplexBlockColor)
            .fillMaxWidth()
            .clickable(onClick = {
                addBlock(value)
            })
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
        Icon(
            Icons.Rounded.Add,
            contentDescription = null,
            modifier = Modifier
                .padding(9.dp)
                .clip(BlockShape)
                .background(Color.Gray)
                .align(Alignment.End)
        )
    }
}