package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.MedicalServices
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

import com.example.data.Medecin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMedecinDialog(
    medecin: Medecin? = null,
    onDismiss: () -> Unit,
    onConfirm: (Medecin) -> Unit
) {
    val isEditing = medecin != null

    var nom by remember { mutableStateOf(medecin?.nom ?: "") }
    var datenais by remember { mutableStateOf(medecin?.datenais ?: "") }
    var specialite by remember { mutableStateOf(medecin?.specialite ?: "Médecine Générale") }
    var hopital by remember { mutableStateOf(medecin?.hopital ?: "") }
    var telephone by remember { mutableStateOf(medecin?.telephone ?: "") }
    var email by remember { mutableStateOf(medecin?.email ?: "") }
    var photo by remember { mutableStateOf(medecin?.photo ?: "avatar_female_1") }

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

    val avatarOptions = listOf(
        "avatar_female_1",
        "avatar_male_1",
        "avatar_female_2",
        "avatar_male_2",
        "avatar_female_3"
    )

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
                // Choix de l'avatar
                Text(
                    text = "Avatar du médecin:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    avatarOptions.forEach { avatarKey ->
                        DoctorAvatar(
                            photoKey = avatarKey,
                            name = if (nom.isNotBlank()) nom else "Doc",
                            size = 44.dp,
                            modifier = Modifier
                                .clickable { photo = avatarKey }
                                .padding(2.dp)
                        )
                    }
                }

                // Field: Nom (Mandatory)
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

                // Field: Date de naissance (Sujet: datenais)
                OutlinedTextField(
                    value = datenais,
                    onValueChange = { datenais = it },
                    label = { Text("Date de naissance (ex: 14/05/1982)") },
                    leadingIcon = { Icon(Icons.Default.Cake, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_datenais")
                )

                // Field: Spécialité Dropdown
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

                // Field: Hôpital
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

                // Field: Téléphone
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

                // Field: Email
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
