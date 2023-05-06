package com.example.android_blueprint.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.android_blueprint.model.BlockValue
import com.example.android_blueprint.ui.theme.BlockBackgroundColor
import com.example.android_blueprint.ui.theme.BlockHeight
import com.example.android_blueprint.ui.theme.BlockShape

@Composable
fun InitializationBlockWrapper(
    value: BlockValue.InitializationBlock,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(BlockHeight)
            .clip(BlockShape)
            .background(BlockBackgroundColor)
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
    }
}

@Composable
fun OperatorBlockWrapper(
    value: BlockValue.Operator,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(BlockHeight)
            .clip(BlockShape)
            .background(BlockBackgroundColor)
    ) {
        OperatorText(modifier = Modifier.align(Alignment.Center), text = value.text)
        SupportingFlow()
        SupportingFlow(modifier = Modifier.align(Alignment.BottomStart))
        SupportingFlow(modifier = Modifier.align(Alignment.CenterEnd))
    }
}