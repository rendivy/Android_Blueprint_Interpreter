package com.example.android_blueprint.model

import block.BlockEntity
import com.example.android_blueprint.viewModel.BlockViewModel

data class FieldBlock(
    val value: Any = -1,
    val index: Int = -1,
    val block: BlockEntity? = null,
    val pathViewModel: BlockViewModel? = null
)
