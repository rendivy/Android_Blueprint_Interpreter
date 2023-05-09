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
        BlockValue.BranchBlock,
        BlockValue.Operator.ADDITION,
        BlockValue.Operator.SUBTRACTION,
        BlockValue.Operator.MULTIPLICATION,
        BlockValue.Operator.DIVISION,
        BlockValue.Operator.REMAINDER,
        BlockValue.Operator.EQUALITY,
        BlockValue.Operator.INVERSION,
        BlockValue.Operator.NOT_EQUAL,
        BlockValue.Operator.GREATER,
        BlockValue.Operator.LESS,
        BlockValue.Operator.GREATER_OR_EQUAL,
        BlockValue.Operator.LESS_OR_EQUAL
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
            SetBlock(addBlock = addBlock, value = item)
        }

    }
}

