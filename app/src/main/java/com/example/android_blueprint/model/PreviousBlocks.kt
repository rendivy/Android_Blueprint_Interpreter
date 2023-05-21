package com.example.android_blueprint.model

import block.BlockEntity

data class PreviousBlocks(
    val previousMainFlowTrueBlock: BlockEntity? = null,
    val previousMainFlowFalseBlock: BlockEntity? = null,
    val previousSupportFlowBlock: BlockEntity? = null
)
