package com.example.android_blueprint.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomBarScreen.Field.route) {
        composable(route = BottomBarScreen.Field.route){
            FieldScreen()
        }
        composable(route = BottomBarScreen.Console.route){
            ConsoleScreen()
        }
        composable(route = BottomBarScreen.BlockOfList.route){
            ListScreen()
        }
    }
}