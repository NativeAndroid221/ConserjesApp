package com.example.conserjes.Screen

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.conserjes.Components.ImagePlaceholder
import com.example.conserjes.Components.SelectedImage
import com.example.conserjes.data.cardpublication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    onBack: () -> Unit,
    onPublish: (cardpublication) -> Unit
) {
    var text by rememberSaveable { mutableStateOf("") }
    var textname by rememberSaveable { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva publicación") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = textname,
                onValueChange = { textname = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Indica tu nombre") },
                minLines = 1
            )

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("¿Qué quieres publicar?") },
                minLines = 4
            )

            // Imagen: si existe la muestra, si no muestra opciones
            if (imageUri != null) {
                SelectedImage(imageUri!!)
                TextButton(onClick = { imageUri = null }) {
                    Text("Quitar foto")
                }
            } else {
                ImagePlaceholder(
                    onPickFromGallery = {
                        // luego lo conectamos con launcher
                    },
                    onTakePhoto = {
                        // luego lo conectamos con launcher + FileProvider
                    }
                )
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    val now = java.text.SimpleDateFormat(
                        "dd MMM · HH:mm",
                        java.util.Locale.getDefault()
                    ).format(java.util.Date())

                    val cleanName = textname.trim()
                    val cleanContent = text.trim()
                    val cleanHandle = if (cleanName.isNotBlank()) {
                        "@${cleanName.lowercase().replace(" ", "")}"
                    } else ""

                    onPublish(
                        cardpublication(
                            name = cleanName,
                            handle = cleanHandle,
                            dateTime = now,
                            content = cleanContent,
                            image = imageUri
                        )
                    )
                },
                enabled = text.isNotBlank() && textname.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Publicar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreatePostPreview() {
    MaterialTheme {
        CreatePostScreen(
            onBack = {},
            onPublish = { _: cardpublication -> }
        )
    }
}
