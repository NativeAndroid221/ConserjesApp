package com.example.conserjes.Screen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.conserjes.Components.SelectedImage

import com.example.conserjes.data.cardpublication

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun PostDetailScreen(
    post: cardpublication,
    onBack: () -> Unit,
    onLike: () -> Unit,
    onShare: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Post") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = onLike,
                    icon = {
                        Icon(
                            Icons.Default.FavoriteBorder,
                            contentDescription = "Me gusta"
                        )
                    },
                    label = { Text("Me gusta") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onShare,
                    icon = {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Compartir"
                        )
                    },
                    label = { Text("Compartir") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                with(sharedTransitionScope) {
                    Text(
                        text = post.name,
                        modifier = Modifier.sharedElement(
                            sharedContentState = rememberSharedContentState(
                                key = "postName-${post.id}"
                            ),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(Modifier.width(8.dp))

                Text(
                    text = post.handle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = post.dateTime,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            with(sharedTransitionScope) {
                Text(
                    text = post.content,
                    modifier = Modifier.sharedElement(
                        sharedContentState = rememberSharedContentState(
                            key = "postContent-${post.id}"
                        ),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            if (post.images.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(post.images) { index, imageUri ->
                        with(sharedTransitionScope) {
                            SelectedImage(
                                uri = imageUri,
                                modifier = Modifier.sharedElement(
                                    sharedContentState = rememberSharedContentState(
                                        key = "postImage-${post.id}-$index"
                                    ),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}