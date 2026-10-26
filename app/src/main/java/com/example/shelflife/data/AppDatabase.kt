package com.example.shelflife.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shelflife.model.Amigo
import com.example.shelflife.model.Livro

@Database(entities = [Livro::class, Amigo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun livroDao(): LivroDao
    abstract fun amigoDao(): AmigoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obterInstancia(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shelflife_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}