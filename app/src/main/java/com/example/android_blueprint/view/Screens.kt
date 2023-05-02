package com.example.android_blueprint.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_blueprint.model.BlockDimension
import com.example.android_blueprint.model.Transform
import com.example.android_blueprint.ui.theme.BackgroundColor
import com.example.android_blueprint.ui.theme.actionColor
import com.example.android_blueprint.ui.theme.neueMedium

@Composable
fun FieldScreen(
    blocks: MutableList<BlockDimension>,
    transform: Transform,
    changeTransform: (zoomChange: Float, offsetChange: Offset) -> Unit
) {
    InfiniteField(blocks, transform, changeTransform)
}

@Composable
fun ListScreen(addBlock: (height: Dp, color: Color, symbol: String) -> Unit) {
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
        Text(
            text = "Здесь будет наша консоль с выводом, планирую сюда перекидывать пользователя, либо по клику, либо по нажанию кнопки компиляции на главном ",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = neueMedium,
            color = actionColor
        )
    }
}