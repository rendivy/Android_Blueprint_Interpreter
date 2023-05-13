package com.example.android_blueprint.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.android_blueprint.model.BlockValue
import com.example.android_blueprint.ui.theme.AdaptiveWidth
import com.example.android_blueprint.ui.theme.BackgroundColor
import com.example.android_blueprint.ui.theme.BlockListPadding
import com.example.android_blueprint.ui.theme.BottomBarPadding


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BlockList(addBlock: (blockValue: Any) -> Unit) {

    val blocks = listOf(
        BlockValue.InitializationBlock,
        BlockValue.PrintBlock,
        BlockValue.IfBlock,
        BlockValue.BinaryOperator.ADDITION,
        BlockValue.BinaryOperator.SUBTRACTION,
        BlockValue.BinaryOperator.MULTIPLICATION,
        BlockValue.BinaryOperator.DIVISION,
        BlockValue.BinaryOperator.REMAINDER,
        BlockValue.BinaryOperator.EQUALITY,
        BlockValue.UnaryOperator.INVERSION,
        BlockValue.BinaryOperator.NOT_EQUAL,
        BlockValue.BinaryOperator.GREATER,
        BlockValue.BinaryOperator.LESS,
        BlockValue.BinaryOperator.GREATER_OR_EQUAL,
        BlockValue.BinaryOperator.LESS_OR_EQUAL
    )

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(AdaptiveWidth),
        contentPadding = PaddingValues(BlockListPadding),
        horizontalArrangement = Arrangement.spacedBy(BlockListPadding),
        verticalItemSpacing = BlockListPadding,
        modifier = Modifier
            .padding(bottom = BottomBarPadding)
            .background(BackgroundColor)
            .fillMaxSize(),
    ) {

        items(blocks) { item ->
            SetBlock(value = item, addBlock = addBlock)
        }
    }
}

