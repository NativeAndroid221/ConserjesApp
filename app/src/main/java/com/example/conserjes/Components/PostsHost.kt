package com.example.conserjes.Components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.*
import com.example.conserjes.Screen.HomeScreen
import com.example.conserjes.Screen.PostDetailScreen
import com.example.conserjes.data.cardpublication

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PostsHost(
    posts: List<cardpublication>,
    onFabClick: () -> Unit,
) {
    var selectedPostId by remember { mutableStateOf<String?>(null) }

    // estado actual
    val selectedPost = posts.firstOrNull { it.id == selectedPostId }

    SharedTransitionLayout {
        AnimatedContent(
            targetState = selectedPost,
            transitionSpec = {
                // Transición de pantalla tipo gist (slide + fade)
                val enter = slideIntoContainer(
                    towards = androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(450)
                ) + fadeIn(tween(220))

                val exit = slideOutOfContainer(
                    towards = androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(450)
                ) + fadeOut(tween(220))

                (enter togetherWith exit).using(SizeTransform(clip = false))
            },
            label = "posts-content"
        ) { postOrNull ->
            // ESTE "this" es AnimatedContentScope => aquí sacas el animatedVisibilityScope
            val animatedVisibilityScope = this

            if (postOrNull == null) {
                HomeScreen(
                    posts = posts,
                    onFabClick = onFabClick,
                    onPostClick = { id -> selectedPostId = id },
                    // NUEVO
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = animatedVisibilityScope
                )
            } else {
                PostDetailScreen(
                    post = postOrNull,
                    onBack = { selectedPostId = null },
                    onLike = { },
                    onShare = { },
                    // NUEVO
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = animatedVisibilityScope
                )
            }
        }
    }
}