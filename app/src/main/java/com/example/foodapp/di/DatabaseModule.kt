package com.example.foodapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodapp.data.database.RecipesDatabase
import com.example.foodapp.data.database.RecipesDatabase.Companion.Migration1_2
import com.example.foodapp.util.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        RecipesDatabase::class.java,
        DATABASE_NAME
    )   .addMigrations(Migration1_2)
        .build()

    @Singleton
    @Provides
    fun provideData(recipesDatabase: RecipesDatabase) = recipesDatabase.recipesDao()

}