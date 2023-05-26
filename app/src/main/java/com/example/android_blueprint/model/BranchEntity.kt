package com.example.android_blueprint.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.Path

class BranchEntity(
    var xStart: MutableState<Float>,
    var yStart: MutableState<Float>,
    var isMainFlowBranch: Boolean = true,
    val idStartBlock: Int,
) {
    companion object {
        private var nextId = 0
        var pathData = mutableStateMapOf<Int, BranchEntity>()
    }

    private val id = nextId++
    var idFinishBlock = -1
    private var isConnected = false

    val path = Path()
    var xFinish: MutableState<Float> = xStart
    var yFinish: MutableState<Float> = yStart

    fun getId(): Int {
        return id
    }

    fun switchIsConnected(){
        isConnected = !isConnected
    }

    fun getIsConnected(): Boolean{
        return isConnected
    }

    fun drawBranch() {
        path.reset()
        path.moveTo(xStart.value, yStart.value)
        path.cubicTo(
            (xStart.value + xFinish.value) / 2,
            yStart.value,
            (xStart.value + xFinish.value) / 2,
            yFinish.value,
            xFinish.value,
            yFinish.value
        )
       updateMap()
    }

    fun putInMap() {
        pathData[id] = this
    }

    private fun updateMap(){
        pathData.remove(id)
        pathData[id] = this
    }

    fun deleteBranch(){
        path.moveTo(xStart.value, yStart.value)
        pathData.remove(id)
    }

    fun isInMap(): Boolean{
        return pathData.containsKey(id)
    }
}
