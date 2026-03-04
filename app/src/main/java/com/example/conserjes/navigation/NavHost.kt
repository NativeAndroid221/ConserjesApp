package com.example.conserjes.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.conserjes.Components.PostsHost
import com.example.conserjes.Screen.CreatePostScreen
import com.example.conserjes.viewmodels.PostViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val postViewModel: PostViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            // ✅ Home + Detail con Shared Element va dentro de PostsHost
            PostsHost(
                posts = postViewModel.posts,
                onFabClick = { navController.navigate(Routes.CREATE_POST) }
            )
        }

        composable(Routes.CREATE_POST) {
            CreatePostScreen(
                onBack = { navController.popBackStack() },
                onPublish = { newPost ->
                    postViewModel.addPost(newPost)
                    navController.popBackStack()
                }
            )
        }
    }
}