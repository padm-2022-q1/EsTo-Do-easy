package br.edu.ufabc.EsToDoeasy.model

import com.beust.klaxon.Klaxon
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Repository for data.
 */
class Repository {
    private lateinit var tasks: List<Task>
    private lateinit var groups: List<Group>
    private lateinit var achievements: List<Achievement>

    private data class TaskJson(
        val id: Long,
        val title: String,
        val details: String,
        val dateStarted: String,
        val dateFinished: String,
        val timeElapsed: Long,
        val groupId: Long,
        val difficulty: String,
        val priority: String,
        val status: String,
        val children: List<Long>,
        val parents: List<Long>,
    ) {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.US)
        fun toTask() = Task(
            id,
            title,
            details,
            formatter.parse(dateStarted),
            formatter.parse(dateFinished),
            timeElapsed,
            groupId,
            Difficulty.valueOf(difficulty),
            Priority.valueOf(priority),
            Status.valueOf(status),
            children,
            parents
        )
    }

    private data class TaskGroup(
        val id: Long,
        val name: String,
    ) {
        fun toGroup() = Group(id, name)
    }

    private data class AchievementJson(
        val id: Long,
        val title: String,
        val details: String,
        val date: String,
        val achieved: String,
        val difficulty: String
    ) {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.US)
        fun toAchievement() = Achievement(
            id,
            title,
            details,
            date,
            achieved,
            difficulty
        )
    }

    /**
     * Loads tasks data from given JSON input stream.
     */
    fun loadDataTasks(stream: InputStream) {
        tasks = Klaxon().parseArray<TaskJson>(stream)?.map { it.toTask() } ?: emptyList()
    }

    /**
     * Loads groups data from given JSON input stream.
     */
    fun loadDataGroups(stream: InputStream) {
        groups = Klaxon().parseArray<TaskGroup>(stream)?.map { it.toGroup() } ?: emptyList()
    }

    fun getTasksbyGroupId(groupId: Long) = validTasks().filter { getTask(groupId).children.contains(it.groupId)}

    /**
     * Loads achievements data from given JSON input stream.
     */
    fun loadDataAchievements(stream: InputStream) {
        achievements = Klaxon().parseArray<AchievementJson>(stream)?.map { it.toAchievement() } ?: emptyList()
    }
    /**
     * Returns all tasks.
     */
    fun getAllTasks() = validTasks()

    /**
     * Returns all groups.
     */
    fun getAllGroups() = validGroups()

    /**
     * Returns all achievements.
     */
    fun getAllAchievements() = validAchievements()


    /**
     * Returns all dependecies for a given task
     */
    fun getDependencies(id: Long) = validTasks().filter { getTask(id).children.contains(it.id) }
    
    /**
     * Returns a single task information by its given ID.
     */
    fun getTask(id: Long) =
        validTasks().find { it.id == id } ?: throw NoSuchElementException("Task not found")

    /**
     * Returns a single group information by its given ID.
     */
    fun getGroup(id: Long) =
        validGroups().find { it.id == id } ?: throw NoSuchElementException("Group not found")

    /**
     * Returns a single achievement information by its given ID.
     */
    fun getAchievement(id: Long) =
        validAchievements().find { it.id == id } ?: throw NoSuchElementException("Achievement not found")

    private fun validTasks() = if (this::tasks.isInitialized) tasks else
        throw NullPointerException("Repository has not been initialized")

    private fun validGroups() = if (this::groups.isInitialized) groups else
        throw NullPointerException("Repository has not been initialized")

    private fun validAchievements() = if (this::achievements.isInitialized) achievements else
        throw NullPointerException("Repository has not been initialized")

}
