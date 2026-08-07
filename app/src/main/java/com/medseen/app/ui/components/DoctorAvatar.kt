package com.medseen.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.io.File

/**
 * Affiche la photo du médecin depuis un chemin local / URI, ou un placeholder.
 */
@Composable
fun DoctorAvatar(
    photoPath: String,
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 52.dp,
    cornerRadius: Dp = 8.dp
) {
    val shape = RoundedCornerShape(cornerRadius)
    val hasImage = photoPath.isNotBlank() &&
        !photoPath.startsWith("avatar_") &&
        (photoPath.startsWith("content:") ||
            photoPath.startsWith("file:") ||
            File(photoPath).exists())

    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        if (hasImage) {
            AsyncImage(
                model = photoPath,
                contentDescription = name,
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopCenter,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = name,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(size * 0.45f)
            )
        }
    }
}
