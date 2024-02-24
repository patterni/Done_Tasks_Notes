package com.example.yes.feature_task.data.repository

import android.util.Log
import com.example.yes.feature_task.domain.model.InvalidTaskException
import com.example.yes.feature_task.domain.model.Task
import com.example.yes.feature_task.domain.repository.FirebaseTaskRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


class FirebaseTaskRepositoryImpl : FirebaseTaskRepository {

    private val firestore = FirebaseFirestore.getInstance()

    private fun getTasksCollection(userId: String?): CollectionReference? {
        return userId?.let { firestore.collection("users").document(it).collection("tasks") }
    }

    override fun getTasks(userId: String?): Flow<List<Task>> {
        val tasksCollection = getTasksCollection(userId)

        return callbackFlow {
            val listener = tasksCollection?.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val tasks =
                    snapshot?.documents?.map { it.toObject(Task::class.java)!! } ?: emptyList()
                trySend(tasks).isSuccess
            }

            // Ensure that the listener is removed when the flow is closed
            awaitClose {
                listener?.remove()
            }
        }
    }

    override suspend fun getTaskById(userId: String?, taskId: String): Task? {
        return try {
            val taskDocument = getTasksCollection(userId)?.document(taskId)?.get()?.await()
            taskDocument?.toObject(Task::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun insertTask(userId: String?, task: Task) {
            userId ?: return // Return if userId is null

            val tasksCollectionRef = getTasksCollection(userId)

            if (task.id==null) {
                val taskDocumentRef = tasksCollectionRef?.add(task)?.await()
                val firebaseId = taskDocumentRef?.id
                taskDocumentRef?.set(task.copy(id=firebaseId))?.await()
            }else {
                val taskDocumentRef = tasksCollectionRef?.document(task.id)
                val existingTask = taskDocumentRef?.get()?.await()?.toObject(Task::class.java)
                if (existingTask != null) {
                    taskDocumentRef.set(task).await()
                }
                else{
                    val taskDocumentRef = tasksCollectionRef?.add(task)?.await()
                    val firebaseId = taskDocumentRef?.id
                    taskDocumentRef?.set(task.copy(id=firebaseId))?.await()
                }
            }
    }

    override suspend fun updateTask(userId: String?, completed: Boolean, taskId: String?) {
        // Assuming taskId is not null; handle the case where it might be null as needed
        taskId?.let {
            getTasksCollection(userId)?.document(it)?.update("completed", completed)?.await() ?: return@let
        }
    }

    override suspend fun deleteTask(userId: String?, task: Task) {
        try {
            userId ?: return // Return if userId is null
            val taskId = task.id ?: return // Return if taskId is null

            val tasksCollectionRef = firestore.collection("users").document(userId).collection("tasks")
            tasksCollectionRef.document(taskId).delete().await()
        } catch (e: Exception) {
            // Handle exceptions appropriately, e.g., log or throw a custom exception
            throw InvalidTaskException("Error deleting task: ${e.message}")
        }
    }
}