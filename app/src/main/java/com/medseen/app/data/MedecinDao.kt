package com.medseen.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Interface DAO pour les requêtes SQL Room de la table MEDECIN
 */
@Dao
interface MedecinDao {

    @Query("SELECT * FROM medecin ORDER BY nom ASC")
    fun getAllMedecins(): Flow<List<Medecin>>

    @Query("SELECT * FROM medecin WHERE idmed = :id")
    suspend fun getMedecinById(id: Int): Medecin?

    @Query("""
        SELECT * FROM medecin 
        WHERE LOWER(nom) LIKE '%' || LOWER(:query) || '%' 
           OR LOWER(specialite) LIKE '%' || LOWER(:query) || '%' 
           OR LOWER(hopital) LIKE '%' || LOWER(:query) || '%'
        ORDER BY nom ASC
    """)
    fun searchMedecins(query: String): Flow<List<Medecin>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedecin(medecin: Medecin)

    @Update
    suspend fun updateMedecin(medecin: Medecin)

    @Delete
    suspend fun deleteMedecin(medecin: Medecin)

    @Query("DELETE FROM medecin WHERE idmed = :id")
    suspend fun deleteMedecinById(id: Int)

    @Query("SELECT COUNT(*) FROM medecin")
    suspend fun getCount(): Int
}
