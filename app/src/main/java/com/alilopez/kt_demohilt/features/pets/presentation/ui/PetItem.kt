package com.alilopez.kt_demohilt.features.pets.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.alilopez.kt_demohilt.features.pets.domain.model.Pet
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PetItem(
    pet: Pet,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Imagen de la mascota
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pet.image)
                    .crossfade(true)
                    .build(),
                contentDescription = "Foto de ${pet.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            // Información de la mascota
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Raza: ${pet.race}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Edad: ${pet.age} años",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Dueño: ${pet.owner}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Última cita: ${dateFormat.format(pet.lastAppointment)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}