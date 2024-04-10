package org.mathieu.cleanrmapi.ui.core.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EpisodeCard(
    modifier : Modifier = Modifier,
    date : String,
    episode : String,
    name : String
) {
    Card(
        modifier = modifier,
        shape = RectangleShape
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = date)
            Text(text = "$episode - $name")
        }

    }
}


@Composable
@Preview
private fun EpisodeCardPreview() = PreviewContent(spacingVertical = 6.dp) {
    EpisodeCard(date = "2021-12-12", episode = "S01E01", name = "Pilot")
    EpisodeCard(date = "2022-12-12", episode = "S02E01", name = "Test")
}