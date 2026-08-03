package com.medseen.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.medseen.app.data.Medecin
import com.medseen.app.ui.components.AddEditMedecinDialog
import com.medseen.app.ui.components.DoctorCard
import com.medseen.app.ui.components.MedecinDetailDialog
import com.medseen.app.ui.components.SearchAndFilterHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedecinMainScreen(
    viewModel: MedecinViewModel
) {
    val doctors by viewModel.medecinList.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedSpecialite by viewModel.selectedSpecialite.collectAsStateWithLifecycle()
    val showFavoritesOnly by viewModel.showFavoritesOnly.collectAsStateWithLifecycle()

    var doctorToEdit by remember { mutableStateOf<Medecin?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var doctorToViewDetail by remember { mutableStateOf<Medecin?>(null) }
    var doctorToDelete by remember { mutableStateOf<Medecin?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MedicalServices,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Medseen",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "CRUD & Recherche (Room SQL)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.resetSampleData() },
                        modifier = Modifier.testTag("reset_sample_data_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Réinitialiser les données de test",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nouveau Médecin") },
                modifier = Modifier.testTag("add_doctor_fab"),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header: Recherche + Filtres
            SearchAndFilterHeader(
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.onSearchQueryChanged(it) },
                selectedSpecialite = selectedSpecialite,
                onSpecialiteSelected = { viewModel.onSpecialiteSelected(it) },
                showFavoritesOnly = showFavoritesOnly,
                onToggleFavoritesOnly = { viewModel.toggleFavoritesOnly() },
                resultCount = doctors.size
            )

            // Liste des médecins
            if (doctors.isEmpty()) {
                EmptyStateView(
                    searchQuery = searchQuery,
                    selectedSpecialite = selectedSpecialite,
                    showFavoritesOnly = showFavoritesOnly,
                    onAddClick = { showAddDialog = true },
                    onResetClick = { viewModel.resetSampleData() }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(
                        items = doctors,
                        key = { it.idmed }
                    ) { medecin ->
                        DoctorCard(
                            medecin = medecin,
                            onClick = { doctorToViewDetail = medecin },
                            onEditClick = { doctorToEdit = medecin },
                            onDeleteClick = { doctorToDelete = medecin },
                            onFavoriteToggle = { viewModel.toggleFavorite(medecin) }
                        )
                    }
                }
            }
        }
    }

    // Modal Dialog: Ajouter un nouveau médecin
    if (showAddDialog) {
        AddEditMedecinDialog(
            medecin = null,
            onDismiss = { showAddDialog = false },
            onConfirm = { newMedecin ->
                viewModel.addMedecin(newMedecin)
                showAddDialog = false
            }
        )
    }

    // Modal Dialog: Modifier un médecin existant
    doctorToEdit?.let { medecin ->
        AddEditMedecinDialog(
            medecin = medecin,
            onDismiss = { doctorToEdit = null },
            onConfirm = { updatedMedecin ->
                viewModel.updateMedecin(updatedMedecin)
                doctorToEdit = null
            }
        )
    }

    // Modal Dialog: Afficher les détails d'un médecin
    doctorToViewDetail?.let { medecin ->
        MedecinDetailDialog(
            medecin = medecin,
            onDismiss = { doctorToViewDetail = null },
            onEditClick = {
                doctorToEdit = medecin
                doctorToViewDetail = null
            },
            onDeleteClick = {
                doctorToDelete = medecin
                doctorToViewDetail = null
            }
        )
    }

    // Dialog de confirmation de suppression
    doctorToDelete?.let { medecin ->
        AlertDialog(
            onDismissRequest = { doctorToDelete = null },
            title = { Text("Confirmer la suppression") },
            text = { Text("Voulez-vous vraiment supprimer ${medecin.nom} de la base de données ?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteMedecin(medecin)
                        doctorToDelete = null
                    },
                    modifier = Modifier.testTag("confirm_delete_button")
                ) {
                    Text("Supprimer")
                }
            },
            dismissButton = {
                TextButton(onClick = { doctorToDelete = null }) {
                    Text("Annuler")
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
private fun EmptyStateView(
    searchQuery: String,
    selectedSpecialite: String,
    showFavoritesOnly: Boolean,
    onAddClick: () -> Unit,
    onResetClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.MedicalInformation,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                modifier = Modifier.size(72.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            val isFiltered = searchQuery.isNotBlank() || selectedSpecialite != "Toutes" || showFavoritesOnly

            Text(
                text = if (isFiltered) "Aucun médecin ne correspond à votre recherche" else "Aucun médecin dans la base de données",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isFiltered) "Essayez de modifier vos mots-clés ou filtres de spécialité." else "Ajoutez un médecin manuellement ou générez un jeu de données de démonstration.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onResetClick,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Charger exemples")
                }

                Button(
                    onClick = onAddClick,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ajouter")
                }
            }
        }
    }
}
