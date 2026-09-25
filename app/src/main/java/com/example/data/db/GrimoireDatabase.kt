package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GameEntity::class], version = 1, exportSchema = false)
abstract class GrimoireDatabase : RoomDatabase() {
    abstract fun grimoireDao(): GrimoireDao

    companion object {
        @Volatile
        private var INSTANCE: GrimoireDatabase? = null

        fun getDatabase(context: Context): GrimoireDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GrimoireDatabase::class.java,
                    "botc_grimoire_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
