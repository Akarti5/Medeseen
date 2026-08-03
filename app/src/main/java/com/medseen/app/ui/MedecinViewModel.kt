package com.medseen.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.medseen.app.data.AppDatabase
import com.medseen.app.data.Medecin
import com.medseen.app.data.MedecinRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel gérant le d'état de l'interface et les actions CRUD / Recherche
 */
class MedecinViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MedecinRepository

    val searchQuery = MutableStateFlow("")
    val selectedSpecialite = MutableStateFlow("Toutes")
    val showFavoritesOnly = MutableStateFlow(false)

    init {
        val dao = AppDatabase.getDatabase(application).medecinDao()
        repository = MedecinRepository(dao)
        viewModelScope.launch {
            repository.seedSampleDataIfEmpty()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val medecinList: StateFlow<List<Medecin>> = combine(
        searchQuery,
        selectedSpecialite,
        showFavoritesOnly
    ) { query, specialite, favOnly ->
        Triple(query, specialite, favOnly)
    }.flatMapLatest { (query, specialite, favOnly) ->
        repository.searchMedecins(query).flatMapLatest { list ->
            val filtered = list.filter { med ->
                val matchesSpecialite = (specialite == "Toutes" || med.specialite.equals(specialite, ignoreCase = true))
                val matchesFav = (!favOnly || med.isFavorite)
                matchesSpecialite && matchesFav
            }
            flowOf(filtered)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChanged(newQuery: String) {
        searchQuery.value = newQuery
    }

    fun onSpecialiteSelected(specialite: String) {
        selectedSpecialite.value = specialite
    }

    fun toggleFavoritesOnly() {
        showFavoritesOnly.value = !showFavoritesOnly.value
    }

    fun addMedecin(medecin: Medecin) {
        viewModelScope.launch {
            repository.insert(medecin)
        }
    }

    fun updateMedecin(medecin: Medecin) {
        viewModelScope.launch {
            repository.update(medecin)
        }
    }

    fun deleteMedecin(medecin: Medecin) {
        viewModelScope.launch {
            repository.delete(medecin)
        }
    }

    fun toggleFavorite(medecin: Medecin) {
        viewModelScope.launch {
            repository.update(medecin.copy(isFavorite = !medecin.isFavorite))
        }
    }

    fun resetSampleData() {
        viewModelScope.launch {
            repository.seedSampleDataIfEmpty()
        }
    }
}
