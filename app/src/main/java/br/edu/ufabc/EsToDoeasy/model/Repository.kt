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
    suspend fun getDependencies(id: Long): Tasks

    /**
     * Returns a single task information by its given ID.
     */
    suspend fun getTask(id: Long): Task

    /**
     * Returns a single group information by its given ID.
     */
    suspend fun getGroup(id: Long): Group

    /**
     * Returns a single achievement information by its given ID.
     */
    suspend fun getAchievement(id: Long): Achievement

    /**
     * Refresh the repository.
     */
    suspend fun refresh()

    /**
     * Refresh the remove group by Id.
     */
    suspend fun deleteGroup(id: Long)

    /**
     * Refresh the remove group by Id.
     */
    suspend fun addGroup(group: Group): Long

    /**
     * Refresh the remove group by Id.
     */
    suspend fun addTask(task: Task): Long

    /**
     * updates a task by Id.
     */
    suspend fun updateTask(task: Task)

    /**
     * updates a group by Id.
     */
    suspend fun updateGroup(group: Group)

    /**
     * Deletes a task by its ID.
     */
    suspend fun deleteTask(id: Long)

    /**
     * Finishes a task (mark as done) by its ID.
     */
    suspend fun finishTask(id: Long) : Boolean

    /**
     * Get user id.
     */
    fun getUserId() : String
}
