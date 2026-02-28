package com.example.conserjes.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.conserjes.Screen.CreatePostScreen
import com.example.conserjes.Screen.HomeScreen
import com.example.conserjes.Screen.PostDetailScreen
import com.example.conserjes.viewmodels.PostViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val postViewModel: PostViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {

        // ✅ HOME debe ir dentro de composable(...)
        composable(Routes.HOME) {
            HomeScreen(
                posts = postViewModel.posts,
                onFabClick = { navController.navigate(Routes.CREATE_POST) },
                onPostClick = { postId ->
                    navController.navigate("${Routes.POST_DETAIL}/$postId")
                }
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

        composable("${Routes.POST_DETAIL}/{postId}") { backStackEntry ->
            val postId = backStackEntry.arguments?.getString("postId") ?: return@composable
            val post = postViewModel.posts.firstOrNull { it.id == postId } ?: return@composable

            PostDetailScreen(
                post = post,
                onBack = { navController.popBackStack() },
                onLike = { },
                onShare = { }
            )
        }
    }
}