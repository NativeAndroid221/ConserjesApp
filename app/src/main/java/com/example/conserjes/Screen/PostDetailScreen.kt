package com.example.conserjes.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    post: cardpublication,
    onBack: () -> Unit,
    onLike: () -> Unit,
    onShare: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Post") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            // Acciones tipo X abajo
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = onLike,
                    icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Me gusta") },
                    label = { Text("Me gusta") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onShare,
                    icon = { Icon(Icons.Default.Share, contentDescription = "Compartir") },
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
            // Header: avatar + nombre + handle + fecha
            Row(verticalAlignment = Alignment.CenterVertically) {
                // reutiliza tu avatar si quieres
                // AvatarCircle(post.name)
                Text(post.name, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.width(8.dp))
                Text(post.handle, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Text(post.dateTime, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

            // Texto grande
            Text(
                text = post.content,
                style = MaterialTheme.typography.titleLarge
            )

            // Imagen grande si existe
            if (post.image != null) {
                SelectedImage(post.image)
            }
        }
    }
}