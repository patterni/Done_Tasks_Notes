package com.example.yes.di

import com.example.yes.feature_note.data.repository.FirebaseNoteRepositoryImpl
import com.example.yes.feature_note.domain.repository.FirebaseNoteRepository
import com.example.yes.feature_note.domain.use_case.AddNote
import com.example.yes.feature_note.domain.use_case.DeleteNote
import com.example.yes.feature_note.domain.use_case.GetNote
import com.example.yes.feature_note.domain.use_case.GetNotes
import com.example.yes.feature_note.domain.use_case.NotesUseCases
import com.example.yes.feature_profile.data.repository.AuthRepositoryImpl
import com.example.yes.feature_task.data.repository.FirebaseTaskRepositoryImpl
import com.example.yes.feature_profile.domain.repository.AuthRepository
import com.example.yes.feature_task.domain.repository.FirebaseTaskRepository
import com.example.yes.feature_task.domain.use_case.AddTask
import com.example.yes.feature_task.domain.use_case.DeleteTask
import com.example.yes.feature_task.domain.use_case.GetTask
import com.example.yes.feature_task.domain.use_case.GetTasks
import com.example.yes.feature_task.domain.use_case.TasksUseCases
import com.example.yes.feature_task.domain.use_case.UpdateTask
import com.google.firebase.auth.FirebaseAuth
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
    fun provideFirebaseNoteRepository():FirebaseNoteRepository{
        return FirebaseNoteRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideNotesUseCases(repository: FirebaseNoteRepository):NotesUseCases{
        return NotesUseCases(
            getNotes =  GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNote = GetNote(repository)
        )
    }

    @Provides
    @Singleton
    fun provideFirebaseTaskRepository(): FirebaseTaskRepository {
        return FirebaseTaskRepositoryImpl()
    }


    @Provides
    @Singleton
    fun provideTaskUseCases(repository: FirebaseTaskRepository): TasksUseCases {
        return TasksUseCases(
            getTasks = GetTasks(repository),
            deleteTask = DeleteTask(repository),
            addTask = AddTask(repository),
            getTask = GetTask(repository),
            updateTask = UpdateTask(repository)
        )
    }

    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesRepositoryImpl(firebaseAuth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }
}