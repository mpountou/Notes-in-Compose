package com.intelligent.notes.di

import android.content.Context
import androidx.room.Room
import com.intelligent.notes.data.NotesDao
import com.intelligent.notes.data.NotesDatabase
import com.intelligent.notes.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Install module to NotesApplication
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): NotesDatabase = Room.databaseBuilder(
        context,
        NotesDatabase::class.java,
        DATABASE_NAME
    ).build()


    @Singleton
    @Provides
    fun provideDao(database: NotesDatabase): NotesDao = database.notesDao()

}