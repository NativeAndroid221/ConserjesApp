package com.example.conserjes.Screen

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.conserjes.Components.ImagePlaceholder
import com.example.conserjes.data.cardpublication
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    onBack: () -> Unit,
    onPublish: (cardpublication) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var name by rememberSaveable { mutableStateOf("") }
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }
    var importance by rememberSaveable { mutableStateOf("Media") }

    var imageUris by remember { mutableStateOf(listOf<Uri>()) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    var expanded by remember { mutableStateOf(false) }
    var isPublishing by remember { mutableStateOf(false) }

    val importanceOptions = listOf("Alta", "Media", "Baja")

    val canPublish = name.isNotBlank() &&
            title.isNotBlank() &&
            content.isNotBlank() &&
            !isPublishing

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 5)
    ) { selectedUris ->
        if (selectedUris.isNotEmpty()) {
            imageUris = (imageUris + selectedUris).distinct().take(5)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraImageUri != null) {
            imageUris = (imageUris + cameraImageUri!!).distinct().take(5)
        }
    }

    fun publishPost() {
        if (!canPublish) return

        scope.launch {
            isPublishing = true

            val now = SimpleDateFormat(
                "dd MMM · HH:mm",
                Locale.getDefault()
            ).format(Date())

            val cleanName = name.trim()
            val cleanTitle = title.trim()
            val cleanContent = content.trim()

            val cleanHandle = if (cleanName.isNotBlank()) {
                "@${cleanName.lowercase().replace(" ", "")}"
            } else {
                ""
            }

            delay(350)

            onPublish(
                cardpublication(
                    id = UUID.randomUUID().toString(),
                    name = cleanName,
                    handle = cleanHandle,
                    dateTime = now,
                    title = cleanTitle,
                    content = cleanContent,
                    importance = importance,
                    images = imageUris
                )
            )

            isPublishing = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva publicación") },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        enabled = !isPublishing
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = { publishPost() },
                        enabled = canPublish
                    ) {
                        AnimatedContent(
                            targetState = isPublishing,
                            transitionSpec = {
                                fadeIn() togetherWith fadeOut()
                            },
                            label = "publish_animation"
                        ) { publishing ->
                            if (publishing) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp
                                    )
                                    Text("Publicando")
                                }
                            } else {
                                Text("Publicar")
                            }
                        }
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Autor") },
                placeholder = { Text("Ej: Armando Porras") },
                singleLine = true,
                enabled = !isPublishing
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Título") },
                placeholder = { Text("Ej: Actualización urgente de seguridad") },
                maxLines = 2,
                enabled = !isPublishing
            )

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Descripción") },
                placeholder = { Text("Escribe los detalles de la novedad") },
                minLines = 4,
                enabled = !isPublishing
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    if (!isPublishing) {
                        expanded = !expanded
                    }
                }
            ) {
                OutlinedTextField(
                    value = importance,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Importancia") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    enabled = !isPublishing
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    importanceOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                importance = option
                                expanded = false
                            }
                        )
                    }
                }
            }

            if (imageUris.isNotEmpty()) {
                Text(
                    text = "Fotos seleccionadas (${imageUris.size}/5)",
                    style = MaterialTheme.typography.titleSmall
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    imageUris.forEach { uri ->
                        ImagePreviewItem(
                            uri = uri,
                            onRemove = {
                                if (!isPublishing) {
                                    imageUris = imageUris - uri
                                }
                            }
                        )
                    }
                }

                TextButton(
                    onClick = { imageUris = emptyList() },
                    enabled = !isPublishing
                ) {
                    Text("Quitar todas las fotos")
                }
            }

            ImagePlaceholder(
                onPickFromGallery = {
                    if (isPublishing) return@ImagePlaceholder
                    if (imageUris.size >= 5) return@ImagePlaceholder

                    galleryLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onTakePhoto = {
                    if (isPublishing) return@ImagePlaceholder
                    if (imageUris.size >= 5) return@ImagePlaceholder

                    val newUri = createImageUri(context)
                    cameraImageUri = newUri
                    cameraLauncher.launch(newUri)
                }
            )

            Text(
                text = "Puedes agregar hasta 5 fotos.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
private fun ImagePreviewItem(
    uri: Uri,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.size(110.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = uri,
                contentDescription = "Imagen seleccionada",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )

            IconButton(
                onClick = onRemove,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Quitar imagen"
                )
            }
        }
    }
}

private fun createImageUri(context: Context): Uri {
    val imageDir = File(context.cacheDir, "images")
    if (!imageDir.exists()) {
        imageDir.mkdirs()
    }

    val imageFile = File(
        imageDir,
        "camera_photo_${System.currentTimeMillis()}.jpg"
    )

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
}

@Preview(showBackground = true)
@Composable
fun CreatePostScreenPreview() {
    MaterialTheme {
        CreatePostScreen(
            onBack = {},
            onPublish = {}
        )
    }
}