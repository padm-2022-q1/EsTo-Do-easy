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
    suspend fun getAllGroups()

    /**
     * Returns all achievements.
     */
    suspend fun getAllAchievements()


    /**
     * Returns all dependecies for a given task.
     */
    suspend fun getDependencies(id: String): Tasks

    /**
     * Returns a single task information by its given ID.
     */
    suspend fun getTask(id: String)

    /**
     * Returns a single group information by its given ID.
     */
    suspend fun getGroup(id: String)

    /**
     * Returns a single achievement information by its given ID.
     */
    suspend fun getAchievement(id: String)

    /**
     * Refresh the repository.
     */
    suspend fun refresh()
}
