package com.example.android_blueprint.view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.android_blueprint.model.BlockValue
import com.example.android_blueprint.ui.theme.BlockHeight
import com.example.android_blueprint.ui.theme.BlockShape
import com.example.android_blueprint.ui.theme.BlockWidth
import com.example.android_blueprint.ui.theme.ComplexBlockColor
import com.example.android_blueprint.ui.theme.DefaultPadding
import com.example.android_blueprint.ui.theme.OperatorBlockColor
import kotlin.math.roundToInt

@Composable
fun MovableOperatorBlock(value: BlockValue.Operator) {
    var offsetX by rememberSaveable { mutableStateOf(0f) }
    var offsetY by rememberSaveable { mutableStateOf(0f) }
    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .width(BlockWidth)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
            .height(BlockHeight)
            .clip(BlockShape)
            .background(OperatorBlockColor)
    ) {
        OperatorText(modifier = Modifier.align(Alignment.Center), text = value.text)
        SupportingFlow()
        SupportingFlow(modifier = Modifier.align(Alignment.BottomStart))
        SupportingFlow(modifier = Modifier.align(Alignment.CenterEnd))
    }
}

@Composable
fun MovablePrintBlock(value: BlockValue.PrintBlock) {
    var offsetX by rememberSaveable { mutableStateOf(0f) }
    var offsetY by rememberSaveable { mutableStateOf(0f) }
    Column(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .width(BlockWidth)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
            .heightIn(min = BlockHeight)
            .clip(BlockShape)
            .background(ComplexBlockColor)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            MainFlow()
            MainFlow()
        }
        SupportingFlow(modifier = Modifier.align(Alignment.Start))
    }
}

@Composable
fun MovableBranchBlock(value: BlockValue.BranchBlock) {
    var offsetX by rememberSaveable { mutableStateOf(0f) }
    var offsetY by rememberSaveable { mutableStateOf(0f) }
    Column(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .width(BlockWidth)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
            .heightIn(min = BlockHeight)
            .clip(BlockShape)
            .background(ComplexBlockColor)
            .fillMaxWidth()
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            MainFlow(modifier = Modifier.align(Alignment.TopStart))
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                TextForFlow(text = "True")
                MainFlow()
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier.align(Alignment.TopStart)) {
                SupportingFlow()
                TextForFlow(text = "Condition")
            }
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                TextForFlow(text = "False")
                MainFlow()
            }
        }

    }
}

@Composable
fun MovableInitializationBlock(
    value: BlockValue.InitializationBlock,
    addVariable: (list: List<String>) -> List<String>,
    removeAtIndex: (list: List<String>, indexToRemove: Int) -> List<String>,
    valueChange: (list: List<String>, index: Int, value: String) -> List<String>
) {
    var variableInformation by rememberSaveable { mutableStateOf(listOf<String>()) }
    var offsetX by rememberSaveable { mutableStateOf(0f) }
    var offsetY by rememberSaveable { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .width(250.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
            .clip(BlockShape)
            .background(ComplexBlockColor)
    ) {
        ComplexBlockText(modifier = value.modifier, text = value.text)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            MainFlow()
            MainFlow()
        }
        for (i in variableInformation.indices) {
            val focusManager = LocalFocusManager.current
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(
                    onClick = { variableInformation = removeAtIndex(variableInformation, i) },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        Icons.Rounded.Delete, contentDescription = null,
                        modifier = Modifier
                            .padding(DefaultPadding)
                            .clip(BlockShape)
                            .background(Color.Gray)
                    )
                }
                TextField(
                    value = variableInformation[i],
                    onValueChange = {
                        variableInformation = valueChange(variableInformation, i, it)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.White,
                        backgroundColor = ComplexBlockColor,
                        cursorColor = Color.White,
                        focusedIndicatorColor = ComplexBlockColor
                    ),
                    textStyle = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .weight(1f)
                )
                SupportingFlow(modifier = Modifier.align(Alignment.CenterVertically))
            }
        }
        IconButton(
            onClick = { variableInformation = addVariable(variableInformation) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(
                Icons.Rounded.Add, contentDescription = null,
                modifier = Modifier
                    .padding(DefaultPadding)
                    .clip(BlockShape)
                    .background(Color.Gray)
            )
        }
    }
}
