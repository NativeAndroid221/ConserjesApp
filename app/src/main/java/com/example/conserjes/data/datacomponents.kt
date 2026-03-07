package com.example.conserjes.data

import android.net.Uri

data class cardpublication(
        val id: String,
        val name: String,
        val handle: String,
        val dateTime: String,
        val title: String,
        val content: String,
        val importance: String,
        val images: List<Uri> = emptyList()
)