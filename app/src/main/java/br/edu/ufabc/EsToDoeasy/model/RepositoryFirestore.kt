package br.edu.ufabc.EsToDoeasy.model

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
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
        private const val tasksCollection = "todoItems"
        private const val achievementsCollection = "achievements"
        private const val groupsCollection = "groups"

        private const val taskIdDoc = "taskId"
        private const val achievementIdDoc = "achievementId"
        private const val groupIdDoc = "groupId"

        private object TaskDoc {
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
            const val dependencies = "dependencies"
        }

        private object GroupDoc {
            const val id = "id"
            const val userId = "userId"
            const val name = "name"
        }

        private object AchievementDoc {
            const val id = "id"
            const val userId = "userId"
            const val name = "name"
            const val title = "title"
            const val details = "details"
            const val date = "date"
            const val achieved = "achieved"
            const val difficulty = "difficulty"
        }
    }

    private data class TaskFirestore(
        val id: Long? = null,
        val userId: String? = null,
        val title: String? = null,
        val details: String? = null,
        val dateStarted: Date? = null,
        val dateFinished: Date? = null,
        val dateDue: Date? = null,
        val timeElapsed: Long? = null,
        val groupId: Long? = null,
        val difficulty: String? = null,
        val priority: String? = null,
        val status: String? = null,
        val dependencies: List<Long>? = null,
    ) {
        fun toTask() = Task(
            id = id ?: 0,
            userId = userId ?: "",
            title = title ?: "",
            details = details ?: "",
            dateStarted = dateStarted ?: Date(),
            dateFinished = dateFinished ?: Date(),
            dateDue = dateDue ?: Date(),
            timeElapsed = timeElapsed ?: 0,
            groupId = groupId ?: 0L,
            difficulty = Difficulty.valueOf(difficulty ?: "EASY"),
            priority = Priority.valueOf(priority ?: "LOW"),
            status = Status.valueOf(status ?: "TODO"),
            dependencies = dependencies ?: listOf()
        )

        companion object {
            fun fromTask(task: Task, user: String) = TaskFirestore(
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

    private data class GroupFirestore(
        val id: Long? = null,
        val userId: String? = null,
        val name: String? = null
    ) {
        fun toGroup() = Group(
            id = id ?: 0,
            userId = userId ?: "",
            name = name ?: ""
        )

        companion object {
            fun fromGroup(group: Group) = GroupFirestore(
                id = group.id,
                userId = group.userId,
                name = group.name
            )
        }
    }

    private data class AchievementFirestore(
        val id: Long? = null,
        val userId: String? = null,
        val title: String? = null,
        val name: String? = null,
        val details: String? = null,
        val date: Date? = null,
        val achieved: Boolean? = null,
        val difficulty: String? = null,
    ) {
        fun toAchievement() = Achievement(
            id = id ?: 0,
            userId = userId ?: "",
            name = name ?: "",
            title = title ?: "",
            details = details ?: "",
            date = date ?: Date(),
            achieved = achieved ?: false,
            difficulty = Difficulty.valueOf(difficulty ?: "EASY")
        )

        companion object {
            fun fromAchievement(achievement: Achievement) = AchievementFirestore(
                id = achievement.id,
                userId = achievement.userId,
                name = achievement.name,
                title = achievement.title,
                details = achievement.details,
                date = achievement.date,
                achieved = achievement.achieved,
                difficulty = achievement.difficulty.toString()
            )
        }
    }

    private data class TaskId(
        val value: Long? = null
    )

    private data class GroupId(
        val value: Long? = null
    )

    private data class AchievementId(
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

    private fun getTaskCollection() = db.collection(tasksCollection)

    private fun getGroupCollection() = db.collection(groupsCollection)

    private fun getAchievementCollection() = db.collection(achievementsCollection)

    override suspend fun getAllTasks(): Tasks = getTaskCollection()
        .whereEqualTo(TaskDoc.userId, getCurrentUser())
        .get(getSource())
        .await()
        .toObjects(TaskFirestore::class.java).map { it.toTask() }

    suspend fun getAllDueTasks(): Tasks = getTaskCollection()
        .whereGreaterThan(TaskDoc.dateDue, Task.simplifyDate(Date()) ?: Date())
        .orderBy(TaskDoc.dateDue)
        .get(getSource())
        .await()
        .toObjects(TaskFirestore::class.java)
        .map { it.toTask() }



    override suspend fun getAllGroups(): Groups = getGroupCollection()
        .whereEqualTo(GroupDoc.userId, getCurrentUser())
        .get(getSource())
        .await()
        .toObjects(GroupFirestore::class.java).map { it.toGroup() }

    override suspend fun getAllAchievements(): Achievements = getAchievementCollection()
        .whereEqualTo(AchievementDoc.userId, getCurrentUser())
        .get(getSource())
        .await()
        .toObjects(AchievementFirestore::class.java).map { it.toAchievement() }


    override suspend fun getDependencies(id: Long): Tasks = getTask(id)
        .dependencies.map { getTask(it) }

    override suspend fun getTask(id: Long): Task = getTaskCollection()
        .whereEqualTo(TaskDoc.userId, getCurrentUser())
        .whereEqualTo(TaskDoc.id, id)
        .get(getSource())
        .await()
        .toObjects(TaskFirestore::class.java)
        .first()
        .toTask()

    override suspend fun getGroup(id: Long) : Group = getGroupCollection()
        .whereEqualTo(GroupDoc.userId, getCurrentUser())
        .whereEqualTo(GroupDoc.id, id)
        .get(getSource())
        .await()
        .toObjects(GroupFirestore::class.java)
        .first()
        .toGroup()

    override suspend fun getAchievement(id: Long) : Achievement = getAchievementCollection()
        .whereEqualTo(AchievementDoc.userId, getCurrentUser())
        .whereEqualTo(AchievementDoc.id, id)
        .get(getSource())
        .await()
        .toObjects(AchievementFirestore::class.java)
        .first()
        .toAchievement()

    override suspend fun refresh() {
        TODO("Not yet implemented")
    }

    override suspend fun addGroup(group: Group): Long = GroupFirestore(
        id = nextIdGroup(),
        userId = getCurrentUser(),
        name = group.name
    ).let {
        getGroupCollection().add(it)
        it.id ?: throw Exception("Failed to add group")
    }

    override suspend fun addTask(task: Task): Long = TaskFirestore(
        userId = getCurrentUser(),
        title = task.title,
        id = nextId(),
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
    ).let {
        getTaskCollection().add(it)
        it.id ?: throw Exception("Failed to add task") // FIXME:
    }

    override suspend fun removeGroupById(id: Long) {
        getGroupCollection()
            .whereEqualTo(GroupDoc.userId, getCurrentUser())
            .whereEqualTo(GroupDoc.id, id)
            .get(getSource())
            .await()
            .let { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    throw Exception("Failed to remove group with non-existing id $id")
                }
                querySnapshot.first().reference.delete()
            }
    }

    override suspend fun updateTask(task: Task) {
        getTaskCollection()
            .whereEqualTo(TaskDoc.userId, getCurrentUser())
            .whereEqualTo(TaskDoc.id, task.id)
            .get(getSource())
            .await()
            .let { querySnapshot ->
                if (querySnapshot.isEmpty)
                    throw Exception("Failed to update Task with non-existing id ${task.id}")
                querySnapshot.first().reference.set(TaskFirestore.fromTask(task, getCurrentUser()))
            }
    }
    private suspend fun nextId() = getTaskCollection()
        .document(taskIdDoc)
        .get(getSource())
        .await()
        .let { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val oldValue = documentSnapshot.toObject(TaskId::class.java)?.value
                    ?: throw Exception("Failed to retrieve previous id")
                TaskId(oldValue + 1)
            } else {
                TaskId(1)
            }.let { newTaskId ->
                documentSnapshot.reference.set(newTaskId)
                newTaskId.value ?: throw Exception("New id should not be null")
            }
        }

    private suspend fun nextIdGroup() = getTaskCollection()
        .document(groupIdDoc)
        .get(getSource())
        .await()
        .let { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val oldValue = documentSnapshot.toObject(TaskId::class.java)?.value
                    ?: throw Exception("Failed to retrieve previous id")
                TaskId(oldValue + 1)
            } else {
                TaskId(1)
            }.let { newTaskId ->
                documentSnapshot.reference.set(newTaskId)
                newTaskId.value ?: throw Exception("New id should not be null")
            }
        }

    override suspend fun deleteTask(id: String) {
        getTaskCollection()
            .whereEqualTo(TaskDoc.userId, getCurrentUser())
            .whereEqualTo(TaskDoc.id, id)
            .get(getSource())
            .await()
            .let { snapshot ->
                if (snapshot.isEmpty) throw Exception("Failed to delete task with non-existing id $id")
                snapshot.first().reference.delete()
            }

        Log.d("REPOSITORY", "Removing all tasks references...")
        // Removes all references to this task in other tasks' dependencies.
        getTaskCollection()
            .whereEqualTo(TaskDoc.userId, getCurrentUser())
            .whereArrayContains(TaskDoc.dependencies, id)
            .get(getSource())
            .await()
            .let { snapshot ->
                Log.d("REPOSITORY", "Is snapshot empty? ${snapshot.isEmpty}")
                if (snapshot.isEmpty) return
                snapshot.documents.forEach { document ->
                    val dependencies = document.get(TaskDoc.dependencies) as? List<String>
                    Log.d("REPOSITORY", "Document ${document.id} dependencies: $dependencies")

                    val filteredDependencies = dependencies?.filter { e -> e != id }
                    Log.d(
                        "REPOSITORY",
                        "Document ${document.id} updated dependencies: $filteredDependencies"
                    )

                    document.reference.update(TaskDoc.dependencies, filteredDependencies)
                }
            }
    }
}
