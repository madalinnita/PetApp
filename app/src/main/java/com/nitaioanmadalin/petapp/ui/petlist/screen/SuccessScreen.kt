package com.nitaioanmadalin.petapp.ui.petlist.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.nitaioanmadalin.petapp.domain.model.Breed
import com.nitaioanmadalin.petapp.domain.model.Pet
import com.nitaioanmadalin.petapp.ui.theme.PetAppTheme

@Composable
fun SuccessScreen(
    data: List<Pet>,
    onPetClicked: (Pet) -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(data.size) { index ->
            PetInfo(
                item = data[index],
                onPetClicked = onPetClicked
            )
        }
    }
}

@Preview
@Composable
fun SuccessScreenPreview() {
    PetAppTheme {
        val items = mutableListOf<Pet>()
        for (index in 1..10) {
            items.add(
                Pet(
                  name="Luna",
                    breed= Breed(primary = "MockPrimary", secondary = "MockSecond", mixed = true, unknown = false),
                    size="Medium",
                    gender="Female",
                    status="Unavailable",
                    distance = 233.33
                )
            )
        }
        SuccessScreen(data = items, onPetClicked = {})
    }
}

@Composable
fun PetInfo(
    item: Pet,
    onPetClicked: (Pet) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable {
                onPetClicked.invoke(item)
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageUrl = item.largeImageUrl
                ?: getRandomisedPhotoUrl()
            Image(
                painter = rememberImagePainter(
                    data = imageUrl,
                    builder = {
                        transformations(CircleCropTransformation())
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .padding(16.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Pet name: ${item.name}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Breed: ${item.breed?.primary ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (item.status == "adoptable") {
                Column(modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Canvas(
                            modifier = Modifier.size(12.dp)
                        ) {
                            drawCircle(
                                color = Color.Green,
                                center = center,
                                radius = size.minDimension / 2f
                            )
                        }
                    }
                    Text(
                        text = "Adoptable!",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}

private fun getRandomisedPhotoUrl(): String {
    val listOfUrls = listOf(
        "https://www.princeton.edu/sites/default/files/styles/1x_full_2x_half_crop/public/images/2022/02/KOA_Nassau_2697x1517.jpg?itok=Bg2K7j7J",
        "https://i.natgeofe.com/n/4f5aaece-3300-41a4-b2a8-ed2708a0a27c/domestic-dog_thumb_3x2.jpg",
        "https://static01.nyt.com/images/2024/01/16/multimedia/16xp-dog-01-lchw/16xp-dog-01-lchw-mediumSquareAt3X.jpg",
        "https://www.purina.co.uk/sites/default/files/2020-12/Dog_1098119012_Teaser.jpg",
        "https://cdn.britannica.com/16/234216-050-C66F8665/beagle-hound-dog.jpg"
    )
    return listOfUrls.random()
}
