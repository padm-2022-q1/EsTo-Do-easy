package br.edu.ufabc.EsToDoeasy.viewmodel

import SingleLiveEvent
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import br.edu.ufabc.EsToDoeasy.model.*
import br.edu.ufabc.EsToDoeasy.view.TaskAdapter
import java.util.*

/**
 * Application's main ViewModel.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RepositoryFactory(application).create()

    /**
     * Status hierarchy.
     */
    sealed class Status {
        /**
         * The error status.
         * @property e the exception
         */
        class Failure(val e: Exception) : Status()

        /**
         * The success status.
         * @property result the result
         */
        class Success(val result: Result) : Status()

        /**
         * The loading status.
         */
        object Loading : Status()
    }

    /**
     * The result hierarchy.
     */
    sealed class Result {
        /**
         * Result type that holds a list of tasks.
         * @property value the list of tasks
         */
        data class TaskList(
            val value: Tasks
        ) : Result()

        /**
         * Result type that holds a single task.
         * @property value the task
         */
        data class SingleTask(
            val value: Task
        ) : Result()

        /**
         * Result type that holds a list of tags.
         * @property value the list of tags
         */
        data class TagList(
            val value: List<String>
        ) : Result()

        /**
         * Result type that holds an id.
         * @property value the id.
         */
        data class Id(
            val value: String
        ) : Result()

        /**
         * A Result without value.
         */
        object EmptyResult : Result()

        data class GroupList(
            val value: Groups
        ) : Result()

        data class SingleGroup(
            val value: Group
        ) : Result()

    }

    /**
     * Maintains the currently selected task ID.
     */
    val clickedItemId by lazy { SingleLiveEvent<String?>() }

    val clickedAddNewGroup by lazy { SingleLiveEvent<Boolean?>() }

    val clickedAddNewTask by lazy { SingleLiveEvent<Boolean?>() }

    /**
     * Maintains the currently selected scheduled task ID.
     */
    val clickedScheduledTaskId by lazy { SingleLiveEvent<String?>() }

    val clickedStudyTechniqueSelect by lazy { SingleLiveEvent<Boolean?>() }

    val selectedStudyTechnique by lazy { SingleLiveEvent<String?>() }

    val clickedTaskToPlay by lazy { SingleLiveEvent<Boolean?>() }

    val clickedAtDetails by lazy { SingleLiveEvent<String?>() }


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

    val clickedGroupId by lazy { SingleLiveEvent<String?>() }

    val clickedPlanningTaskId by lazy { SingleLiveEvent<String?>() }

    val clickedAchievementProfile by lazy { SingleLiveEvent<Boolean?>() }

    val clickedAchievementItemId by lazy { SingleLiveEvent<String?>() }

    val clickedDashboardDaily by lazy { SingleLiveEvent<Boolean?>() }

    val profileUser by lazy { SingleLiveEvent<String?>() }

    val profileEmail by lazy { SingleLiveEvent<String?>() }

    fun getAll() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.TaskList(repository.getAllTasks())))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        }
    }

    /**
     *
     */
    fun addTask(task: Task) = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.Id(repository.addTask(task))))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        }
    }

    fun getSuggestTask() = Task(
        "id",
        "userId",
        "title",
        "detalis",
        Date(),
        Date(),
        Date(),
        0, "groupId",
        Difficulty.EASY,
        Priority.HIGH,
        br.edu.ufabc.EsToDoeasy.model.Status.TODO,
        listOf()
    )

    /**
     * Returns all tasks to be done next.
     */
    fun getAllNextTasks() = listOf<Task>()

    /**
     * Returns all tasks that aren't done.
     */
    fun getAllDueTasks() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.TaskList(repository.getAllDueTasks())))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        }
    }

    /**
     * Returns all groups.
     */
    fun getAllGroups() = liveData {
        try {
            emit(Status.Loading)
            emit(Status.Success(Result.GroupList(repository.getAllGroups())))
        } catch (e: Exception) {
            emit(Status.Failure(Exception("Failed to fetch pending items from repository", e)))
        }
    }


    /**
     * Returns all dependencies for a given task.
     */
    fun getDependencies(id: String) = listOf<Task>()

    /**
     * Returns a single task information by its given ID.
     */
    fun get(id: String) = Task(
        "id",
        "userId",
        "title",
        "detalis",
        Date(),
        Date(),
        Date(),
        0, "groupId",
        Difficulty.EASY,
        Priority.HIGH,
        br.edu.ufabc.EsToDoeasy.model.Status.TODO,
        listOf()
    )

    fun getTasksByGroupId(id: String) = listOf<Task>()

    /**
     * Returns all achievement.
     */
    fun getAllAchievements() = listOf<Achievement>()

    /**
     * Returns a group by its given ID.
     */
    fun getGroup(id: String) = listOf<Group>()
}
