package br.edu.ufabc.EsToDoeasy.viewmodel

import SingleLiveEvent
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.edu.ufabc.EsToDoeasy.model.Repository
import br.edu.ufabc.EsToDoeasy.model.Status

/**
 * Application's main ViewModel.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        /**
         * Tasks JSON file asset name.
         */
        const val taskFile = "tasks.json"

        /**
         * Groups JSON file asset name.
         */
        const val groupFile = "groups.json"

        /**
         * Achievement JSON file asset name.
         */
        const val achievementFile = "achievements.json"
    }

    private val repository = Repository()

    init {
        application.resources.assets.open(taskFile).use {
            repository.loadDataTasks(it)
        }

        application.resources.assets.open(groupFile).use {
            repository.loadDataGroups(it)
        }

        application.resources.assets.open(achievementFile).use {
            repository.loadDataAchievements(it)
        }
    }

    /**
     * Maintains the currently selected task ID.
     */
    val clickedItemId by lazy { SingleLiveEvent<Long?>() }

    val clickedAddNewGroup by lazy { SingleLiveEvent<Boolean?>() }

    /**
     * Maintains the currently selected scheduled task ID.
     */
    val clickedScheduledTaskId by lazy { SingleLiveEvent<Long?>() }

    val clickedStudyTechniqueSelect by lazy { SingleLiveEvent<Boolean?>() }

    val selectedStudyTechnique by lazy { SingleLiveEvent<String?>() }

    val clickedTaskToPlay by lazy { SingleLiveEvent<Boolean?>() }

    val clickedAtDetails by lazy { SingleLiveEvent<Long?>() }


    val clickedAtConfigPomodoro by lazy { SingleLiveEvent<Boolean?>() }

    val clickedAtSkipSession by lazy { SingleLiveEvent<Long?>() }

    val clickedAtAddTask by lazy { SingleLiveEvent<Boolean?>() }

    /**
     * Returns the suggested task to be done.
     */

    val clickedSignOutProfile by lazy { SingleLiveEvent<Boolean?>() }

    val clickedSkipLogin by lazy { SingleLiveEvent<Boolean?>() }

    val clickedLoginLogin by lazy { SingleLiveEvent<Boolean?>() }

    val clickedSettingsProfile by lazy { SingleLiveEvent<Boolean?>() }

    val clickedGroupId by lazy { SingleLiveEvent<Long?>() }

    val clickedPlanningTaskId by lazy { SingleLiveEvent<Long?>() }

    val clickedAchievementProfile by lazy { SingleLiveEvent<Boolean?>() }

    val clickedAchievementItemId by lazy { SingleLiveEvent<Long?>() }

    val clickedDashboardDaily by lazy { SingleLiveEvent<Boolean?>() }

    fun getSuggestTask() = getAll()[0]

    /**
     * Returns all tasks.
     */
    fun getAll() = repository.getAllTasks()

    /**
     * Returns all tasks to be done next.
     */
    fun getAllNextTasks() = repository.getAllTasks()
        .filter { it.id != getSuggestTask().id && it.status != Status.DONE }

    /**
     * Returns all tasks that aren't done or achived.
     */
    fun getAllDueTasks() =
        repository.getAllTasks().filter { it.status != Status.DONE && it.status != Status.ARCHIVED }

    /**
     * Returns all groups.
     */
    fun getAllGroups() = repository.getAllGroups()


    /**
     * Returns all dependencies for a given task.
     */
    fun getDependencies(id: Long) = repository.getDependencies(id)

    /**
     * Returns a single task information by its given ID.
     */
    fun get(id: Long) = repository.getTask(id)

    fun getTasksByGroupId(id: Long) = repository.getTasksbyGroupId(id)

    /**
     * Returns all achievement.
     */
    fun getAllAchievements() = repository.getAllAchievements()

    /**
     * Returns a group by its given ID.
     */
    fun getGroup(id: Long) = repository.getGroup(id)
}
