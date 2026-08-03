package com.medseen.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Classe singleton de gestion de la base de données SQLite avec Room
 */
@Database(entities = [Medecin::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medecinDao(): MedecinDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "medecin_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
