package org.mathieu.cleanrmapi.domain.models.episode

import java.util.Date

data class Episode(
    val id: Int,
    val name: String,
    val airDate: String,
    val episode: String,
    val url: String,
)