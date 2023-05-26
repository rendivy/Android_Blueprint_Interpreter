package com.example.android_blueprint.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import com.example.android_blueprint.model.BlockValue
import com.example.android_blueprint.model.BranchEntity
import com.example.android_blueprint.ui.theme.BackgroundColor
import com.example.android_blueprint.ui.theme.DefaultPadding
import com.example.android_blueprint.ui.theme.DeleteButtonSize
import com.example.android_blueprint.ui.theme.ActionColor
import com.example.android_blueprint.viewModel.InfiniteFieldViewModel
import com.example.android_blueprint.viewModel.start


@Composable
fun InfiniteField(
    infiniteFieldViewModel: InfiniteFieldViewModel
) {

    val blocks = infiniteFieldViewModel.blocks
    val transform = infiniteFieldViewModel.transform
    val changeTransform = infiniteFieldViewModel::changeTransform
    val changeMode = infiniteFieldViewModel::changeMode
    val getDeleteButtonColor = infiniteFieldViewModel::getDeleteButtonColor
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        changeTransform(zoomChange, offsetChange)
    }
    Box(
        modifier = Modifier
            .transformable(state = state)
            .background(BackgroundColor)
            .fillMaxSize()
            .graphicsLayer(
                scaleX = transform.scale,
                scaleY = transform.scale,
                translationX = transform.offset.x,
                translationY = transform.offset.y
            )
            .drawBehind {
                for (value in BranchEntity.pathData.values) {
                    drawPath(value.path, if (value.isMainFlowBranch) Color.White else ActionColor, style = Stroke(width = 10f))
                }
            }
    )
    {

        EndBlock(
            value = BlockValue.EndBlock, block = infiniteFieldViewModel.endBlock,
            viewModel = infiniteFieldViewModel.endViewModel
        )
        StartBlock(
            value = BlockValue.StartBlock, block = infiniteFieldViewModel.startBlock,
            viewModel = infiniteFieldViewModel.startViewModel
        )

        for (block in blocks) {
            SetMovableBlock(fieldBlock = block, infiniteFieldViewModel = infiniteFieldViewModel)
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            if (infiniteFieldViewModel.isDebug) {
                Box(
                    modifier = Modifier
                        .padding(DefaultPadding)
                        .size(DeleteButtonSize)
                        .clip(CircleShape)
                        .background(getDeleteButtonColor())
                ) {
                    Icon(
                        Icons.Rounded.Call, contentDescription = null,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .padding(DefaultPadding)
                    .size(DeleteButtonSize)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable {
                        start(
                            startBlock = infiniteFieldViewModel.startBlock,
                            interpreter = infiniteFieldViewModel.interpret,
                            openDebugger = infiniteFieldViewModel::openDebugger,
                            closeDebugger = infiniteFieldViewModel::closeDebugger
                        )
                    }
            ) {
                Icon(
                    Icons.Rounded.Build, contentDescription = null,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Box(
                modifier = Modifier
                    .padding(DefaultPadding)
                    .size(DeleteButtonSize)
                    .clip(CircleShape)
                    .background(getDeleteButtonColor())
                    .clickable { changeMode() }
            ) {
                Icon(
                    Icons.Rounded.Delete, contentDescription = null,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}


