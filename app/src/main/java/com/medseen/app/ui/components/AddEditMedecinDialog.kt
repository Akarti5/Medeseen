package com.medseen.app.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.medseen.app.data.Medecin
import com.medseen.app.util.ImageStorage
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMedecinDialog(
    medecin: Medecin? = null,
    onDismiss: () -> Unit,
    onConfirm: (Medecin) -> Unit
) {
    val isEditing = medecin != null
    val context = LocalContext.current

    var nom by remember { mutableStateOf(medecin?.nom ?: "") }
    var datenais by remember {
        mutableStateOf(formatBirthDateInput(medecin?.datenais.orEmpty()))
    }
    var specialite by remember { mutableStateOf(medecin?.specialite ?: "Médecine Générale") }
    var hopital by remember { mutableStateOf(medecin?.hopital ?: "") }
    var telephone by remember { mutableStateOf(medecin?.telephone ?: "") }
    var email by remember { mutableStateOf(medecin?.email ?: "") }
    var photo by remember { mutableStateOf(medecin?.photo ?: "") }

    var nomError by remember { mutableStateOf(false) }
    var expandedSpecialite by remember { mutableStateOf(false) }

    val specialitesList = listOf(
        "Médecine Générale",
        "Cardiologie",
        "Pédiatrie",
        "Neurologie",
        "Dermatologie",
        "Gynécologie",
        "Ophtalmologie",
        "Chirurgie"
    )

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            photo = runCatching { ImageStorage.savePickedImage(context, uri) }
                .getOrDefault(uri.toString())
        }
    }

    val hasImage = photo.isNotBlank() &&
        !photo.startsWith("avatar_") &&
        (photo.startsWith("content:") ||
            photo.startsWith("file:") ||
            File(photo).exists())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isEditing) "Modifier le médecin" else "Ajouter un médecin",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Photo du médecin",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            pickImage.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        .testTag("pick_photo_button"),
                    contentAlignment = Alignment.Center
                ) {
                    if (hasImage) {
                        AsyncImage(
                            model = photo,
                            contentDescription = "Photo sélectionnée",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.AddAPhoto,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Choisir une photo",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = nom,
                    onValueChange = {
                        nom = it
                        nomError = it.isBlank()
                    },
                    label = { Text("Nom complet (ex: Dr. Sophie Martin)") },
                    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                    isError = nomError,
                    supportingText = {
                        if (nomError) {
                            Text("Le nom est obligatoire", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_nom")
                )

                OutlinedTextField(
                    value = datenais,
                    onValueChange = { datenais = formatBirthDateInput(it) },
                    label = { Text("Date de naissance") },
                    placeholder = { Text("../../....") },
                    leadingIcon = { Icon(Icons.Default.Cake, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_datenais")
                )

                ExposedDropdownMenuBox(
                    expanded = expandedSpecialite,
                    onExpandedChange = { expandedSpecialite = !expandedSpecialite }
                ) {
                    OutlinedTextField(
                        value = specialite,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Spécialité") },
                        leadingIcon = { Icon(Icons.Default.MedicalServices, contentDescription = null) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSpecialite) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                            .testTag("input_specialite")
                    )
                    ExposedDropdownMenu(
                        expanded = expandedSpecialite,
                        onDismissRequest = { expandedSpecialite = false }
                    ) {
                        specialitesList.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
                                onClick = {
                                    specialite = item
                                    expandedSpecialite = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = hopital,
                    onValueChange = { hopital = it },
                    label = { Text("Hôpital / Clinique") },
                    leadingIcon = { Icon(Icons.Default.LocalHospital, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_hopital")
                )

                OutlinedTextField(
                    value = telephone,
                    onValueChange = { telephone = it },
                    label = { Text("Téléphone (ex: +33 6 12 34 56 78)") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_telephone")
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email (ex: dr.martin@hopital.fr)") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_email")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nom.isBlank()) {
                        nomError = true
                    } else {
                        val newOrUpdated = (medecin ?: Medecin(nom = nom, datenais = datenais)).copy(
                            nom = nom.trim(),
                            datenais = datenais.trim(),
                            specialite = specialite.trim(),
                            hopital = hopital.trim(),
                            telephone = telephone.trim(),
                            email = email.trim(),
                            photo = photo
                        )
                        onConfirm(newOrUpdated)
                    }
                },
                modifier = Modifier.testTag("save_doctor_button")
            ) {
                Text(if (isEditing) "Enregistrer" else "Ajouter")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_doctor_button")
            ) {
                Text("Annuler")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

/** Digits-only input → `jj/mm/aaaa` with `/` inserted automatically (max 8 digits). */
private fun formatBirthDateInput(input: String): String {
    val digits = input.filter { it.isDigit() }.take(8)
    return buildString {
        digits.forEachIndexed { index, digit ->
            append(digit)
            if (index == 1 || index == 3) append('/')
        }
    }
}

