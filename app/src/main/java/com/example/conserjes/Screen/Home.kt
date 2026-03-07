package com.example.conserjes.Screen

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CarRental
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.example.conserjes.Components.EmptyHomeState
import com.example.conserjes.Components.PostCard


import com.example.conserjes.data.cardpublication


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    posts: List<cardpublication>,
    onFabClick: () -> Unit,
    onPostClick: (String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var previousIndex by remember { mutableIntStateOf(0) }
    var previousOffset by remember { mutableIntStateOf(0) }
    var barsVisible by remember { mutableStateOf(true) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->

                val scrollingDown =
                    index > previousIndex || (index == previousIndex && offset > previousOffset)

                barsVisible = !scrollingDown

                previousIndex = index
                previousOffset = offset
            }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AnimatedVisibility(
                visible = barsVisible,
                enter = fadeIn() + slideInVertically { -it },
                exit = fadeOut() + slideOutVertically { -it }
            ) {
                Surface(tonalElevation = 4.dp) {
                    TopAppBar(
                        title = { Text("Conserjes") },
                        scrollBehavior = scrollBehavior,
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        navigationIcon = {
                            IconButton(onClick = { }) {
                                Icon(Icons.Default.AccountCircle, contentDescription = "Perfil")
                            }
                        },
                        actions = {
                            IconButton(onClick = { }) {
                                Icon(Icons.Default.Build, contentDescription = "Configuración")
                            }
                        }
                    )
                }
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = barsVisible,
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it }
            ) {
                NavigationBar {
                    NavigationBarItem(
                        true,
                        onClick = { },
                        icon = { Icon(Icons.Default.Home, null) },
                        label = { Text("Home") })
                    NavigationBarItem(
                        false,
                        onClick = { },
                        icon = { Icon(Icons.Default.AccountBox, null) },
                        label = { Text("Profile") })
                    NavigationBarItem(
                        false,
                        onClick = { },
                        icon = { Icon(Icons.Default.CarRental, null) },
                        label = { Text("Visita") })
                    NavigationBarItem(
                        false,
                        onClick = { },
                        icon = { Icon(Icons.Default.Backpack, null) },
                        label = { Text("Encomienda") })
                }
            }

        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = barsVisible && posts.isNotEmpty(),
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it }
            ) {
                FloatingActionButton(onClick = onFabClick, shape = CircleShape) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                }
            }
        }
     ) { paddingValues ->

        // ✅ Fondo ocupa toda la pantalla (sin padding)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            // ✅ El padding se aplica SOLO a lo que va dentro (lista / empty state)
            val contentModifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)

            if (posts.isEmpty()) {
                EmptyHomeState(
                    onCreatePost = onFabClick,
                    modifier = contentModifier
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = contentModifier,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(posts, key = { it.dateTime + it.content + it.id }) { post ->
                        PostCard(
                            post = post,
                            onClick = { onPostClick(post.id) },
                            onLike = { },
                            onShare = { },
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope


                        )
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
    }
}
