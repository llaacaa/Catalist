package com.example.catalist.presentation.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

    Column {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = state.race,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = state.alternativeRaces.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.LightGray
            )
        }
        // TODO dodaj 250 cutoff
        Text(
            text = state.description,
            overflow = TextOverflow.Ellipsis,
            maxLines = 3,
            style = MaterialTheme.typography.bodyMedium
        )


        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.attributes.forEach { attribute ->
                ChipItem(
                    modifier = Modifier.width(60.dp),
                    text = attribute
                )
            }
        }

    }
}

@Composable
fun ChipItem(
    modifier: Modifier = Modifier,
    text: String,
    backgroundColor: Color = Color(color = 0xFF509606),
    textColor: Color = Color.White
) {

    Text(
        modifier = modifier
            .clip(CircleShape)
            .background(
                color = backgroundColor,
                shape = CircleShape
            )
            .padding(2.dp),
        text = text,
        color = textColor,
        textAlign = TextAlign.Center,
        fontSize = 12.sp
    )
}

@Preview
@Composable
fun PreviewCatListItem(modifier: Modifier = Modifier) {
    CatalistTheme {
        CatListItem(
            state = CatListItemState(
                race = "persijska",
                alternativeRaces = listOf("britanska", "evropska"),
                description = "Macka sa dugim repom ima kandze i dasdsadsadasdasdasdsadsadsadsadsadsadsadsadsadsa" +
                        "dasdsadsadsadasdsadasdsadsa" +
                        "lepe oci",
                attributes = listOf("playful", "angry", "friendly")
            )
        )
    }
}
