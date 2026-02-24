package com.example.conserjes.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.conserjes.data.cardpublication

class PostViewModel : ViewModel() {

    val posts = mutableStateListOf<cardpublication>()

    fun addPost(post: cardpublication) {
        posts.add(0, post)
    }
}