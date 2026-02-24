package com.example.conserjes.data

import android.net.Uri

data class cardpublication(
        val name: String,
        val handle: String,
        val dateTime: String,
        val content: String,
        val image: Uri?= null

)


