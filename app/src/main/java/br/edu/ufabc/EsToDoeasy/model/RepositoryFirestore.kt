package br.edu.ufabc.EsToDoeasy.model

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class RepositoryFirestore(application: Application) : Repository {
    private val db: FirebaseFirestore = Firebase.firestore
    private val isConnected = AtomicBoolean(true)

    companion object {
        private const val itemsCollection = "todoItems"
        private const val achievementsCollection = "achievements"
        private const val groupsCollection = "groups"

        private const val taskIdDoc = "taskId"
        private const val achievementIdDoc = "achievementId"
        private const val groupIdDoc = "groupId"

        private object ItemDoc {
            const val id = "id"
            const val userId = "userId"
            const val title = "title"
            const val details = "details"
            const val dateStarted = "dateStarted"
            const val dateFinished = "dateFinished"
            const val dateDue = "dateDue"
            const val timeElapsed = "timeElapsed"
            const val groupId = "groupId"
            const val difficulty = "difficulty"
            const val priority = "priority"
            const val status = "status"
            const val dependecies = "dependecies"
        }
    }

    private data class TaskFirestore(
        val id: String? = null,
        val userId: String? = null,
        val title: String? = null,
        val details: String? = null,
        val dateStarted: Date? = null,
        val dateFinished: Date? = null,
        val dateDue: Date? = null,
        val timeElapsed: Long? = null,
        val groupId: String? = null,
        val difficulty: String? = null,
        val priority: String? = null,
        val status: String? = null,
        val dependencies: List<String>? = null,
    ) {
        fun toTask() = Task(
            id = id ?: "",
            userId = userId ?: "",
            title = title ?: "",
            details = details ?: "",
            dateStarted = dateStarted ?: Date(),
            dateFinished = dateFinished ?: Date(),
            dateDue = dateDue ?: Date(),
            timeElapsed = timeElapsed ?: 0,
            groupId = groupId ?: "",
            difficulty = Difficulty.valueOf(difficulty ?: "EASY"),
            priority = Priority.valueOf(priority ?: "LOW"),
            status = Status.valueOf(status ?: "TODO"),
            dependencies = dependencies ?: listOf()
        )

        companion object {
            fun fromTask(task: Task) = TaskFirestore(
                id = task.id,
                userId = task.userId,
                title = task.title,
                details = task.details,
                dateStarted = task.dateStarted,
                dateFinished = task.dateFinished,
                dateDue = task.dateDue,
                timeElapsed = task.timeElapsed,
                groupId = task.groupId,
                difficulty = task.difficulty.toString(),
                priority = task.priority.toString(),
                status = task.status.toString(),
                dependencies = task.dependencies,
            )
        }
    }

    private data class TaskId(
        val value: Long? = null
    )

    init {
        application.applicationContext.getSystemService(ConnectivityManager::class.java).apply {
            val connected =
                getNetworkCapabilities(activeNetwork)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    ?: false

            isConnected.set(connected)
            registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    isConnected.set(true)
                }

                override fun onLost(network: Network) {
                    isConnected.set(false)
                }
            })
        }
    }

    private fun getSource() = if (isConnected.get()) Source.DEFAULT else Source.CACHE

    private fun getCurrentUser(): String = FirebaseAuth.getInstance().currentUser?.uid
        ?: throw Exception("No user is signed in")

    private fun getCollection() = db.collection(itemsCollection)

    override suspend fun getAllTasks(): Tasks = getCollection()
        .whereEqualTo(ItemDoc.userId, getCurrentUser())
        .get(getSource())
        .await()
        .toObjects(TaskFirestore::class.java).map { it.toTask() }

    override suspend fun getAllGroups() {
        TODO("Not yet implemented")
    }

    override suspend fun getAllAchievements() {
        TODO("Not yet implemented")
    }

    override suspend fun getDependencies(id: String): Tasks {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getGroup(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getAchievement(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun refresh() {
        TODO("Not yet implemented")
    }
}
