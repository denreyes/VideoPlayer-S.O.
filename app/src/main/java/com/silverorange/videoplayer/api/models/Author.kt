package com.silverorange.videoplayer.api.models

data class Author(
    val id: String,
    val name: String
) {
    constructor() : this("", "")
}