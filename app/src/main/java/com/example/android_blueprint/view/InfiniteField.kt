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
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.android_blueprint.model.BlockValue
import com.example.android_blueprint.model.PathModel
import com.example.android_blueprint.ui.theme.BackgroundColor
import com.example.android_blueprint.ui.theme.DefaultPadding
import com.example.android_blueprint.ui.theme.DeleteButtonSize
import com.example.android_blueprint.viewModel.InfiniteFieldViewModel

var pathData = mutableStateMapOf<Int, Path>()


fun updatePathInMap(
    pathModel: PathModel, pathNumber: Int
) {
    val path = Path()
    path.moveTo(pathModel.pathList[0].value, pathModel.pathList[1].value)
    path.cubicTo(
        (pathModel.pathList[0].value + pathModel.pathList[2].value) / 2,
        pathModel.pathList[1].value, (pathModel.pathList[0].value + pathModel.pathList[2].value) / 2,
        pathModel.pathList[3].value, pathModel.pathList[2].value, pathModel.pathList[3].value
    )

    if (pathNumber !in pathData) {
        pathData[pathNumber] = path
    } else {
        pathData.put(pathNumber, path)
    }
}


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
                for (value in pathData.values) {
                    drawPath(value, Color.White, style = Stroke(width = 10f))

                }
            }
    )
    {

        EndBlock(value = BlockValue.EndBlock, block = infiniteFieldViewModel.endBlock)
        StartBlock(value = BlockValue.StartBlock, block = infiniteFieldViewModel.startBlock)


        for (block in blocks) {
            SetMovableBlock(fieldBlock = block, infiniteFieldViewModel = infiniteFieldViewModel)
        }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .padding(DefaultPadding)
                .align(Alignment.TopEnd)
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


