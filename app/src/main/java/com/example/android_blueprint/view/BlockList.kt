package com.example.android_blueprint.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.android_blueprint.ui.theme.BackgroundColor
import com.example.android_blueprint.ui.theme.BlockListPadding
import com.example.android_blueprint.ui.theme.BlockShape
import com.example.android_blueprint.ui.theme.BlockWidth
import com.example.android_blueprint.ui.theme.BottomBarPadding
import kotlin.random.Random

// Test version
data class ListItem(
    val height: Dp,
    val color: Color
)

@Composable
fun RandomColorBox(item: ListItem, addBlock: (height: Dp, color: Color) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(item.height)
            .clip(BlockShape)
            .background(color = item.color)
            .clickable(onClick = {
                addBlock(item.height, item.color)
            })
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BlockList(addBlock: (height: Dp, color: Color) -> Unit) {
    val items = (1..100).map {
        ListItem(
            height = Random.nextInt(100, 150).dp,
            color = Color(
                Random.nextLong(0xFFFFFFFFF)
            ).copy(alpha = 1f)
        )
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(BlockWidth),
        contentPadding = PaddingValues(BlockListPadding),
        horizontalArrangement = Arrangement.spacedBy(BlockListPadding),
        verticalItemSpacing = BlockListPadding,
        modifier = Modifier
            .padding(bottom = BottomBarPadding)
            .background(BackgroundColor)
            .fillMaxSize(),
    ) {
        items(items) { item ->
            RandomColorBox(item = item, addBlock)
        }
    }
}

