package com.example.android_blueprint.model

import com.example.android_blueprint.R

sealed class BottomBarModel(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Field : BottomBarModel(
        route = "Field",
        title = "Field",
        icon = R.drawable.field_page_logo
    )

    object BlockOfList : BottomBarModel(
        route = "BlockList",
        title = "Block list",
        icon = R.drawable.list_page_logo
    )

    object Console : BottomBarModel(
        route = "Console",
        title = "Console",
        icon = R.drawable.console_page_logo
    )
}