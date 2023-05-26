package com.example.android_blueprint.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import block.BlockEntity
import com.example.android_blueprint.ui.theme.BackgroundColor
import com.example.android_blueprint.ui.theme.BlockShape
import com.example.android_blueprint.ui.theme.BottomBarPadding
import com.example.android_blueprint.ui.theme.ButtonSize
import com.example.android_blueprint.ui.theme.OperatorsTextColor
import com.example.android_blueprint.ui.theme.UnaryOperatorsTextSize
import com.example.android_blueprint.ui.theme.actionColor
import com.example.android_blueprint.ui.theme.neuMedium
import com.example.android_blueprint.viewModel.ConsoleViewModel
import com.example.android_blueprint.viewModel.InfiniteFieldViewModel
import interpretator.Interpret

@Composable
fun FieldScreen(
    infiniteFieldViewModel: InfiniteFieldViewModel
) {
    InfiniteField(infiniteFieldViewModel = infiniteFieldViewModel)
}

@Composable
fun ListScreen(addBlock: (blockValue: Any) -> Unit) {
    BlockList(addBlock)
}


@Composable
fun ConsoleScreen(interpret: Interpret) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(8.dp),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(bottom = BottomBarPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = ConsoleViewModel.consoleText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = neuMedium,
                color = ConsoleViewModel.defaultTextColor
            )
        }
        if (BlockEntity.checkBreakPointInBlocks()) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = BottomBarPadding)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(ButtonSize)
                        .clip(BlockShape)
                        .background(Color.Gray)
                        .clickable { interpret.switchStepTo() }
                ) {
                    Text(
                        text = "Step to",
                        fontFamily = neuMedium,
                        color = OperatorsTextColor,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(ButtonSize)
                        .clip(BlockShape)
                        .background(Color.Gray)
                        .clickable { interpret.switchStepInto() }
                ) {
                    Text(
                        text = "Step into",
                        fontFamily = neuMedium,
                        color = OperatorsTextColor,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}