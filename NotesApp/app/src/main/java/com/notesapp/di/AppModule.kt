package com.notesapp.di

import android.app.Application
import androidx.room.Room
import com.notesapp.feature_note.data.data_source.NoteDatabase
import com.notesapp.feature_note.data.repository.NoteRepositoryImplementation
import com.notesapp.feature_note.domain.repository.NoteRepository
import com.notesapp.feature_note.domain.use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImplementation(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(rep: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNote = GetNotesUseCase(rep),
            deleteNote = DeleteNoteUseCase(rep),
            addNote = AddNoteUseCase(rep),
            getSingleNote = GetNoteUseCase(rep)
        )
    }
}