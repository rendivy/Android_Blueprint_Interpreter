package com.example.android_blueprint.viewModel

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.accompanist.navigation.animation.composable
import com.example.android_blueprint.model.BottomBarModel
import com.example.android_blueprint.ui.theme.TargetOffsetNavigation
import com.example.android_blueprint.view.ConsoleScreen
import com.example.android_blueprint.view.FieldScreen
import com.example.android_blueprint.view.ListScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost

class BottomNavGraphViewModel : ViewModel() {
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun BottomNav(navController: NavHostController) {
        val infiniteFieldViewModel: InfiniteFieldViewModel = viewModel()
        AnimatedNavHost(
            navController = navController,
            startDestination = BottomBarModel.Field.route
        ) {
            composable(
                route = BottomBarModel.Field.route,
                enterTransition = {
                    when (initialState.destination.route) {
                        BottomBarModel.BlockOfList.route ->
                            fadeIn(animationSpec = tween(TargetOffsetNavigation))
                        else -> null
                    }
                },
                exitTransition = {
                    when (targetState.destination.route) {
                        BottomBarModel.Field.route ->
                            slideOutHorizontally (
                                targetOffsetX = {-TargetOffsetNavigation},
                                animationSpec = tween(
                                    durationMillis = TargetOffsetNavigation,
                                    easing = FastOutSlowInEasing
                                )
                            ) +  fadeOut(animationSpec = tween(TargetOffsetNavigation))

                        else -> null
                    }
                },
                popEnterTransition = {
                    when (initialState.destination.route) {
                        BottomBarModel.Field.route ->
                        slideInHorizontally(
                            initialOffsetX = {TargetOffsetNavigation},
                            animationSpec = tween(
                                durationMillis = TargetOffsetNavigation,
                                easing = FastOutSlowInEasing
                            )
                        ) + fadeIn(animationSpec = tween(TargetOffsetNavigation))
                        else -> null
                    }
                }
        ) {
                FieldScreen(
                    infiniteFieldViewModel = infiniteFieldViewModel
                )
            }
            composable(route = BottomBarModel.Console.route) {
                ConsoleScreen()
            }
            composable(route = BottomBarModel.BlockOfList.route) {
                ListScreen(infiniteFieldViewModel::addBlock)
            }
        }
    }
}
