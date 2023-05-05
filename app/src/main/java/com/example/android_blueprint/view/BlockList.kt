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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.android_blueprint.model.Blocks
import com.example.android_blueprint.ui.theme.AdaptiveWidth
import com.example.android_blueprint.ui.theme.BackgroundColor
import com.example.android_blueprint.ui.theme.BlockBackgroundColor
import com.example.android_blueprint.ui.theme.BlockHeight
import com.example.android_blueprint.ui.theme.BlockListPadding
import com.example.android_blueprint.ui.theme.BlockShape
import com.example.android_blueprint.ui.theme.BlockTextSize
import com.example.android_blueprint.ui.theme.BottomBarPadding
import com.example.android_blueprint.ui.theme.OperatorsTextColor
import com.example.android_blueprint.ui.theme.neueMedium

// Test version



@Composable
fun SetBlock(addBlock: (height: Dp, color: Color, symbol: String) -> Unit, block: Blocks) {

   Box(
       modifier = Modifier
           .fillMaxWidth()
           .height(BlockHeight)
           .clip(BlockShape)
           .background(BlockBackgroundColor)
           .clickable(onClick = {
               addBlock(BlockHeight, BlockBackgroundColor, block.type)
           })
   ) {
       Text(
           text = block.type,
           fontFamily = neueMedium,
           fontSize = BlockTextSize,
           color = OperatorsTextColor,
           modifier = Modifier
               .align(Alignment.Center)
       )

        Box(
            modifier = Modifier
                .padding(9.dp)
                .size(12.dp)
                .clip(BlockShape)
                .background(Color.White)
        )

       Box(
           modifier = Modifier
               .padding(9.dp)
               .align(Alignment.BottomStart)
               .clip(BlockShape)
               .size(12.dp)
               .background(Color.White)
       )

       Box(
           modifier = Modifier
               .padding(9.dp)
               .align(Alignment.CenterEnd)
               .clip(BlockShape)
               .size(12.dp)
               .background(Color.White)
       )
   }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BlockList(addBlock: (height: Dp, color: Color, symbol: String) -> Unit) {

    val blocks = listOf(
        Blocks.ADDITION,
        Blocks.SUBTRACTION,
        Blocks.MULTIPLICATION,
        Blocks.DIVISION,
        Blocks.REMAINDER,
        Blocks.EQUALITY
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
            SetBlock(addBlock = addBlock, block = item)
        }

    }
}

