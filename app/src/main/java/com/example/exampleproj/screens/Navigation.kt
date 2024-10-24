package com.example.exampleproj.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.exampleproj.viewmodel.DrinksViewModel

@Composable
fun Navigation(modifier: Modifier){
    val navController = rememberNavController()
    val viewModel: DrinksViewModel = viewModel()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Graph.MAIN_SCREEN
    ){
        composable(route = Graph.MAIN_SCREEN){
            MainScreen(viewModel, navController)
        }
        composable(route = Graph.DRINK_SCREEN){
            DrinkInfo(viewModel)
        }
    }
}

object Graph{
    const val MAIN_SCREEN = "MainScreen"
    const val DRINK_SCREEN = "DrinkScreen"
}
