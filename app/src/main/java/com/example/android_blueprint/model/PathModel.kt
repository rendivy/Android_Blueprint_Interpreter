package com.example.android_blueprint.model

import androidx.compose.runtime.MutableState

class PathModel(var pathId: Int, var pathList:  MutableList<MutableState<Float>>, var isPathConnected: Boolean ) {
}