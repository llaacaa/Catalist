
package com.example.catalist.presentation.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.catalist.presentation.theme.CatalistTheme

@Composable
fun CatListItem(
    modifier: Modifier = Modifier,
    state: CatListItemState,
) {

    Column(
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = state.race,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = state.alternativeRaces,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                maxLines = 2,
            )
        }
        Text(
            text = state.description,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            style = MaterialTheme.typography.bodyMedium
        )


        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            items(state.temperaments) { attribute ->
                ChipItem(
                    modifier = Modifier,
                    text = attribute
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Composable
fun ChipItem(
    modifier: Modifier = Modifier,
    text: String,
) {

    val width = LocalConfiguration.current.screenWidthDp.dp / 5
    Text(
        modifier = modifier
            .clip(CircleShape)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .widthIn(max = width)
            .padding(6.dp),
        text = text,
        color = MaterialTheme.colorScheme.onPrimary,
        textAlign = TextAlign.Center,
        fontSize = 12.sp,
        softWrap = false

    )
}

@Preview
@Composable
fun PreviewCatListItem(modifier: Modifier = Modifier) {
    CatalistTheme {
        CatListItem(
            state = CatListItemState(
                race = "persijska",
                alternativeRaces = "britanska , evropska",
                description = "Macka sa dugim repom ima kandze i dasdsadsadasdasdasdsadsadsadsadsadsadsadsadsadsa" +
                        "dasdsadsadsadasdsadasdsadsa" +
                        "lepe oci",
                temperaments = listOf("playful", "angry", "friendly")
            )
        )
    }
}
