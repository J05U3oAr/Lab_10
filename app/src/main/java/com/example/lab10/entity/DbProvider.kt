package com.example.lab10.entity

import android.content.Context
import androidx.room.Room

object DbProvider {
    @Volatile private var instance: AppDatabase? = null

    fun get(context: Context): AppDatabase =
        instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "lab10.db"
            ).fallbackToDestructiveMigration()
                .build()
                .also { instance = it }
        }
}
