package com.silverorange.videoplayer.api.models

data class Videos(
    val id: String,
    val title: String,
    val hlsURL: String,
    val fullURL: String,
    val description: String,
    val publishedAt: String,
    val author: Author
) {
    constructor() : this("", "", "", "", "", "", Author())
}