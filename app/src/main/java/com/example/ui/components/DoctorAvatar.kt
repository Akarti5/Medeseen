package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Composant Avatar simple et élégant affichant l'icône du médecin ou ses initiales.
 */
@Composable
fun DoctorAvatar(
    photoKey: String,
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = 52.dp
) {
    val backgroundColor = when (photoKey.lowercase()) {
        "avatar_female_1" -> Color(0xFFE1F5FE)
        "avatar_male_1" -> Color(0xFFE8F5E9)
        "avatar_female_2" -> Color(0xFFF3E5F5)
        "avatar_male_2" -> Color(0xFFFFF3E0)
        "avatar_female_3" -> Color(0xFFFCE4EC)
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    val iconColor = when (photoKey.lowercase()) {
        "avatar_female_1" -> Color(0xFF0288D1)
        "avatar_male_1" -> Color(0xFF388E3C)
        "avatar_female_2" -> Color(0xFF7B1FA2)
        "avatar_male_2" -> Color(0xFFF57C00)
        "avatar_female_3" -> Color(0xFFC2185B)
        else -> MaterialTheme.colorScheme.primary
    }

    val initials = name.trim()
        .split(" ")
        .filter { it.isNotEmpty() && !it.equals("Dr.", ignoreCase = true) }
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(1.5.dp, iconColor.copy(alpha = 0.3f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (initials.isNotEmpty()) {
            Text(
                text = initials,
                fontWeight = FontWeight.Bold,
                color = iconColor,
                fontSize = (size.value * 0.38f).sp
            )
        } else {
            Icon(
                imageVector = Icons.Default.MedicalServices,
                contentDescription = "Doctor Icon",
                tint = iconColor,
                modifier = Modifier.size(size * 0.5f)
            )
        }
    }
}
