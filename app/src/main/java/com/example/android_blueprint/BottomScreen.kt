package com.example.android_blueprint.view

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.android_blueprint.ui.theme.Purple40

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview(
    showSystemUi = true, device = "id:pixel_5"
)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomBar(
                modifier = Modifier.background(Color.Black),
                navController = navController
            )
        },
    )
    {
        BottomNavGraph(navController = navController)
    }
}


@Composable
fun BottomBar(modifier: Modifier, navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Console,
        BottomBarScreen.Field,
        BottomBarScreen.BlockOfList,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Box(modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))){
        Row(
            modifier = Modifier
                .background(Purple40)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            screens.forEach() { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}


@Composable
fun AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
    val iconColor = if (selected) Color.White else Color.Black
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }),
        contentAlignment = Alignment.Center
    )
    {
        Column(
            modifier = Modifier.padding(bottom = 16.dp, top = 8.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Icon(
                painter = painterResource(id = screen.icon), contentDescription = null,
                tint = iconColor
            )
            AnimatedVisibility(visible = selected) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
        }
    }
}