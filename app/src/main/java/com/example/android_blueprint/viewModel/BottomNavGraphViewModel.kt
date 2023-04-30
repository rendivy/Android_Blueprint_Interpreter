package com.example.android_blueprint.viewModel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.android_blueprint.view.ConsoleScreen
import com.example.android_blueprint.view.FieldScreen
import com.example.android_blueprint.view.ListScreen

class BottomNavGraphViewModel: ViewModel(){
    @Composable
    fun BottomNav(navController: NavHostController) {
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
}
