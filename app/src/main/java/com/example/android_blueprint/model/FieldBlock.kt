package com.example.android_blueprint.model

import block.BlockEntity
import com.example.android_blueprint.viewModel.PathViewModel

data class FieldBlock(
    val value: Any = -1,
    val index: Int = -1,
    val block: BlockEntity? = null,
    val pathViewModel: PathViewModel? = null
)
