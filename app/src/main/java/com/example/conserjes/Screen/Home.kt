package com.example.conserjes.Screen


import android.R
import android.content.res.Configuration
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
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarDefaults
import com.example.conserjes.Components.EmptyHomeState
import com.example.conserjes.Components.PostCard
import com.example.conserjes.data.cardpublication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    posts: List<cardpublication>,
    onFabClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Home") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Perfil")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Default.Build,
                            contentDescription = "Configuración",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },
        bottomBar = {
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
        },
        floatingActionButton = {
            if (posts.isNotEmpty()) {
                FloatingActionButton(onClick = onFabClick, shape = CircleShape) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                }
            }
        }
    ) { paddingValues ->
        if (posts.isEmpty()) {
            EmptyHomeState(
                onCreatePost = onFabClick,
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(posts, key = { it.dateTime + it.content }) { post ->
                    PostCard(
                        post = post,
                        onLike = { },
                        onShare = { }
                    )
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
