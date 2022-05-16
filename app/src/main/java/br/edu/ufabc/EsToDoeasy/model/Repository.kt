package br.edu.ufabc.EsToDoeasy.model

/**
 * General API for repositories.
 */
interface Repository {

    /**
     * Returns all tasks.
     */
    suspend fun getAllTasks(): Tasks

    /**
     * Returns all groups.
     */
    suspend fun getAllGroups(): Groups

    /**
     * Returns all achievements.
     */
    suspend fun getAllAchievements():Achievements

    /**
     * Returns all dependecies for a given task.
     */
    suspend fun getDependencies(id: String): Tasks

    /**
     * Returns a single task information by its given ID.
     */
    suspend fun getTask(id: String): Task

    /**
     * Returns a single group information by its given ID.
     */
    suspend fun getGroup(id: String): Group

    /**
     * Returns a single achievement information by its given ID.
     */
    suspend fun getAchievement(id: String): Achievement

    /**
     * Refresh the repository.
     */
    suspend fun refresh()

    /**
     * Refresh the remove group by Id.
     */
    suspend fun removeGroupById(id: String)

    /**
     * Refresh the remove group by Id.
     */
    suspend fun addGroup(group: Group): String

    /**
     * Refresh the remove group by Id.
     */
    suspend fun addTask(task: Task): String

    /**
     * Refresh the remove group by Id.
     */
    suspend fun updateTask(task: Task)

}
