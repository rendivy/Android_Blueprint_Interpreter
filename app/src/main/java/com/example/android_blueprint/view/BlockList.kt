package com.example.android_blueprint.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.android_blueprint.ui.theme.BackgroundColor
import kotlin.random.Random


data class ListItem(
    val height: Dp,
    val color: Color
)

@Composable
fun RandomColorBox(item: ListItem) {
    var isButtonClicked by remember { mutableStateOf(false) }
    val backgroundColor = if (isButtonClicked) Color.Gray else item.color

    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(item.height)
            .clip(RoundedCornerShape(10.dp))
            .background(color = backgroundColor)
            .clickable(onClick = {
                isButtonClicked = !isButtonClicked
            })
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BlockList() {
    val items = (1..100).map {
        ListItem(
            height = Random.nextInt(100, 120).dp,
            color = Color(
                Random.nextLong(0xFFFFFFFFF)
            ).copy(alpha = 1f)
        )
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(100.dp),
        modifier = Modifier // Отступ для нав бара
            .fillMaxSize().background(BackgroundColor).padding(56.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp
    ) {
        items(items) { item ->
            RandomColorBox(item = item)
        }
    }
}

