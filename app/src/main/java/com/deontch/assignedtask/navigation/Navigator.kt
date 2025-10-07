package com.deontch.baseapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.deontch.characterdetail.presentation.MovieCharacterDetailScreen
import com.deontch.characterdetail.presentation.MovieCharacterDetailScreenDestination
import com.deontch.characterslist.presentation.MovieCharacterScreen
import com.deontch.characterslist.presentation.MovieCharacterScreenDestination

@Composable
fun Navigator(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = MovieCharacterScreenDestination) {

        composable<MovieCharacterScreenDestination> {
            MovieCharacterScreen(onCharacterCardClick = {
                navController.navigate(MovieCharacterDetailScreenDestination(it))
            })
        }

        composable<MovieCharacterDetailScreenDestination> {
            val args = it.toRoute<MovieCharacterDetailScreenDestination>()
            MovieCharacterDetailScreen(args.name, onBackClick = { navController.popBackStack() })
        }
    }
}
