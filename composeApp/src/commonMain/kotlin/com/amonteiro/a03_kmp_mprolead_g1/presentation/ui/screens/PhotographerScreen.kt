package com.amonteiro.a03_kmp_mprolead_g1.presentation.ui.screens

import a03_kmp_mprolead_g1.composeapp.generated.resources.Res
import a03_kmp_mprolead_g1.composeapp.generated.resources.compose_multiplatform
import a03_kmp_mprolead_g1.composeapp.generated.resources.error
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.amonteiro.a03_kmp_mprolead_g1.data.remote.PhotographerDTO
import com.amonteiro.a03_kmp_mprolead_g1.presentation.ui.theme.AppTheme
import org.jetbrains.compose.resources.painterResource

@Preview
@Composable
fun PhotographerScreenPreview() {
    AppTheme {
        val photograph = PhotographerDTO(
            id = 1,
            stageName = "Bob la Menace",
            photoUrl = "https://www.amonteiro.fr/img/fakedata/jc.jpg",
            story = "Ancien agent secret, Bob a troqué ses gadgets pour un appareil photo après une mission qui a mal tourné. Il traque désormais les instants volés plutôt que les espions.",
            portfolio = listOf(
                "https://picsum.photos/407",
                "https://picsum.photos/125",
                "https://picsum.photos/549"
            )
        )
        PhotographerScreen(photographer =  photograph)
    }
}


@Composable
fun PhotographerScreen(
    modifier: Modifier = Modifier,
    photographer: PhotographerDTO
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Image de couverture
            AsyncImage(
                model = photographer.photoUrl,
                error = painterResource(Res.drawable.error),
                contentDescription = "Photo principale",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Nom du photographe
                Text(
                    text = photographer.stageName,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Histoire du photographe
                Text(
                    text = photographer.story,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Galerie d'images
                Text(
                    text = "Galerie de photos",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(photographer.portfolio.size) {
                        ImageCard(photographer.portfolio[it])
                    }
                }
            }
        }
    }
}


@Composable
fun ImageCard(imageUrl: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .width(160.dp)
            .height(160.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Image",
            placeholder = painterResource(Res.drawable.compose_multiplatform),
            onError = { println(it) },
            error = painterResource(Res.drawable.error),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}