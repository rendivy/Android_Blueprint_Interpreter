package com.example.android_blueprint.viewModel

import com.example.android_blueprint.R

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Field : BottomBarScreen(
        route = "Field",
        title = "Field",
        icon = R.drawable.field_page_logo
    )

    object BlockOfList : BottomBarScreen(
        route = "BlockList",
        title = "Block list",
        icon = R.drawable.list_page_logo
    )

    object Console : BottomBarScreen(
        route = "Console",
        title = "Console",
        icon = R.drawable.console_page_logo
    )

}