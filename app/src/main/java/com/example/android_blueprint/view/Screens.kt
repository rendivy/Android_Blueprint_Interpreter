package com.example.android_blueprint.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.android_blueprint.ui.theme.BottomBarPadding
import com.example.android_blueprint.ui.theme.SecondaryColor
import com.example.android_blueprint.ui.theme.SheetShape
import com.example.android_blueprint.ui.theme.neuMedium
import com.example.android_blueprint.viewModel.ConsoleViewModel
import com.example.android_blueprint.viewModel.InfiniteFieldViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FieldScreen(
    infiniteFieldViewModel: InfiniteFieldViewModel
) {
    ModalBottomSheetLayout(
        sheetState = infiniteFieldViewModel.sheetState,
        sheetContent = {
            DebugSheetState(
                interpret = infiniteFieldViewModel.interpret,
                infiniteFieldViewModel::closeBottomSheet
            )
        },
        content = {
            InfiniteField(infiniteFieldViewModel = infiniteFieldViewModel)
        },
        sheetShape = SheetShape
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
            .background(SecondaryColor)
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