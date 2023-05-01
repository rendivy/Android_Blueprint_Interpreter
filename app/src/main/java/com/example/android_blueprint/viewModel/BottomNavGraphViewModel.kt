package com.example.android_blueprint.viewModel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.android_blueprint.model.BottomBarModel
import com.example.android_blueprint.view.ConsoleScreen
import com.example.android_blueprint.view.FieldScreen
import com.example.android_blueprint.view.ListScreen

class BottomNavGraphViewModel : ViewModel() {
    @Composable
    fun BottomNav(navController: NavHostController) {
        val viewModel: InfiniteFieldViewModel = viewModel()
        NavHost(navController = navController, startDestination = BottomBarModel.Field.route) {
            composable(route = BottomBarModel.Field.route) {
                FieldScreen(blocks = viewModel.blocks, viewModel.transform, viewModel::changeTransform)
            }
            composable(route = BottomBarModel.Console.route) {
                ConsoleScreen()
            }
            composable(route = BottomBarModel.BlockOfList.route) {
                ListScreen(addBlock = viewModel::addBlock)
            }
        }
    }
}
