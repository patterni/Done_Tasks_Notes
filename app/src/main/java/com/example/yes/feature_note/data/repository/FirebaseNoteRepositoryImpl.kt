package com.example.yes.feature_note.data.repository

import com.example.yes.feature_note.domain.model.Note
import com.example.yes.feature_note.domain.repository.FirebaseNoteRepository
import com.example.yes.feature_task.domain.model.InvalidTaskException
import com.example.yes.feature_task.domain.model.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseNoteRepositoryImpl:FirebaseNoteRepository {

    private val firestore = FirebaseFirestore.getInstance()

    private fun getNotesCollection(userId: String?): CollectionReference? {
        return userId?.let { firestore.collection("users").document(it).collection("notes") }
    }

    override fun getNotes(userId: String?): Flow<List<Note>> {
            val tasksCollection = getNotesCollection(userId)

            return callbackFlow {
                val listener = tasksCollection?.addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    val notes =
                        snapshot?.documents?.map { it.toObject(Note::class.java)!! } ?: emptyList()
                    trySend(notes).isSuccess
                }

                // Ensure that the listener is removed when the flow is closed
                awaitClose {
                    listener?.remove()
                }
            }
        }

    override suspend fun getNoteById(userId: String?, noteId: String): Note? {
        return try {
            val taskDocument = getNotesCollection(userId)?.document(noteId)?.get()?.await()
            taskDocument?.toObject(Note::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun insertNote(userId: String?, note: Note) {
        userId ?: return // Return if userId is null

        val tasksCollectionRef = getNotesCollection(userId)

        if (note.id==null) {
            val taskDocumentRef = tasksCollectionRef?.add(note)?.await()
            val firebaseId = taskDocumentRef?.id
            taskDocumentRef?.set(note.copy(id=firebaseId))?.await()
        }else {
            val taskDocumentRef = tasksCollectionRef?.document(note.id)
            val existingTask = taskDocumentRef?.get()?.await()?.toObject(Task::class.java)
            if (existingTask != null) {
                taskDocumentRef.set(note).await()
            }
            else{
                val taskDocumentRef = tasksCollectionRef?.add(note)?.await()
                val firebaseId = taskDocumentRef?.id
                taskDocumentRef?.set(note.copy(id=firebaseId))?.await()
            }
        }
    }

    override suspend fun deleteNote(userId: String?, note: Note) {
        try {
            userId ?: return // Return if userId is null
            val noteId = note.id ?: return // Return if taskId is null

            val tasksCollectionRef = firestore.collection("users").document(userId).collection("notes")
            tasksCollectionRef.document(noteId).delete().await()
        } catch (e: Exception) {
            // Handle exceptions appropriately, e.g., log or throw a custom exception
            throw InvalidTaskException("Error deleting task: ${e.message}")
        }
    }
}