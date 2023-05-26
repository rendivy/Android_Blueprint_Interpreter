package com.example.android_blueprint.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_blueprint.ui.theme.BackgroundColor
import com.example.android_blueprint.ui.theme.BottomBarPadding
import com.example.android_blueprint.ui.theme.actionColor
import com.example.android_blueprint.ui.theme.neuMedium
import com.example.android_blueprint.viewModel.ConsoleViewModel
import com.example.android_blueprint.viewModel.InfiniteFieldViewModel
import com.example.android_blueprint.viewModel.PathViewModel

@Composable
fun FieldScreen(
    infiniteFieldViewModel: InfiniteFieldViewModel,
) {
    InfiniteField(
        infiniteFieldViewModel = infiniteFieldViewModel
    )
}

@Composable
fun ListScreen(addBlock: (blockValue: Any) -> Unit) {
    BlockList(addBlock)
}


@Composable
fun ConsoleScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(8.dp),
        contentAlignment = Alignment.Center
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

    }
}