package com.medseen.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Structure de la table MEDECIN selon le sujet:
 * MEDECIN(idmed, nom, datenais, photo)
 *
 * Des champs complémentaires utiles ont été ajoutés pour rendre l'application
 * réaliste, intuitive et simple à comprendre (spécialité, téléphone, hôpital, etc.)
 */
@Entity(tableName = "medecin")
data class Medecin(
    @PrimaryKey(autoGenerate = true)
    val idmed: Int = 0,
    val nom: String,
    val datenais: String, // Date de naissance (ex: "12/05/1982")
    val photo: String = "", // Chemin local de la photo choisie depuis le téléphone
    val specialite: String = "Médecine Générale",
    val telephone: String = "",
    val email: String = "",
    val hopital: String = "Hôpital Central",
    val isFavorite: Boolean = false
)
