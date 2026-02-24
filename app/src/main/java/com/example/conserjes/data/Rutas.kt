package com.example.conserjes.data

sealed class Route(val path: String) {
    data object Home : Route("home")
    data object CreatePost : Route("create_post")
}
